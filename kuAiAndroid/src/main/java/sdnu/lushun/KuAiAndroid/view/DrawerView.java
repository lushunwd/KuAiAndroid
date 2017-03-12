package sdnu.lushun.KuAiAndroid.view;

import sdnu.lushun.KuAiAndroid.R;
import android.app.Activity;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class DrawerView {

	private final Activity activity;
	SlidingMenu slidingMenu;

	public DrawerView(Activity activity) {
		this.activity = activity;

	}

	public SlidingMenu initSlidingMenu() {

		slidingMenu = new SlidingMenu(activity);
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN); // 触摸边界拖出菜单
		slidingMenu.setShadowWidthRes(R.dimen.shadow_width);// 设置阴影图片的宽度
		slidingMenu.setShadowDrawable(R.drawable.shadow);// 设置阴影图片
		slidingMenu.setSecondaryShadowDrawable(R.drawable.shadow);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// SlidingMenu划出时主页面显示的剩余宽度
		slidingMenu.setFadeDegree(0.35F);// SlidingMenu滑动时的渐变程度
		slidingMenu.setMenu(R.layout.slidingmenu_left);
		slidingMenu.setSecondaryMenu(R.layout.slidingmenu_right);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		return slidingMenu;

	}

}
