package com.renhao.cats.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renhao.cats.repositories.CatRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: CatRepository) : ViewModel() {

    fun fetchCatsData() {
        viewModelScope.launch {
            try {
                repository.fetchRandomCat()
            } catch (exception: Exception) {
                val x = 10
            }
        }
    }
}