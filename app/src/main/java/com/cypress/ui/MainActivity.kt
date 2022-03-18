package com.cypress.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.whenResumed
import androidx.lifecycle.whenStarted
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cypress.adapters.UserAdapter
import com.cypress.api_call.Services
import com.cypress.api_call.checkStatus
import com.cypress.databinding.ActivityMainBinding
import com.cypress.infinity_scroll.VerticalScrollListener
import com.cypress.models.Photo
import com.cypress.models.UserItem
import com.cypress.network.NetworkConnection
import com.cypress.view_models.AlbumViewModel
import com.cypress.view_models.UserViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val userViewModel by viewModels<UserViewModel>()

    private val albumViewModel by viewModels<AlbumViewModel>()

    private lateinit var userAdapter: UserAdapter

    private lateinit var networkConnection: NetworkConnection

    private var isLastPage: Boolean = false

    private var isLoading: Boolean = false

    private var limit = 10

    private var userSize = limit

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        loadPage()

    }

    private fun loadPage() {

        checkInternet()

        getData()

    }

    private fun getData() {

        if (networkConnection.isNetworkAvailable()) {

            with(binding) {

                userViewModel.viewModelScope.launch {

                    whenStarted {

                        pbUser.visibility = View.VISIBLE

                        try {

                            val userResponse =
                                withContext(Dispatchers.Default) { Services.getUsers() }

                            val userResult = checkStatus(null, userResponse)

                            userResult.exception?.apply {

                                Snackbar.make(rvUser, errorMessage.toString(), Snackbar.LENGTH_LONG)
                                    .show()

                            } ?: userResult.result?.apply {

                                userSize = size

                                userViewModel.insertAll(toList())

                            }

                        } catch (e: Exception) {

                            e.message?.let {
                                Snackbar.make(rvUser, it, Snackbar.LENGTH_LONG).show()
                            }

                        }

                    }

                    whenResumed {

                        loadAdapter()

                    }

                }

            }

        } else {

            loadAdapter()

        }

    }

    private fun loadAdapter() {

        with(binding) {

            val layoutManager =
                LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)

            rvUser.layoutManager = layoutManager

            userAdapter = UserAdapter(this@MainActivity)

            rvUser.adapter = userAdapter

            rvUser.addOnScrollListener(object : VerticalScrollListener(layoutManager) {

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }

                override fun dataSwapping() {
                    userViewModel.viewModelScope.launch { dataSwappingOnView(SCROLL_DOWN) }
                }

                override fun scrollUpFeatures() {
                    userViewModel.viewModelScope.launch { dataSwappingOnView(SCROLL_UP) }
                }

                override fun scrollDownFeatures() {

                    isLoading = true

                    if (limit < userSize) limit += 10
                    else isLastPage = true

                    observes()

                }

            })

            observes()

        }

    }

    private fun observes() {

        userViewModel.getUsers(limit).observeForever {

            getPhotosFromApi(it)

            isLoading = false

            val photos = getUsersAll(it)

            if (photos.isNotEmpty()) {

                binding.tvNoDataFound.visibility = View.GONE

                userAdapter.submitList(photos.toMutableList())

            } else {

                binding.tvNoDataFound.visibility = View.VISIBLE

            }

        }

    }

    private fun getUsersAll(userItems: List<UserItem>?): MutableList<Photo> {

        val photos = mutableListOf<Photo>()

        albumViewModel.viewModelScope.launch {

            userItems?.forEach {

                photos.add(Photo(it, albumViewModel.getAlbums(it.id)))

            }

        }

        return photos

    }

    private fun getPhotosFromApi(userItems: List<UserItem>?) {

        with(binding) {

            if (networkConnection.isNetworkAvailable() && !isLastPage) {

                userItems?.forEach {

                    albumViewModel.viewModelScope.launch {

                        pbUser.visibility = View.VISIBLE

                        val albumResponse =
                            withContext(Dispatchers.Default) { Services.getAlbums(it.id) }

                        val albumResult = checkStatus(null, albumResponse)

                        albumResult.exception?.apply {

                            Snackbar.make(
                                rvUser,
                                errorMessage.toString(),
                                Snackbar.LENGTH_LONG
                            ).show()

                        } ?: albumResult.result?.apply {

                            albumViewModel.insertAll(toList())

                        }

                        pbUser.visibility = View.GONE

                    }

                }

            }

        }

    }

    private suspend fun dataSwappingOnView(scrollDirection: Int) {

        with(binding) {

            if (rvUser.canScrollVertically(SCROLL_DIRECTION_VERTICALLY)) {

                val firstPosition =
                    (rvUser.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                val lastPosition =
                    (rvUser.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                if (firstPosition != RecyclerView.NO_POSITION) {

                    if (SCROLL_DOWN == scrollDirection) {

                        val currentList = userAdapter.currentList

                        val secondPart = currentList.subList(0, firstPosition)

                        val firstPart = currentList.subList(firstPosition, currentList.size)

                        userAdapter.submitList(firstPart + secondPart)

                    } else if (SCROLL_UP == scrollDirection) {

                        val currentList = userAdapter.currentList

                        val secondPart = currentList.subList(0, currentList.size - lastPosition)

                        val firstPart =
                            currentList.subList(currentList.size - lastPosition, currentList.size)

                        userAdapter.submitList(firstPart + secondPart)

                    }

                }

                delay(DELAY_BETWEEN_SCROLL_MS)

            }

        }

    }

    companion object {
        private const val DELAY_BETWEEN_SCROLL_MS = 25L
        private const val SCROLL_DIRECTION_VERTICALLY = 1
        private const val SCROLL_UP = 2
        private const val SCROLL_DOWN = 3
    }

    private fun checkInternet() {

        networkConnection = NetworkConnection(applicationContext)

        networkConnection.observe(this) { isConnected ->

            if (!isConnected)

                Snackbar.make(binding.rvUser, "No Internet!", Snackbar.LENGTH_SHORT).show()

        }

    }

}