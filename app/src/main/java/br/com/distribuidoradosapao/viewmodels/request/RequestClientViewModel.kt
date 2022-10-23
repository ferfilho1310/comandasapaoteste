package br.com.distribuidoradosapao.viewmodels.request

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.distribuidoradosapao.firebaseservice.user.request.RequestClientServiceContract
import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
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

    private var _somaRequestClient: MutableLiveData<Float> = MutableLiveData()
    var somaRequestClient: LiveData<Float> = _somaRequestClient

    private var _insertRequestPartial: MutableLiveData<Boolean> = MutableLiveData()
    var insertRequestPartial: LiveData<Boolean> = _insertRequestPartial

    private var _somaPedidosParcial: MutableLiveData<Float> = MutableLiveData()
    var somaPedidosParcial: LiveData<Float> = _somaPedidosParcial

    private var _recebido: MutableLiveData<Float> = MutableLiveData()
    var recebido: LiveData<Float> = _recebido


    private var sum: ArrayList<Float> = arrayListOf()
    private var sumRecebidoParcial: ArrayList<Float> = arrayListOf()
    private var sumRecebidoParcial1: ArrayList<Float> = arrayListOf()

    private var sumTotal = 0f
    private var sumTotalParcial = 0f

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
        requestClientService.somaRequestsClient(idClient)
            .onEach {
                it?.forEach { valueProduct ->
                    sum.add(valueProduct.valueTotal!!.toFloat())
                }
                sum.forEach {
                    sumTotal += it
                }
                somaReceberParcial(idClient, sumTotal)
                _somaRequestClient.value = sumTotal
            }.catch {
                Log.e("TAG", "Erro ao efetuar a soma dos pedidos $it")
            }.launchIn(viewModelScope)
    }

    override fun receberPedidoParcial(recebidoParcial: PedidoRecebidoParcial) {
        requestClientService.receberPedidoParcial(recebidoParcial)
            .onEach {
                _insertRequestPartial.value = it
            }.catch {
                Log.e("TAG", "Erro ao receber pedido parcial $it")
            }.launchIn(viewModelScope)
    }

    override fun somaReceberParcial(idClient: String) {
        sumRecebidoParcial.clear()
        requestClientService.somaReceberParcial(idClient)
            .onEach {
                it?.forEach { valueProduct ->
                    sumRecebidoParcial.add(valueProduct.value!!.toFloat())
                }
                sumRecebidoParcial.forEach {
                    sumTotalParcial += it
                }
                _somaPedidosParcial.value = sumTotalParcial
            }.catch {

            }.launchIn(viewModelScope)
    }

    override fun somaReceberParcial(idClient: String, somaTotal: Float) {
        sumRecebidoParcial1.clear()
        requestClientService.somaReceberParcial(idClient)
            .onEach {
                it?.forEach { valueProduct ->
                    sumRecebidoParcial1.add(valueProduct.value!!.toFloat())
                }
                sumRecebidoParcial1.forEach {
                    sumTotalParcial += it
                }
                _recebido.value = somaTotal - sumTotalParcial
            }.catch {

            }.launchIn(viewModelScope)
    }
}