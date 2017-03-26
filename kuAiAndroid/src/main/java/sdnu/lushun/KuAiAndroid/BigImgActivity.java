package sdnu.lushun.KuAiAndroid;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sdnu.lushun.KuAiAndroid.adapter.ViewPagerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

public class BigImgActivity extends Activity {

	RelativeLayout photo_relativeLayout;
	private ViewPager mViewPager;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	private int[] mImgIds = new int[] { R.drawable.muke, R.drawable.jike,
			R.drawable.zixuew };
	private List<ImageView> mImages = new ArrayList<ImageView>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bigimg);

		photo_relativeLayout = (RelativeLayout) findViewById(R.id.tuichu);
		photo_relativeLayout.setBackgroundColor(0x00000000);

		Intent intent = getIntent();
		int id_1 = intent.getIntExtra("ID", -1);
		String str = intent.getStringExtra("a");

		if (id_1 == -1) {
			id_1 = 1;
		}

		final String[] newstr = str.split(",");

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		// mViewPager.setPageTransformer(true, new DepthPageTransformer());
		mViewPager.setAdapter(new PagerAdapter() {
			@Override
			public Object instantiateItem(ViewGroup container, int position) {

				ImageView imageview = new ImageView(BigImgActivity.this);
				if (!newstr.equals("") && position < newstr.length) {
					Picasso.with(BigImgActivity.this)
							.load(Model.VALUEIMG+File.separator + newstr[position])
							.error(R.drawable.shop_photo_frame).into(imageview);
				}

				imageview.setScaleType(ScaleType.FIT_CENTER);
				container.addView(imageview);
				mImages.add(imageview);
				return imageview;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(mImages.get(position));
			}

			@Override
			public boolean isViewFromObject(View view, Object object) {
				return view == object;
			}

			@Override
			public int getCount() {
				return newstr.length;
			}
		});

		mViewPager.setOffscreenPageLimit(4);
		mViewPager.setCurrentItem(id_1);

		Button photo_bt_exit = (Button) findViewById(R.id.photo_bt_exit);
		photo_bt_exit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
	}
}
