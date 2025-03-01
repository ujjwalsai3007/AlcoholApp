package com.example.alcoholapp.presentation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alcoholapp.data.repository.HomeRepository
import com.example.alcoholapp.domain.model.HomeResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel(){
    private val _homeData = MutableLiveData<HomeResponse>(null)
    val homeData: MutableLiveData<HomeResponse> = _homeData


    private val _isLoading = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow("")
    val error: StateFlow<String> = _error

    fun fetchHomeData(){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getHomeData()
                _homeData.value = response
            }catch (e:Exception) {
                _error.value = e.message ?: "Unknown error"
            }finally {
                _isLoading.value=false
            }
        }
    }



}