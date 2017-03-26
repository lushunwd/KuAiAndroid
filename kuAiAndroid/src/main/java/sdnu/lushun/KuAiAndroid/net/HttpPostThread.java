package sdnu.lushun.KuAiAndroid.net;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 网络Post请求的线程
 */
public class HttpPostThread implements Runnable {

    private Handler hand;
    private String url;
    private String value;
    private String img = "";
    private MyPost myPost = new MyPost();

    public HttpPostThread(Handler hand, String url, String value, String img) {
        this.hand = hand;
        this.url = url;
        this.value = value;
        this.img = img;
    }

    public HttpPostThread(Handler hand, String url, String value) {
        this.hand = hand;
        // 拼接访问服务器完整的地址
        this.url = url;
        this.value = value;
    }

    @Override
    public void run() {
        // 获取我们回调主ui的message
        Message msg = hand.obtainMessage();
        String result = null;
//        if (img.equalsIgnoreCase("")) {
//            result = myPost.doPost(url, value);
//        } else {
            result = myPost.doPost(url, img, value);
//        }
        msg.what = 200;
        msg.obj = result;

        hand.sendMessage(msg);

    }

}
