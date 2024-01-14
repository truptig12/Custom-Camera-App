package com.hurix.cameraexample

import io.reactivex.Observable
import okhttp3.MultipartBody


/**
 * Created by Payal on 26/3/20.
 */
interface UserRepository {

    fun updateProfileImage(
        image: MultipartBody.Part?,
        imageText: String?,
        authHeader: String
    ): Observable<UpdateProfileImageEntity>

}