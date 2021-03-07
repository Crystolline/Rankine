/**
 * MainViewModel class.
 * This is responsible for connecting the Firebase database to the recyclerview and actively updating the list
 */
package edu.uc.group.rankine.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import edu.uc.group.rankine.dto.ObjectSet


class MainViewModel : ViewModel() {
    var list: MutableLiveData<ArrayList<ObjectSet>> = MutableLiveData<ArrayList<ObjectSet>>()
}
