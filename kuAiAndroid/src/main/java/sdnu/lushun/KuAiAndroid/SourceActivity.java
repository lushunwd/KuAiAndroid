package sdnu.lushun.KuAiAndroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sdnu.lushun.KuAiAndroid.adapter.SourceAdapter;
import sdnu.lushun.KuAiAndroid.bean.Source;
import sdnu.lushun.KuAiAndroid.net.HttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class SourceActivity extends Activity {
    public static final int HTTP_REQUEST_SUCCESS = -1;
    public static final int HTTP_REQUEST_ERROR = 0;
    private int tag = 1;
    private List<Source> sourceList;
    private SourceAdapter sAdapter;
    private String endUrl;
    public String XIAO = Model.HTTPURL;

    private PullToRefreshListView sourceLv;
    private boolean ONREFRESH = true;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_source);
        this.endUrl = getIntent().getStringExtra("content_url");
        Log.e("bn", endUrl);
        // this.endUrl = "xiaoJingYan.php";
        sourceLv = (PullToRefreshListView) findViewById(R.id.sourceLv);
        back = (ImageView) findViewById(R.id.S_Close);
        sourceList = new ArrayList<Source>();
        sourceLv.setMode(Mode.BOTH);
        sAdapter = new SourceAdapter(this, sourceList);

        sourceLv.setAdapter(sAdapter);

        HttpUtils.getJson(XIAO + endUrl + "?mstart=" + tag, getNewsHandler);
        sourceLv.setOnRefreshListener(new MyOnRefreshListener2(sourceLv, 1));
        sourceLv.setOnItemClickListener(new MyOnItem());
        MainActivity.initPullUp(sourceLv);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Handler getNewsHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            String jsonData = (String) msg.obj;
            if (jsonData.equalsIgnoreCase("null")) {
                Toast.makeText(SourceActivity.this, "已经没有了，亲", 1).show();
            }
            Log.e("weidan", jsonData);
            // System.out.println(jsonData);
            try {
                if (tag == 1) {
                    // xiaoJYList.clear();
                    sourceList.removeAll(sourceList);
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
                    sourceList.add(new Source(title, desc, time, content_url,
                            pic_url));

                }
                if (sourceList.size() != 0) {
                    findViewById(R.id.source_load).setVisibility(View.GONE);
                }
                tag++;
                ONREFRESH = true;
                // Toast.makeText(getApplicationContext(), tag + "", 0).show();
                sAdapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        ;
    };

    class MyOnItem implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            Source sc = sourceList.get(position - 1);
            Intent intent = new Intent(SourceActivity.this,
                    BrowseNewsActivity.class);
            intent.putExtra("content_url", sc.getContent_url());
            startActivity(intent);
        }

    }

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

                case 1:
                    if (reType == -1) {// 下拉刷新
                        if (ONREFRESH) {
                            tag = 1;
                            HttpUtils.getJson(XIAO + endUrl + "?mstart=" + tag,
                                    getNewsHandler);
                            ONREFRESH = false;
                        }

                    } else if (reType == -2) {
                        if (ONREFRESH) {
                            HttpUtils.getJson(XIAO + endUrl + "?mstart=" + tag,
                                    getNewsHandler);
                            ONREFRESH = false;
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

                    break;
                case HTTP_REQUEST_ERROR:
                    Toast.makeText(SourceActivity.this, "请检查网络", Toast.LENGTH_SHORT)
                            .show();
                    break;
            }
            mPtrlv.onRefreshComplete();
        }

    }

}
