package br.com.distribuidoradosapao.firebaseservice.remoteConfig

import android.content.Context

interface RemoteConfigServiceContract {
    fun remoteConfigFetch(context: Context): kotlinx.coroutines.flow.Flow<Boolean>
}