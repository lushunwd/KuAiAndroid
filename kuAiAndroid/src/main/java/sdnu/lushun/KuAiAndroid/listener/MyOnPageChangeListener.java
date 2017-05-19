package sdnu.lushun.KuAiAndroid.listener;

import android.graphics.Color;
import android.support.v4.view.ViewPager;

import sdnu.lushun.KuAiAndroid.MainActivity;
import sdnu.lushun.KuAiAndroid.R;

public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

    public static void  resetImg() {
        MainActivity.tag1_tv.setTextColor(Color.parseColor("#7d7d7d"));
        MainActivity.tag2_tv.setTextColor(Color.parseColor("#7d7d7d"));
        MainActivity.tag3_tv.setTextColor(Color.parseColor("#7d7d7d"));
        MainActivity.tag1_iv.setImageResource(R.drawable.yb);
        MainActivity.tag2_iv.setImageResource(R.drawable.fx);
        MainActivity.tag3_iv.setImageResource(R.drawable.wjx);
    }


    @Override
    public void onPageSelected(int arg0) {
        resetImg();
        switch (arg0) {
            case 0:
                MainActivity.tag1_tv.setTextColor(Color.parseColor("#00CCFF"));
                MainActivity.tag1_iv.setImageResource(R.drawable.yuanbao);
                break;
            case 1:
                MainActivity.tag2_tv.setTextColor(Color.parseColor("#00CCFF"));
                MainActivity.tag2_iv.setImageResource(R.drawable.fenxiang);
                break;
            case 2:
                MainActivity.tag3_tv.setTextColor(Color.parseColor("#00CCFF"));
                MainActivity.tag3_iv.setImageResource(R.drawable.wujiaoxing);
                break;
            default:
                break;
        }

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

}