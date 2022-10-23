package br.com.distribuidoradosapao.model

data class Request(
    val idClient: String? = null,
    val amount: Int? = null,
    val nameProduct: String? = null,
    val valueUnit: Float? = null,
    val valueTotal: Float? = null
)