package com.chplalex.notesgb.ui

import android.app.Application
import android.util.Log
import com.chplalex.notesgb.di.appModule
import com.chplalex.notesgb.di.mainModule
import com.chplalex.notesgb.di.noteModule
import com.chplalex.notesgb.di.splashModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}