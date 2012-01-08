package com.starbug1.android.htmldomviewer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ConsoleLogActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        setContentView(R.layout.console_log);
		if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent == null) {
            	Log.e("SourceActivity", "no intent.");
            	return;
            }
            TextView message = (TextView)this.findViewById(R.id.txt_message);
            message.setText(intent.getStringExtra("consoleLog"));
		}
	}

}
