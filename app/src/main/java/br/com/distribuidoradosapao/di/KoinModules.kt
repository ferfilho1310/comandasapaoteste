package br.com.distribuidoradosapao.di

import br.com.distribuidoradosapao.firebaseservice.user.user.UserService
import br.com.distribuidoradosapao.firebaseservice.user.user.UserServiceContract
import br.com.distribuidoradosapao.firebaseservice.user.client.ClientService
import br.com.distribuidoradosapao.firebaseservice.user.client.ClientServiceContract
import br.com.distribuidoradosapao.view.MainActivity
import br.com.distribuidoradosapao.view.login.RegisterUserActivity
import br.com.distribuidoradosapao.view.login.SignUpUserActivity
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import br.com.distribuidoradosapao.viewmodels.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModules {

    val activity = module {
        single { MainActivity() }
        single { SignUpUserActivity() }
        single { RegisterUserActivity() }
    }

    val service = module {
        factory<UserServiceContract> { UserService() }
        factory<ClientServiceContract> { ClientService() }
    }

    val viewModels = module {
        viewModel { UserViewModel(userService = get()) }
        viewModel { ClientViewModel(service = get()) }
    }
}