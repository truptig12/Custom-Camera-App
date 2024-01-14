package com.go2future.tuvoowner.data.datasource.cloud.network.service

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.hurix.cameraexample.*
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response


/**
 * Created by Payal on 26/3/20.
 */
class RestApiImp constructor(
    var mNetworkService: NetworkService,
    var mContext: Context
) : RestApi {

    val type = object : TypeToken<ErrorResponse>() {}.type
    val gson = Gson()


    override fun updateProfileImage(
        image: MultipartBody.Part?,
        imageText: String?,
        authHeader: String
    ): Observable<Response<UpdateProfileImageResponse>> {
        return Observable.create<Response<UpdateProfileImageResponse>> { emitter ->
            if (!isThereInternetConnection()) {
                emitter.onError(NetworkUnavailableException())
                return@create
            }

            val data: RequestBody = RequestBody.create(
                "multipart/form-data".toMediaTypeOrNull(),
                imageText!!
            )

            val sessionEntity: Response<UpdateProfileImageResponse> =
                mNetworkService.updateProfileImage(data, image, authHeader).execute()

            if (sessionEntity.isSuccessful) {
                if (sessionEntity.body() != null) {
                    emitter.onNext(sessionEntity)

                    emitter.onComplete()
                } else {
                    emitter.onError(UnknownError())
                }
            } else {
                val error = sessionEntity.errorBody()
                Log.e("Error", "" + error)
                val errorResponse: ErrorResponse? =
                    gson.fromJson(sessionEntity.errorBody()!!.charStream(), type)
                emitter.onError(
                    ExceptionFactory.create(
                        sessionEntity.code(),
                        errorResponse?.message
                    )
                )
            }

        }
    }

    @SuppressLint("MissingPermission")
    fun isThereInternetConnection(): Boolean {
        val cm =
            this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        return cm!!.activeNetworkInfo != null
    }

}