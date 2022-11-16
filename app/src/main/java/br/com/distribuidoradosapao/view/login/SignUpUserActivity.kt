package br.com.distribuidoradosapao.view.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.SignUpActivityBinding
import br.com.distribuidoradosapao.model.User
import br.com.distribuidoradosapao.view.main.MainActivity
import br.com.distribuidoradosapao.viewmodels.remoteConfig.RemoteConfigViewModel
import br.com.distribuidoradosapao.viewmodels.user.UserViewModel
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject


class SignUpUserActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: UserViewModel by inject()
    private val remoteConfig: RemoteConfigViewModel by inject()
    private lateinit var binding: SignUpActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        remoteConfig.remoteConfigFetch(this)
        super.onCreate(savedInstanceState)
        binding = SignUpActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setObservers()
        setListeners()
        setViewModel()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_register -> {
                startActivity(Intent(this, RegisterUserActivity::class.java))
            }
            R.id.bt_entrar -> {
                validateInformationLogin()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().uid != null) {
            startMainActivity()
        }


    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setViewModel() {
        remoteConfig.remoteConfigLiveData.observe(this) {
            binding.tvRegister.isVisible = it
        }
    }

    private fun setListeners() {
        binding.let {
            it.tvRegister.setOnClickListener(this)
            it.btEntrar.setOnClickListener(this)
        }
    }

    private fun setObservers() {
        viewModel.signUp.observe(this) {
            if (it == true) {
                setVisibilityProgressBarButtonTextView(
                    isVisibleBt = true,
                    isVisibleTv = true,
                    isVisiblePg = false,
                    isEnabledEdEmail = true,
                    isEnabledEdSenha = true
                )
                startMainActivity()
            } else {
                setVisibilityProgressBarButtonTextView(
                    isVisibleBt = true,
                    isVisibleTv = true,
                    isVisiblePg = false,
                    isEnabledEdEmail = false,
                    isEnabledEdSenha = false
                )
                Toast.makeText(
                    this,
                    "Erro ao fazer login. Verifique seu e-mail e/ou senha.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setVisibilityProgressBarButtonTextView(
        isVisibleBt: Boolean,
        isVisibleTv: Boolean,
        isVisiblePg: Boolean,
        isEnabledEdEmail: Boolean,
        isEnabledEdSenha: Boolean
    ) {
        binding.apply {
            btEntrar.isVisible = isVisibleBt
            tvRegister.isVisible = isVisibleTv
            pgLogin.isVisible = isVisiblePg
            edEmail.isEnabled = isEnabledEdEmail
            edSenha.isEnabled = isEnabledEdSenha
        }
    }

    private fun validateInformationLogin() {
        binding.apply {
            when {
                edEmail.text.toString().isEmpty() -> edEmail.error = "Informe seu email"
                edSenha.text.toString().isEmpty() -> edSenha.error = "Informe sua senha"
                else -> {
                    setVisibilityProgressBarButtonTextView(
                        isVisibleBt = false,
                        isVisibleTv = false,
                        isVisiblePg = true,
                        isEnabledEdEmail = false,
                        isEnabledEdSenha = false
                    )
                    viewModel.signUp(
                        User(
                            email = edEmail.text.toString(),
                            senha = edSenha.text.toString()
                        )
                    )
                }
            }

        }
    }
}