package br.com.distribuidoradosapao.firebaseService.client

import android.util.Log
import br.com.distribuidoradosapao.model.Client
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ClientService : ClientServiceContract {

    val db = FirebaseFirestore.getInstance()

    override fun insertClient(client: Client): Flow<Boolean> {
        return callbackFlow {
            val clientMap: MutableMap<String, String> = HashMap()
            clientMap["name"] = client.name.toString()
            clientMap["date"] = client.date.toString()
            clientMap["nameAtendente"] = client.nameAtendente.toString()

            val insertDb = db.collection("Client")
                .document().set(clientMap)
                .addOnSuccessListener {
                    trySend(true).isSuccess
                }.addOnFailureListener {
                    trySend(false).isFailure
                    Log.e("TAG", "Erro ao inserir cliente $it")
                }
            awaitClose {
                insertDb.isCanceled
            }
        }
    }

    override fun loadClients(): Flow<Query> {
        return callbackFlow {
            var query: Query? = null
            try {
                query = db.collection("Client")
                trySend(query).isSuccess
            } catch (ex: FirebaseException) {
                query?.let { trySend(it).isFailure }
            }
            awaitClose {

            }
        }
    }

    override fun deleteClient(idClient: String): Flow<Boolean> {
        return callbackFlow {
            val listener = db.collection("Client").document(idClient)
                .delete()
                .addOnSuccessListener {
                    trySend(true).isSuccess
                }.addOnFailureListener {
                    trySend(false).isFailure
                    Log.e("Error", "Erro ao dele client")
                }
            awaitClose {
                listener.isCanceled
            }
        }
    }

    override fun searchClientBeforeDeleted(idClient: String): Flow<Client?> {
        return callbackFlow {
            val listerner = db.collection("Client").document(idClient).get()
                .addOnSuccessListener {
                    val client = it.toObject(Client::class.java)
                    trySend(client).isSuccess
                }.addOnFailureListener {
                    Log.e("Error", "Erro ao buscar cliente")
                    trySend(null).isFailure
                }
            awaitClose {
                listerner.isCanceled
            }
        }
    }

    override fun insertClientDeleted(client: Client, idClient: String, isComandaClosed: Boolean): Flow<Boolean> {
        return callbackFlow {
            val clientMap: MutableMap<String, Any> = HashMap()
            clientMap["name"] = client.name.toString()
            clientMap["date"] = client.date.toString()
            clientMap["nameAtendente"] = client.nameAtendente.toString()
            clientMap["idClient"] = idClient

            val insertDb = db.collection("ClientDeleted")
                .document().set(clientMap)
                .addOnSuccessListener {
                    trySend(true).isSuccess
                }.addOnFailureListener {
                    trySend(false).isFailure
                    Log.e("TAG", "Erro ao inserir cliente $it")
                }
            awaitClose {
                insertDb.isCanceled
            }
        }
    }

    override fun loadOneClient(idClient: String): Flow<Client?> {
        return callbackFlow {
            val listener = db.collection("Client").document(idClient)
                .get()
                .addOnSuccessListener {
                    val client = it.toObject(Client::class.java)
                    trySend(client).isSuccess
                }.addOnFailureListener {
                    trySend(null).isSuccess
                }
            awaitClose {
                listener.isCanceled
            }
        }
    }

    override fun updateClient(idClient: String, client: Client): Flow<Boolean> {
        return callbackFlow {
            val mapUpdateClient: MutableMap<String, Any> = HashMap()
            mapUpdateClient["name"] = client.name.toString()

            val listerner = db.collection("Client").document(idClient).update(mapUpdateClient)
                .addOnSuccessListener { trySend(true).isSuccess }
                .addOnFailureListener { trySend(false).isFailure }
            awaitClose {
                listerner.isCanceled
            }
        }
    }

    override fun loadClientsDeleted(): Flow<Query> {
        return callbackFlow {
            var query: Query? = null
            try {
                query = db.collection("ClientDeleted")
                trySend(query).isSuccess
            } catch (ex: FirebaseException) {
                query?.let { trySend(it).isFailure }
            }
            awaitClose {

            }
        }
    }

    override fun updateClientId(idClient: String): Flow<Boolean> {
        return callbackFlow {
            val mapUpdateClient: MutableMap<String, Any> = HashMap()
            mapUpdateClient["id"] = idClient

            val listerner = db.collection("Client").document(idClient).update(mapUpdateClient)
                .addOnSuccessListener { trySend(true).isSuccess }
                .addOnFailureListener { trySend(false).isFailure }
            awaitClose {
                listerner.isCanceled
            }
        }
    }
}