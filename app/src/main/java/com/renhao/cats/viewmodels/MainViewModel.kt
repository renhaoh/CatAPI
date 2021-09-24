package com.renhao.cats.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renhao.cats.R
import com.renhao.cats.models.Cat
import com.renhao.cats.network.DataResult
import com.renhao.cats.network.NetworkResponse
import com.renhao.cats.repositories.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: CatsRepository) : ViewModel() {

    private val mutableRandomCatListData : MutableLiveData<DataResult<List<Cat>>> = MutableLiveData()
    val randomCatListLiveData: LiveData<DataResult<List<Cat>>> = mutableRandomCatListData

    init {
        fetchRandomCatList()
    }

    fun fetchRandomCatList() {
        mutableRandomCatListData.value = DataResult.Loading()
        viewModelScope.launch {
            try {
                when (val callResult = repository.fetchRandomCatList()) {
                    is NetworkResponse.Success -> {
                        callResult.data?.let {
                            mutableRandomCatListData.value = DataResult.Success(it)
                        } ?: run {
                            Timber.e("Empty data response")
                            mutableRandomCatListData.value = DataResult.Error(R.string.random_cat_load_error_available)
                        }
                    }
                    is NetworkResponse.Error -> {
                        Timber.e("Network error with code: ${callResult.errorCode} and message: ${callResult.errorMessage}")
                        mutableRandomCatListData.value = DataResult.Error(R.string.random_cat_load_error_network)
                    }
                }
            } catch (exception: Exception) {
                Timber.e(exception)
                mutableRandomCatListData.value = DataResult.Error(R.string.random_cat_load_error_available)
            }
        }
    }
}