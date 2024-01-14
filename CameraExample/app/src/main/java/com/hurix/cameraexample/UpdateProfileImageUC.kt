package com.hurix.cameraexample

import io.reactivex.Observable


class UpdateProfileImageUC constructor(val userRepository: UserRepository) :
    UseCase<UpdateProfileImageEntity, UpdateProfileImageRequest>() {

    override fun build(param: UpdateProfileImageRequest): Observable<UpdateProfileImageEntity> {
        return userRepository.updateProfileImage(
            param.image,
            param.image_text,
            param.authHeader
        )
    }
}