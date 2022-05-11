package com.example.okhttpissues.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.okhttpissues.R
import com.example.okhttpissues.data.database_entities.IssuesEntity

class IssuesListAdapter(context: Context, private val dataList: List<IssuesEntity>) :
    ArrayAdapter<IssuesEntity>(context, R.layout.list_item_layout, dataList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item_layout, null)

        val title: TextView = view.findViewById<TextView>(R.id.IssueTitle)
        val issueDescription: TextView = view.findViewById<TextView>(R.id.issueDescription)
        val userName: TextView = view.findViewById<TextView>(R.id.userName)
        val userImage: ImageView = view.findViewById<ImageView>(R.id.userImage)
        val updateDate: TextView = view.findViewById<TextView>(R.id.updateDate)

        Glide.with(context).load(dataList[position].userAvatar).into(userImage)
        userName.text = dataList[position].username
        title.text = dataList[position].title
        issueDescription.text = dataList[position].issueDescription
        updateDate.text = dataList[position].updatedOn

        return view
    }
}