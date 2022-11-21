package br.com.distribuidoradosapaoteste.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Client(
    var idClient: String? = null,
    val name: String? = null,
    val date: String? = null,
    val nameAtendente: String? = null
) : Parcelable
