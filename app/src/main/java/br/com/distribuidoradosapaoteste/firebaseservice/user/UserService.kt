package br.com.distribuidoradosapaoteste.firebaseservice.user

import android.util.Log
import br.com.distribuidoradosapaoteste.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserService : UserServiceContract {

    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()

    override fun register(user: User): Flow<Boolean> {
        return callbackFlow {
            val listener =
                auth.createUserWithEmailAndPassword(user.email.orEmpty(), user.senha.orEmpty())
                    .addOnSuccessListener {
                        val userMap: MutableMap<String, String> = HashMap()
                        userMap["email"] = user.email.orEmpty()
                        userMap["senha"] = user.senha.orEmpty()
                        userMap["confirmaSenha"] = user.confirmaSenha.orEmpty()
                        userMap["id"] = auth.uid.orEmpty()
                        userMap["name"] = user.name.toString()

                        db.collection("User")
                            .document(auth.uid.orEmpty())
                            .set(userMap)

                        trySend(true).isSuccess
                    }.addOnFailureListener {
                        trySend(false).isFailure
                        cancel("Error ao fazer o cadastro", it)

                        Log.e("TAG", "Error ao cadastrar usuário $it")
                    }
            awaitClose {
                listener.isCanceled
            }
        }
    }

    override fun signUp(user: User): Flow<Boolean> {
        return callbackFlow {
            val listener =
                auth.signInWithEmailAndPassword(user.email.orEmpty(), user.senha.orEmpty())
                    .addOnSuccessListener {
                        trySend(true).isSuccess
                    }.addOnFailureListener {
                        trySend(false).isFailure

                        cancel("Error ao efetuar login", it)
                        Log.e("TAG", "Error ao efetuar login $it")
                    }
            awaitClose {
                listener.isCanceled
            }
        }
    }

    override fun searchUser(idUser: String): Flow<User?> {
        return callbackFlow {
            val listerner = db.collection("User")
                .document(idUser)
                .get()
                .addOnSuccessListener { snapShot ->
                    val user = snapShot.toObject(User::class.java)
                     trySend(user).isSuccess
                }.addOnFailureListener {
                    trySend(null).isFailure
                }
            awaitClose{
                listerner.isCanceled
            }
        }
    }
}