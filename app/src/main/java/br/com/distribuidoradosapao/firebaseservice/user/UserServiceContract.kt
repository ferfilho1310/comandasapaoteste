package br.com.distribuidoradosapao.firebaseservice.user

import br.com.distribuidoradosapao.model.User
import kotlinx.coroutines.flow.Flow

interface UserServiceContract {

    fun register(user: User): Flow<Boolean>
    fun signUp(user: User): Flow<Boolean>
}