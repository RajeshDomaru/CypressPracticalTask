package com.cypress.infinity_scroll

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class HorizontalScrollListener(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    abstract fun scrollLeftFeatures()

    abstract fun scrollRightFeatures()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount

        val totalItemCount = layoutManager.itemCount

        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (dx < 0 && firstVisibleItemPosition == 0) {

            scrollLeftFeatures()

        } else if (dx > 0 || (firstVisibleItemPosition >= 0 &&
                    visibleItemCount + firstVisibleItemPosition >= totalItemCount)
        ) {

            scrollRightFeatures()

        }

    }


    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) scrollLeftFeatures()

    }

}