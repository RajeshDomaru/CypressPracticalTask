package com.cypress.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cypress.databinding.UserItemBinding
import com.cypress.infinity_scroll.HorizontalScrollListener
import com.cypress.models.Photo
import com.cypress.ui.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserAdapter(private val activity: MainActivity) :
    ListAdapter<Photo, UserAdapter.UserViewHolder>(userDiffUtils) {

    inner class UserViewHolder(private val itemViewBinding: UserItemBinding) :
        RecyclerView.ViewHolder(itemViewBinding.root) {

        fun loadValue(photo: Photo) {

            with(itemViewBinding) {

                tvTitle.text = photo.userItem.title

                tvAlbumNo.text = String.format("Album: " + photo.userItem.id)

                val layoutManager =
                    LinearLayoutManager(root.context, RecyclerView.HORIZONTAL, false)

                rvAlbum.layoutManager = layoutManager

                val albumAdapter = AlbumAdapter()

                rvAlbum.adapter = albumAdapter

                activity.lifecycleScope.launch {

                    photo.albumItems?.observe(activity) {
                        albumAdapter.submitList(it.toMutableList())
                    }

                }

                rvAlbum.addOnScrollListener(object : HorizontalScrollListener(layoutManager) {

                    override fun scrollLeftFeatures() {
                        activity.lifecycleScope.launch {
                            dataSwappingOnView(rvAlbum, albumAdapter, SCROLL_LEFT)
                        }
                    }

                    override fun scrollRightFeatures() {
                        activity.lifecycleScope.launch {
                            dataSwappingOnView(rvAlbum, albumAdapter, SCROLL_RIGHT)
                        }
                    }

                })

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.loadValue(currentList[position])
        holder.setIsRecyclable(true)
    }

    private suspend fun dataSwappingOnView(
        recyclerView: RecyclerView,
        albumAdapter: AlbumAdapter,
        scrollDirection: Int
    ) {

        if (recyclerView.canScrollHorizontally(SCROLL_DIRECTION_HORIZONTAL)) {

            val firstPosition =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            if (firstPosition != RecyclerView.NO_POSITION) {

                if (SCROLL_RIGHT == scrollDirection) {

                    val currentList = albumAdapter.currentList

                    val secondPart = currentList.subList(0, firstPosition)

                    val firstPart = currentList.subList(firstPosition, currentList.size)

                    albumAdapter.submitList(firstPart + secondPart)

                } else if (SCROLL_LEFT == scrollDirection) {

                    val currentList = albumAdapter.currentList

                    val secondPart = currentList.subList(0, currentList.size - 1)

                    val firstPart =
                        currentList.subList(currentList.size - 1, currentList.size)

                    albumAdapter.submitList(firstPart + secondPart)

                }

                delay(DELAY_BETWEEN_SCROLL_MS)

            }

        }

    }

    companion object {
        private const val DELAY_BETWEEN_SCROLL_MS = 25L
        private const val SCROLL_DIRECTION_HORIZONTAL = 1
        private const val SCROLL_LEFT = 2
        private const val SCROLL_RIGHT = 3
    }

}

private val userDiffUtils = object : DiffUtil.ItemCallback<Photo>() {

    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.userItem.id == newItem.userItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.userItem == newItem.userItem
    }

}