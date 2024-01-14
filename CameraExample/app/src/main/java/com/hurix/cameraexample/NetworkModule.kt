package com.hurix.cameraexample

import android.content.Context
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.go2future.tuvoowner.data.datasource.cloud.network.service.RestApiImp
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by Payal on 30/3/20.
 */

val networkModule = module {
    single {
        createRetrofit(
            createOkHttpClient()
        )
    }
    single { createNetworkApi(get()) }
    single {
        createRestAPI(
            get(),
            get()
        )
    }
    single {
        createUserRepository(
            get(),
            get()
        )
    }
    single { createUpdateProfileImageUC(get()) }
}


fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .baseUrl(Constant.BASE_URL)
        .client(okHttpClient)
        .build()
}

fun createOkHttpClient(): OkHttpClient {

    val httpClient = OkHttpClient.Builder()

    httpClient.addInterceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Api-Version", "v1")
            .header("Accept", "application/json")
            .build()

        chain.proceed(request)
    }
        .addNetworkInterceptor(StethoInterceptor())
        .retryOnConnectionFailure(true)
        .callTimeout(
            2, TimeUnit.MINUTES
        )
        .connectTimeout(3000, TimeUnit.SECONDS)
        .writeTimeout(4000, TimeUnit.SECONDS)
        .readTimeout(3000, TimeUnit.SECONDS)

    return httpClient.build()
}

fun createNetworkApi(retrofit: Retrofit): NetworkService {
    return retrofit.create(NetworkService::class.java)
}

fun createRestAPI(mNetworkService: NetworkService, mContext: Context): RestApi {
    return RestApiImp(
        mNetworkService,
        mContext
    )
}

fun createUserRepository(mRestApi: RestApi, mapper: UserDataMapper): UserRepository {
    return UserDataRepository(mRestApi, mapper)
}


fun createUpdateProfileImageUC(
    mUserRepository: UserRepository
): UpdateProfileImageUC {
    return UpdateProfileImageUC(mUserRepository)
}
