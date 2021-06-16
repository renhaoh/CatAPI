package com.renhao.cats.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.renhao.cats.BuildConfig
import com.renhao.cats.R
import com.renhao.cats.viewmodels.MainViewModel
import com.renhao.cats.viewmodels.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this, MainViewModelFactory()).get(MainViewModel::class.java)


        viewModel.fetchCatsData()

    }
}