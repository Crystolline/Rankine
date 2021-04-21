package edu.uc.group.rankine.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.dto.RankedObjectSet

/**
 * Shared ViewModel for all fragments
 */
@Suppress("NAME_SHADOWING")
class MainViewModel : ViewModel() {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var _objectSets: MutableLiveData<ArrayList<ObjectSet>> = MutableLiveData()
    private var _objectSet = ObjectSet()
    private var _rankSets: MutableLiveData<ArrayList<RankedObjectSet>> = MutableLiveData()
    private var _rankSet: RankedObjectSet = RankedObjectSet()
    private var allObjectSets = ArrayList<ObjectSet>()
    private var allRankedObjectSets = ArrayList<RankedObjectSet>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        firebaseDBListenerSets()
        firebaseDBListenerRanks()
    }

    companion object {
        var imageUriString = ""
    }

    /**
     *  Populates the [objectSet] dto with data.
     *  Calls the save function to save the dto in the database.
     *  Calls the clearAll function to clear the data in the dto.
     */
    fun saveSet(setName: String) {
        with(objectSet) {
            this.name = setName
            if (imageUriString != "") localUri = imageUriString
        }

        val document = if (objectSet.id.isNotBlank()) {
            //update existing
            firestore.collection("rankData").document(objectSet.id)
        } else {
            //create new
            firestore.collection("rankData").document()
        }
        saveSetToFirebase(document, objectSet)
        objectSet = ObjectSet()
        imageUriString = ""
    }

    fun getImageUriString(imageUri: String) {
        imageUriString = imageUri
    }

    /**
     * Saves a [RankedObjectSet] to Firestore
     * @param rankedSet The [RankedObjectSet] to be saved
     */
    fun saveRankToFirebase(rankedSet: RankedObjectSet) {
        val document = if (rankedSet.id.isNotBlank()) {
            firestore.collection("rankedData").document(rankedSet.id)
        } else {
            firestore.collection("rankedData").document()
        }
        rankedSet.id = document.id
        val set = document.set(rankedSet)
        set.addOnSuccessListener {
            //Save rankedSet.set
            saveSetToFirebase(
                document.collection("set").document("set"), rankedSet.set
            )
            //Save rankedSet.rankedElements
            saveNullableArrayListOfElementsToFirebase(
                document.collection("rankedElements"),
                rankedSet.rankedElements
            )
            //Save rankedSet.fullMergeList
            saveArrayListOfArrayListOfElementsToFirebase(
                document.collection("fullMergeList"),
                rankedSet.fullMergeList
            )
            //Save rankedSet.arrayLeft
            saveArrayListOfElementsToFirebase(
                document.collection("arrayLeft"),
                rankedSet.arrayLeft
            )
            //Save rankedSet.leftElement
            saveElement(
                rankedSet.leftElement,
                document.collection("NextElements").document("leftElement")
            )
            //Save rankedSet.arrayRight
            saveArrayListOfElementsToFirebase(
                document.collection("arrayRight"),
                rankedSet.arrayRight
            )
            //Save rankedSet.rightElement
            saveElement(
                rankedSet.rightElement,
                document.collection("NextElements").document("rightElement")
            )
            //Save rankedSet.arrayMerge
            saveArrayListOfElementsToFirebase(
                document.collection("arrayMerge"),
                rankedSet.arrayMerge
            )
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    /**
     * Saves an [ObjectSet] to Firestore
     * @param document The location to save the [ObjectSet]
     * @param objectSet The [ObjectSet] to be saved
     */
    private fun saveSetToFirebase(document: DocumentReference, objectSet: ObjectSet) {
        objectSet.id = document.id
        val set = document.set(objectSet)
        set.addOnSuccessListener {
            saveArrayListOfElementsToFirebase(document.collection("elements"), objectSet.elements)
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    /**
     * Saves an ArrayList of [ElementObject]s to Firestore
     * @param collection The location to save the ArrayList
     * @param elements The ArrayList of [ElementObject]s to be saved
     */
    private fun saveArrayListOfElementsToFirebase(
        collection: CollectionReference,
        elements: ArrayList<ElementObject>
    ) {
        for (i in 0 until elements.size) {
            val id = "00000000000000000000".dropLast(i.toString().length) + i.toString()
            saveElement(elements[i], collection.document(id))
        }
        collection.get()
            .addOnSuccessListener { doc ->
                for (document in doc)
                    if (document.reference.id.toInt() >= elements.size) document.reference.delete()
            }
    }

    /**
     * Saves an [ElementObject] to Firestore
     * @param elementObject The [ElementObject] to be saved
     * @param elementDocument The location to save the [ElementObject]
     */
    private fun saveElement(elementObject: ElementObject, elementDocument: DocumentReference) {
        elementObject.id = elementDocument.id
        val set = elementDocument.set(elementObject)
        set.addOnSuccessListener {
            Log.d("Firebase", "Element saved $elementObject")
        }
        set.addOnFailureListener {
            Log.d("Firebase", "Save Failed $it")
        }
    }

    /**
     * Saves a nullable ArrayList of [ElementObject]s to Firestore
     * @param collection The location to save the ArrayList
     * @param list The nullable ArrayList of [ElementObject]s to be saved
     */
    private fun saveNullableArrayListOfElementsToFirebase(
        collection: CollectionReference,
        list: ArrayList<ElementObject>?
    ) {
        if (list != null) {
            saveArrayListOfElementsToFirebase(collection, list)
        }
    }

    /**
     * Saves an ArrayList of ArrayLists of [ElementObject]s to Firestore
     * @param collection The location to save the nested ArrayList
     * @param list The ArrayList of ArrayLists of [ElementObject]s to be saved
     */
    private fun saveArrayListOfArrayListOfElementsToFirebase(
        collection: CollectionReference,
        list: ArrayList<ArrayList<ElementObject>>
    ) {
        collection.get()
            .addOnSuccessListener { doc ->
                for (document in doc)
                    removeArrayListFromFirebase(document.reference)
            }
        for (i in 0 until list.size) {
            val id = "00000000000000000000".dropLast(i.toString().length) + i.toString()
            saveArrayListOfElementsToFirebase(
                collection.document(id).collection("elements"),
                list[i]
            )
        }
    }

    /**
     * Removes an ArrayList of [ElementObject]s from Firestore
     * @param document The location where the ArrayList to be removed is located
     */
    private fun removeArrayListFromFirebase(document: DocumentReference) {
        document.collection("elements").get()
            .addOnSuccessListener { doc ->
                for (document in doc)
                    document.reference.delete()
            }
        document.delete()
    }

    /**
     * deletes the specified document from the DB
     * @param id the id of the document to be deleted
     */
    fun removeDbDoc(id: String) {
        val document = firestore.collection("rankData").document(id)
        document.delete()
    }

    /**
     * deletes the specified document from the DB
     * @param id the id of the document to be deleted
     */
    fun removeRankedDbDoc(id: String) {
        val document = firestore.collection("rankedData").document(id)
        document.delete()
    }

    /**
     * Fetches all [ObjectSet]s from Firestore and stores them in [_objectSets]
     */
    private fun firebaseDBListenerSets() {
        firestore.collection("rankData").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                allObjectSets = ArrayList()
                val documents = snapshot.documents
                documents.forEach {
                    val objectSet = it.toObject(ObjectSet::class.java)

                    if (objectSet != null) {
                        val elementsCollection = firestore.collection("rankData")
                            .document(objectSet.id)
                            .collection("elements")
                        elementsCollection.addSnapshotListener { querySnapshot, _ ->
                            if (querySnapshot != null) {
                                val innerElements =
                                    querySnapshot.toObjects(ElementObject::class.java)
                                objectSet.elements = innerElements as ArrayList<ElementObject>
                            }
                        }
                        allObjectSets.add(objectSet)
                    }
                }
                _objectSets.value = allObjectSets
            }
        }
    }

    /**
     * Fetches all [RankedObjectSet]s from Firestore and stores them in [_rankSets]
     */
    private fun firebaseDBListenerRanks() {
        firestore.collection("rankedData").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                allRankedObjectSets = ArrayList()
                val documents = snapshot.documents
                documents.forEach {
                    val rankedSet = it.toObject(RankedObjectSet::class.java)

                    if (rankedSet != null) {
                        //Retrieve set
                        fetchSetFromFirebase(
                            it.reference.collection("set").document("set"),
                            rankedSet.set
                        )
                        //Retrieve rankedElements
                        fetchArrayListOfElementsFromFirebase(
                            it.reference.collection("rankedElements"),
                            rankedSet.rankedElements
                        )
                        //Retrieve fullMergeList
                        fetchArrayListOfArrayListOfElementsFromFirebase(
                            it.reference.collection("fullMergeList"),
                            rankedSet.fullMergeList
                        )
                        //Retrieve arrayLeft
                        fetchArrayListOfElementsFromFirebase(
                            it.reference.collection("arrayLeft"),
                            rankedSet.arrayLeft
                        )
                        //Retrieve leftElement
                        fetchElement(
                            it.reference.collection("NextElements").document("leftElement"),
                            rankedSet.leftElement
                        )
                        //Retrieve arrayRight
                        fetchArrayListOfElementsFromFirebase(
                            it.reference.collection("arrayRight"),
                            rankedSet.arrayRight
                        )
                        //Retrieve rightElement
                        fetchElement(
                            it.reference.collection("NextElements").document("rightElement"),
                            rankedSet.rightElement
                        )
                        //Retrieve arrayMerge
                        fetchArrayListOfElementsFromFirebase(
                            it.reference.collection("arrayMerge"),
                            rankedSet.arrayMerge
                        )
                        allRankedObjectSets.add(rankedSet)
                    }
                }
                _rankSets.value = allRankedObjectSets
            }
        }
    }

    /**
     * Fetches an [ObjectSet] from Firestore
     * @param document The location the [ObjectSet] to fetch is located
     * @param objectSet Where to store the fetched [ObjectSet]
     */
    private fun fetchSetFromFirebase(
        document: DocumentReference,
        objectSet: ObjectSet
    ) {
        document.get().addOnSuccessListener {
            val localObjectSet = it.toObject(ObjectSet::class.java)
            if (localObjectSet != null) {
                objectSet.id = localObjectSet.id
                objectSet.name = localObjectSet.name
                objectSet.localUri = localObjectSet.localUri
                fetchArrayListOfElementsFromFirebase(
                    document.collection("elements"),
                    objectSet.elements
                )
            }
        }
    }

    /**
     * Fetches an ArrayList of ArrayLists of [ElementObject]s from Firestore
     * @param collection The location the nested Arraylist to fetch is located
     * @param list Where to store the fetched nested ArrayList
     */
    private fun fetchArrayListOfArrayListOfElementsFromFirebase(
        collection: CollectionReference,
        list: ArrayList<ArrayList<ElementObject>>
    ) {
        collection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            snapshot?.documents?.forEach {
                val elementList: ArrayList<ElementObject> = ArrayList()
                fetchArrayListOfElementsFromFirebase(
                    it.reference.collection("elements"),
                    elementList
                )
                list.add(elementList)
            }
        }
    }

    /**
     * Fetches an ArrayList of [ElementObject]s from Firestore
     * @param collection The location the ArrayList to fetch is located
     * @param elements Where to store the fetched ArrayList of [ElementObject]s
     */
    private fun fetchArrayListOfElementsFromFirebase(
        collection: CollectionReference,
        elements: ArrayList<ElementObject>
    ) {
        collection.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen Failed", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val allElements =
                    snapshot.toObjects(ElementObject::class.java) as ArrayList<ElementObject>
                allElements.forEach {
                    elements.add(it)
                }
            }
        }
    }

    /**
     * Fetches an [ElementObject] from Firestore
     * @param document The location the [ElementObject] to fetch is located
     * @param elementObject Where to store the fetched [ElementObject]
     */
    private fun fetchElement(document: DocumentReference, elementObject: ElementObject) {
        document.get().addOnSuccessListener {
            val localElement = it.toObject(ElementObject::class.java)
            if (localElement != null) {
                elementObject.element = localElement.element
                elementObject.id = localElement.id
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

    internal var rankSets: MutableLiveData<ArrayList<RankedObjectSet>>
        get() {
            return _rankSets
        }
        set(value) {
            _rankSets = value
        }

    internal var objectSet: ObjectSet
        get() {
            return _objectSet
        }
        set(value) {
            _objectSet = value
        }

    internal var rankSet: RankedObjectSet
        get() {
            return _rankSet
        }
        set(value) {
            _rankSet = value
        }
}


