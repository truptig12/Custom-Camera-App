package com.hurix.cameraexample

import android.app.Application
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


/**
 * Created by Payal on 27/3/20.
 */

private const val APPLICATION_ID = "86562"
private const val AUTH_KEY = "F-zHLUYmAUYY5ee"
private const val AUTH_SECRET = "MytdTF4H8rrtHhS"
private const val ACCOUNT_KEY = "nWHSnuqk9bowC4yxL7gu"

const val USER_DEFAULT_PASSWORD = "quickblox"
const val CHAT_PORT = 5223
const val SOCKET_TIMEOUT = 300

//Chat credentials range
private const val MAX_PORT_VALUE = 65535
private const val MIN_PORT_VALUE = 1000
private const val MIN_SOCKET_TIMEOUT = 300
private const val MAX_SOCKET_TIMEOUT = 60000

const val KEEP_ALIVE: Boolean = true
const val USE_TLS: Boolean = true
const val AUTO_JOIN: Boolean = false
const val AUTO_MARK_DELIVERED: Boolean = true
const val RECONNECTION_ALLOWED: Boolean = true
const val ALLOW_LISTEN_NETWORK: Boolean = true

class UserApplication : Application() {

    companion object {
        private lateinit var instance: UserApplication
        fun getInstance(): UserApplication = instance
    }

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        instance = this
        startKoin {
            androidLogger()
            androidContext(this@UserApplication)
            modules(
                listOf(
                    PostModule,
                    networkModule
                )
            )
        }

    }


}