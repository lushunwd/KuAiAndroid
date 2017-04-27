package sdnu.lushun.KuAiAndroid.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {
    private String userid;
    private String uname;
    private String uhead;
    private String utime;
    private String usex;

    public String getUsex() {
        return usex;
    }

    public void setUsex(String usex) {
        this.usex = usex;
    }

    public String getUserid() {
        return userid;

    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }


    public String getUtime() {
        return utime;
    }

    public void setUtime(String utime) {
        this.utime = utime;
    }


    public String getUhead() {
        return uhead;
    }

    public void setUhead(String uhead) {
        this.uhead = uhead;
    }


}
