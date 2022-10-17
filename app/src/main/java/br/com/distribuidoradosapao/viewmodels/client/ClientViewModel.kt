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

    private var _deletClient: MutableLiveData<Boolean> = MutableLiveData()
    var deleteClient: LiveData<Boolean> = _deletClient

    private var _loadOneClient: MutableLiveData<Client> = MutableLiveData()
    var loadOneClient: LiveData<Client> = _loadOneClient

    private var _updateClient: MutableLiveData<Boolean> = MutableLiveData()
    var updateClient: LiveData<Boolean> = _updateClient

    override fun insertClient(client: Client) {
        service.insertClient(client)
            .onEach {
                _insertClient.value = it
            }.catch {
                Log.e("TAG", "Erro ao inserir pedido $it")
                _insertClient.value = false
            }.launchIn(viewModelScope)
    }

    override fun loadClients() {
        service.loadClients()
            .onEach {
                _loadClient.value = it
            }.catch {
                Log.e("Erro","Não foi possível encontrar nenhum cliente: $it")
            }.launchIn(viewModelScope)
    }

    override fun deletClient(isClient: String) {
        service.deleteClient(isClient)
            .onEach {
                _deletClient.value = it
            }.catch {
                Log.e("Erro","Não foi possível deletar o cliente: $it")
            }.launchIn(viewModelScope)
    }

    override fun searchClient(idClient: String) {
        service.searchClientBeforeDeleted(idClient)
            .onEach {
                insertClientBeforeDelete(it,idClient)
            }.catch {
                _deletClient.value = false
                Log.e("Erro","Não foi possível encontrar o cliente: $it")
            }.launchIn(viewModelScope)
    }

    override fun insertClientBeforeDelete(client: Client?, idClient: String){
        if (client != null) {
            service.insertClientDeleted(client)
                .onEach {
                    if(it){
                        deletClient(idClient)
                    } else {
                        _deletClient.value = false
                    }
                }.catch {
                    _deletClient.value = false
                    Log.e("Erro","Não foi possível inserir o cliente: $it")
                }.launchIn(viewModelScope)
        }
    }

    override fun loadOneClient(idClient: String) {
        service.loadOneClient(idClient)
            .onEach {
                _loadOneClient.value = it
            }.catch {
                Log.e("Erro","Não foi possível carregar os dados do cliente: $it")
            }.launchIn(viewModelScope)
    }

    override fun updateClient(idClient: String,client: Client) {
        service.updateClient(idClient, client)
            .onEach {
                _updateClient.value = it
            }.catch {
                Log.e("Erro","Não foi possível atualizar os dados do cliente: $it")
            }.launchIn(viewModelScope)
    }
}