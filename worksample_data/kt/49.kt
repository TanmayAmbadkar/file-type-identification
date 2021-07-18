package com.thesurix.example.gesturerecycler.adapter

import android.view.View
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import com.thesurix.example.gesturerecycler.databinding.LinearItemBinding

class LinearItemViewHolder (private val binding: LinearItemBinding) : BaseMonthViewHolder(binding.root) {
    override val monthText: TextView
        get() = binding.monthText
    override val monthPicture: ImageView
        get() = binding.monthImage
    override val itemDrag: ImageView
        get() = binding.monthDrag
    override val foreground: View?
        get() = null
    override val background: ViewStub?
        get() = null
}