package sdnu.lushun.KuAiAndroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import sdnu.lushun.KuAiAndroid.adapter.ShareAdapter;
import sdnu.lushun.KuAiAndroid.adapter.SmallAdapter;
import sdnu.lushun.KuAiAndroid.adapter.TuijianAdapter;
import sdnu.lushun.KuAiAndroid.adapter.YMAdapter;
import sdnu.lushun.KuAiAndroid.bean.Advertisement;
import sdnu.lushun.KuAiAndroid.bean.Info;
import sdnu.lushun.KuAiAndroid.bean.TuiJian;
import sdnu.lushun.KuAiAndroid.bean.UserInfo;
import sdnu.lushun.KuAiAndroid.bean.XiaoJingYan;
import sdnu.lushun.KuAiAndroid.bean.YuanMa;
import sdnu.lushun.KuAiAndroid.http.AsyncHttpClient;
import sdnu.lushun.KuAiAndroid.http.AsyncHttpResponseHandler;
import sdnu.lushun.KuAiAndroid.http.RequestParams;
import sdnu.lushun.KuAiAndroid.listener.LoginRegisterListener;
import sdnu.lushun.KuAiAndroid.net.HttpGetThread;
import sdnu.lushun.KuAiAndroid.net.HttpUtils;
import sdnu.lushun.KuAiAndroid.net.MyJson;
import sdnu.lushun.KuAiAndroid.net.ThreadPoolUtils;
import sdnu.lushun.KuAiAndroid.util.EditTextWithDel;
import sdnu.lushun.KuAiAndroid.util.LoginUtil;
import sdnu.lushun.KuAiAndroid.view.AdvViewPager;

import static sdnu.lushun.KuAiAndroid.util.FileUtils_a.delete;

public class MainActivity extends Activity implements OnClickListener {

    public static int screenW;

    private Uri photoUri;
    private int PIC_FROM_CAMERA = 3;
    private int PIC_FROM＿LOCALPHOTO = 4;
    File pictureFileDir;

    // 广告版

    private ImageView iv1;//
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    private AdvViewPager vpAdv = null;//滑动广告板
    private ViewGroup vg = null;
    private ImageView[] imageViews = null;
    private int currentPage = 0;
    private List<View> advs = null;

    private ViewPager vpViewPager = null;
    private List<View> views = null;
    private PullToRefreshListView leftLv;
    private PullToRefreshListView love = null;
    private PullToRefreshListView small;
    private PullToRefreshListView big;
    public SlidingMenu slidingMenu = null;

    // 刷新部分
    private RelativeLayout shuaxin_more;
    public static final int HTTP_REQUEST_SUCCESS = -1;
    public static final int HTTP_REQUEST_ERROR = 0;
    private Boolean S_ONREFRESH = true;
    private Boolean Y_ONREFRESH = true;
    private Boolean T_ONREFRESH = true;
    private int tag = 1;
    private int Ytag = 1;
    private int Ttag = 1;
    private int mStart = 0;
    private int mEnd = 5;

    // 分享部分
    private String hotUrl = Model.SHARE;
    private List<Info> list = new ArrayList<Info>();
    private List<XiaoJingYan> xiaoJYList = new ArrayList<XiaoJingYan>();
    private List<YuanMa> ymList;
    private List<TuiJian> tjList;
    private List<Advertisement> advList = new ArrayList<Advertisement>();
    private String url1 = null;
    private TextView sendShare;
    private MyJson myJson = new MyJson();

    // 登录注册部分
    private Button mLogin;
    private EditText mName, mPassword;
    private TextView tag1_tv, tag2_tv, tag3_tv;
    private ImageView tag1_iv, tag2_iv, tag3_iv;
    private LinearLayout tag1, tag2, tag3;
    private String NameValue = null;
    private String PasswordValue = null;
    private RelativeLayout mRegister;
    private RelativeLayout tx;

    private ShareAdapter shAdapter = null;
    private SmallAdapter sAdapter;
    private YMAdapter ymAdapter;
    private TuijianAdapter tAdapter;
    public static final String XIAO = Model.HTTPURL + "xiaoJingYan.php";
    public static final String YUAN = Model.HTTPURL + "yuanMa.php";
    public static final String TUI = Model.HTTPURL + "aiTuiJian.php";
    public static final String ADV = Model.HTTPURL + "advertisement.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSlidingMenu();
        initViewPager();
        initViewLogin();

        jiazai();
        if (Model.MYUSERINFO != null) {
            LoginUtil.loginSetting(Model.LOGIN_FLAG.LOGIN_SUCCESS, MainActivity.this, slidingMenu);
        }
        shuaxin_more = (RelativeLayout) findViewById(R.id.shuaxin_more);
        shuaxin_more.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                findViewById(R.id.small_load).setVisibility(View.VISIBLE);
                tag = 1;
                HttpUtils.getJson(XIAO + "?mstart=" + tag, getNewsHandler);
                jiazai();
                Ytag = 1;
                HttpUtils.getJson(YUAN + "?mstart=" + Ytag, getYuanHandler);
                mStart = 0;
                mEnd = 5;
                url1 = hotUrl + "start=" + mStart + "&end=" + mEnd;
                ThreadPoolUtils.execute(new HttpGetThread(hand1, url1));
                Ttag = 1;
                HttpUtils.getJson(TUI + "?mstart=" + Ttag, getTuiHandler);
                HttpUtils.getJson(ADV + "?mstart=" + 1, getAdvHandler);
            }
        });

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screenW = metric.widthPixels; // 屏幕宽度（像素）

    }

    private void jiazai() {
        if (xiaoJYList.size() == 0) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    findViewById(R.id.small_load).setVisibility(View.GONE);

                }
            }, 10000);
        }
    }

    private Handler getNewsHandler = new Handler() {
        @SuppressLint("NewApi")
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            if (jsonData.equalsIgnoreCase("null")) {
                Toast.makeText(MainActivity.this, "已经没有了，亲", Toast.LENGTH_LONG).show();
            }

            try {
                if (tag == 1) {
                    xiaoJYList.clear();
                }
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("title");
                    String desc = object.getString("desc");
                    String time = object.getString("time");
                    String content_url = object.getString("content_url");
                    String pic_url = object.getString("pic_url");
                    System.out.println("title = " + title);
                    System.out.println("pic_url = " + pic_url);
                    xiaoJYList.add(new XiaoJingYan(title, desc, time,
                            content_url, pic_url));

                }


                if (xiaoJYList.size() != 0) {

                    findViewById(R.id.small_load).setVisibility(View.GONE);
                    findViewById(R.id.shuaxin_more).setVisibility(View.GONE);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            findViewById(R.id.vpViewPager1).setBackgroundColor(
                                    Color.parseColor("#f6f5ec"));

                        }
                    }, 1000);

                }
                tag++;
                S_ONREFRESH = true;
                sAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ;
    };

    private Handler getYuanHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            if (jsonData.equalsIgnoreCase("null")) {
                Toast.makeText(MainActivity.this, "已经没有了，亲", Toast.LENGTH_LONG).show();
            }
            try {
                if (Ytag == 1) {
                    ymList.clear();
                }
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("title");
                    String desc = object.getString("desc");
                    String time = object.getString("time");
                    String content_url = object.getString("content_url");
                    String pic_url = object.getString("pic_url");
                    System.out.println("title = " + title);
                    System.out.println("pic_url = " + pic_url);
                    ymList.add(new YuanMa(title, desc, time, content_url,
                            pic_url));

                }
                Ytag++;
                Y_ONREFRESH = true;
                ymAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ;
    };

    private Handler getTuiHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            if (jsonData.equalsIgnoreCase("null")) {
                Toast.makeText(MainActivity.this, "已经没有了，亲", Toast.LENGTH_LONG).show();
            }
            try {
                if (Ttag == 1) {
                    tjList.clear();
                }
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("title");
                    String desc = object.getString("desc");
                    String time = object.getString("time");
                    String content_url = object.getString("content_url");
                    String pic_url = object.getString("pic_url");
                    System.out.println("title = " + title);
                    System.out.println("pic_url = " + pic_url);
                    tjList.add(new TuiJian(title, desc, time, content_url,
                            pic_url));

                }
                Ttag++;
                T_ONREFRESH = true;
                tAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ;
    };
    private Handler getAdvHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;

            try {

                advList.clear();
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("title");
                    String desc = object.getString("desc");
                    String time = object.getString("time");
                    String content_url = object.getString("content_url");
                    String pic_url = object.getString("pic_url");
                    System.out.println("title = " + title);
                    System.out.println("pic_url = " + pic_url);
                    advList.add(new Advertisement(title, desc, time,
                            content_url, pic_url));

                }

                if (advList.size() != 0) {

                    Picasso.with(getApplicationContext())
                            .load(Model.HTTPURL + "ggb/"
                                    + (advList.get(0)).getPic_url())
                            .error(R.drawable.jike).into(iv1);
                    Picasso.with(getApplicationContext())
                            .load(Model.HTTPURL + "ggb/"
                                    + (advList.get(1)).getPic_url())
                            .error(R.drawable.muke).into(iv2);
                    Picasso.with(getApplicationContext())
                            .load(Model.HTTPURL + "ggb/"
                                    + (advList.get(2)).getPic_url())
                            .error(R.drawable.zixuew).into(iv3);
                    Picasso.with(getApplicationContext())
                            .load(Model.HTTPURL + "ggb/"
                                    + (advList.get(3)).getPic_url())
                            .error(R.drawable.chuanke).into(iv4);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ;
    };

    static void initPullUp(PullToRefreshListView lv) {
        ILoadingLayout endLabels = lv.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 刷新
     */
    class MyOnRefreshListener2 implements OnRefreshListener2<ListView> {

        private PullToRefreshListView mPtflv;
        private int i;

        public MyOnRefreshListener2(PullToRefreshListView ptflv, int i) {
            this.i = i;
            this.mPtflv = ptflv;
        }

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            // 下拉刷新
            String label = DateUtils.formatDateTime(getApplicationContext(),
                    System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                            | DateUtils.FORMAT_SHOW_DATE
                            | DateUtils.FORMAT_ABBREV_ALL);

            refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
            new GetNewsTask(mPtflv, i, -1).execute();

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            // 上拉加载
            new GetNewsTask(mPtflv, i, -2).execute();
        }

    }

    /**
     * 请求网络获得新闻信息
     *
     * @author Louis
     */
    class GetNewsTask extends AsyncTask<String, Void, Integer> {
        private PullToRefreshListView mPtrlv;
        private int i;
        private int reType;

        public GetNewsTask(PullToRefreshListView ptrlv, int i, int reType) {
            this.mPtrlv = ptrlv;
            this.i = i;
            this.reType = reType;
        }

        @Override
        protected Integer doInBackground(String... params) {

            switch (i) {
                case 0:
                    if (reType == -1) {// 下拉刷新
                        if (Y_ONREFRESH) {
                            Ytag = 1;
                            HttpUtils.getJson(YUAN + "?mstart=" + Ytag,
                                    getYuanHandler);
                            Y_ONREFRESH = false;
                        }

                    } else if (reType == -2) {
                        if (Y_ONREFRESH) {
                            HttpUtils.getJson(YUAN + "?mstart=" + Ytag,
                                    getYuanHandler);
                            Y_ONREFRESH = false;
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;

                case 1:
                    if (reType == -1) {// 下拉刷新
                        if (S_ONREFRESH) {
                            tag = 1;
                            HttpUtils.getJson(XIAO + "?mstart=" + tag,
                                    getNewsHandler);
                            HttpUtils.getJson(ADV + "?mstart=" + 1,
                                    getAdvHandler);
                            S_ONREFRESH = false;
                        } else if (!S_ONREFRESH) {

                        }

                    } else if (reType == -2) {
                        if (S_ONREFRESH) {
                            HttpUtils.getJson(XIAO + "?mstart=" + tag,
                                    getNewsHandler);
                        } else if (!S_ONREFRESH) {

                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    break;
                case 2:
                    if (reType == -1) {// 下拉刷新
                        // tag = 1;
                        mStart = 0;
                        mEnd = 5;
                        url1 = hotUrl + "start=" + mStart + "&end=" + mEnd;
                        ThreadPoolUtils.execute(new HttpGetThread(hand1, url1));

                    } else if (reType == -2) {
                        url1 = hotUrl + "start=" + mStart + "&end=" + mEnd;
                        ThreadPoolUtils.execute(new HttpGetThread(hand1, url1));
                    }

                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    if (reType == -1) {// 下拉刷新
                        if (T_ONREFRESH) {

                            Ttag = 1;
                            HttpUtils.getJson(TUI + "?mstart=" + Ttag,
                                    getTuiHandler);
                            T_ONREFRESH = false;
                        } else if (!S_ONREFRESH) {

                        }

                    } else if (reType == -2) {
                        if (T_ONREFRESH) {
                            HttpUtils.getJson(TUI + "?mstart=" + tag,
                                    getTuiHandler);
                        } else if (!T_ONREFRESH) {

                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }

            return HTTP_REQUEST_SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            switch (result) {
                case HTTP_REQUEST_SUCCESS:

                    // sAdapter.notifyDataSetChanged();
                    break;
                case HTTP_REQUEST_ERROR:
                    Toast.makeText(MainActivity.this, "请检查网络", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
            mPtrlv.onRefreshComplete();
        }

    }

	/*
     * viewpager部分
	 */

    private void initViewPager() {

        views = new ArrayList<View>();
        views.add(LayoutInflater.from(this).inflate(R.layout.activity_xiaojingyan, null));
        views.add(LayoutInflater.from(this).inflate(R.layout.activity_bigshare, null));
        views.add(LayoutInflater.from(this).inflate(R.layout.activity_tuijian, null));

        vpViewPager = (ViewPager) findViewById(R.id.vpViewPager1);
        vpViewPager.setAdapter(new MyPagerAdapter(views));
        vpViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

        MyPagerAdapter myPagerAdapter = (MyPagerAdapter) vpViewPager.getAdapter();
        View v1 = myPagerAdapter.getItemAtPosition(0);
        View v2 = myPagerAdapter.getItemAtPosition(1);
        View v3 = myPagerAdapter.getItemAtPosition(2);

        findViewById(R.id.yuanma).setOnClickListener(this);
        findViewById(R.id.bPersonal).setOnClickListener(this);
        tag1 = ((LinearLayout) findViewById(R.id.Tag1));
        tag2 = ((LinearLayout) findViewById(R.id.Tag2));
        tag3 = ((LinearLayout) findViewById(R.id.Tag3));

        tag1_tv = (TextView) findViewById(R.id.tag1_tv);
        tag2_tv = (TextView) findViewById(R.id.tag2_tv);
        tag3_tv = (TextView) findViewById(R.id.tag3_tv);
        tag1_iv = (ImageView) findViewById(R.id.tag1_iv);
        tag2_iv = (ImageView) findViewById(R.id.tag2_iv);
        tag3_iv = (ImageView) findViewById(R.id.tag3_iv);

        tag1.setOnClickListener(this);
        tag2.setOnClickListener(this);
        tag3.setOnClickListener(this);

        xiaoJYList = new ArrayList<XiaoJingYan>();
        ymList = new ArrayList<YuanMa>();
        tjList = new ArrayList<TuiJian>();


        //小经验
        small = (PullToRefreshListView) v1.findViewById(R.id.small);
        small.setMode(Mode.BOTH);
        initPullUp(small);
        small.setOnItemClickListener(new ItemClick(1));
        sAdapter = new SmallAdapter(this, xiaoJYList);
        small.setAdapter(sAdapter);
        small.setOnRefreshListener(new MyOnRefreshListener2(small, 1));
        initPullToRefreshListView(small, sAdapter);
        HttpUtils.getJson(XIAO + "?mstart=" + tag, getNewsHandler);


        //大动态
        big = (PullToRefreshListView) v2.findViewById(R.id.ptrlvEntertainmentNews);


        //爱推荐
        love = (PullToRefreshListView) v3.findViewById(R.id.love);
        love.setMode(Mode.BOTH);
        initPullUp(love);
        love.setOnRefreshListener(new MyOnRefreshListener2(love, 3));
        tAdapter = new TuijianAdapter(this, tjList);
        love.setAdapter(tAdapter);
        HttpUtils.getJson(TUI + "?mstart=" + Ttag, getTuiHandler);

        //左侧菜单
        ymAdapter = new YMAdapter(this, ymList);
        leftLv = (PullToRefreshListView) slidingMenu.findViewById(R.id.leftLV);
        leftLv.setMode(Mode.BOTH);
        initPullUp(leftLv);
        leftLv.setOnItemClickListener(new ItemClick(0));
        leftLv.setAdapter(ymAdapter);
        leftLv.setOnRefreshListener(new MyOnRefreshListener2(leftLv, 0));
        HttpUtils.getJson(YUAN + "?mstart=" + Ytag, getYuanHandler);
    }


    class MyOnPageChangeListener implements OnPageChangeListener {
        @SuppressLint("NewApi")
        @Override
        public void onPageSelected(int arg0) {
            resetImg();
            switch (arg0) {
                case 0:
                    tag1_tv.setTextColor(Color.parseColor("#00CCFF"));
                    tag1_iv.setImageResource(R.drawable.yuanbao);
                    break;
                case 1:
                    tag2_tv.setTextColor(Color.parseColor("#00CCFF"));
                    tag2_iv.setImageResource(R.drawable.fenxiang);
                    break;
                case 2:
                    tag3_tv.setTextColor(Color.parseColor("#00CCFF"));
                    tag3_iv.setImageResource(R.drawable.wujiaoxing);
                    break;

                default:
                    break;
            }

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

    }


    class MyPagerAdapter extends PagerAdapter {

        private List<View> views;

        public MyPagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            // 将指定的view从viewPager中移除
            ((ViewPager) container).removeView(views.get(position));
        }

        @SuppressLint("NewApi")
        @Override
        public Object instantiateItem(View container, int position) {
            // 将view添加到viewPager中

            switch (position) {
                case 1:
                    sendShare = (TextView) views.get(position).findViewById(
                            R.id.tv_send);
                    sendShare.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (Model.MYUSERINFO != null) {
                                Intent intent = new Intent(MainActivity.this,
                                        UploadActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                                slidingMenu.showSecondaryMenu();
                            }
                        }
                    });
                    hotUrl = Model.SHARE;
                    big.setMode(Mode.BOTH);
                    initPullUp(big);
                    shAdapter = new ShareAdapter(MainActivity.this,
                            MainActivity.this, list);
                    big.setAdapter(shAdapter);
                    big.setOnRefreshListener(new MyOnRefreshListener2(big, 2));
                    url1 = Model.SHARE + "start=" + mStart + "&end=" + mEnd;
                    ThreadPoolUtils.execute(new HttpGetThread(hand1, url1));

            }

            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }

        public View getItemAtPosition(int position) {
            return views.get(position);
        }

    }

    Handler hand1 = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            if (msg.what == 404) {
                Toast.makeText(MainActivity.this, "找不到地址", Toast.LENGTH_LONG).show();
            } else if (msg.what == 100) {

            } else if (msg.what == 200) {

                if (true) {
                    findViewById(R.id.dt_load).setVisibility(View.GONE);

                }
                if (mStart == 0) {
                    list.clear();
                }

                String result = (String) msg.obj;
                if (result != null) {
                    List<Info> newList = myJson.getAshamedList(result);
                    if (newList != null) {
                        if (newList.size() > 0) {
                            mStart += 5;
                            mEnd += 5;
                        } else if (newList.size() == 0) {
                            // if (list.size() == 0)
                            // Toast.makeText(MainActivity.this, "已经没有了...", 1)
                            // .show();
                        } else {
                        }

                        for (Info info : newList) {
                            list.add(info);
                        }
                    } else {
                        if (list.size() != 0) {
                            // findViewById(R.id.dt_load).setVisibility(View.VISIBLE);

                        }
                    }
                }
                shAdapter.notifyDataSetChanged();
            }
            shAdapter.notifyDataSetChanged();
        }

        ;
    };

    /**
     * 滑动菜单
     */

    private void initSlidingMenu() {

        // 设置抽屉菜单
        slidingMenu = new SlidingMenu(this);
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 触摸边界拖出菜单

        slidingMenu.setMenu(R.layout.slidingmenu_left);
        slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right);

        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度

        slidingMenu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片
        slidingMenu.setSecondaryShadowDrawable(R.drawable.shadow);
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影图片的宽度

        slidingMenu.setFadeDegree(0.35F);// SlidingMenu滑动时的渐变程度
        slidingMenu.setBehindScrollScale(0.3f);// 设置slidingMenu与下方视图的移动速度比（0.0f-1.0f）

        // 将抽屉菜单与主页面关联起来
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    }

    class ItemClick implements OnItemClickListener {
        private int i;

        public ItemClick(int i) {
            this.i = i;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {

            switch (i) {
                case 0:
                    YuanMa Ym = ymList.get(position - 1);
                    Intent intent0 = new Intent(MainActivity.this,
                            SourceActivity.class);
                    Log.e("luwei", Ym.getContent_url());
                    intent0.putExtra("content_url", Ym.getContent_url());
                    startActivity(intent0);
                    break;
                case 1:
                    XiaoJingYan jy = xiaoJYList.get(position - 2);
                    Intent intent1 = new Intent(MainActivity.this, JYchild.class);
                    intent1.putExtra("content_url", jy.getContent_url());
                    intent1.putExtra("desc", jy.getDesc());
                    intent1.putExtra("title", jy.getTitle());
                    startActivity(intent1);
                    break;
                default:
                    break;
            }

        }

    }

    private void initViewLogin() {
        new LoginRegisterListener(slidingMenu, MainActivity.this, handler);
        tx = (RelativeLayout) findViewById(R.id.tx);
        slidingMenu.findViewById(R.id.userHead).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        new PopupWindows(MainActivity.this, tx);
                        pictureFileDir = new File(Environment
                                .getExternalStorageDirectory(),
                                "/KuAiAndroid/upload/");

                        delete(pictureFileDir);
                        pictureFileDir.mkdirs();

                    }
                });
    }


    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            String result = msg.obj.toString();
            if (msg.what == 200 && null != result && !("nouser").equalsIgnoreCase(result) && !("nopass").equalsIgnoreCase(result)) {
                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                List<UserInfo> newList = myJson.getNearUserList(result);
                if (newList != null) {
                    Model.MYUSERINFO = newList.get(0);
                }
                LoginUtil.loginSetting(Model.LOGIN_FLAG.LOGIN_SUCCESS, MainActivity.this, slidingMenu);
                SharedPreferences sp = getSharedPreferences("UserInfo",
                        MODE_PRIVATE);
                SharedPreferences.Editor mSettingsEd = sp.edit();
                mSettingsEd.putString("UserInfoJson", result);
                // 提交保存
                mSettingsEd.commit();

            } else {
                LoginUtil.loginFail(MainActivity.this, msg);

            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2 && data != null) {
            NameValue = data.getStringExtra("NameValue");
            mName.setText(NameValue);
        } else if (requestCode == PIC_FROM_CAMERA) {
            Bitmap bitmap = decodeUriAsBitmap(photoUri);
            if (bitmap != null) {
                try {
                    cropImageUriByTakePhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == PIC_FROM＿LOCALPHOTO) {
            try {
                if (photoUri != null) {
                    Bitmap bitmap = decodeUriAsBitmap(photoUri);
                    sendImage(bitmap);
                    delete(pictureFileDir);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (slidingMenu.isMenuShowing()
                    || slidingMenu.isSecondaryMenuShowing()) {
                slidingMenu.showContent();
            } else {

                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                } else {

                    finish();
                }
            }
            return true;
        }
        // 拦截MENU按钮点击事件，让他无任何操作
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.Tag1:
                resetImg();
                vpViewPager.setCurrentItem(0);
                tag1_tv.setTextColor(Color.parseColor("#00CCFF"));
                tag1_iv.setImageResource(R.drawable.yuanbao);
                break;
            case R.id.Tag2:
                resetImg();
                vpViewPager.setCurrentItem(1);
                tag2_tv.setTextColor(Color.parseColor("#00CCFF"));
                tag2_iv.setImageResource(R.drawable.fenxiang);
                break;
            case R.id.Tag3:
                resetImg();
                vpViewPager.setCurrentItem(2);
                tag3_tv.setTextColor(Color.parseColor("#00CCFF"));
                tag3_iv.setImageResource(R.drawable.wujiaoxing);
                break;
            case R.id.yuanma:
                slidingMenu.showMenu();
                break;
            case R.id.bPersonal:
                slidingMenu.showSecondaryMenu();
//                break;
//            case R.id.UserLogout:
//                logout();

        }
    }


    private void resetImg() {
        tag1_tv.setTextColor(Color.parseColor("#7d7d7d"));
        tag2_tv.setTextColor(Color.parseColor("#7d7d7d"));
        tag3_tv.setTextColor(Color.parseColor("#7d7d7d"));
        tag1_iv.setImageResource(R.drawable.yb);
        tag2_iv.setImageResource(R.drawable.fx);
        tag3_iv.setImageResource(R.drawable.wjx);

    }


    /**
     * 初始化PullToRefreshListView<br>
     * 初始化在PullToRefreshListView中的ViewPager广告栏
     *
     * @param rtflv
     * @param adapter
     */
    public void initPullToRefreshListView(PullToRefreshListView rtflv,
                                          SmallAdapter adapter) {
        rtflv.setMode(Mode.BOTH);

        if (rtflv.getId() == R.id.small) {
            RelativeLayout rlAdv = (RelativeLayout) LayoutInflater.from(this)
                    .inflate(R.layout.sliding_advertisement, null);
            vpAdv = (AdvViewPager) rlAdv.findViewById(R.id.vpAdv);
            vg = (ViewGroup) rlAdv.findViewById(R.id.viewGroup);

            advs = new ArrayList<View>();
            ImageView iv;
            iv1 = new ImageView(this);

            iv1.setScaleType(ScaleType.FIT_XY);
            iv1.setBackgroundResource(R.drawable.jike);

            advs.add(iv1);

            iv2 = new ImageView(this);
            iv2.setScaleType(ScaleType.FIT_XY);
            iv2.setBackgroundResource(R.drawable.muke);

            advs.add(iv2);

            iv3 = new ImageView(this);
            iv3.setScaleType(ScaleType.FIT_XY);
            iv3.setBackgroundResource(R.drawable.zixuew);

            advs.add(iv3);

            iv4 = new ImageView(this);
            iv4.setScaleType(ScaleType.FIT_XY);
            iv4.setBackgroundResource(R.drawable.chuanke);

            advs.add(iv4);

            vpAdv.setAdapter(new AdvAdapter());
            vpAdv.setOnPageChangeListener(new OnPageChangeListener() {

                @Override
                public void onPageSelected(int arg0) {
                    currentPage = arg0;
                    for (int i = 0; i < advs.size(); i++) {
                        if (i == arg0) {
                            imageViews[i]
                                    .setBackgroundResource(R.drawable.banner_dian_focus);
                        } else {
                            imageViews[i]
                                    .setBackgroundResource(R.drawable.banner_dian_blur);
                        }
                    }
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            });

            imageViews = new ImageView[advs.size()];
            ImageView imageView;
            for (int i = 0; i < advs.size(); i++) {
                imageView = new ImageView(this);
                imageView.setLayoutParams(new LayoutParams(20, 20));
                imageViews[i] = imageView;
                if (i == 0) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.banner_dian_focus);
                } else {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.banner_dian_blur);
                }
                vg.addView(imageViews[i]);
            }

            rtflv.getRefreshableView().addHeaderView(rlAdv, null, false);

            final Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    vpAdv.setCurrentItem(msg.what);
                    super.handleMessage(msg);
                }
            };
            new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(5000);
                            currentPage++;
                            if (currentPage > advs.size() - 1) {
                                currentPage = 0;
                            }
                            handler.sendEmptyMessage(currentPage);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }


    class AdvAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return advs.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(advs.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(advs.get(position));
            return advs.get(position);
        }

    }


    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            super(mContext);

            View view = View.inflate(mContext, R.layout.item_popupwindowstx,
                    null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.xyh_fade_ins));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.xyh_push_bottom_in_2));

            setWidth(LayoutParams.FILL_PARENT);
            setHeight(LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            Button bt1 = (Button) view
                    .findViewById(R.id.item_popupwindows_camera);
            Button bt2 = (Button) view
                    .findViewById(R.id.item_popupwindows_Photo);
            Button bt3 = (Button) view
                    .findViewById(R.id.item_popupwindows_cancel);
            bt1.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    doHandlerPhoto(PIC_FROM_CAMERA);// 用户点击了从照相机获取
                    dismiss();
                }
            });
            bt2.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    doHandlerPhoto(PIC_FROM＿LOCALPHOTO);// 从相册中去获取
                    dismiss();
                }
            });
            bt3.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }


    /**
     * 根据不同方式选择图片设置ImageView
     *
     * @param type 0-本地相册选择，非0为拍照
     */
    private void doHandlerPhoto(int type) {
        try {

            File picFile = new File(pictureFileDir, "upload.png");

            photoUri = Uri.fromFile(picFile);

            if (type == PIC_FROM＿LOCALPHOTO) {
                Intent intent = getCropImageIntent();
                startActivityForResult(intent, PIC_FROM＿LOCALPHOTO);
            } else {
                Intent cameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(cameraIntent, PIC_FROM_CAMERA);
            }

        } catch (Exception e) {
            Log.i("HandlerPicError", "处理图片出现错误");
        }
    }


    /**
     * 调用图片剪辑程序
     */
    public Intent getCropImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        setIntentParams(intent);
        return intent;
    }


    /**
     * 启动裁剪
     */
    private void cropImageUriByTakePhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        setIntentParams(intent);
        startActivityForResult(intent, PIC_FROM＿LOCALPHOTO);

    }


    /**
     * 设置公用参数
     */
    private void setIntentParams(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    }


    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver()
                    .openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }


    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(
                android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    private void sendImage(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap roundCornerBitmap = toRoundCorner(bm, 150);
        roundCornerBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("img", img);
        params.add("uid", Model.MYUSERINFO.getUserid());
        client.post(Model.HTTPURL + "ImgUpload.php", params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Toast.makeText(MainActivity.this, "头像上传成功,请重新登陆",
                                Toast.LENGTH_LONG).show();

                        EditTextWithDel e = (EditTextWithDel) findViewById(R.id.accounts);
                        e.setText(Model.MYUSERINFO.getUname());

                        Model.MYUSERINFO = null;
                        SharedPreferences sp = getSharedPreferences("UserInfo",
                                MODE_PRIVATE);
                        Editor editor = sp.edit();
                        editor.clear();
                        editor.commit();
                        LoginUtil.loginSetting(Model.LOGIN_FLAG.LOGIN_FAIL, MainActivity.this, slidingMenu);

                    }

                    @Override
                    public void onFailure(int i, Header[] headers,
                                          byte[] bytes, Throwable throwable) {
                        Toast.makeText(MainActivity.this, "头像上传失败",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}
