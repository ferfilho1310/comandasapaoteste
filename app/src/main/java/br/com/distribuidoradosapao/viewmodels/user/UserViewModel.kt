package br.com.distribuidoradosapao.viewmodels.user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.distribuidoradosapao.firebaseservice.user.UserServiceContract
import br.com.distribuidoradosapao.model.User
import br.com.distribuidoradosapao.util.FirebaseCrashlyticsUtils
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class UserViewModel(
    val userService: UserServiceContract
) : ViewModel(),
    UserViewModelContract {

    private val _register: MutableLiveData<Boolean> = MutableLiveData()
    var register: LiveData<Boolean> = _register
    private val _signUp: MutableLiveData<Boolean> = MutableLiveData()
    var signUp: LiveData<Boolean> = _signUp
    private val _searchUser: MutableLiveData<User?> = MutableLiveData()
    var searchUser: LiveData<User?> = _searchUser

    override fun register(user: User) {
        userService.register(user)
            .onEach {
                _register.value = it
            }.catch {
                Log.e("ERROR", "Error ao efetuar o cadastro ${it.cause}")
                _register.value = false
                FirebaseCrashlyticsUtils.log("Error ao efetuar o cadastro ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun signUp(user: User) {
        userService.signUp(user)
            .onEach {
                _signUp.value = it
            }.catch {
                Log.e("ERROR", "Error ao efetuar login ${it.cause}")
                _signUp.value = false
                FirebaseCrashlyticsUtils.log("Error ao efetuar login ${it.cause}")
            }.launchIn(viewModelScope)
    }

    override fun searchUser(idUser: String) {
        userService.searchUser(idUser)
            .onEach {
                _searchUser.value = it
            }.catch {
                Log.e("ERROR", "Error ao buscar o usuário ${it.cause}")
                _searchUser.value = null
                FirebaseCrashlyticsUtils.log("Error ao buscar o usuário ${it.cause}")
            }.launchIn(viewModelScope)
    }
}