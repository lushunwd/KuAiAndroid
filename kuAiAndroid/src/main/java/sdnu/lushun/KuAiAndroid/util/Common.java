package sdnu.lushun.KuAiAndroid.util;

import android.app.Activity;
import android.util.DisplayMetrics;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lushun on 2017/3/17.
 */

public class Common {
    public static void initPullUp(PullToRefreshListView lv) {
        ILoadingLayout endLabels = lv.getLoadingLayoutProxy(false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 获取屏幕相关参数
     */
    public static int getScreenParam(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels; // 屏幕宽度（像素）
    }

    public static String getLikeJson( String qid, String tag) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("qid", qid);
            jsonObject.put("tag", tag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
