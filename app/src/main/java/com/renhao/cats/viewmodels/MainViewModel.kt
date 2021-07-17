package com.renhao.cats.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renhao.cats.R
import com.renhao.cats.models.Cat
import com.renhao.cats.network.DataResult
import com.renhao.cats.network.NetworkResponse
import com.renhao.cats.repositories.CatRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: CatRepository) : ViewModel() {

    private val mutableRandomCatData : MutableLiveData<DataResult<Cat>> = MutableLiveData()
    val randomCatLiveData: LiveData<DataResult<Cat>> = mutableRandomCatData

    private val mutableRandomCatListData : MutableLiveData<DataResult<List<Cat>>> = MutableLiveData()
    val randomCatListLiveData: LiveData<DataResult<List<Cat>>> = mutableRandomCatListData

    init {
        fetchRandomCatList()
    }

    // To avoid passing in context references, recommended approach for string values is to grab the Resource ID
    fun fetchRandomCat() {
        mutableRandomCatData.value = DataResult.Loading()
        viewModelScope.launch {
            try {
                when (val callResult = repository.fetchRandomCat()) {
                    is NetworkResponse.Success -> {
                        val random = callResult.data?.getOrNull(0)
                        if (random == null) {
                            // TODO: Log
                            mutableRandomCatData.value = DataResult.Error(R.string.random_cat_load_error_available)
                        } else {
                            mutableRandomCatData.value = DataResult.Success(random)
                        }
                    }
                    is NetworkResponse.Error -> {
                        // TODO: Log
                        mutableRandomCatData.value = DataResult.Error(R.string.random_cat_load_error_network)
                    }
                }
            } catch (exception: Exception) {
                // TODO: Log
                mutableRandomCatData.value = DataResult.Error(R.string.random_cat_load_error_available)
            }
        }
    }

    private fun fetchRandomCatList() {
        mutableRandomCatListData.value = DataResult.Loading()
        viewModelScope.launch {
            try {
                when (val callResult = repository.fetchRandomCatList()) {
                    is NetworkResponse.Success -> {
                        callResult.data?.let {
                            mutableRandomCatListData.value = DataResult.Success(it)
                        } ?: run {
                            // TODO: Log
                            mutableRandomCatListData.value = DataResult.Error(R.string.random_cat_load_error_available)
                        }
                    }
                    is NetworkResponse.Error -> {
                        // TODO: Log
                        mutableRandomCatListData.value = DataResult.Error(R.string.random_cat_load_error_network)
                    }
                }
            } catch (exception: Exception) {
                // TODO: Log
                mutableRandomCatListData.value = DataResult.Error(R.string.random_cat_load_error_available)
            }
        }
    }
}