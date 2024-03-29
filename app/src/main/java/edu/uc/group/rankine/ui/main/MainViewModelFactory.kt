package edu.uc.group.rankine.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 *  A view model factory that allows the context to be sent to the constructor of the CreateRankViewModel
 */
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom((MainViewModel::class.java)) -> {

            }
        }
        return MainViewModel() as T
    }
}
