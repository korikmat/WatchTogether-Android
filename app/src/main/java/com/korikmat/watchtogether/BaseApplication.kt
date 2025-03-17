package com.korikmat.watchtogether

import android.app.Application
import com.korikmat.watchtogether.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@BaseApplication)
            modules(appModule)

        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}