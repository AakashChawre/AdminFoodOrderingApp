package com.adminfoodorderingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adminfoodorderingapp.R
import com.adminfoodorderingapp.databinding.MenuItemBinding
import com.adminfoodorderingapp.model.Item
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference

class MenuAdapter(
    private val context: Context,
    private val items: ArrayList<Item>,
    databaseReference: DatabaseReference
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            MenuItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(context, items, position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, items: ArrayList<Item>, position: Int) {
            val item = items.get(position)

            binding.apply {
                tvName.text = item.itemName
                tvPrice.text = "$${item.itemPrice}"

                // set image using glide
                Glide.with(context)
                    .load(item.itemImage)
                    .centerCrop()
                    .placeholder(R.drawable.addimage)
                    .into(ivImage)

                var count = 1
                ibPlus.setOnClickListener {
                    binding.tvCount.text = "${count++}"
                }
                ibMinus.setOnClickListener {
                    binding.tvCount.text = "${count--}"
                }
            }
        }
    }
}