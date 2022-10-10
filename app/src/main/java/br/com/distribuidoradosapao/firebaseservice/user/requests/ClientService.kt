package br.com.distribuidoradosapao.firebaseservice.user.requests

import android.util.Log
import br.com.distribuidoradosapao.model.Client
import com.google.firebase.firestore.FirebaseFirestore
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

           val insertDb = db.collection("Client").document().set(clientMap)
                .addOnSuccessListener {
                    trySend(true).isSuccess
                }.addOnFailureListener {
                    trySend(false).isFailure
                   Log.e("TAG","Erro ao inserir cliente $it")
                }
            awaitClose {
                insertDb.isCanceled
            }
        }
    }
}