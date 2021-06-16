package com.renhao.cats.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.renhao.cats.utils.AppContainer

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(AppContainer.catRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}