package sdnu.lushun.KuAiAndroid.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import sdnu.lushun.KuAiAndroid.BigImgActivity;
import sdnu.lushun.KuAiAndroid.Model;
import sdnu.lushun.KuAiAndroid.R;
import sdnu.lushun.KuAiAndroid.bean.Info;
import sdnu.lushun.KuAiAndroid.net.HttpPostThread;
import sdnu.lushun.KuAiAndroid.net.ThreadPoolUtils;
import sdnu.lushun.KuAiAndroid.util.Common;
import sdnu.lushun.KuAiAndroid.util.LoadImg;
import sdnu.lushun.KuAiAndroid.util.LoadImg.ImageDownloadCallBack;

import static sdnu.lushun.KuAiAndroid.util.NetUtils.isNetConnected;

public class ShareAdapter extends BaseAdapter {

    Holder hold;
    private Activity activity;

    private String imgStr;
    private String imgStr1;
    private PullToRefreshListView listView;

    private List<Info> list;
    private Context ctx;
    private LoadImg loadImgHeadImg;
    private LoadImg loadImgMainImg;
    private boolean upFlag = false;
    private boolean downFlag = false;
    private ShareAdapter mAdapter;
    private int[] images = {R.drawable.chuanke, R.drawable.icon_camera,
            R.drawable.zixuew, R.drawable.chuanke, R.drawable.icon_camera,
            R.drawable.zixuew, R.drawable.chuanke, R.drawable.icon_camera,
            R.drawable.zixuew,};

    public ShareAdapter(Context ctx, Activity activity, List<Info> list, PullToRefreshListView listView) {
        this.list = list;
        this.ctx = ctx;
        this.activity = activity;
        this.listView = listView;

        // 加载图片对象
        loadImgHeadImg = new LoadImg(ctx);
        loadImgMainImg = new LoadImg(ctx);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {

        if (arg1 == null) {
            hold = new Holder();
            arg1 = View.inflate(ctx, R.layout.item_share, null);
            hold.UserHead = (ImageView) arg1.findViewById(R.id.Item_UserHead);
            hold.UserName = (TextView) arg1.findViewById(R.id.Item_UserName);
            hold.MainText = (TextView) arg1.findViewById(R.id.Item_MainText);
            hold.Up = (LinearLayout) arg1.findViewById(R.id.Item_Up);
            hold.Up_Img = (ImageView) arg1.findViewById(R.id.Item_Up_img);
            hold.Up_text = (TextView) arg1.findViewById(R.id.Item_Up_text);
            hold.Down = (LinearLayout) arg1.findViewById(R.id.Item_Down);
            hold.Down_Img = (ImageView) arg1.findViewById(R.id.Item_Down_img);
            hold.Down_text = (TextView) arg1.findViewById(R.id.Item_Down_text);
            hold.MainText.setMovementMethod(LinkMovementMethod.getInstance());
            hold.gv = (GridView) arg1.findViewById(R.id.noScrollgridview1);

            arg1.setTag(hold);
        } else {
            hold = (Holder) arg1.getTag();
        }

        imgStr = list.get(arg0).getAimg();
        if (arg0 > 0) {
            imgStr1 = list.get(arg0 - 1).getAimg();
        }

        if (imgStr.equals("") || imgStr == "") {
            arg1.findViewById(R.id.noScrollgridview1).setBackgroundResource(
                    R.drawable.input_normal);
            arg1.findViewById(R.id.noScrollgridview1).setVisibility(View.GONE);
        } else {
            arg1.findViewById(R.id.noScrollgridview1).setVisibility(
                    View.VISIBLE);
            arg1.findViewById(R.id.noScrollgridview1).setBackgroundResource(
                    R.drawable.input_normal);

            MyAdapter a = new MyAdapter(ctx, imgStr);
            hold.gv.setAdapter(a);
            a.notifyDataSetInvalidated();
        }

        hold.gv.setNumColumns(GridView.AUTO_FIT);
        hold.gv.setVerticalSpacing(10);
        hold.gv.setHorizontalSpacing(10);

        hold.gv.setOnItemClickListener(new OnItemClickListener() {

            String imgStr1 = list.get(arg0).getAimg();

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent(ctx, BigImgActivity.class);
                intent.putExtra("ID", arg2);
                intent.putExtra("a", imgStr1);
                ctx.startActivity(intent);
            }

        });

        hold.UserName.setText(Html.fromHtml(list.get(arg0).getUname()));
        hold.MainText.setText(Html.fromHtml(list.get(arg0).getQvalue()));
        hold.Up_text.setText(list.get(arg0).getQlike());
        hold.Down_text.setText("-" + list.get(arg0).getQunlike());

        // 设置监听
        hold.Up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final String qid = String.valueOf(Integer.parseInt(list.get(arg0).getQid()));
                Handler hand = new Handler() {
                    public void handleMessage(android.os.Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 200) {
                            if (isNetConnected(activity)) {
                                Toast.makeText(ctx, "点赞成功", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(ctx, "提交失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                String json = Common.getLikeJson( qid, "1");

                ThreadPoolUtils.execute(new HttpPostThread(hand, Model.LikeOrUnlike, json));

            }
        });

        hold.Down.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                final String qid = String.valueOf(Integer.parseInt(list.get(arg0).getQid()));

                Handler hand1 = new Handler() {
                    public void handleMessage(android.os.Message msg) {
                        super.handleMessage(msg);
                        if (msg.what == 200) {
                            if (isNetConnected(activity)) {
                                Toast.makeText(ctx, "反对有效", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ctx, "提交失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                String json = Common.getLikeJson(qid, "0");

                ThreadPoolUtils.execute(new HttpPostThread(hand1, Model.LikeOrUnlike, json));

            }
        });

        hold.Up.setTag(list.get(arg0).getQid());

        hold.Up.setBackgroundResource(R.drawable.button_vote_enable);
        hold.Up_Img.setImageResource(R.drawable.support);
        hold.Up_text.setTextColor(Color.parseColor("#815F3D"));
        if (hold.Up.getTag().equals("0")) {
            hold.Up.setBackgroundResource(R.drawable.button_vote_active);
            hold.Up_Img.setImageResource(R.drawable.support);
            hold.Up_text.setTextColor(Color.RED);
        }
        hold.UserHead.setImageResource(R.drawable.user);
        if (list.get(arg0).getUhead().equalsIgnoreCase("")) {
            hold.UserHead.setImageResource(R.drawable.user);
        } else {
            hold.UserHead.setTag(Model.USERHEADURL + list.get(arg0).getUhead());
            Bitmap bitHead = loadImgHeadImg.loadImage(hold.UserHead,
                    Model.USERHEADURL +File.separator+ list.get(arg0).getUhead(),
                    new ImageDownloadCallBack() {
                        @Override
                        public void onImageDownload(ImageView imageView, Bitmap bitmap) {
                            if (arg0 >= list.size()) {
                                if (hold.UserHead.getTag().equals(Model.USERHEADURL + list.get(arg0 - 1).getUhead())) {
                                    hold.UserHead.setImageBitmap(bitmap);
                                }
                            } else {
                                if (hold.UserHead.getTag().equals(
                                        Model.USERHEADURL + list.get(arg0).getUhead())) {
                                    hold.UserHead.setImageBitmap(bitmap);
                                }
                            }

                        }
                    });
            if (bitHead != null) {
                hold.UserHead.setImageBitmap(bitHead);
            }
        }

        return arg1;
    }

    static class Holder {
        ImageView UserHead;
        TextView UserName;
        TextView MainText;
        LinearLayout Up;
        ImageView Up_Img;
        TextView Up_text;
        LinearLayout Down;
        ImageView Down_Img;
        TextView Down_text;
        GridView gv;
    }


    // 自定义适配器
    class MyAdapter extends BaseAdapter {
        // 上下文对象
        private Context context;
        private String str;
        // 图片数组
        private Integer[] imgs = {R.drawable.zixuew, R.drawable.chuanke,
                R.drawable.muke, R.drawable.zixuew, R.drawable.chuanke,
                R.drawable.muke, R.drawable.zixuew, R.drawable.chuanke,
                R.drawable.muke, R.drawable.zixuew, R.drawable.chuanke,
                R.drawable.muke,

        };

        MyAdapter(Context context, String str) {
            this.context = context;
            this.str = str;
        }

        public int getCount() {
            return imgs.length;
        }

        public Object getItem(int item) {
            return item;
        }

        public long getItemId(int id) {
            return id;
        }

        // 创建View方法
        @SuppressLint("NewApi")
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                if (hold.gv.getNumColumns() > -1) {
                    imageView.setLayoutParams(new GridView.LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            (Common.getScreenParam(activity) - (30 + (hold.gv
                                    .getNumColumns() + 1) * 10)) / hold.gv
                                    .getNumColumns()));// 设置ImageView对象布局
                } else {

                    imageView.setLayoutParams(new GridView.LayoutParams(
                            LayoutParams.WRAP_CONTENT, 115));// 设置ImageView对象布局
                }
                imageView.setAdjustViewBounds(false);// 设置边界对齐

                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);// 设置刻度的类型
            } else {
                imageView = (ImageView) convertView;
            }

            String[] newstr = str.split(",");

            if (!newstr.equals("") && position < newstr.length) {
                Picasso.with(ctx)
                        .load(Model.VALUEIMG+File.separator + newstr[position])
                        .error(R.drawable.shop_photo_frame).into(imageView);
            }

            return imageView;
        }
    }

}
