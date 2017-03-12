package sdnu.lushun.KuAiAndroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BrowseNewsActivity extends Activity implements OnClickListener {

	ProgressDialog pd;
	Handler handler;
	public static WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_browser);

		webView = (WebView) findViewById(R.id.wv);
		findViewById(R.id.ymClose).setOnClickListener(this);
		String source_des = getIntent().getStringExtra("content_url");
		webView.loadUrl(Model.HTTPURL + "yuanma/" + source_des);
		// webView.loadUrl("http://172.26.18.84:9096/KuAiAndroid/");

		// loadurl(webView, pic_url);
		// pd = new ProgressDialog(BrowseNewsActivity.this);
		// pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// pd.setMessage("数据载入中，请稍候！");
		// // Show/Hide message
		// handler = new Handler() {
		// public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
		// if (!Thread.currentThread().isInterrupted()) {
		// switch (msg.what) {
		// case 0:
		// pd.show();// 显示进度对话框
		// break;
		// case 1:
		// pd.hide();//
		// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
		// break;
		// }
		// }
		// super.handleMessage(msg);
		// }
		// };

		
		webView.setDownloadListener(new MyDownload());
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.clearCache(true);
		webView.getSettings().setBuiltInZoomControls(true);
		// webView.getSettings().setBlockNetworkImage(true);
		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onReceivedTitle(WebView view, String title) {

				super.onReceivedTitle(view, title);
			}

		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}

		});
	}

	class MyDownload implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimeType, long contentLength) {

			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);

		}

	}

	// public void loadurl(final WebView view, final String url) {
	// new Thread() {
	// public void run() {
	// handler.sendEmptyMessage(0);
	// view.loadUrl(url);// 载入网页
	// }
	// }.start();
	// }

	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键
		// if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
		// webView.goBack();
		// return true;
		// } else if (keyCode == KeyEvent.KEYCODE_BACK) {
		// ConfirmExit();// 按了返回键，但已经不能返回，则执行退出确认
		finish();
		return true;
		// }
		// return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ymClose:
			if (!webView.canGoBack()) {
				finish();
			} else {
				webView.goBack();
			}
			break;

		default:
			break;
		}

	}

}
