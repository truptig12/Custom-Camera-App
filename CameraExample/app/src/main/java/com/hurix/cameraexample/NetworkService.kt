package com.hurix.cameraexample

import com.hurix.cameraexample.UpdateProfileImageResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

interface NetworkService {


    @Multipart
    @POST("requestBooks")
    fun updateProfileImage(
        @Part("image_text") userId: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Header("Authorization") authHeader: String
    ): Call<UpdateProfileImageResponse>



}