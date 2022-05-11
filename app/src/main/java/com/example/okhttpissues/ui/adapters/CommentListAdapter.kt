package com.example.okhttpissues.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.okhttpissues.R
import com.example.okhttpissues.data.database_entities.CommentDataEntity
import com.example.okhttpissues.data.database_entities.IssuesEntity

class CommentListAdapter(context: Context, private val dataList: List<CommentDataEntity>) :
    ArrayAdapter<CommentDataEntity>(
        context,
        R.layout.comment_list_item_layout, dataList
    ) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.comment_list_item_layout, null)

        val description: TextView = view.findViewById<TextView>(R.id.descData)
        val userLoginName: TextView = view.findViewById<TextView>(R.id.userLoginName)

        description.text = dataList[position].body
        userLoginName.text = dataList[position].userName

        return view
    }
}