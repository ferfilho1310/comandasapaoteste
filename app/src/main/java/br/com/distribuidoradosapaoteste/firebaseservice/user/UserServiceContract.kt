package br.com.distribuidoradosapaoteste.firebaseservice.user

import br.com.distribuidoradosapaoteste.model.User
import kotlinx.coroutines.flow.Flow

interface UserServiceContract {

    fun register(user: User): Flow<Boolean>
    fun signUp(user: User): Flow<Boolean>
    fun searchUser(idUser: String): Flow<User?>
}