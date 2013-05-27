package com.five.air.browser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class MainActivity extends Activity {

	private WebView wView;
	private ProgressBar load_bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		wView = (WebView) findViewById(R.id.web_view);
		load_bar = (ProgressBar) findViewById(R.id.load_bar);
		LinearLayout main_web = (LinearLayout) findViewById(R.id.lt_main_web);
		Intent mIntent = getIntent();
		String url = "";
		if (mIntent != null) {
			Bundle extras=mIntent.getExtras();
			if(extras!=null){
				url+=extras.getString("app_url");
			}
		}

		wView.getSettings().setJavaScriptEnabled(true);

		wView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different
				// scales.
				// The progress meter will automatically disappear when we reach
				// 100%
				load_bar.setProgress(progress);
				if (progress > 98)
					load_bar.setVisibility(View.GONE);
			}
		});

		wView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				wView.loadUrl(url);
				load_bar.setVisibility(View.VISIBLE);
				return true;
			}
		});

		wView.loadUrl(url);

	}

}
