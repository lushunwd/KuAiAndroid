package sdnu.lushun.KuAiAndroid.listener;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import sdnu.lushun.KuAiAndroid.R;
import sdnu.lushun.KuAiAndroid.RegisterActivity;
import sdnu.lushun.KuAiAndroid.util.LoginUtil;

public class LoginRegisterListener implements View.OnClickListener {


    private Button mLogin;
    private EditText mName, mPassword;
    private String NameValue = null;
    private String PasswordValue = null;
    private RelativeLayout mRegister;
    private RelativeLayout tx;

    private SlidingMenu slidingMenu;
    private Activity activity;
    private Handler handler;


    public LoginRegisterListener(SlidingMenu slidingMenu, Activity activity, Handler handler) {
        this.slidingMenu = slidingMenu;
        this.activity = activity;
        this.handler = handler;
        initViewLogin();
    }


    private void initViewLogin() {

        mLogin = (Button) slidingMenu.findViewById(R.id.login);
        mName = (EditText) slidingMenu.findViewById(R.id.accounts);
        mPassword = (EditText) slidingMenu.findViewById(R.id.password);
        mRegister = (RelativeLayout) slidingMenu.findViewById(R.id.newUserRegister);
        tx = (RelativeLayout) slidingMenu.findViewById(R.id.tx);
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        tx.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int mId = v.getId();
        switch (mId) {
            case R.id.login:
                NameValue = mName.getText().toString();
                PasswordValue = mPassword.getText().toString();
                if (NameValue.equalsIgnoreCase(null)
                        || PasswordValue.equalsIgnoreCase(null)
                        || NameValue.equals("") || PasswordValue.equals("")) {
                    Toast.makeText(activity, "请填写帐号或密码", Toast.LENGTH_LONG).show();
                } else {
                    LoginUtil.login(NameValue, PasswordValue, handler, activity);
                }
                break;

            case R.id.newUserRegister:
                Intent intent = new Intent(activity, RegisterActivity.class);
                activity.startActivityForResult(intent, 1);
                break;
        }
    }
}