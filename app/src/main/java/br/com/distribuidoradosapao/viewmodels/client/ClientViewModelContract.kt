package br.com.distribuidoradosapao.viewmodels.client

import br.com.distribuidoradosapao.model.Client

interface ClientViewModelContract {

    fun insertClient(client: Client)
    fun loadClients()
    fun deletClient(isClient: String)
    fun searchClient(idClient: String)
    fun insertClientBeforeDelete(client: Client?, idClient: String)
    fun loadOneClient(idClient: String)
    fun updateClient(idClient: String,client: Client)
}