package br.com.distribuidoradosapao.firebaseservice.user.requests

import br.com.distribuidoradosapao.model.Client
import kotlinx.coroutines.flow.Flow

interface ClientServiceContract {

    fun insertClient(client: Client): Flow<Boolean>
}