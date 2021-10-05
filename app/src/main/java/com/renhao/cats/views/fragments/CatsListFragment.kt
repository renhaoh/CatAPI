package com.renhao.cats.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.renhao.cats.R
import com.renhao.cats.models.Cat
import com.renhao.cats.network.DataResult
import com.renhao.cats.viewmodels.MainViewModel
import com.renhao.cats.views.utils.CatListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatsListFragment : Fragment() {

    private lateinit var catListRecyclerView: RecyclerView
    private lateinit var errorMessage: TextView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    // use activityViewModels for activity-scoped ViewModel
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_cat_list, container, false)
        catListRecyclerView = view.findViewById(R.id.random_cat_list_recycler_view)
        swipeRefresh = view.findViewById(R.id.random_cat_list_swipe_refresh)
        errorMessage = view.findViewById(R.id.error_message)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CatListAdapter(this, listOf()) {
            onCatsListItemCLick(it)
        }
        catListRecyclerView.layoutManager = LinearLayoutManager(context)
        catListRecyclerView.adapter = adapter

        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing = true
            viewModel.fetchRandomCatList()
        }

        viewModel.randomCatListLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataResult.Success -> {
                    swipeRefresh.isRefreshing = false
                    catListRecyclerView.visibility = View.VISIBLE
                    errorMessage.visibility = View.GONE
                    adapter.updateData(it.data)
                }
                is DataResult.Error -> {
                    catListRecyclerView.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    it.messageId?.let { msgId ->
                        errorMessage.text = getString(msgId)
                    }
                }
                else -> {
                    errorMessage.visibility = View.GONE
                    catListRecyclerView.visibility = View.GONE
                }
            }
        })
    }

    private fun onCatsListItemCLick(position: Cat) {
        // action here
    }

}