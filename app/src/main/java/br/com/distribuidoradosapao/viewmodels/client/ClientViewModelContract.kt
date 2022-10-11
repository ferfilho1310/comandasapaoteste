package br.com.distribuidoradosapao.viewmodels.client

import br.com.distribuidoradosapao.model.Client

interface ClientViewModelContract {

    fun insertClient(client: Client)
    fun loadClient()
}