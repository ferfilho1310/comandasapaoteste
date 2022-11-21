package br.com.distribuidoradosapaoteste.viewmodels.request

import br.com.distribuidoradosapaoteste.model.RequestReceivedPartial
import br.com.distribuidoradosapaoteste.model.Request

interface RequestClientViewModelContract {

    fun insertRequestClient(request: Request)
    fun loadRequests(idClient: String)
    fun somaRequestsClient(idClient: String)
    fun insertValueReceivedPartial(recebidoParcial: RequestReceivedPartial)
    fun somaReceberParcial(idClient: String)
    fun loadSomaParcial(idClient: String)
    fun updateRequest(idRequest: String, request: Request)
    fun filterRequestForDate(data: String)
    fun loadAllRequest()
    fun deleteRequestReceived(idRequestReceived: String)
}