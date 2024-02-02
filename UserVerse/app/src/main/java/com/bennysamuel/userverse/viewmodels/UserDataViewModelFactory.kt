package com.bennysamuel.userverse.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bennysamuel.whatsmynextcountry.database.UserDatabaseDAO
import java.lang.IllegalArgumentException

class UserDataViewModelFactory (
    private val dataSource: UserDatabaseDAO,
    private val application: Application): ViewModelProvider.Factory{
        @Suppress
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UserDataViewModel::class.java)){
                return UserDataViewModel(dataSource, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel")
        }
}