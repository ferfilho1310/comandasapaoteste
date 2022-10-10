package br.com.distribuidoradosapao.viewmodels.user

import br.com.distribuidoradosapao.model.User

interface UserViewModelContract {

    fun register(user: User)
    fun signUp(user: User)
}