package com.example.dkdus.cashbook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.dkdus.cashbook.model.MyChildViewHolder;
import com.example.dkdus.cashbook.model.MyParentViewHolder;
import com.example.dkdus.cashbook.model.ParentList;

import java.util.ArrayList;
import java.util.List;

public class ExpandAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<ParentList> parentList;
    private LayoutInflater inflater;

    public ExpandAdapter(Context context, List<ParentList> parentList) {
        this.context = context;
        this.parentList = parentList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return parentList.get(groupPosition).items.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return parentList.get(groupPosition).items.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        MyParentViewHolder holder;
        if(convertView == null) {
            holder = new MyParentViewHolder();
            convertView = inflater.inflate(R.layout.item_parent, null);

            TextView groupName = (TextView) convertView.findViewById(R.id.listParent);
            holder.listGroup = groupName;
            convertView.setTag(holder);
        }else{
            holder = (MyParentViewHolder) convertView.getTag();
        }

        holder.listGroup.setText(parentList.get(groupPosition).title);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        MyChildViewHolder holder;
        if(convertView == null){
            holder = new MyChildViewHolder();
            convertView = inflater.inflate(R.layout.item_child, null);

            TextView money = (TextView)convertView.findViewById(R.id.money);
            TextView category = (TextView)convertView.findViewById(R.id.category);
            TextView contents = (TextView)convertView.findViewById(R.id.contents);

            holder.category = category;
            holder.money = money;
            holder.contents = contents;
            convertView.setTag(holder);
        } else{
            holder = (MyChildViewHolder) convertView.getTag();
        }

        holder.money.setText(parentList.get(groupPosition).items.get(childPosition).money);
        holder.category.setText(parentList.get(groupPosition).items.get(childPosition).category);
        holder.contents.setText(parentList.get(groupPosition).items.get(childPosition).contents);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
