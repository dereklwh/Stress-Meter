package com.example.stressmeter.ui.home

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class ImageAdapter(private val context: Context) : BaseAdapter() {
    private var imageCollection: List<Int> = emptyList()
    private var currentGroup: Int = 1

    override fun getCount(): Int {
        return imageCollection.size
    }

    override fun getItem(position: Int): Any {
        return imageCollection[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val imageView = ImageView(context)
        imageView.setImageResource(imageCollection[position])
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        imageView.layoutParams = ViewGroup.LayoutParams(350, 350)
        return imageView
    }

    fun setImageCollection(images: List<Int>) {
        imageCollection = images
        notifyDataSetChanged()
    }

    fun setCurrentGroup(group: Int) {
        currentGroup = group
    }
}
