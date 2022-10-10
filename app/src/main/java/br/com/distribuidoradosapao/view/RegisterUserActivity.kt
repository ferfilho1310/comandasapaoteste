package br.com.distribuidoradosapao.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.RegisterUserActivityBinding
import br.com.distribuidoradosapao.model.User
import br.com.distribuidoradosapao.viewmodels.user.UserViewModel
import com.google.firebase.FirebaseApp
import org.koin.android.ext.android.inject

class RegisterUserActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: RegisterUserActivityBinding
    private val viewModel: UserViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        binding = RegisterUserActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setObservers()
        setListeners()
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
                setVisibleProgressBarButton()
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
                            edRegistrarEmail.text.toString(),
                            edRegistrarSenha.text.toString(),
                            edConfirmaSenha.text.toString(),
                            edNome.text.toString()
                        )
                    )
                }
            }
        }
    }

    private fun startSignUpActivity() {
        startActivity(Intent(this, SignUpUserActivity::class.java))
        finish()
    }

    private fun setVisibleProgressBarButton() {
        binding.pgRegisterUser.isVisible = false
        binding.btCadastrar.isVisible = true
    }
}