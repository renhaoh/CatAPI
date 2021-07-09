package com.renhao.cats.views.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.renhao.cats.R
import com.renhao.cats.network.DataResult
import com.renhao.cats.viewmodels.MainViewModel
import com.renhao.cats.viewmodels.MainViewModelFactory

class RandomCatFragment : Fragment() {

    private lateinit var loadRandomCatButton: Button
    private lateinit var randomCatImageView: ImageView
    private lateinit var randomCatProgressbar: ProgressBar
    private lateinit var randomCatErrorMessage: TextView

    private val viewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_random_cat, container, false)
        loadRandomCatButton = view.findViewById(R.id.load_random_cat_button)
        randomCatImageView = view.findViewById(R.id.random_cat_image_view)
        randomCatProgressbar = view.findViewById(R.id.random_cat_progress_bar)
        randomCatErrorMessage = view.findViewById(R.id.random_cat_error_message)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRandomCatButton.setOnClickListener {
            viewModel.fetchRandomCat()
        }

        viewModel.randomCatLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataResult.Error -> {
                    randomCatProgressbar.visibility = View.GONE
                    it.messageId?.let { msgId ->
                        randomCatErrorMessage.text = getString(msgId)
                    }
                }
                is DataResult.Loading -> {
                    randomCatProgressbar.visibility = View.VISIBLE
                }
                is DataResult.Success -> {
                    randomCatProgressbar.visibility = View.GONE
                    Log.d("CATS", "${it.data.height} x ${it.data.width}")
                    Glide.with(this).load(it.data.imageUrl).into(randomCatImageView)
                }
            }
        })
    }

}