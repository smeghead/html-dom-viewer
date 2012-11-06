package com.starbug1.android.htmldomviewer;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.polidea.treeview.InMemoryTreeStateManager;
import pl.polidea.treeview.TreeBuilder;
import pl.polidea.treeview.TreeNodeInfo;
import pl.polidea.treeview.TreeStateManager;
import pl.polidea.treeview.TreeViewList;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

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
				Log.e(TAG, "no intent.");
				return;
			}
			html_ = intent.getStringExtra("html");

		}
		setContentView(R.layout.source);

		if (html_ == null) {
			Log.e(TAG, "html string is null.");
			return;
		}
		source_ = (TreeViewList) SourceActivity.this
				.findViewById(R.id.html_source);
		registerForContextMenu(source_);

		new Thread(new Runnable() {
			@Override
			public void run() {
				manager_ = new InMemoryTreeStateManager<Element>();
				final TreeBuilder<Element> treeBuilder = new TreeBuilder<Element>(
						manager_);
				DomCreator dom = new DomCreator(treeBuilder);
				maxLevel = dom.parse(new StringReader(html_));
				handler_.post(new Runnable() {
					@Override
					public void run() {

						newCollapsible_ = true;
						simpleAdapter_ = new SimpleStandardAdapter(
								SourceActivity.this, selected, manager_,
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.source, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			Toast.makeText(this, "search.", Toast.LENGTH_LONG).show();
			searchDialog();
			break;
		}
		return true;
	}

	private void searchDialog() {
		final EditText editView = new EditText(SourceActivity.this);
		new AlertDialog.Builder(SourceActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Search for...")
				// setViewにてビューを設定します。
				.setView(editView)
				.setPositiveButton("Search",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 入力した文字をトースト出力する
								search(editView.getText().toString());
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).show();
	}

	private void search(String keyword) {
		List<Element> acc = new ArrayList<Element>();
		for (Element e : manager_.getVisibleList()) {
			acc = searchRec(keyword, e, acc);
		}
		for (Element e : acc) {
			expandParents(e);
		}
	}

	private void expandParents(Element element) {
		Element parent = manager_.getParent(element);
		if (parent != null) {
			manager_.expandDirectChildren(parent);
			expandParents(parent);
		}
	}

	private List<Element> searchRec(String keyword, Element parent,
			List<Element> acc) {
		TreeNodeInfo<Element> info = manager_.getNodeInfo(parent);
		parent.setHitLine(false); // 初期化
		if (parent.getContent().contains(keyword)) {
			parent.setHitLine(true);
			acc.add(parent);
		}
		if (info.isWithChildren()) {
			for (Element e : manager_.getChildren(parent)) {
				searchRec(keyword, e, acc);
			}
		}
		return acc;
	}
}
