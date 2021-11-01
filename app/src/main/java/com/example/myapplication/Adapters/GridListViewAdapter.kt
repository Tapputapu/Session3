package com.example.myapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import com.example.myapplication.Activities.SeatInfo
import com.example.myapplication.databinding.ListSeatBinding
import java.lang.StringBuilder

class GridListViewAdapter(
    private val context: Context,
    private val resourceID: Int,
    private val items: List<SeatInfo>
) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return -1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return ListSeatBinding.inflate(LayoutInflater.from(context)).apply {
            val item = items[position]
            if (item.ID % 5 == 3) root.visibility = View.INVISIBLE
            switchButton.apply {
                text = item.Number
                textOff = item.Number
                textOn = item.Number
            }
            if (item.IsReserved) switchButton.isEnabled = false
            if (item.IsSelected) switchButton.isChecked = true
            switchButton.setOnClickListener {
                items.firstOrNull { it.IsSelected }?.IsSelected = false
                item.IsSelected = true
                notifyDataSetChanged()
            }
        }.root
    }
}