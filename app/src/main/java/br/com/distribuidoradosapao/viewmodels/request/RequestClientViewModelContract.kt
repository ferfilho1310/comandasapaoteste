package br.com.distribuidoradosapao.viewmodels.request

import br.com.distribuidoradosapao.model.Request

interface RequestClientViewModelContract {

    fun insertRequestClient(request: Request)
    fun loadRequests(idClient: String)
    fun somaRequestsClient(idClient: String)
}