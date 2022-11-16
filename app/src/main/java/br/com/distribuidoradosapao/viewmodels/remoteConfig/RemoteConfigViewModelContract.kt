package br.com.distribuidoradosapao.viewmodels.remoteConfig

import android.content.Context

interface RemoteConfigViewModelContract {

    fun remoteConfigFetch(context: Context)
}