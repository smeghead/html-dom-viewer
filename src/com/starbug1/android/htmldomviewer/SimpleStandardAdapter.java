package com.starbug1.android.htmldomviewer;

import java.util.Set;

import pl.polidea.treeview.AbstractTreeViewAdapter;
import pl.polidea.treeview.TreeNodeInfo;
import pl.polidea.treeview.TreeStateManager;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

/**
 * This is a very simple adapter that provides very basic tree view with a
 * checkboxes and simple item description.
 * 
 */
class SimpleStandardAdapter extends AbstractTreeViewAdapter<Element> {

    public SimpleStandardAdapter(final Activity activity,
            final Set<Element> selected,
            final TreeStateManager<Element> treeStateManager,
            final int numberOfLevels) {
        super(activity, treeStateManager, numberOfLevels);
    }

    @Override
    public View getNewChildView(final TreeNodeInfo<Element> treeNodeInfo) {
        final HorizontalScrollView viewLayout = (HorizontalScrollView) getActivity()
                .getLayoutInflater().inflate(R.layout.demo_list_item, null);
        return updateView(viewLayout, treeNodeInfo);
    }

    @Override
    public HorizontalScrollView updateView(final View view,
            final TreeNodeInfo<Element> treeNodeInfo) {
        final HorizontalScrollView viewLayout = (HorizontalScrollView) view;
        final TextView descriptionView = (TextView) viewLayout
                .findViewById(R.id.demo_list_item_description);
        descriptionView.setText(treeNodeInfo.getId().getContent());
        return viewLayout;
    }

    @Override
    public void handleItemClick(final View view, final Object id) {
        final TreeNodeInfo<Element> info = getManager().getNodeInfo((Element)id);
        if (info.isWithChildren()) {
            super.handleItemClick(view, id);
        }
    }

    @Override
    public long getItemId(final int position) {
    	Log.d("SimpleStandardAdapter", "getItemId position:" + position);
    	Element e =  getTreeId(position);
    	return e.getId();
    }
}