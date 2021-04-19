package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.children
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.utilities.DynamicFieldUtil
import edu.uc.group.rankine.utilities.GetAllViewChildren

/**
 * CreateRankViewModel that takes CreateRanks Context as a constructor
 */
class MainViewModel(activity: Activity) : ViewModel() {
    private var ctx = activity
    private var dynamicFieldService = DynamicFieldUtil(ctx)
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _objectSets: MutableLiveData<ArrayList<ObjectSet>> = MutableLiveData()
    private var _objectSet = ObjectSet()
    var allObjectSets = ArrayList<ObjectSet>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        firebaseDBListener()

    }

    companion object {
        var setData: ObjectSet? = null
        private var imageUriString = ""


        fun getSetData(element: ObjectSet) {
            setData = element
        }


    }


    /**
     *  calls addElements function from DynamicFieldUtil
     */
    fun addElements(parent: ViewGroup) {
        val getAllViewChildren = GetAllViewChildren()
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView: View = inflater.inflate(R.layout.dynamic_elements, null)
        rowView.id = View.generateViewId()
        parent.addView(rowView)
        val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(parent)
        for (child: View in allViews) {
            if (child is ImageButton) {
                child.setOnClickListener {
                    removeElements(it, parent)
                }
            }
        }
    }

    /**
     *  calls removeElements function from DynamicFieldUtil
     */
    fun removeElements(view: View, scrollContainer: ViewGroup) {
        val parent = view.parent.parent
        scrollContainer.removeView(parent as View)
    }

    /**
     *  Populates the ObjectSet dto with data.
     *  Calls the save function to save the dto in the database.
     *  Calls the clearAll function to clear the data in the dto.
     */
    fun create() {
        objectSet = ObjectSet()
        dynamicFieldService.create()

        with(objectSet) {
            name = dynamicFieldService.name
            elements = dynamicFieldService.elementArray
            localUri = imageUriString
        }

        save(objectSet)
        objectSet.getAllElements()
        clearAll()
    }

    /**
     * Clears the data sent to the dto
     */
    private fun clearAll() {
        objectSet.elements = ArrayList<ElementObject>()
        objectSet.localUri = ""
        objectSet.name = ""
        objectSet.element = ArrayList<String>()
    }

    fun getImageUriString(imageUri: String) {
        imageUriString = imageUri
    }

    /**
     * Saves the data from the dto to the firebase
     */
    private fun save(objectSet: ObjectSet) {
        val document = firestore.collection("rankData").document()
        val id = document.id
        objectSet.id = id
        val set = document.set(objectSet)
        set.addOnSuccessListener {
            Log.d("Firebase", "document saved")
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    private fun firebaseDBListener() {
        firestore.collection("rankData").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                allObjectSets = ArrayList<ObjectSet>()
                val documents = snapshot.documents
                documents.forEach {
                    val objectSet = it.toObject(ObjectSet::class.java)
                    if (objectSet != null) {
                        allObjectSets.add(objectSet)
                    }
                }
                _objectSets.value = allObjectSets
            }
        }
    }

    fun removeDbDoc(id: String) {
        val document = firestore.collection("rankData").document(id)
        document.delete()
    }

    fun editSet() {
        TODO("Not yet implemented")
    }

    internal var objectSets: MutableLiveData<ArrayList<ObjectSet>>
        get() {
            return _objectSets
        }
        set(value) {
            _objectSets = value
        }

    internal var objectSet: ObjectSet
        get() {
            return _objectSet
        }
        set(value) {
            _objectSet = value
        }
}

