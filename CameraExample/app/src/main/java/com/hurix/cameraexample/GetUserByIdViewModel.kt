package com.hurix.cameraexample

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.go2future.tuvoowner.presentation.view.profile.UpdateProfileImageModel
import io.reactivex.observers.DisposableObserver

class GetUserByIdViewModel(
    var updateProfileImage: UpdateProfileImageUC

) : ViewModel() {

    var mMutableLiveDataImageModel = MutableLiveData<UpdateProfileImageModel>()


    fun updateProfileImageResponse(): LiveData<UpdateProfileImageModel> {
        return mMutableLiveDataImageModel
    }

    fun updateProfileImage(updateProfileImageRequest: UpdateProfileImageRequest) {
        updateProfileImage.execute(object : DisposableObserver<UpdateProfileImageEntity>() {

            override fun onNext(response: UpdateProfileImageEntity) {
                mMutableLiveDataImageModel.value = UpdateProfileImageModel.success(response)
            }


            override fun onComplete() {
                Log.d("TAG--> ", "onComplete")

            }

            override fun onError(error: Throwable) {
                Log.d("TAG--> ", "onError" + error.message)
                mMutableLiveDataImageModel.value = UpdateProfileImageModel.error(error)

            }

        }, updateProfileImageRequest)
    }

}