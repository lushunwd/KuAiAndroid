package sdnu.lushun.KuAiAndroid;

import sdnu.lushun.KuAiAndroid.bean.UserInfo;

public class Model {
    public static String HTTPURL = "http://10.0.2.2/KuAiAndroid/";
   // public static String HTTPURL = "http://192.168.1.2/KuAiAndroid/";
    public static String SHARE = "share.php?";
    public static String ADDVALUE = "addvalue.php";
    public static String REGISTET = "adduser.php";
    public static final String LikeOrUnlike = "LikeOrUlike.php";
    public static String LOGIN = "login.php";
    public static String USERHEADURL = HTTPURL + "Userimg/";
    public static boolean IMGFLAG = false;
    public static UserInfo MYUSERINFO = null;

    public enum LOGIN_FLAG {
        LOGIN_SUCCESS, LOGIN_FAIL;
    }

    public int LOGIN_SUCCESS = 1;
    public int LOGIN_FAIL = 0;

}
