package com.cypress.api_call

sealed class RetrofitResult<T>(val result: T? = null, val exception: NetworkException? = null) {

    class Success<T>(data: T) : RetrofitResult<T>(data)

    class Error<T>(
        data: T? = null,
        exception: NetworkException? = NetworkException(errorMessage = "Something went wrong!"),
    ) : RetrofitResult<T>(data, exception)

}
