package br.com.distribuidoradosapaoteste.viewmodels.user

import br.com.distribuidoradosapaoteste.model.User

interface UserViewModelContract {

    fun register(user: User)
    fun signUp(user: User)
    fun searchUser(idUser: String)
}