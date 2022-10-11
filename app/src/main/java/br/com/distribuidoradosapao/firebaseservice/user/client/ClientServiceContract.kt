package br.com.distribuidoradosapao.firebaseservice.user.client

import br.com.distribuidoradosapao.model.Client
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

interface ClientServiceContract {

    fun insertClient(client: Client): Flow<Boolean>
    fun loadClients() : Flow<Query>
}