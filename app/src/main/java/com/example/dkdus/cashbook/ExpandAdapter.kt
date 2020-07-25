package com.example.dkdus.cashbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.example.dkdus.cashbook.model.MyChildViewHolder
import com.example.dkdus.cashbook.model.MyParentViewHolder
import com.example.dkdus.cashbook.model.ParentList

class ExpandAdapter(private val context: Context, private val parentList: List<ParentList>) : BaseExpandableListAdapter() {
    private val inflater: LayoutInflater

    override fun getGroupCount(): Int {
        return parentList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return parentList[groupPosition].items.size
    }

    override fun getGroup(groupPosition: Int): Any {
        return parentList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return parentList[groupPosition].items[childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val holder: MyParentViewHolder
        if (convertView == null) {
            holder = MyParentViewHolder()
            convertView = inflater.inflate(R.layout.item_parent, null)
            val groupName = convertView.findViewById<View>(R.id.listParent) as TextView
            holder.listGroup = groupName
            convertView.tag = holder
        } else {
            holder = convertView.tag as MyParentViewHolder
        }
        holder.listGroup!!.text = parentList[groupPosition].title
        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        val holder: MyChildViewHolder
        if (convertView == null) {
            holder = MyChildViewHolder()
            convertView = inflater.inflate(R.layout.item_child, null)
            val money = convertView.findViewById<View>(R.id.money) as TextView
            val category = convertView.findViewById<View>(R.id.category) as TextView
            val contents = convertView.findViewById<View>(R.id.contents) as TextView
            holder.category = category
            holder.money = money
            holder.contents = contents
            convertView.tag = holder
        } else {
            holder = convertView.tag as MyChildViewHolder
        }
        holder.money!!.text = parentList[groupPosition].items[childPosition].money
        holder.category!!.text = parentList[groupPosition].items[childPosition].category
        holder.contents!!.text = parentList[groupPosition].items[childPosition].contents
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}