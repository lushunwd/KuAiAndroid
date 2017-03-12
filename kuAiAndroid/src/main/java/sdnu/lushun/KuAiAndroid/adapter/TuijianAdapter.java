package sdnu.lushun.KuAiAndroid.adapter;

import java.util.List;

import sdnu.lushun.KuAiAndroid.Model;
import sdnu.lushun.KuAiAndroid.R;
import sdnu.lushun.KuAiAndroid.bean.TuiJian;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class TuijianAdapter extends BaseAdapter {

	private Context context;
	private List<TuiJian> newsList;

	public TuijianAdapter(Context context, List<TuiJian> newsList) {
		this.context = context;
		this.newsList = newsList;

	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public TuiJian getItem(int arg0) {
		return newsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {

		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_tuijian,
					null);
		}
		TextView tvTitle = (TextView) view.findViewById(R.id.love_tvTitle);
		TextView tvDesc = (TextView) view.findViewById(R.id.love_tvDesc);
		WebView wv = (WebView) view.findViewById(R.id.love_wv);
		TextView tvTime = (TextView) view.findViewById(R.id.love_tvTime);
		ImageView ivPic = (ImageView) view.findViewById(R.id.love_ivPic);

//		wv.getSettings().setJavaScriptEnabled(true);
//		wv.getSettings().setSupportZoom(true);
	//	wv.clearCache(true);
	
		tvTitle.setMovementMethod(LinkMovementMethod.getInstance());
		tvDesc.setMovementMethod(LinkMovementMethod.getInstance());
		
		TuiJian news = newsList.get(arg0);
		tvTitle.setText(Html.fromHtml(news.getTitle()));
		tvDesc.setText(Html.fromHtml(news.getDesc()));
		if (news.getContent_url().length() != 0) {
			wv.setBackgroundColor(0);
		}
		wv.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return true;
			}
		});
		
		String tempContent=news.getContent_url().replace("hhost/", Model.HTTPURL+"tuijianImg/");
		
		wv.loadDataWithBaseURL(null, tempContent, "text/html",
				"utf8", null);
		tvTime.setText(news.getTime());

		String pic_url = news.getPic_url();
		// HttpUtils.setPicBitmap(ivPic, pic_url);

		if (!news.getPic_url().equals("")) {
			Picasso.with(context).load(news.getPic_url())
					.error(R.drawable.ic_launcher).into(ivPic);
		}

		return view;
	}

}
