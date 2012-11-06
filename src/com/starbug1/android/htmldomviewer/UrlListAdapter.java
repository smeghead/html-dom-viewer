package com.starbug1.android.htmldomviewer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class UrlListAdapter extends BaseAdapter implements Filterable {
	private static final String TAG = "UrlListAdapter";
	private List<String> urlsOriginal_ = new ArrayList<String>();
	private List<String> urls_ = new ArrayList<String>();
	private Context context_ = null;
	private final int resourceId_;
	private final LayoutInflater inflater_;

	public UrlListAdapter(Context context, int resourceId, List<String> urls) {
		context_ = context;
		resourceId_ = resourceId;
		inflater_ = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		urls_ = urlsOriginal_ = urls;
	}

	@Override
	public int getCount() {
		return urls_.size();
	}

	@Override
	public Object getItem(int position) {
		return urls_.get(position);
	}

	@Override
	public long getItemId(int position) {
		return urls_.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (view == null) {
			view = inflater_.inflate(resourceId_, null);
		}
		TextView text = (TextView) view;
		text.setText(getItem(position).toString());
		return text;
	}

	Filter filter = new Filter() {
		@Override
		public String convertResultToString(Object resultValue) {
			if (resultValue == null)
				return "";

			return resultValue.toString();
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			Log.d(TAG, "performFiltering");
			if (constraint == null)
				return null;
			String inputed = constraint.toString().toLowerCase();
			FilterResults filterResults = new FilterResults();
			List<String> matches = new ArrayList<String>();
			for (String v : urlsOriginal_) {
				if (v.toLowerCase().contains(inputed)) {
					matches.add(v);
				}
			}
			urls_ = matches; // = matches;
			filterResults.values = matches;// フィルタリング結果オブジェクト
			filterResults.count = matches.size();// フィルタリング結果件数
			return filterResults;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (results != null && results.count > 0) {
				// 再描画させる
				notifyDataSetChanged();
			}
		}
	};

	@Override
	public Filter getFilter() {
		Log.d(TAG, "getFilter");
		return filter;
	}

}
