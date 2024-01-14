package com.hurix.cameraexample

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class ErrorResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null
    @SerializedName("message")
    @Expose
    val message: String? = null
}