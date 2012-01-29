package com.starbug1.android.htmldomviewer;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import pl.polidea.treeview.InMemoryTreeStateManager;
import pl.polidea.treeview.TreeBuilder;
import pl.polidea.treeview.TreeStateManager;
import pl.polidea.treeview.TreeViewList;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SourceActivity extends Activity {
    private final Set<Element> selected = new HashSet<Element>();

    private static final String TAG = SourceActivity.class.getSimpleName();
    private TreeViewList source_;

    private static int maxLevel = 4;
    private TreeStateManager<Element> manager_ = null;
    private SimpleStandardAdapter simpleAdapter_;
    private boolean collapsible_;
	final Handler handler_ = new Handler();
	private ProgressDialog progressDialog_;
    boolean newCollapsible_;
    String html_ = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent == null) {
            	Log.e("SourceActivity", "no intent.");
            	return;
            }
            html_ = intent.getStringExtra("html");

        }
        setContentView(R.layout.source);
        source_ = (TreeViewList) SourceActivity.this.findViewById(R.id.html_source);
        registerForContextMenu(source_);

        new Thread(new Runnable() {
			@Override
			public void run() {
		        manager_ = new InMemoryTreeStateManager<Element>();
		        final TreeBuilder<Element> treeBuilder = new TreeBuilder<Element>(manager_);
		        DomCreator dom = new DomCreator(treeBuilder);
		        maxLevel = dom.parse(new StringReader(html_));
				handler_.post(new Runnable() {
					@Override
					public void run() {

			            Log.d(TAG, manager_.toString());
			            newCollapsible_ = true;
				        simpleAdapter_ = new SimpleStandardAdapter(SourceActivity.this, selected, manager_,
				                maxLevel + 1);
				        source_.setAdapter(simpleAdapter_);
				        setCollapsible(newCollapsible_);
						if (progressDialog_ != null) {
							progressDialog_.dismiss();
						}
					}
				});
			}
		}).start();
		progressDialog_ = new ProgressDialog(SourceActivity.this);
		progressDialog_.setMessage("Processing... Please wait.");
		progressDialog_.show();
	}
	
    protected final void setCollapsible(final boolean newCollapsible) {
        this.collapsible_ = newCollapsible;
        source_.setCollapsible(this.collapsible_);
    }

}
