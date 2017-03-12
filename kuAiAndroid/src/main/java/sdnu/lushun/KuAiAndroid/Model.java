package sdnu.lushun.KuAiAndroid;

import sdnu.lushun.KuAiAndroid.bean.UserInfo;

public class Model {
    public static String HTTPURL = "http://10.0.2.2/KuAiAndroid/";
    public static String SHARE = "share.php?";
    public static String GETUSER = "getuser.php?";
    public static String ADDVALUE = "addvalue.php";
    public static String REGISTET = "adduser.php";
    public static final String LikeOrUnlike = "LikeOrUlike.php";
    public static String LOGIN = "login.php";
    public static String USERHEADURL = HTTPURL + "Userimg/";
    public static String QIMGURL = HTTPURL + "Valueimg/";
    public static boolean IMGFLAG = false;
    public static UserInfo MYUSERINFO = null;
    // APP客服KEY
    public static String APPKEY = "f241ebf4d4a1e1dfae6f2a3e49aad2f1";
    public static final String APP_KEY = "3987368837";

    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";

}
