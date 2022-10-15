package br.com.distribuidoradosapao.firebaseservice.user.request

import br.com.distribuidoradosapao.model.Request
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RequestClientService : RequestClientServiceContract {

    val db = FirebaseFirestore.getInstance()

    override fun insertRequestClient(request: Request): Flow<Boolean> {
        return callbackFlow {
            val mapRequest: MutableMap<String, String> = HashMap()
            mapRequest["idClient"] = request.idClient.toString()
            mapRequest["amount"] = request.amount.toString()
            mapRequest["nameProduct"] = request.nameProduct.toString()
            mapRequest["value"] = request.value.toString()

            val listener =
                db.collection("Pedidos").add(mapRequest)
                    .addOnSuccessListener {
                        trySend(true).isSuccess
                    }.addOnFailureListener {
                        trySend(false).isFailure
                    }
            awaitClose {
                listener.isCanceled
            }
        }
    }

    override fun loadRequests(idClient: String): Flow<Query> {
        return callbackFlow {
            var query: Query? = null
            try {
                query = db.collection("Pedidos").whereEqualTo("idClient", idClient)
                trySend(query).isSuccess
            } catch (ex: FirebaseException) {
                query?.let { trySend(it).isFailure }
            }
            awaitClose {

            }
        }
    }

    override fun somaRequestsClinet(idClient: String): Flow<MutableList<Request>?> {
        return callbackFlow {
            try {
                db.collection("Pedidos").whereEqualTo("idClient", idClient)
                    .addSnapshotListener { value, error ->
                        val requestList = value?.toObjects(Request::class.java)
                        trySend(requestList).isSuccess
                    }
            } catch (ex: Exception) {
                trySend(null).isFailure
            }

            awaitClose {

            }
        }
    }
}