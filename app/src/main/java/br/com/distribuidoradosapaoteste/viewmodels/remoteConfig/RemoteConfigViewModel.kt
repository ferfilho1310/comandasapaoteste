package br.com.distribuidoradosapaoteste.viewmodels.remoteConfig

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.distribuidoradosapaoteste.firebaseservice.remoteConfig.RemoteConfigServiceContract
import br.com.distribuidoradosapaoteste.util.FirebaseCrashlyticsUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RemoteConfigViewModel(
    val remoteConfigService: RemoteConfigServiceContract
) : ViewModel(),
    RemoteConfigViewModelContract {

    private val _remoteConfig: MutableLiveData<Boolean> = MutableLiveData()
    var remoteConfigLiveData: LiveData<Boolean> = _remoteConfig

    override fun remoteConfigFetch(context: Context) {
        remoteConfigService.remoteConfigFetch(context)
            .onEach {
                _remoteConfig.value = it
            }.catch {
                Log.i("RemoteConfig", "Error remoteConfig $it")
                FirebaseCrashlyticsUtils.log("Falha ao buscar parametro no remoteConfig ${it.cause}")
            }.launchIn(viewModelScope)
    }
}