package edu.uc.group.rankine.ui.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *  A view model factory that allows the context to be sent to the constructor of the CreateRankViewModel
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val activity: Activity) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((MainViewModel::class.java))) {

        }
        return MainViewModel(activity) as T
    }
}
