package com.android.eatwell.data.api

import com.android.eatwell.data.service.AuthState
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(private val authState: AuthState) :
    Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        when (response.code) {
            HttpURLConnection.HTTP_UNAUTHORIZED -> {
                authState.logout()
            }
        }
        return response
    }


}

