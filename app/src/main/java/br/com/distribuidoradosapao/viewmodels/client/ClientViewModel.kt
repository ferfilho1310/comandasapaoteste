package br.com.distribuidoradosapao.viewmodels.client

import android.util.Log
import androidx.lifecycle.*
import br.com.distribuidoradosapao.firebaseservice.user.client.ClientServiceContract
import br.com.distribuidoradosapao.model.Client
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ClientViewModel(
    val service: ClientServiceContract
) : ViewModel(),
    ClientViewModelContract {

    private var _insertClient: MutableLiveData<Boolean> = MutableLiveData()
    var insertClient: LiveData<Boolean> = _insertClient

    private var _loadClient: MutableLiveData<Query> = MutableLiveData()
    var loadClient: LiveData<Query> = _loadClient


    override fun insertClient(client: Client) {
        service.insertClient(client)
            .onEach {
                _insertClient.value = it
            }.catch {
                Log.e("TAG", "Erro ao inserir pedido $it")
                _insertClient.value = false
            }.launchIn(viewModelScope)
    }

    override fun loadClient() {
        service.loadClients()
            .onEach {
                _loadClient.value = it
            }.catch {
                Log.e("Erro","Não foi possível encontrar nenhum cliente: $it")
            }.launchIn(viewModelScope)
    }
}