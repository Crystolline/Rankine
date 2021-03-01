package edu.uc.group.rankine.ui.createRank

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *  A view model factory that allows the context to be sent to the constructor of the CreateRankViewModel
 */
class CreateRankSetViewModelFactory(private val activity: Activity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((CreateRankSetViewModel::class.java))) {

        }
        return CreateRankSetViewModel(activity) as T
    }
}
