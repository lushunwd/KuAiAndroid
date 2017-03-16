package sdnu.lushun.KuAiAndroid.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import sdnu.lushun.KuAiAndroid.Model;
import sdnu.lushun.KuAiAndroid.R;
import sdnu.lushun.KuAiAndroid.net.HttpPostThread;
import sdnu.lushun.KuAiAndroid.net.ThreadPoolUtils;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lushun on 2017/3/16.
 */

public class LoginUtil {

    /**
     * 执行登录
     *
     * @param username
     * @param password
     * @param handler
     * @param activity
     */
    public static void login(String username, String password, Handler handler, Activity activity) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uname", username);
            jsonObject.put("upassword", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (NetUtils.isNetConnected(activity)) {
            ThreadPoolUtils.execute(new HttpPostThread(handler, Model.LOGIN, jsonObject.toString()));
        } else {
            Toast.makeText(activity, "请检查网络设置", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 登录失败逻辑
     *
     * @param context
     * @param msg
     */
    public static void loginFail(Context context, Message msg) {
        switch (msg.what) {

            case 404:
                Toast.makeText(context, "请求失败，服务器故障", Toast.LENGTH_LONG).show();
                break;
            case 100:
                Toast.makeText(context, "服务器无响应", Toast.LENGTH_LONG).show();
                break;
            case 200:
                String result = (String) msg.obj;
                if (result == null) {
                    Toast.makeText(context, "服务器请求失败", Toast.LENGTH_LONG)
                            .show();
                } else if (result.equalsIgnoreCase("NOUSER")) {
                    Toast.makeText(context, "用户名不存在", Toast.LENGTH_LONG).show();
                } else if (result.equalsIgnoreCase("NOPASS")) {
                    Toast.makeText(context, "密码错误", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                Toast.makeText(context, "登录失败！", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 退出出登录
     * @param activity
     * @param slidingMenu
     */
    private static void logout(final Activity activity, final SlidingMenu slidingMenu) {
        if (Model.MYUSERINFO != null) {
            new AlertDialog.Builder(activity)
                    .setMessage("确认退出登录?")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    Model.MYUSERINFO = null;
                                    SharedPreferences sp = activity.getSharedPreferences(
                                            "UserInfo", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.clear();
                                    editor.commit();
                                    // 退出登录
                                    loginSetting(Model.LOGIN_FLAG.LOGIN_FAIL, activity, slidingMenu);

                                }
                            }).setNegativeButton("取消", null).create().show();
        }
    }


    /**
     * 登录状态改变后设置相关内容
     * @param flag
     * @param activity
     * @param slidingMenu
     */
    public static void loginSetting(Model.LOGIN_FLAG flag, final Activity activity, final SlidingMenu slidingMenu) {

        switch (flag) {
            case LOGIN_SUCCESS:
                slidingMenu.findViewById(R.id.denglu).setVisibility(View.INVISIBLE);
                slidingMenu.findViewById(R.id.UserLogout).setVisibility(View.VISIBLE);
                slidingMenu.findViewById(R.id.loginSuccess).setVisibility(View.VISIBLE);
                ((TextView) slidingMenu.findViewById(R.id.userName)).setText(Html
                        .fromHtml("贵用户" + "<font color=#ff0000>"
                                + Model.MYUSERINFO.getUname() + "</font>"
                                + "，您已经成功登录"));
                ImageView userHead = (ImageView) slidingMenu.findViewById(R.id.userHead);

                if (!Model.MYUSERINFO.getUhead().equals("")) {
                    Picasso.with(activity)
                            .load(Model.USERHEADURL + Model.MYUSERINFO.getUhead())
                            .error(R.drawable.user).into(userHead);
                }
                slidingMenu.findViewById(R.id.UserLogout).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        logout(activity, slidingMenu);
                    }
                });
                break;
            case LOGIN_FAIL:
                slidingMenu.findViewById(R.id.loginSuccess).setVisibility(View.INVISIBLE);
                slidingMenu.findViewById(R.id.UserLogout).setVisibility(View.INVISIBLE);
                slidingMenu.findViewById(R.id.denglu).setVisibility(View.VISIBLE);
                break;

        }

    }

}
