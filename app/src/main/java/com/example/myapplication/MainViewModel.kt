package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val viewStateLiveData = MutableLiveData<String>()

    init {
        viewStateLiveData.value = "Hello world!"
    }

    fun viewState() : LiveData<String> = viewStateLiveData

    fun buttonClick(){
        viewStateLiveData.value = "Hello Kotlin!"
        viewState()
    }
}