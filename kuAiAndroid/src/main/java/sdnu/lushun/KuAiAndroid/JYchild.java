package sdnu.lushun.KuAiAndroid;

import sdnu.lushun.KuAiAndroid.util.AlwaysMarqueeTextView;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class JYchild extends Activity {
	private TextView tv;
	private WebView wv;
	private AlwaysMarqueeTextView tv_title;
	private ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_jychild);
		wv = (WebView) findViewById(R.id.small_wv);

		tv_title = (AlwaysMarqueeTextView) findViewById(R.id.small_text);
		tv = (TextView) findViewById(R.id.small_content);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		iv = (ImageView) findViewById(R.id.small_close);
		String tv_t = getIntent().getStringExtra("desc");
		String tv_title_t = getIntent().getStringExtra("title");
		String wv_content = getIntent().getStringExtra("content_url");
		if (wv_content.length() != 0) {
			wv.setBackgroundColor(0);
		}

		wv.loadDataWithBaseURL(null, wv_content, "text/html", "utf8", null);

		tv_title.setText(Html.fromHtml(tv_title_t));
		tv.setText(Html.fromHtml(tv_t));
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
