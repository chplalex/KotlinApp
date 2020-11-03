package com.chplalex.notesgb.di

import com.chplalex.notesgb.ui.main.MainViewModel
import com.chplalex.notesgb.ui.note.NoteViewModel
import com.chplalex.notesgb.ui.splash.SplashViewModel
import com.chplalex.notesgb.data.provider.FirestoreProvider
import com.chplalex.notesgb.data.provider.DataProvider
import com.chplalex.notesgb.data.Repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirestoreProvider(get(), get()) } bind DataProvider::class
    single { Repository(get()) }
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