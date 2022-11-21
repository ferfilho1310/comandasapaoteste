package br.com.distribuidoradosapao.viewmodels.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.distribuidoradosapao.firebaseservice.request.RequestClientServiceContract
import br.com.distribuidoradosapao.model.RequestReceivedPartial
import br.com.distribuidoradosapao.model.Request
import br.com.distribuidoradosapao.util.FirebaseCrashlyticsUtils
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

    private var _somaRequestClient: MutableLiveData<Float> = MutableLiveData()
    var somaRequestClient: LiveData<Float> = _somaRequestClient

    private var _insertRequestPartial: MutableLiveData<Boolean> = MutableLiveData()
    var insertRequestPartial: LiveData<Boolean> = _insertRequestPartial

    private var _somaPedidosParcial: MutableLiveData<Float> = MutableLiveData()
    var somaPedidosParcial: LiveData<Float> = _somaPedidosParcial

    private var _recebido: MutableLiveData<Float> = MutableLiveData()
    var recebido: LiveData<Float> = _recebido

    private var _loadSomaParcial: MutableLiveData<Query> = MutableLiveData()
    var loadSomaParcial: LiveData<Query> = _loadSomaParcial

    private var _updateRequest: MutableLiveData<Boolean> = MutableLiveData()
    var updateRequest: LiveData<Boolean> = _updateRequest

    private var _filterRequestForDate: MutableLiveData<Float> = MutableLiveData()
    var filterRequestForDate: LiveData<Float> = _filterRequestForDate

    private var _loadAllRequest: MutableLiveData<List<String>?> = MutableLiveData()
    var loadAllRequest: LiveData<List<String>?> = _loadAllRequest

    private var _deleteRequest: MutableLiveData<Boolean> = MutableLiveData()
    var deleteRequest: LiveData<Boolean> = _deleteRequest

    private var sum: ArrayList<Float> = arrayListOf()
    private var sumRecebidoParcial: ArrayList<Float> = arrayListOf()
    private var sumPedidoPorData: ArrayList<Float> = arrayListOf()

    private var sumTotal = 0f
    private var sumTotalParcial = 0f
    private var sumTotalPedidosPorData = 0f

    override fun insertRequestClient(request: Request) {
        requestClientService.insertRequestClient(request)
            .onEach {
                _insertRequestClient.postValue(it)
            }.catch {
                Log.e("TAG", "Erro ao inserir pedido ${it.cause}")
                FirebaseCrashlyticsUtils.log("Erro ao inserir pedido ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun loadRequests(idClient: String) {
        requestClientService.loadRequests(idClient)
            .onEach { requests ->
                _loadRequestClient.postValue(requests)
            }.catch {
                Log.e("TAG", "Erro ao carregar pedidos ${it.cause}")
                FirebaseCrashlyticsUtils.log("Erro ao carregar pedidos ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun somaRequestsClient(idClient: String) {
        sum.clear()
        requestClientService.somaRequestsClient(idClient)
            .onEach {
                it?.forEach { valueProduct ->
                    sum.add(valueProduct.valueTotal!!.toFloat())
                }
                sum.forEach {
                    sumTotal += it
                }
                _somaRequestClient.postValue(sumTotal)
            }.catch {
                Log.e("TAG", "Erro ao efetuar a soma dos pedidos ${it.cause}")
                FirebaseCrashlyticsUtils.log("Erro ao efetuar a soma dos pedidos ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun insertValueReceivedPartial(recebidoParcial: RequestReceivedPartial) {
        requestClientService.receberPedidoParcial(recebidoParcial)
            .onEach {
                _insertRequestPartial.postValue(it)
            }.catch {
                Log.e("TAG", "Erro ao receber pedido parcial ${it.cause}")
                FirebaseCrashlyticsUtils.log("Erro ao receber pedido parcial ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun somaReceberParcial(idClient: String) {
        requestClientService.somaReceberParcial(idClient)
            .onEach {
                sumRecebidoParcial.clear()
                sumTotalParcial = 0.00F

                it?.forEach { valueProduct ->
                    sumRecebidoParcial.add(valueProduct.value!!.toFloat())
                }
                sumRecebidoParcial.forEach {
                    sumTotalParcial += it
                }
                _somaPedidosParcial.postValue(sumTotalParcial)
            }.catch {
                Log.e("TAG", "Error ao fazer a soma parcial ${it.cause}")
                FirebaseCrashlyticsUtils.log("Error ao fazer a soma parcial ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun loadSomaParcial(idClient: String) {
        requestClientService.loadSomaParcial(idClient)
            .onEach {
                _loadSomaParcial.postValue(it)
            }.catch {
                Log.e("TAG", "Error ao carregar o recebido parcial ${it.cause}")
                FirebaseCrashlyticsUtils.log("Error ao carregar o recebido parcial ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun updateRequest(idRequest: String, request: Request) {
        requestClientService.updateRequest(idRequest, request)
            .onEach {
                _updateRequest.postValue(it)
            }.catch {
                Log.e("TAG", "Error ao atualizar o pedido ${it.cause}")
                FirebaseCrashlyticsUtils.log("Error ao atualizar o pedido ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun filterRequestForDate(data: String) {
        if (data == "Selecione uma data") {
            sumPedidoPorData.clear()
            _filterRequestForDate.value = 0.00f
            sumTotalPedidosPorData = 0f
        } else {
            sumTotalPedidosPorData = 0f
            requestClientService.filterRequestForDate(data)
                .onEach {
                    sumPedidoPorData.clear()
                    it?.forEach {
                        sumPedidoPorData.add(it.valueTotal!!.toFloat())
                    }
                    sumPedidoPorData.forEach {
                        sumTotalPedidosPorData += it
                    }

                    _filterRequestForDate.postValue(sumTotalPedidosPorData)
                }.catch {
                    Log.e("TAG", "Error ao criar o relatório ${it.cause}")
                    FirebaseCrashlyticsUtils.log("Erro ao criar o relatório ${it.cause}")
                }.launchIn(viewModelScope)
        }
    }

    override fun loadAllRequest() {
        requestClientService.loadAllRequest()
            .onEach {
                _loadAllRequest.postValue(it?.map { it.date!! }?.distinct())
            }.catch {
                Log.e("ERROR", "Erro ao buscar as datas para o relatório ${it.cause}")
                FirebaseCrashlyticsUtils.log("Erro ao buscar as datas para o relatório ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun deleteRequestReceived(idRequestReceived: String) {
        requestClientService.deleteRequestReceived(idRequestReceived)
            .onEach {
                _deleteRequest.value = it
            }.catch {
                Log.e("ERROR", "Erro ao deletar pedido parcial ${it.cause}")
                FirebaseCrashlyticsUtils.log("Erro ao deletar pedido parcial ${it.cause}")
            }.launchIn(viewModelScope)
    }
}