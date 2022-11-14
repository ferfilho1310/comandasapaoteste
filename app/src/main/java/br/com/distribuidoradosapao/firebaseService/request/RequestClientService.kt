package br.com.distribuidoradosapao.firebaseService.request

import android.util.Log
import br.com.distribuidoradosapao.model.PedidoRecebidoParcial
import br.com.distribuidoradosapao.model.Request
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RequestClientService : RequestClientServiceContract {

    val db = FirebaseFirestore.getInstance()

    private var sum: ArrayList<Float> = arrayListOf()
    private var sumRecebidoParcial: ArrayList<Float> = arrayListOf()

    private var sumTotal = 0f
    private var sumTotalParcial = 0f
    private var recebido = 0f

    override fun insertRequestClient(request: Request): Flow<Boolean> {
        return callbackFlow {
            val mapRequest: MutableMap<String, Any> = HashMap()
            mapRequest["idClient"] = request.idClient.toString()
            mapRequest["amount"] = request.amount.orEmpty()
            mapRequest["nameProduct"] = request.nameProduct.toString()
            mapRequest["valueUnit"] = request.valueUnit!!
            mapRequest["valueTotal"] = request.amount?.toInt()!! * request.valueUnit
            mapRequest["date"] = request.date.toString()
            mapRequest["isComandaClosed"] = request.isComandaClosed

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

    override fun somaRequestsClient(idClient: String): Flow<MutableList<Request>?> {
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

    override fun receberPedidoParcial(recebidoParcial: PedidoRecebidoParcial): Flow<Boolean> {
        return callbackFlow {
            val mapRequest: MutableMap<String, Any> = HashMap()
            mapRequest["idClient"] = recebidoParcial.idClient.toString()
            mapRequest["value"] = recebidoParcial.value!!
            mapRequest["name"] = recebidoParcial.name.toString()

            val listener =
                db.collection("RecebidoParcial").add(mapRequest)
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

    override fun somaReceberParcial(idClient: String): Flow<MutableList<PedidoRecebidoParcial>?> {
        return callbackFlow {
            try {
                db.collection("RecebidoParcial").whereEqualTo("idClient", idClient)
                    .addSnapshotListener { value, error ->
                        val requestList = value?.toObjects(PedidoRecebidoParcial::class.java)
                        trySend(requestList).isSuccess
                    }
            } catch (ex: Exception) {
                trySend(null).isFailure
            }

            awaitClose {

            }
        }
    }

    override fun recebido(idClient: String): Flow<Float> {
        return callbackFlow {
            try {
                db.collection("Pedidos").whereEqualTo("idClient", idClient)
                    .addSnapshotListener { value, error ->
                        val requestList = value?.toObjects(Request::class.java)
                        requestList?.forEach { valueProduct ->
                            sum.add(valueProduct.valueTotal!!.toFloat())
                        }
                        sum.forEach {
                            sumTotal += it
                        }
                    }

                db.collection("RecebidoParcial").whereEqualTo("idClient", idClient)
                    .addSnapshotListener { value, error ->
                        val requestList = value?.toObjects(PedidoRecebidoParcial::class.java)
                        requestList?.forEach { valueProduct ->
                            sumRecebidoParcial.add(valueProduct.value!!.toFloat())
                        }
                        sumRecebidoParcial.forEach {
                            sumTotalParcial += it
                        }
                    }
                recebido = sumTotal - sumTotalParcial

            } catch (ex: Exception) {
                trySend(0f).isFailure
            }

        }
    }

    override fun loadSomaParcial(idClient: String): Flow<Query> {
        return callbackFlow {
            var query: Query? = null
            try {
                query = db.collection("RecebidoParcial").whereEqualTo("idClient", idClient)
                trySend(query).isSuccess
            } catch (ex: FirebaseException) {
                query?.let { trySend(it).isFailure }
            }
            awaitClose {

            }
        }
    }

    override fun updateRequest(idRequest: String, request: Request): Flow<Boolean> {
        return callbackFlow {

            val mapUpdateRequest: MutableMap<String, Any> = HashMap()
            mapUpdateRequest["amount"] = request.amount!!
            mapUpdateRequest["nameProduct"] = request.nameProduct.toString()
            mapUpdateRequest["valueUnit"] = request.valueUnit!!
            mapUpdateRequest["valueTotal"] = request.amount.toInt() * request.valueUnit

            val listerner = db.collection("Pedidos").document(idRequest).update(mapUpdateRequest)
                .addOnSuccessListener { trySend(true).isSuccess }
                .addOnFailureListener {
                    Log.e("TAG", "Error ao atualizar o pedido $it")
                    trySend(false).isFailure
                }
            awaitClose {
                listerner.isCanceled
            }
        }
    }

    override fun filterRequestForDate(data: String): Flow<MutableList<Request>?> {
        return callbackFlow {
            try {
                val listener = db.collection("Pedidos")
                    .whereEqualTo("date", data)
                    .orderBy("date")

                listener.addSnapshotListener { value, error ->
                    trySend(value?.toObjects(Request::class.java)).isSuccess
                }
            } catch (ex: Exception) {
                trySend(arrayListOf()).isFailure
            }
            awaitClose {

            }
        }
    }

    override fun loadAllRequest(): Flow<MutableList<Request>?> {
        return callbackFlow {
            val listener = db.collection("Pedidos")
                .get()
                .addOnSuccessListener {
                    val requestList = it.toObjects(Request::class.java)
                    trySend(requestList).isSuccess
                }.addOnFailureListener {
                    trySend(arrayListOf()).isFailure
                }
            awaitClose {
                listener.isCanceled
            }
        }
    }

    override fun deleteRequestReceived(idRequestReceived: String): Flow<Boolean> {
       return callbackFlow {
           val listerner = db.collection("RecebidoParcial")
               .document(idRequestReceived)
               .delete()
               .addOnSuccessListener {
                   trySend(true).isSuccess
               }.addOnFailureListener {
                   trySend(false).isFailure
               }
           awaitClose {
               listerner.isCanceled
           }
       }
    }
}