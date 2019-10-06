package com.example.myapplication.di

import com.example.myapplication.data.NotesRepository
import com.example.myapplication.data.provider.FireStoreProvider
import com.example.myapplication.ui.main.MainViewModel
import com.example.myapplication.ui.note.NoteViewModel
import com.example.myapplication.ui.splash.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module


val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FireStoreProvider(get(), get()) }
    single { NotesRepository(get<FireStoreProvider>()) }
}

val splashModule = module {
    viewModel { SplashViewModel(get()) }
}

val mainModule = module {
    viewModel { MainViewModel(get()) }
}

val noteModule = module {
    viewModel { NoteViewModel(get()) }
}