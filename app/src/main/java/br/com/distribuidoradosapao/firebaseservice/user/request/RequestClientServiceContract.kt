package br.com.distribuidoradosapao.firebaseservice.user.request

import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.model.Request
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

interface RequestClientServiceContract {

    fun insertRequestClient(request: Request): Flow<Boolean>
    fun loadRequests(idClient: String): Flow<Query>
    fun somaRequestsClient(idClient: String): Flow<MutableList<Request>?>
    fun receberPedidoParcial(recebidoParcial: PedidoRecebidoParcial): Flow<Boolean>
    fun somaReceberParcial(idClient: String): Flow<MutableList<PedidoRecebidoParcial>?>
    fun recebido(idClient: String): Flow<Float>
    fun loadSomaParcial(idClient: String): Flow<Query>
}