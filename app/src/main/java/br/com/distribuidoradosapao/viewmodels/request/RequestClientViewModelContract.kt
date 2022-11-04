package br.com.distribuidoradosapao.viewmodels.request

import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.model.Request

interface RequestClientViewModelContract {

    fun insertRequestClient(request: Request)
    fun loadRequests(idClient: String)
    fun somaRequestsClient(idClient: String)
    fun receberPedidoParcial(recebidoParcial: PedidoRecebidoParcial)
    fun somaReceberParcial(idClient: String)
    fun somaReceberParcial(idClient: String, somaTotal: Float)
    fun loadSomaParcial(idClient: String)
    fun updateRequest(idRequest: String, request: Request)
    fun filterRequestForDate(data: String)
    fun loadAllRequest()
}