package com.cypress.infinity_scroll

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class VerticalScrollListener(var layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean

    abstract fun dataSwapping()

    abstract fun scrollUpFeatures()

    abstract fun scrollDownFeatures()

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount

        val totalItemCount = layoutManager.itemCount

        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

        if (dy < 0 && firstVisibleItemPosition == 0) {

            scrollUpFeatures()

        } else if (!isLoading() && !isLastPage()) {

            if (firstVisibleItemPosition >= 0 &&
                visibleItemCount + firstVisibleItemPosition >= totalItemCount
            ) {

                scrollDownFeatures()

            }

        } else if (isLastPage() && dy > 0) {

            dataSwapping()

        }

    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

        super.onScrollStateChanged(recyclerView, newState)

        if (newState == RecyclerView.SCROLL_STATE_IDLE) scrollUpFeatures()

    }

}