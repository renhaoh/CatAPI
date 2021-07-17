package com.renhao.cats.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.renhao.cats.R
import com.renhao.cats.network.DataResult
import com.renhao.cats.viewmodels.MainViewModel
import com.renhao.cats.viewmodels.MainViewModelFactory
import com.renhao.cats.views.utils.RandomCatAdapter

class CatListFragment : Fragment() {

    private lateinit var catListRecyclerView: RecyclerView
    private lateinit var randomCatProgressbar: ProgressBar

    private val viewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_cat_list, container, false)
        catListRecyclerView = view.findViewById(R.id.random_cat_list_recycler_view)
        randomCatProgressbar = view.findViewById(R.id.random_cat_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        catListRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.randomCatListLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataResult.Success -> {
                    catListRecyclerView.visibility = View.VISIBLE
                    randomCatProgressbar.visibility = View.GONE
                    catListRecyclerView.adapter = RandomCatAdapter(this, it.data)
                }
                else -> {
                    randomCatProgressbar.visibility = View.VISIBLE
                    catListRecyclerView.visibility = View.GONE
                }
            }
        })
    }

}