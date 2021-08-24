package com.android.mealpass.dependencyinjection

import com.android.mealpass.data.api.*
import com.android.mealpass.data.service.PreferenceService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import mealpass.com.mealpass.BuildConfig
import mealpass.com.mealpass.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

const val API_VERSION_HEADER = "Accept: application/json; version="

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        @Named("api_url") baseUrl: String, httpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl).client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
    }


    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return buildApiClient(retrofit, AuthApi.MODULE_PATH).create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun provideProductApi(retrofit: Retrofit): ProductApi {
        return buildApiClient(retrofit, ProductApi.MODULE_PATH).create(ProductApi::class.java)
    }


    @Singleton
    @Provides
    fun provideReceiptApi(retrofit: Retrofit): ReceiptApi {
        return buildApiClient(retrofit, ReceiptApi.MODULE_PATH).create(ReceiptApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMerchantApi(retrofit: Retrofit): MerchantApi {
        return buildApiClient(retrofit, MerchantApi.MODULE_PATH).create(MerchantApi::class.java)
    }




    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        preferenceService: PreferenceService,
        authenticationInterceptor: AuthenticationInterceptor,
    ): OkHttpClient {
        val httpClient = OkHttpClient.Builder().apply {
            readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
        }
        httpClient.addNetworkInterceptor(loggingInterceptor)
        httpClient.addNetworkInterceptor(authenticationInterceptor)
        httpClient.addInterceptor { chain ->
            val ongoing = chain.request().newBuilder()
            ongoing.addHeader(
                "Token",
                preferenceService.getString(R.string.pkey_secure_token) ?: ""
            )
            ongoing.addHeader("Content-Type", "application/json;charset=utf-8")
            ongoing.addHeader("Accept", "application/json; application/problem+json")
            val response = chain.proceed(ongoing.build())
            response
        }
        return httpClient.build()
    }

    @Singleton
    @Provides
    @Named("api_url")
    fun provideApiUrl(): String {
        return BuildConfig.API_URL
    }

    private fun buildApiClient(retrofit: Retrofit, path: String): Retrofit {
        return retrofit.newBuilder().baseUrl(retrofit.baseUrl().toUrl().toString() + path)
            .validateEagerly(true).build()
    }

    companion object {
        private const val HTTP_TIMEOUT = 40L
    }


    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}
