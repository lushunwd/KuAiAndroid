package sdnu.lushun.KuAiAndroid.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import sdnu.lushun.KuAiAndroid.MainActivity;

public class AdvAdapter extends PagerAdapter {

    @Override
    public int getCount() {
        return MainActivity.advs.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(MainActivity.advs.get(position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(MainActivity.advs.get(position));
        return MainActivity.advs.get(position);
    }

}