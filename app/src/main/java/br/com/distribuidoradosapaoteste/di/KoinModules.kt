package br.com.distribuidoradosapaoteste.di

import br.com.distribuidoradosapao.firebaseservice.user.UserService
import br.com.distribuidoradosapao.firebaseservice.user.UserServiceContract
import br.com.distribuidoradosapaoteste.firebaseservice.client.ClientService
import br.com.distribuidoradosapao.firebaseservice.client.ClientServiceContract
import br.com.distribuidoradosapao.firebaseservice.remoteConfig.RemoteConfigService
import br.com.distribuidoradosapao.firebaseservice.remoteConfig.RemoteConfigServiceContract
import br.com.distribuidoradosapao.firebaseservice.request.RequestClientService
import br.com.distribuidoradosapao.firebaseservice.request.RequestClientServiceContract
import br.com.distribuidoradosapao.view.main.MainActivity
import br.com.distribuidoradosapao.view.request.RequestClientFinishActivity
import br.com.distribuidoradosapao.view.login.RegisterUserActivity
import br.com.distribuidoradosapao.view.login.SignUpUserActivity
import br.com.distribuidoradosapao.viewmodels.client.ClientViewModel
import br.com.distribuidoradosapao.viewmodels.remoteConfig.RemoteConfigViewModel
import br.com.distribuidoradosapao.viewmodels.request.RequestClientViewModel
import br.com.distribuidoradosapao.viewmodels.user.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object KoinModules {

    val activity = module {
        single { MainActivity() }
        single { SignUpUserActivity() }
        single { RegisterUserActivity() }
        single { RequestClientFinishActivity() }
    }

    val service = module {
        factory<UserServiceContract> { UserService() }
        factory<ClientServiceContract> { br.com.distribuidoradosapaoteste.firebaseservice.client.ClientService() }
        factory<RequestClientServiceContract> { RequestClientService() }
        factory<RemoteConfigServiceContract> { RemoteConfigService() }
    }

    val viewModels = module {
        viewModel { UserViewModel(userService = get()) }
        viewModel { ClientViewModel(service = get()) }
        viewModel { RequestClientViewModel(requestClientService = get()) }
        viewModel { RemoteConfigViewModel(remoteConfigService = get()) }
    }
}