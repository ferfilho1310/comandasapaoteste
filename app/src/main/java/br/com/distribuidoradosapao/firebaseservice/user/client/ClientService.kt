package br.com.distribuidoradosapao.firebaseservice.user.client

import android.util.Log
import br.com.distribuidoradosapao.model.Client
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SnapshotMetadata
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
}