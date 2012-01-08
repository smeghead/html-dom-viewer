package com.starbug1.android.htmldomviewer;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;


import pl.polidea.treeview.InMemoryTreeStateManager;
import pl.polidea.treeview.TreeBuilder;
import pl.polidea.treeview.TreeStateManager;
import pl.polidea.treeview.TreeViewList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

public class SourceActivity extends Activity {
    private final Set<Element> selected = new HashSet<Element>();

    private static final String TAG = SourceActivity.class.getSimpleName();
    private TreeViewList source_;

    private static int maxLevel = 4;
    private TreeStateManager<Element> manager_ = null;
    private SimpleStandardAdapter simpleAdapter_;
    private boolean collapsible_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        boolean newCollapsible;
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent == null) {
            	Log.e("SourceActivity", "no intent.");
            	return;
            }
            String html = intent.getStringExtra("html");

            manager_ = new InMemoryTreeStateManager<Element>();
            final TreeBuilder<Element> treeBuilder = new TreeBuilder<Element>(manager_);
            DomCreator dom = new DomCreator(treeBuilder);
            maxLevel = dom.parse(new StringReader(html));

            Log.d(TAG, manager_.toString());
            newCollapsible = true;
        } else {
            manager_ = (TreeStateManager<Element>) savedInstanceState
                    .getSerializable("treeManager");
            newCollapsible = savedInstanceState.getBoolean("collapsible");
        }
        setContentView(R.layout.source);

        source_ = (TreeViewList) this.findViewById(R.id.html_source);
        simpleAdapter_ = new SimpleStandardAdapter(this, selected, manager_,
                maxLevel + 1);
        source_.setAdapter(simpleAdapter_);
        source_.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final TextView text = (TextView) view.findViewById(R.id.demo_list_item_description);

				// prepare the alert box
				AlertDialog.Builder alertbox = new AlertDialog.Builder(SourceActivity.this);

				alertbox.setTitle("Complete Line String");
		        // set the message to display
		        alertbox.setMessage(text.getText());

		        // add a neutral button to the alert box and assign a click listener
		        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
		
		            // click listener on the alert box
		            public void onClick(DialogInterface arg0, int arg1) {
		            }
		        });

		        // show it
		        alertbox.show();
				return false;
			}
		});

        setCollapsible(newCollapsible);
        registerForContextMenu(source_);
	}
	
    protected final void setCollapsible(final boolean newCollapsible) {
        this.collapsible_ = newCollapsible;
        source_.setCollapsible(this.collapsible_);
    }

}
