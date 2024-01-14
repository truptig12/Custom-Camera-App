package com.hurix.cameraexample


import okhttp3.MultipartBody
import java.io.Serializable


class UpdateProfileImageRequest : Serializable {
    var authHeader: String = ""
    var image_text: String? = null
    var image: MultipartBody.Part? = null

}