package br.com.distribuidoradosapao.firebaseservice.remoteConfig

import android.app.Activity
import android.content.Context
import android.util.Log
import br.com.distribuidoradosapao.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow


class RemoteConfigService : RemoteConfigServiceContract {

    val frc = FirebaseRemoteConfig.getInstance()

    override fun remoteConfigFetch(context: Context): kotlinx.coroutines.flow.Flow<Boolean> {
        return callbackFlow {
            frc.setDefaultsAsync(R.xml.remote_config_defaults)
            val listener = frc.fetch(30)
                .addOnCompleteListener(context as Activity) { task ->
                    if (task.isSuccessful) {
                        frc.fetchAndActivate()
                        trySend(frc.getBoolean("isRegistred")).isSuccess
                    } else {
                        trySend(false).isFailure
                        Log.e("Error", "Erro ao fazer o fetch no remote config ${task.exception}")
                    }
                }
            awaitClose {
                listener.isCanceled
            }
        }
    }
}