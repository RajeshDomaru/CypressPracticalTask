package com.cypress.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cypress.R
import com.cypress.databinding.AlbumItemBinding
import com.cypress.models.AlbumItem
import com.squareup.picasso.Picasso

class AlbumAdapter : ListAdapter<AlbumItem, AlbumAdapter.AlbumViewHolder>(albumDiffUtil) {

    inner class AlbumViewHolder(private val itemViewBinding: AlbumItemBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun loadAlbum(albumItem: AlbumItem) {

            with(itemViewBinding) {

                tvPhotoNo.text = String.format("Photo: " + albumItem.id)

                Picasso.get()
                    .load(albumItem.thumbnailUrl)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.broken_image)
                    .into(ivPhoto)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            AlbumItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.loadAlbum(currentList[position])
    }

}

private val albumDiffUtil = object : DiffUtil.ItemCallback<AlbumItem>() {

    override fun areItemsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AlbumItem, newItem: AlbumItem): Boolean {
        return oldItem == newItem
    }

}