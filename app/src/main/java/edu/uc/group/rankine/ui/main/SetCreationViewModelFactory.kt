package edu.uc.group.rankine.ui.main

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SetCreationViewModelFactory(private val activity: Activity): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom((SetCreationViewModel::class.java))) {

        }
        return SetCreationViewModel(activity) as T
    }
}
