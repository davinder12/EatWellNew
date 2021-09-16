package com.android.mealpass.data.network

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.android.mealpass.data.extension.postUpdate
import com.android.mealpass.data.models.CommonResponseModel
import com.android.mealpass.data.models.ResponseValidator
import com.android.mealpass.data.repository.UserRepository.Companion.RESPONSE_FALIURE
import com.android.mealpass.data.repository.UserRepository.Companion.SESSION_EXPIRED
import com.exactsciences.portalapp.data.network.AppExecutors
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import mealpass.com.mealpass.R
import org.json.JSONObject

/**
 * Class that performs a single network request
 */
open class NetworkRequest<ResponseType>
@MainThread constructor(
    private val appExecutors: AppExecutors,
    private val cb: INetworkRequestCallback<ResponseType>
) : IRequest<ResponseType> {

    override fun retry(networkState: NetworkState) {
        Log.e("response","sdfsd")
        request
    }

//    private val responseLiveData : MediatorLiveData<ResponseType> = MediatorLiveData()
//
//    override val responseData: MutableLiveData<ResponseType>
//        get() =  responseLiveData

    private val _networkState: MediatorLiveData<NetworkState> = MediatorLiveData()

    override val networkState: LiveData<NetworkState> get() = _networkState

    override val request = prepareNetworkRequest()

    private fun prepareNetworkRequest(): Maybe<ResponseType> {
        return createNetworkRequest()
    }

    protected open fun createNetworkRequest(): Maybe<ResponseType> {
        val request = cb.createNetworkRequest() ?: return Maybe.empty()
        return request.doOnError {
            _networkState.postUpdate(cb.getExceptionState(it))
            // Timber.e(it)
        }.doOnSubscribe {
            _networkState.postUpdate(NetworkState.loading)
        }
            .subscribeOn(Schedulers.from(appExecutors.networkIO()))
            .observeOn(Schedulers.from(appExecutors.diskIO()))
            .filter(this::filterAndUpdateNetworkState)
            .doOnSuccess(cb::onSuccess)
            .onErrorComplete()
    }

    private fun filterAndUpdateNetworkState(response: ResponseType): Boolean {
        val responseStatus = cb.getResponseStatus(response)
        val isResponseCodeOk = cb.isSuccess(response)
        val filter = isResponseCodeOk && responseStatus.code == 1
        when {
            responseStatus.code == RESPONSE_FALIURE -> _networkState.postUpdate(NetworkState.error(responseStatus.message, R.string.network_error_unknown))
            responseStatus.code == SESSION_EXPIRED ->{
                _networkState.postUpdate(NetworkState.error(responseStatus.message, R.string.network_error_unknown))
                cb.sessionExpired()
            }
            filter -> _networkState.postUpdate(NetworkState.success)
            else -> _networkState.postUpdate(cb.getErrorState(response))
        }
        return filter
    }

    private fun checkBodyStatusCode(jsonObject: String): Boolean {
        var isConditionTrue = false
        try {
            val data = JSONObject(jsonObject)
            val status = data.getJSONObject("status")
            isConditionTrue = when {
                status.getString("code").equals("1", ignoreCase = true) -> true
                else -> {
                    _networkState.postUpdate(
                        NetworkState.error(
                            status.getString("message"),
                            R.string.network_error_unknown
                        )
                    )
                    false
                }
            }
        } catch (e: Exception) {
            _networkState.postUpdate(NetworkState.error(null, R.string.network_error_unknown))
        }
        return isConditionTrue
    }
}


/**
 * Callback for [NetworkRequest]
 */
interface INetworkRequestCallback<ResponseType> {
    /**
     * Called if [isSuccess] returns false.
     * @return A [NetworkState] representing the error.
     */
    fun getErrorState(response: ResponseType): NetworkState

    /**
     * Create and return the network request.
     */
    @MainThread
    fun createNetworkRequest(): Single<ResponseType>

    /**
     * Filter http errors.
     * @return True if successful, false otherwise.
     */
    fun isSuccess(response: ResponseType): Boolean

    /**
     * Called if [isSuccess] returns false.
     * @return A [NetworkState] representing the error.
     */


    fun getResponseStatus(response: ResponseType): ResponseValidator = ResponseValidator(1,"")

    fun sessionExpired() {}

    fun getBodyErrorStatusCode(response: ResponseType): String = "{\"status\":{\"code\":\"1\"}}"

    /**
     * Called when the fetch operation throws an exception.
     * @return A [NetworkState] representing the exception.
     */
    fun getExceptionState(it: Throwable): NetworkState {
        return NetworkState.error(it.localizedMessage)
    }

    /**
     * Called on success
     */
    fun onSuccess(response: ResponseType) {}
}

