package sdnu.lushun.KuAiAndroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import sdnu.lushun.KuAiAndroid.net.HttpPostThread;
import sdnu.lushun.KuAiAndroid.net.ThreadPoolUtils;
import sdnu.lushun.KuAiAndroid.util.Bimp;
import sdnu.lushun.KuAiAndroid.util.FileUtils_a;

import static sdnu.lushun.KuAiAndroid.Model.IMGUPLOAD;
import static sdnu.lushun.KuAiAndroid.util.NetUtils.isNetConnected;

public class UploadActivity extends Activity {
	private ImageView mClose;
	private EditText mNeirongEdit;
	private String data = "";
	private TextView textSend;
	private GridView noScrollgridview;
	private GridAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upload);
		initView();
	}

	private void initView() {
		mClose = (ImageView) findViewById(R.id.close);
		textSend = (TextView) findViewById(R.id.text_Send);
		mNeirongEdit = (EditText) findViewById(R.id.neirongEdit);
		MyOnclickListener mOnclickListener = new MyOnclickListener();
		mClose.setOnClickListener(mOnclickListener);
		textSend.setOnClickListener(mOnclickListener);
		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (arg2 == Bimp.bmp.size()) {
					new PopupWindows(UploadActivity.this, noScrollgridview);
				} else {
					Intent intent = new Intent(UploadActivity.this, PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}

	private class MyOnclickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			int ID = v.getId();
			switch (ID) {
			case R.id.close:
				UploadActivity.this.finish();
				break;
			case R.id.text_Send:
				if (Model.MYUSERINFO != null) {
					sendMessage();
				}
				break;
			}
		}
	}

	private void sendMessage() {

		if (mNeirongEdit.getText().toString().equals("")) {
			Toast.makeText(UploadActivity.this, "请填写文字再提交", Toast.LENGTH_SHORT).show();
			return;
		}

		List<String> list = new ArrayList<String>();
		String Simg = "";
		for (int i = 0; i < Bimp.drr.size(); i++) {
			String Str = Bimp.drr.get(i).substring(Bimp.drr.get(i).lastIndexOf("/") + 1, Bimp.drr.get(i).lastIndexOf("."));
			list.add(FileUtils_a.SDPATH + Str + ".JPEG");
			String fileName= System.currentTimeMillis()+"";
			Simg = Simg + fileName+".png" + ",";
			uploadFile(list.get(i), fileName);
		}
		if (!Simg.equals("")){
			Simg = Simg.substring(0, Simg.length() - 1);
		}


		String uid = Model.MYUSERINFO.getUserid();
		String tid = "1";
		String qvalue = mNeirongEdit.getText().toString();
		String qimg = "";
		if (!data.equalsIgnoreCase("")) {
			qimg = System.currentTimeMillis() + ".png";
		}


		String Json = "{\"uid\":\"" + uid + "\"," + "\"aimg\":\"" + Simg
				+ "\"," + "\"tid\":\"" + tid + "\"," + "\"qimg\":\"" + ""
				+ "\"," + "\"qvalue\":\"" + qvalue + "\"," + "\"qlike\":\"0\","
				+ "\"qunlike\":\"0\"}";
		ThreadPoolUtils.execute(new HttpPostThread(hand, Model.ADDVALUE, Json, ""));
		Simg = "";
		Bimp.bmp.clear();
		Bimp.drr.clear();
		Bimp.max = 0;
		FileUtils_a.deleteDir();
		adapter.notifyDataSetChanged();

		UploadActivity.this.finish();
	}

	Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				String result = (String) msg.obj;
				if (isNetConnected(UploadActivity.this) && result.equals("ok")) {
					Toast.makeText(UploadActivity.this, "上传成功", Toast.LENGTH_LONG).show();
					UploadActivity.this.finish();
				}
			}
		};
	};

	private void uploadFile(String s,String l) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		try {
			URL url = new URL(IMGUPLOAD);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			/* 设置StrictMode 否则HTTPURLConnection连接失败，因为这是在主进程中进行网络连接 */
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			//String Str = s.substring(s.lastIndexOf("/") + 1, s.lastIndexOf("."));
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\";filename=\"" + l + ".png" + "\"" + end);
			ds.writeBytes(end);
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(s);
			/* 设置每次写入8192bytes */
			int bufferSize = 8192;
			byte[] buffer = new byte[bufferSize]; // 8k
			int length = -1;
			while ((length = fStream.read(buffer)) != -1) {
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			fStream.close();
			ds.close();
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
		} catch (Exception e) {
			Toast.makeText(UploadActivity.this, "Fail:" + e, Toast.LENGTH_SHORT).show();
		}
	}


	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update1() {
				new Thread(new Runnable() {
					public void run() {
						while (true) {
							if (Bimp.max == Bimp.drr.size()) {
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
								break;
							} else {
								try {
									String path = Bimp.drr.get(Bimp.max);
									System.out.println(path);
									Bitmap bm = Bimp.revitionImageSize(path);
									Bimp.bmp.add(bm);
									String newStr = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
									FileUtils_a.saveBitmap(bm, "" + newStr);
									Bimp.max += 1;
									Message message = new Message();
									message.what = 1;
									handler.sendMessage(message);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}).start();

		}

		public int getCount() {
			return (Bimp.bmp.size() + 1);
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida, parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.image.setVisibility(View.VISIBLE);

			if (position == Bimp.bmp.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));

			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			if (position == 5) {
				holder.image.setVisibility(View.GONE);
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};
	}



	protected void onRestart() {
		adapter.update1();
		super.onRestart();
	}

	public class PopupWindows extends PopupWindow {
		public PopupWindows(Context mContext, View parent) {
			super(mContext);
			View view = View.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.xyh_fade_ins));
			LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.xyh_push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					photo();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(UploadActivity.this, TestPicActivity.class);
					startActivity(intent);
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}


	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	public void photo() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			File dir = new File(Environment.getExternalStorageDirectory() + "/myimage/");
			if (!dir.exists())
				dir.mkdirs();

			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File file = new File(dir, String.valueOf(System.currentTimeMillis()) + ".jpg");
			path = file.getPath();
			Uri imageUri = Uri.fromFile(file);
			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			openCameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			startActivityForResult(openCameraIntent, TAKE_PICTURE);
		} else {
			Toast.makeText(UploadActivity.this, "没有储存卡", Toast.LENGTH_LONG).show();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < 5 && resultCode == -1) {
				Bimp.drr.add(path);
			}
			break;
		}
	}

}
