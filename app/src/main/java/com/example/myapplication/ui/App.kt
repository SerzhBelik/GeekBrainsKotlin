package com.example.myapplication.ui

import android.app.Application
import com.example.myapplication.di.appModule
import com.example.myapplication.di.mainModule
import com.example.myapplication.di.noteModule
import com.example.myapplication.di.splashModule

import org.koin.android.ext.android.startKoin


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}