package com.renhao.cats.views.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.renhao.cats.R
import com.renhao.cats.models.Cat

class RandomCatAdapter(private val fragment: Fragment, private var catData: List<Cat>): RecyclerView.Adapter<RandomCatAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val catImage: ImageView = view.findViewById(R.id.random_cat_card_image)
        val catText: TextView = view.findViewById(R.id.random_cat_card_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.random_cat_list_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cat = catData.getOrNull(position)
        cat?.let {
            Glide.with(holder.itemView).load(it.imageUrl).into(holder.catImage)
            holder.catText.text = String.format("%d x %d", it.height, it.width)
        }
    }

    override fun getItemCount(): Int {
        return catData.size
    }

    fun updateData(catList: List<Cat>) {
        catData = catList
        notifyDataSetChanged()
    }
}