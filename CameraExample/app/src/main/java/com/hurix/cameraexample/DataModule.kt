package com.hurix.cameraexample

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


/**
 * Created by Payal on 30/3/20.
 */

val PostModule = module {


    viewModel { GetUserByIdViewModel(get()) }

    factory { UserDataMapper() }

    single { Navigator }
}

