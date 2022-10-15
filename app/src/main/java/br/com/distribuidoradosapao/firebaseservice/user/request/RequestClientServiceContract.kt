package br.com.distribuidoradosapao.firebaseservice.user.request

import br.com.distribuidoradosapao.model.Client
import br.com.distribuidoradosapao.model.Request
import com.firebase.ui.firestore.ObservableSnapshotArray
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow

interface RequestClientServiceContract {

    fun insertRequestClient(request: Request): Flow<Boolean>
    fun loadRequests(idClient: String): Flow<Query>
    fun somaRequestsClinet(idClient: String): Flow<MutableList<Request>?>
}