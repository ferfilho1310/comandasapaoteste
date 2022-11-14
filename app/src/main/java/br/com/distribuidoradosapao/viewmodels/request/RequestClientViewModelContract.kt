package br.com.distribuidoradosapao.viewmodels.request

import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.model.Request

interface RequestClientViewModelContract {

    fun insertRequestClient(request: Request)
    fun loadRequests(idClient: String)
    fun somaRequestsClient(idClient: String)
    fun insertValueReceivedPartial(recebidoParcial: PedidoRecebidoParcial)
    fun somaReceberParcial(idClient: String)
    fun loadSomaParcial(idClient: String)
    fun updateRequest(idRequest: String, request: Request)
    fun filterRequestForDate(data: String)
    fun loadAllRequest()
    fun deleteRequestReceived(idRequestReceived: String)
}