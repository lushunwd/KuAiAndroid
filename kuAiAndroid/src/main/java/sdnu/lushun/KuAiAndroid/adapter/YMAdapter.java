package sdnu.lushun.KuAiAndroid.adapter;

import java.io.File;
import java.util.List;

import sdnu.lushun.KuAiAndroid.Model;
import sdnu.lushun.KuAiAndroid.R;
import sdnu.lushun.KuAiAndroid.bean.YuanMa;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class YMAdapter extends BaseAdapter {
//	private String YMIMG = Model.HTTPURL + File.separator+ "yuanmaImg/";
	private Context context;
	private List<YuanMa> ymList;

	public YMAdapter(Context context, List<YuanMa> ymList) {
		this.context = context;
		this.ymList = ymList;

	}

	@Override
	public int getCount() {
		return ymList.size();
	}

	@Override
	public YuanMa getItem(int arg0) {
		return ymList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {

		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_yuanma,
					null);
		}
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		ImageView ivPic = (ImageView) view.findViewById(R.id.ivPic);

		YuanMa ym = ymList.get(arg0);
		tvTitle.setText(Html.fromHtml(ym.getTitle()));
		tvDesc.setText(ym.getDesc());
		tvTime.setText(ym.getTime());

		String pic_url = ym.getPic_url();


		if (!ym.getPic_url().equals("")) {
			Picasso.with(context).load(Model.SOURCEIMG+File.separator + ym.getPic_url())
					.error(R.drawable.shop_photo_frame).into(ivPic);
		}

		return view;
	}

}
