package com.korikmat.watchtogether

import android.app.Application
import com.korikmat.watchtogether.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.logger.Level

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(appModules)

        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}