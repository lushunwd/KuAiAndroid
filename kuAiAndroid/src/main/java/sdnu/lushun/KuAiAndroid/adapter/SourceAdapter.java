package sdnu.lushun.KuAiAndroid.adapter;

import java.io.File;
import java.util.List;

import sdnu.lushun.KuAiAndroid.Model;
import sdnu.lushun.KuAiAndroid.R;
import sdnu.lushun.KuAiAndroid.bean.Source;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SourceAdapter extends BaseAdapter {
	
	private Context context;
	private List<Source> souceList;
	//private String MURL=Model.HTTPURL+ File.separator+ "yuanmaImg/";
	public SourceAdapter(Context context,List<Source> sourceList){
		this.context = context;
		this.souceList = sourceList;
		
	}

	@Override
	public int getCount() {
		return souceList.size();
	}

	@Override
	public Source getItem(int arg0) {
		return souceList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		
		if(view == null){
			view = LayoutInflater.from(context).inflate(R.layout.item_source, null);
		}
		TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		TextView tvDesc = (TextView) view.findViewById(R.id.tvDesc);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		ImageView ivPic = (ImageView) view.findViewById(R.id.ivPic);
		
		Source sc = souceList.get(arg0);
		tvTitle.setText(sc.getTitle());
		tvDesc.setText(sc.getDesc());
		tvTime.setText(sc.getTime());
		
		String pic_url = sc.getPic_url();
		//HttpUtils.setPicBitmap(ivPic, pic_url);
	
		if(!sc.getPic_url().equals("")){
			Picasso.with(context).load(Model.SOURCEIMG+File.separator+sc.getPic_url()).error(R.drawable.shop_photo_frame).into(ivPic);
		}
		
		return view;
	}

}
