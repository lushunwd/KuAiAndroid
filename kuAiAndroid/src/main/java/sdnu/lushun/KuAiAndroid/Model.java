package sdnu.lushun.KuAiAndroid;

import java.io.File;

import sdnu.lushun.KuAiAndroid.bean.UserInfo;

public class Model {
    //public static final String HTTPURL = "http://10.0.2.2:8080/";
    public static final String HTTPURL = "http://10.191.53.128:8080";
    public static final String CODE =HTTPURL + File.separator+ "code";
    public static final String SHARE =HTTPURL+File.separator+ "share";
    public static final String ADDVALUE = HTTPURL+File.separator+"addValue";
    public static final String REGISTET = HTTPURL+File.separator+"register";
    public static final String LikeOrUnlike =HTTPURL+File.separator+ "thumb";
    public static final String LOGIN = HTTPURL+File.separator+"login";
    public static final String USERHEADURL = HTTPURL + File.separator+"static"+File.separator+"userImg";
    public static final String HEADIMGUPLOAD = Model.HTTPURL +File.separator+ "headUpload";
    public static final String LOVE = HTTPURL + File.separator+"love";
    public static final String SMALL = HTTPURL + File.separator+"small";
    public static final String SMALLIMG = HTTPURL + File.separator+"static"+File.separator+"smallImg";
    public static final String RECOMMENDIMG = HTTPURL + File.separator+"static"+File.separator+"recommendImg";
    public static final String VALUEIMG = HTTPURL + File.separator+"static"+File.separator+"valueImg";
    public static final String SOURCE = HTTPURL + File.separator+"source";
    public static final String SOURCEIMG = HTTPURL + File.separator+"static"+File.separator+"yuanmaImg";
    public static final String SOURCEHTML = HTTPURL + File.separator+"static"+File.separator+"yuanma";
    public static final String ADV = HTTPURL + File.separator+"adv";
    public static final String ADVIMG = HTTPURL + File.separator+"static"+File.separator+"ggb";

    public static final String IMGUPLOAD = HTTPURL +File.separator+ "imgUpload";

    public static boolean IMGFLAG = false;
    public static UserInfo MYUSERINFO = null;

    public enum LOGIN_FLAG {
        LOGIN_SUCCESS, LOGIN_FAIL;
    }

    public int LOGIN_SUCCESS = 1;
    public int LOGIN_FAIL = 0;

}
