package com.android.mealpass.data.repository

import androidx.lifecycle.MutableLiveData
import com.android.mealpass.data.api.ProductApi
import com.android.mealpass.data.models.*
import com.android.mealpass.data.network.*
import com.android.mealpass.data.service.AuthState
import com.android.mealpass.utilitiesclasses.IListResource
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.exactsciences.portalapp.data.network.AppExecutors
import com.google.gson.JsonObject
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field
import javax.inject.Inject


class ProductRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val productApi: ProductApi,
    private val authState: AuthState
) {


    companion object {
        const val PLAIN_TEXT = "text/plain"
        const val RESTURANT_ACTIVE = 1  //
    }

    fun searchApi(foodRequestModel: FoodRequestModel, updatedOffset:MutableLiveData<Int>): IListResource<FoodData.Body> {
        return PagedListNetworkCall(object : PaginationList<FoodData.Body, FoodData>(appExecutors) {
            override fun loadPage(page: Int): Call<FoodData> {
                return productRequestModel(foodRequestModel)
            }
            override fun loadAfterPage(page: Int): Call<FoodData> {
                foodRequestModel.offset = page + foodRequestModel.perviousHoldOffset
                foodRequestModel.limit = foodRequestModel.defaultLimit
                updatedOffset.postValue(foodRequestModel.offset)
                return productRequestModel(foodRequestModel)
            }
            override fun mapToLocal(items: FoodData): List<FoodData.Body> {
                return items.body.filter { it.is_active == RESTURANT_ACTIVE }
            }
            override fun getResponseStatus(items: FoodData): ResponseValidator {
               return ResponseValidator(items.status.code, items.status.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun getFoodList(userId: String, listOfObject: List<String>): IResource<ProductTypeResponse> {
        return NetworkResource(appExecutors, object :
            IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<ProductTypeResponse, ProductTypeResponse> {
            override fun mapToLocal(response: ProductTypeResponse): ProductTypeResponse {
                return FoodDataFilterMapper.Mapper.from(response, listOfObject)
            }

            override fun createNetworkRequest(): Single<Response<ProductTypeResponse>> {
                return productApi.getFoodType(userId)
            }

            override fun getResponseStatus(response: Response<ProductTypeResponse>): ResponseValidator {
                return  ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }

        })
    }

    fun favouriteListApi(foodRequestModel: FoodRequestModel):  IResource<FoodData> {
        return NetworkResource(appExecutors, object :
            IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<FoodData, FoodData> {
            override fun mapToLocal(response: FoodData): FoodData {
                return response
            }
            override fun createNetworkRequest(): Single<Response<FoodData>> {
                return favouriteModel(foodRequestModel)
            }

            override fun getResponseStatus(response: Response<FoodData>): ResponseValidator {
                return  ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }

        })

    }


    fun updateProfileData(userId: String, mobile: String): IRequest<Response<JsonObject>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<JsonObject> {
            override fun createNetworkRequest(): Single<Response<JsonObject>> {
                val map: HashMap<String, RequestBody> = HashMap()
                map["user_id"] = createPartFromString(userId)
                map["mobile"] = createPartFromString(mobile)
                return productApi.updateProfile(map)
            }
            override fun getBodyErrorStatusCode(response: Response<JsonObject>): String {
                return response.body()?.run { this.toString() } ?: ""
            }
        })
    }


    private fun createPartFromString(stringData: String): RequestBody {
        return stringData.toRequestBody(PLAIN_TEXT.toMediaTypeOrNull())
    }


    fun getSingleResturant(signleResturantRequest: SingleResturantRequest): IResource<SpecificFoodResponse> {
        return NetworkResource(appExecutors, object :
            IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<SpecificFoodResponse, SpecificFoodResponse> {
            override fun mapToLocal(response: SpecificFoodResponse): SpecificFoodResponse {
                return response
            }
            override fun createNetworkRequest(): Single<Response<SpecificFoodResponse>> {
                return productApi.getSingleRes(
                    signleResturantRequest.user_id,
                    signleResturantRequest.restaurent_id, signleResturantRequest.latitude,
                    signleResturantRequest.longitute, signleResturantRequest.notificationId,
                    signleResturantRequest.time_zone
                )
            }
            override fun getResponseStatus(response: Response<SpecificFoodResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }


    fun getAllResturantList(getAllResturantRequest: GetAllResturantRequest): IResource<FoodData> {
        return NetworkResource(appExecutors, object :
            IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<FoodData, FoodData> {
            override fun mapToLocal(response: FoodData): FoodData {
                return response
            }
            override fun createNetworkRequest(): Single<Response<FoodData>> {
                return productApi.getAllResturantList(
                    getAllResturantRequest.user_id,
                    getAllResturantRequest.time_zone,
                    getAllResturantRequest.food_category,
                    getAllResturantRequest.showOpenResturantFood,
                    getAllResturantRequest.toTime,
                    getAllResturantRequest.fromTime,
                    getAllResturantRequest.filter_product_type,
                    getAllResturantRequest.currentTime
                )
            }
            override fun getResponseStatus(response: Response<FoodData>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }

        })
    }


    fun resturantLike(userId: String?, favouriteId: String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return productApi.likeResturantApi(favouriteId, userId)
            }
            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
               return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }



    fun locationUpdateMethod(userId: String?, latitude: String?,longitude: String?,appVersion:String?,country:String?,deviceToken:String?): IRequest<Response<LocationResponse>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<LocationResponse> {
            override fun createNetworkRequest(): Single<Response<LocationResponse>> {
                return productApi.locationUpdateApi(userId, latitude,
                    longitude,appVersion,country,deviceToken)
            }
            override fun getResponseStatus(response: Response<LocationResponse>): ResponseValidator {
                return ResponseValidator(response.body()?.status?.code,response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }

    fun getAdsList(userId: String?,country: String?): IResource<AdsResponse> {
        return NetworkResource(appExecutors, object :
            IRetrofitNetworkRequestCallback.IRetrofitNetworkResourceCallback<AdsResponse, AdsResponse> {
            override fun mapToLocal(response: AdsResponse): AdsResponse {
                return response
            }
            override fun createNetworkRequest(): Single<Response<AdsResponse>> {
                return productApi.getAdds(userId,country)
            }
            override fun getResponseStatus(response: Response<AdsResponse>): ResponseValidator {
               return ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }




    fun resturantUnLike(userId: String?, favouriteId: String?): IRequest<Response<CommonResponseModel>> {
        return NetworkRequest(appExecutors, object : IRetrofitNetworkRequestCallback<CommonResponseModel> {
            override fun createNetworkRequest(): Single<Response<CommonResponseModel>> {
                return productApi.deleteResturantApi(favouriteId, userId)
            }
            override fun getResponseStatus(response: Response<CommonResponseModel>): ResponseValidator {
                return  ResponseValidator(response.body()?.status?.code, response.body()?.status?.message)
            }
            override fun sessionExpired() {
                authState.logout()
            }
        })
    }


    fun favouriteModel(foodRequestModel: FoodRequestModel): Single<Response<FoodData>>{
        return productApi.getFavouriteApi(
            foodRequestModel.user_id,
            foodRequestModel.latitude,
            foodRequestModel.longitude,
            foodRequestModel.limit,
            foodRequestModel.offset,
            foodRequestModel.time_zone
        )
    }


    fun productRequestModel(foodRequestModel: FoodRequestModel): Call<FoodData> {
        return productApi.searchApiMethod(
            foodRequestModel.user_id,
            foodRequestModel.latitude,
            foodRequestModel.longitude,
            foodRequestModel.limit,
            foodRequestModel.offset,
            foodRequestModel.keyword,
            foodRequestModel.countryName,
            foodRequestModel.time_zone,
            foodRequestModel.category,
            foodRequestModel.showOpenResturantFood,
            foodRequestModel.toTime,
            foodRequestModel.fromTime,
            foodRequestModel.productFilterType,
            foodRequestModel.current_date_time
        )
    }


}
