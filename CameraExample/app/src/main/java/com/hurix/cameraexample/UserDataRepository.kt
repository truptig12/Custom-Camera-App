package com.hurix.cameraexample

import io.reactivex.Observable
import okhttp3.MultipartBody


/**
 * Created by Payal on 26/3/20.
 */
class UserDataRepository constructor(var mRestApi: RestApi, var mUserDataMapper: UserDataMapper) :
    UserRepository {

    override fun updateProfileImage(
        image: MultipartBody.Part?,
        imageText: String?,
        authHeader: String
    ): Observable<UpdateProfileImageEntity> {
        return mRestApi.updateProfileImage(
            image, imageText, authHeader
        ).map(mUserDataMapper::mapUploadProfileImage)
    }

}
