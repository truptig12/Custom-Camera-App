package com.hurix.cameraexample


class ExceptionFactory {

    companion object {

        fun create(code: Int, message: String?): Exception {
            when (code) {

                500 -> {
                    return ServerException(message)
                }
                else -> return ServerException(message)

            }

        }

    }
}
