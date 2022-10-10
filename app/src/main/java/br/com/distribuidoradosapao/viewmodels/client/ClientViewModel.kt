package br.com.distribuidoradosapao.viewmodels.client

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.distribuidoradosapao.firebaseservice.user.requests.ClientServiceContract
import br.com.distribuidoradosapao.model.Client
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ClientViewModel(
    val service: ClientServiceContract
) : ViewModel(),
    ClientViewModelContract {

    private var _insertClient: MutableLiveData<Boolean> = MutableLiveData()
    var insertClient: LiveData<Boolean> = _insertClient

    override fun insertClient(client: Client) {
        service.insertClient(client)
            .onEach {
                _insertClient.value = it
            }.catch {
                Log.e("TAG", "Erro ao inserir pedido $it")
                _insertClient.value = false
            }.launchIn(viewModelScope)
    }
}