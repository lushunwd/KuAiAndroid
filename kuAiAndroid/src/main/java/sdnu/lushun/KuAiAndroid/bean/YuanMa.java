package sdnu.lushun.KuAiAndroid.bean;


public class YuanMa {

    private String title;
    private String desc;
    private String time;
    private String pic_url;
    private String type;


    public YuanMa(String title, String desc, String time, String type, String pic_url) {
        setTitle(title);
        setDesc(desc);
        setTime(time);
        setType(type);
        setPic_url(pic_url);
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
}
