package com.hurix.cameraexample

class NetworkUnavailableException : Throwable() {
    override val message: String?
        get() = "Check your network connectivity"
}