package br.com.distribuidoradosapao.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val email: String? = null,
    val senha: String? = null,
    val confirmaSenha: String? = null,
    val name: String? = null,
    val id: String? = null
): Parcelable
