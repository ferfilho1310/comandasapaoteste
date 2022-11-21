package br.com.distribuidoradosapaoteste.view.login

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import br.com.distribuidoradosapaoteste.R
import br.com.distribuidoradosapaoteste.databinding.RegisterUserActivityBinding
import br.com.distribuidoradosapaoteste.model.User
import br.com.distribuidoradosapaoteste.view.main.MainActivity
import br.com.distribuidoradosapaoteste.viewmodels.user.UserViewModel
import com.google.firebase.FirebaseApp
import org.koin.android.ext.android.inject

class RegisterUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: RegisterUserActivityBinding
    private val viewModel: UserViewModel by inject()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = RegisterUserActivityBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDefaultDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        supportActionBar?.title = "Faça seu cadastro"

        setObservers()
        setListeners()
        setListenerToolbar()
    }

    private fun setListenerToolbar() {
        binding.toolbar.let {
            it.navigationIcon = resources.getDrawable(R.drawable.ic_back)
            it.setNavigationOnClickListener {
                startActivity(
                    Intent(
                        this,
                        SignUpUserActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                finish()
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_cadastrar -> {
                binding.apply {
                    validateInformatioRegistration()
                }
            }
        }
    }

    private fun setListeners() {
        binding.btCadastrar.setOnClickListener(this)
    }

    private fun setObservers() {
        viewModel.register.observe(this) {
            if (it == false) {
                binding.pgRegisterUser.isVisible = false
                binding.btCadastrar.isVisible = true
                Toast.makeText(
                    this,
                    "Erro ao cadastrar usuário. Verifique seu e-mail e/ou senha.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                startSignUpActivity()
            }
        }
    }

    private fun validateInformatioRegistration() {
        binding.apply {
            when {
                edNome.text.toString().isEmpty() -> edNome.error = "Informe seu nome"
                edRegistrarEmail.text.toString().isEmpty() -> edRegistrarEmail.error =
                    "Informe seu e-mail"
                edRegistrarSenha.text.toString().isEmpty() -> edRegistrarSenha.error =
                    "Informe uma senha"
                edConfirmaSenha.text.toString().isEmpty() -> edConfirmaSenha.error =
                    "Verifique a senha digitada"
                edRegistrarSenha.text.toString().length < 6 -> edRegistrarSenha.error =
                    "Senha deve ter no minimo 6 dígitos"
                edRegistrarSenha.text.toString() != edConfirmaSenha.text.toString() -> Toast.makeText(
                    applicationContext,
                    "As senhas estão diferentes",
                    Toast.LENGTH_LONG
                ).show()
                else -> {
                    setVisibleProgressBarButton()

                    viewModel.register(
                        User(
                            email = edRegistrarEmail.text.toString(),
                            senha = edRegistrarSenha.text.toString(),
                            confirmaSenha = edConfirmaSenha.text.toString(),
                            name = edNome.text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun startSignUpActivity() {
        startActivity(
            Intent(
                this,
                MainActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        finish()
    }

    private fun setVisibleProgressBarButton() {
        binding.pgRegisterUser.isVisible = true
        binding.btCadastrar.isVisible = false
    }
}