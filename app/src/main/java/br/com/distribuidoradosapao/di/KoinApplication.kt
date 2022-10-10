package br.com.distribuidoradosapao.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin


class KoinApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinApplication)
            androidLogger(org.koin.core.logger.Level.NONE)
            androidFileProperties()
            modules(
                listOf(
                    KoinModules.activity,
                    KoinModules.service,
                    KoinModules.viewModels
                )
            )
        }
    }
}