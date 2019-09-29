package com.example.myapplication.ui.splash

import com.example.myapplication.data.NotesRepository
import com.example.myapplication.data.errors.NoAuthException
import com.example.myapplication.ui.base.BaseViewModel

class SplashViewModel : BaseViewModel<Boolean ?, SplashViewState>(){

    fun requestUser(){
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null){
                SplashViewState(authenticated = true)
            } else{
                SplashViewState(error = NoAuthException())
            }
        }
    }
}