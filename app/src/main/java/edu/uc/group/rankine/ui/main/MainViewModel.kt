package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
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
 * Shared ViewModel for all fragments
 */
class MainViewModel(activity: Activity) : ViewModel() {
    private var ctx = activity
    private var dynamicFieldService = DynamicFieldUtil()
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _objectSets: MutableLiveData<ArrayList<ObjectSet>> = MutableLiveData()
    private var _objectSet = ObjectSet()
    var allObjectSets = ArrayList<ObjectSet>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        firebaseDBListener()

    }

    /**
     * Holds an ObjectSet that is used as data for the EditRankFragment and RankSetFragment
     * Holds a images Uri string that is used to populate localUri in the ObjectSet dto
     */
    companion object {
        var getData: ObjectSet? = null
        var getImageUriString = ""

        /**
         * Sets the getData var to an individual ObjectSet
         * This should be used to get the ObjectSet at a certain position in the MainFragment RecyclerView
         */
        fun setData(objectSet: ObjectSet) {
            getData = objectSet
        }

        /**
         * Gets an imageUriString to be sent to the dto from an intent
         */
        fun setImageUriString(imageUri: String) {
            getImageUriString = imageUri
        }
    }


    /**
     *  Dynamically adds the dynamic_elements layout to a specified ViewGroup
     *  @param parent the parent view that will hold the dynamic_elements layout
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
     *  Removes a specified views grandparent form the a specified ViewGroup
     *  @param view the grandchild of the view that is deleted
     *  @param viewGroup the container that holds the grandparent that is deleted
     */
    private fun removeElements(view: View, viewGroup: ViewGroup) {
        val parent = view.parent.parent
        viewGroup.removeView(parent as View)
    }

    /**
     *  Populates the ObjectSet dto with data from CreateRankSetFragment.
     *  Calls the saveDb function to save the dto in the database.
     *  Calls the clearAll function to clear the data in the dto.
     */
    fun create(nameEditText: EditText, scrollContainer: LinearLayout) {
        objectSet = ObjectSet()
        dynamicFieldService.create(nameEditText, scrollContainer)

        with(objectSet) {
            name = dynamicFieldService.name
            elements = dynamicFieldService.elementArray
            localUri = getImageUriString
        }

        saveDb(objectSet)
        //objectSet.getAllElements()
        clearAll()
    }

    /**
     * Populates the ObjectSet dto with data from EditRankSetFragment.
     *  Calls the editDb function to save the dto in the a specific collection in the database.
     *  Calls the clearAll function to clear the data in the dto.
     */
    fun editSet(nameEditText: EditText, scrollContainer: LinearLayout) {
        objectSet = ObjectSet()
        dynamicFieldService.create(nameEditText, scrollContainer)

        with(objectSet) {
            name = dynamicFieldService.name
            elements = dynamicFieldService.elementArray
            localUri = getImageUriString
            id = getData!!.id
        }

        editDb(objectSet)
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

    /**
     * Saves the data from the dto to the firebase
     * @param objectSet specified ObjectSet to be saved
     */
    private fun saveDb(objectSet: ObjectSet) {
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

    /**
     * Edits the data from a specified document
     * @param objectSet specified ObjectSet to be edited
     */
    private fun editDb(objectSet: ObjectSet) {
        val document = firestore.collection("rankData").document(objectSet.id)
        document.set(objectSet)
    }

    /**
     * deletes the specified document from the DB
     * @param id the id of the document to be deleted
     */
    fun removeDbDoc(id: String) {
        val document = firestore.collection("rankData").document(id)
        document.delete()
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

