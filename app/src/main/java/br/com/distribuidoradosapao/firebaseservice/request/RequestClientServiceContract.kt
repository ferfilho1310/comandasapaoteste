package br.com.distribuidoradosapao.firebaseservice.request

import br.com.distribuidoradosapao.model.RequestReceivedPartial
import br.com.distribuidoradosapao.model.Request
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

interface RequestClientServiceContract {

    fun insertRequestClient(request: Request): Flow<Boolean>
    fun loadRequests(idClient: String): Flow<Query>
    fun somaRequestsClient(idClient: String): Flow<MutableList<Request>?>
    fun receberPedidoParcial(recebidoParcial: RequestReceivedPartial): Flow<Boolean>
    fun somaReceberParcial(idClient: String): Flow<MutableList<RequestReceivedPartial>?>
    fun recebido(idClient: String): Flow<Float>
    fun loadSomaParcial(idClient: String): Flow<Query>
    fun updateRequest(idRequest: String, request: Request): Flow<Boolean>
    fun filterRequestForDate(data: String): Flow<MutableList<Request>?>
    fun loadAllRequest(): Flow<MutableList<Request>?>
    fun deleteRequestReceived(idRequestReceived: String): Flow<Boolean>
}