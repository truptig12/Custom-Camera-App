package com.go2future.tuvoowner.presentation.view.profile

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.hurix.cameraexample.Status
import com.hurix.cameraexample.UpdateProfileImageEntity


class UpdateProfileImageModel(
    status: Status,
    var mUpdateProfileImageEntity: UpdateProfileImageEntity?,
    var error: Throwable?
) {
    @SerializedName("success")
    @Expose
    private var success: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("Token")
    @Expose
    var token: String? = null

    var status: Status? = status


    companion object {

        fun success(response: UpdateProfileImageEntity): UpdateProfileImageModel {
            return UpdateProfileImageModel(
                Status.SUCCESS,
                response,
                null
            )
        }

        fun error(error: Throwable): UpdateProfileImageModel {
            return UpdateProfileImageModel(
                Status.ERROR,
                null,
                error
            )
        }
    }
}