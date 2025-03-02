package com.android.eatwell.data.models

data class ProfileResponse(
        val body: Body,
        val status: Status
){

    data class Status(
            val code: Int?,
            val message: String,
            val success: Boolean,
            val user_exist: Int
    )


    data class Body(val campaign_portion: Int?,
                    val email: String,
                    val mobile: String,
                    val name: String,
                    val newsletter: Int,
                    val user_image: String
    )
}
