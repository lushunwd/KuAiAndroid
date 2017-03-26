package sdnu.lushun.KuAiAndroid.adapter;

import java.io.File;
import java.util.List;

import sdnu.lushun.KuAiAndroid.Model;
import sdnu.lushun.KuAiAndroid.R;
import sdnu.lushun.KuAiAndroid.bean.XiaoJingYan;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SmallAdapter extends BaseAdapter {

	private Context context;
	private List<XiaoJingYan> newsList;

	public SmallAdapter(Context context, List<XiaoJingYan> newsList) {
		this.context = context;
		this.newsList = newsList;

	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public XiaoJingYan getItem(int arg0) {
		return newsList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {

		if (view == null) {
			view = LayoutInflater.from(context).inflate(
					R.layout.item_xiaojingyan, null);
		}
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		ImageView ivPic = (ImageView) view.findViewById(R.id.ivPic);
		

		XiaoJingYan news = newsList.get(arg0);

		
		// String temp=a.replace("\\n", "\n");
		tvTitle.setText(news.getTitle());
		tvDesc.setText(Html.fromHtml(news.getDesc()));
		tvTime.setText(news.getTime());

		String pic_url = news.getPic_url();
		// HttpUtils.setPicBitmap(ivPic, pic_url);

		if (!news.getPic_url().equals("")) {
			Picasso.with(context)
					.load(Model.SMALLIMG+File.separator+ news.getPic_url())
					.error(R.drawable.shop_photo_frame).into(ivPic);
		}


		return view;
	}

}
