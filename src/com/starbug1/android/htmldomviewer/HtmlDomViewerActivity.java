package com.starbug1.android.htmldomviewer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class HtmlDomViewerActivity extends Activity {
	
	final Handler handler_ = new Handler();
	private SharedPreferences pref_;
	AutoCompleteTextView textUrl_ = null;
	WebView browser_ = null;
	ImageButton jump_ = null;
	List<ConsoleMessage> jsConsoleMessages_ = new ArrayList<ConsoleMessage>();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		pref_ = PreferenceManager.getDefaultSharedPreferences(this);
		browser_ = (WebView) this.findViewById(R.id.webView);
		jump_ = (ImageButton)this.findViewById(R.id.button_jump);
		textUrl_ = (AutoCompleteTextView)this.findViewById(R.id.text_url);
        
		final WebView browser = (WebView) this.findViewById(R.id.webView);
		
		textUrl_.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				jump_.performClick();
			}
		});
		
		browser.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
				Log.d("HtmlDomViewerActivity", "console message: " + sourceID + "(" + lineNumber + ") " + message);
				final ConsoleMessage consoleMessage = new ConsoleMessage(message, lineNumber, sourceID);
				jsConsoleMessages_.add(consoleMessage);
				handler_.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(HtmlDomViewerActivity.this, consoleMessage.toString(), Toast.LENGTH_LONG).show();
					}
				});
				super.onConsoleMessage(message, lineNumber, sourceID);
			}
		});
		
		browser.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url,
					Bitmap favicon) {
				Log.d("NewsDetailActivity", "onPageStarted url: " + url);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view,
					String url) {
				Log.d("NewsDetailActivity",
						"shouldOverrideUrlLoading url: " + url);
				addUrl(url);
				textUrl_.setText(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		WebSettings ws = browser.getSettings();
		ws.setBuiltInZoomControls(true);
		ws.setLoadWithOverviewMode(true);
		ws.setPluginsEnabled(true);
		ws.setUseWideViewPort(true);
		ws.setJavaScriptEnabled(true);
		ws.setAppCacheMaxSize(1024 * 1024 * 64); //64MB
		ws.setCacheMode(WebSettings.LOAD_DEFAULT);
		ws.setDomStorageEnabled(true);
		ws.setAppCacheEnabled(true);
		ws.setGeolocationEnabled(false);
		browser.setVerticalScrollbarOverlay(true);
		browser_.addJavascriptInterface(new JsObj(), "___android___");

		jump_.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
		        browser_.requestFocus();
		        
				String url = textUrl_.getText().toString();
				browser.loadUrl(url);
				addUrl(url);
			}
		});
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, getUrls());
		textUrl_.setAdapter(adapter);
    }

    private void addUrl(String url) {
		SharedPreferences.Editor editor =  pref_.edit();
		editor.putString(url, "");
		editor.commit();
    }
    private List<String> getUrls() {
		Map<String, ?> prefs = pref_.getAll();
		Set<String> keys = prefs.keySet();
		List<String> urls = new LinkedList<String>();
		for (String url : keys) {
			if (!url.startsWith("pref_")) {
				urls.add(url);
			}
		}
		return urls;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_view_source:
			viewSource();
			break;
		case R.id.menu_view_source_by_external_editor:
			viewSourceByExternalEditor();
			break;
		case R.id.menu_console:
			console();
			break;
		}
		return true;
	}

	private void console() {
		StringBuilder consoleLog = new StringBuilder();
		for (ConsoleMessage m : jsConsoleMessages_) {
			consoleLog.append(m.toString() + "\n");
		}
		Intent intent = new Intent(HtmlDomViewerActivity.this, ConsoleLogActivity.class);
		intent.setType("text/plain");
		intent.putExtra("consoleLog", consoleLog.toString());
		HtmlDomViewerActivity.this.startActivity(intent);
	}
	
	private void viewSource() {
		browser_.loadUrl("javascript:___android___.vewSource(document.body.parentElement.outerHTML);");
	}

	private void viewSourceByExternalEditor() {
		browser_.loadUrl("javascript:___android___.viewSourceByExternalEditor(document.body.parentElement.outerHTML);");
	}

	public class JsObj {
		public void vewSource(String html) {
			Log.d("HtmlDomViewer", "html: " + html);
			
			Intent intent = new Intent(HtmlDomViewerActivity.this, SourceActivity.class);
			intent.setType("text/plain");
			intent.putExtra("html", html);
			HtmlDomViewerActivity.this.startActivity(intent);
		}
		public void viewSourceByExternalEditor(String html) {
			Log.d("HtmlDomViewer", "html: " + html);
			
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT, html);
			HtmlDomViewerActivity.this.startActivity(Intent.createChooser(
					intent, "共有"));
		}
	}
}