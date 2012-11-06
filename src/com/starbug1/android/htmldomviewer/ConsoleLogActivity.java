package com.starbug1.android.htmldomviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ConsoleLogActivity extends Activity {
	private static final String TAG = "ConsoleLogActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.console_log);
		if (savedInstanceState == null) {
			Intent intent = getIntent();
			if (intent == null) {
				Log.e(TAG, "no intent.");
				return;
			}
			final TextView message = (TextView) this
					.findViewById(R.id.txt_message);
			message.setText(intent.getStringExtra("consoleLog"));

			final ImageView clear = (ImageView) this
					.findViewById(R.id.clear_console);
			clear.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("LOG_CLEAR", true);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		}
	}

}
