package com.cypress.api_call

import android.app.Activity
import com.cypress.R
import com.cypress.extensions.getStringResources
import retrofit2.HttpException
import retrofit2.Response

fun <A, B> Activity.checkStatus(request: A?, response: Response<B?>?): RetrofitResult<B?> {

    try {

        return if (response == null) {

            return RetrofitResult.Error(
                exception = NetworkException(400, getStringResources(R.string.somethingWrong))
            )

        } else {

            if (response.isSuccessful) {

                if (response.code() == 200) RetrofitResult.Success(data = response.body())
                else RetrofitResult.Error(
                    exception = NetworkException(
                        resultCode = response.code(),
                        request = request?.toString(),
                        headers = response.headers().toString(),
                        errorMessage = response.message()
                    )
                )

            } else {

                RetrofitResult.Error(
                    exception = NetworkException(
                        resultCode = response.code(),
                        request = request?.toString(),
                        headers = response.headers().toString(),
                        errorMessage = response.message()
                    )
                )

            }

        }

    } catch (e: HttpException) {

        return RetrofitResult.Error(
            exception = NetworkException(
                resultCode = e.code(), errorMessage = e.message()
            )
        )

    } catch (e: Exception) {

        return RetrofitResult.Error(
            exception = NetworkException(
                resultCode = e.hashCode(), errorMessage = e.message
            )
        )

    }

}