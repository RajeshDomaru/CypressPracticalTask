package com.cypress.api_call

data class NetworkException(
    val resultCode: Int? = null,
    val request: String? = null,
    val headers: String? = null,
    val errorMessage: String? = null
)