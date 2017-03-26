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

import java.io.File;

public class BrowseNewsActivity extends Activity implements OnClickListener {

	public static WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_browser);

		webView = (WebView) findViewById(R.id.wv);
		findViewById(R.id.ymClose).setOnClickListener(this);
		String source_des = getIntent().getStringExtra("content_url");
		webView.loadUrl(Model.SOURCEHTML+File.separator + source_des);

		
		webView.setDownloadListener(new MyDownload());
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.clearCache(true);
		webView.getSettings().setBuiltInZoomControls(true);
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


	public boolean onKeyDown(int keyCode, KeyEvent event) {// 捕捉返回键

		finish();
		return true;
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
