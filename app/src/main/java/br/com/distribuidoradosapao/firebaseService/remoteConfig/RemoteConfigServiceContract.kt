package br.com.distribuidoradosapao.firebaseService.remoteConfig

import android.content.Context
import com.google.android.gms.tasks.Task
import java.util.concurrent.Flow

interface RemoteConfigServiceContract {
    fun remoteConfigFetch(context: Context): kotlinx.coroutines.flow.Flow<Boolean>
}