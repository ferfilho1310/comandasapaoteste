package br.com.distribuidoradosapao.viewmodels.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.distribuidoradosapao.firebaseservice.user.request.RequestClientServiceContract
import br.com.distribuidoradosapao.model.Request
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RequestClientViewModel(
    var requestClientService: RequestClientServiceContract
) : RequestClientViewModelContract,
    ViewModel() {

    private var _insertRequestClient: MutableLiveData<Boolean> = MutableLiveData()
    var insertRequestClient: LiveData<Boolean> = _insertRequestClient

    private var _loadRequestClient: MutableLiveData<Query> = MutableLiveData()
    var loadRequestClient: LiveData<Query> = _loadRequestClient

    private var _somaRequestClient: MutableLiveData<Int> = MutableLiveData()
    var somaRequestClient: LiveData<Int> = _somaRequestClient

    var sum: ArrayList<Int> = arrayListOf()
    var sumTotal = 0

    override fun insertRequestClient(request: Request) {
        requestClientService.insertRequestClient(request)
            .onEach {
                _insertRequestClient.value = it
            }.catch {
                Log.e("TAG", "Erro ao inserir pedido $it")
            }.launchIn(viewModelScope)
    }

    override fun loadRequests(idClient: String) {
        requestClientService.loadRequests(idClient)
            .onEach { requests ->
                _loadRequestClient.value = requests
            }.catch {
                Log.e("TAG", "Erro ao carregar pedidos $it")
            }.launchIn(viewModelScope)
    }

    override fun somaRequestsClient(idClient: String) {
        sum.clear()
        requestClientService.somaRequestsClinet(idClient)
            .onEach {
                it?.forEach { valueProduct ->
                    sum.add(valueProduct.valueTotal!!.toInt())
                }
                sum.forEach {
                    sumTotal += it
                }
                _somaRequestClient.value = sumTotal
            }.catch {
                Log.e("TAG", "Erro ao efetuar a soma dos pedidos $it")
            }.launchIn(viewModelScope)
    }
}