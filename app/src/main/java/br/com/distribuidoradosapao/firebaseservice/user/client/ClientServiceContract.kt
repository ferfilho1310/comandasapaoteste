package br.com.distribuidoradosapao.firebaseservice.user.client

import br.com.distribuidoradosapao.model.Client
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

interface ClientServiceContract {

    fun insertClient(client: Client): Flow<Boolean>
    fun loadClients() : Flow<Query>
    fun deleteClient(idClient: String): Flow<Boolean>
    fun searchClientBeforeDeleted(idClient: String): Flow<Client?>
    fun insertClientDeleted(client: Client): Flow<Boolean>
    fun loadOneClient(idClient: String): Flow<Client?>
    fun updateClient(idClient: String,client: Client): Flow<Boolean>
}