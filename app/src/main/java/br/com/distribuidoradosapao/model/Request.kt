package br.com.distribuidoradosapao.model

data class Request(
    val idClient: String? = null,
    val amount: String? = null,
    val nameProduct: String? = null,
    val valueUnit: String? = null,
    val valueTotal: String? = null
)