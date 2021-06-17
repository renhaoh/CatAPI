package com.renhao.cats.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renhao.cats.models.Cat
import com.renhao.cats.network.DataResult
import com.renhao.cats.network.NetworkResponse
import com.renhao.cats.repositories.CatRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: CatRepository) : ViewModel() {

    private val mutableRandomCatData : MutableLiveData<DataResult<Cat>> = MutableLiveData()
    val randomCatLiveData: LiveData<DataResult<Cat>> = mutableRandomCatData

    init {
        fetchCatsData()
    }

    private fun fetchCatsData() {
        mutableRandomCatData.value = DataResult.Loading()
        viewModelScope.launch {
            try {
                when (val callResult = repository.fetchRandomCat()) {
                    is NetworkResponse.Success -> {
                        val random = callResult.data?.getOrNull(0)
                        if (random == null) {
                            // TODO: Log
                            mutableRandomCatData.value = DataResult.Error(null)
                        } else {
                            mutableRandomCatData.value = DataResult.Success(random)
                        }
                    }
                    is NetworkResponse.Error -> {
                        // TODO: Log
                        mutableRandomCatData.value = DataResult.Error(null)
                    }
                    is NetworkResponse.Loading -> {
                        mutableRandomCatData.value = DataResult.Loading()
                    }
                }
            } catch (exception: Exception) {
                // TODO: Log
                mutableRandomCatData.value = DataResult.Error(null)
            }
        }
    }
}