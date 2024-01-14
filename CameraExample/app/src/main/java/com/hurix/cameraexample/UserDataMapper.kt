package com.hurix.cameraexample

import retrofit2.Response


class UserDataMapper {

    fun mapUploadProfileImage(response: Response<UpdateProfileImageResponse>): UpdateProfileImageEntity {

        val mUpdateProfileResponse: UpdateProfileImageResponse? = response.body()

        val mUpdateProfileEntity = UpdateProfileImageEntity()
        mUpdateProfileEntity.message = mUpdateProfileResponse?.message
        mUpdateProfileEntity.code = mUpdateProfileResponse?.code

        return mUpdateProfileEntity
    }

}