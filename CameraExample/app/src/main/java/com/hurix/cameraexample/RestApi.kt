package com.hurix.cameraexample

import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.Response


interface RestApi {

    fun updateProfileImage(image: MultipartBody.Part?, imageText: String?, authHeader: String): Observable<Response<UpdateProfileImageResponse>>


   
}