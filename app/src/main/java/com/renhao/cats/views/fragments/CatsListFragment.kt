package com.renhao.cats.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.renhao.cats.R
import com.renhao.cats.network.DataResult
import com.renhao.cats.viewmodels.MainViewModel
import com.renhao.cats.views.utils.RandomCatAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatsListFragment : Fragment() {

    private lateinit var catListRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorMessage: TextView

    // use activityViewModels for activity-scoped ViewModel
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_cat_list, container, false)
        catListRecyclerView = view.findViewById(R.id.random_cat_list_recycler_view)
        progressBar = view.findViewById(R.id.progress_bar)
        errorMessage = view.findViewById(R.id.error_message)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catListRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.randomCatListLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataResult.Success -> {
                    catListRecyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    errorMessage.visibility = View.GONE
                    catListRecyclerView.adapter = RandomCatAdapter(this, it.data)
                }
                is DataResult.Error -> {
                    catListRecyclerView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    errorMessage.visibility = View.VISIBLE
                    it.messageId?.let { msgId ->
                        errorMessage.text = getString(msgId)
                    }
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    errorMessage.visibility = View.GONE
                    catListRecyclerView.visibility = View.GONE
                }
            }
        })
    }

}