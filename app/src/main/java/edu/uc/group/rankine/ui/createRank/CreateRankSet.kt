package edu.uc.group.rankine.ui.createRank


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R

class CreateRankSet : AppCompatActivity() {

    private lateinit var viewModelCreateRank: CreateRankSetViewModel
    private lateinit var viewModelFactoryCreateRank: CreateRankSetViewModelFactory
    private val imageCode: Int = 204
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_rank_set)
        setSupportActionBar(findViewById(R.id.toolbar)) // set toolbar
        assert(supportActionBar != null) //implement back button
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        viewModelFactoryCreateRank = CreateRankSetViewModelFactory(this)
        viewModelCreateRank = ViewModelProvider(this, viewModelFactoryCreateRank)
            .get(CreateRankSetViewModel::class.java)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageCode) run {
            var imageView: ImageView = findViewById(R.id.set_image)
            imageView.clipToOutline = true
            imageUri = data?.data
            val uriString = imageUri.toString()
            viewModelCreateRank.getImageUriString(uriString)
            imageView.setImageURI(imageUri)
        }
    }

    /**
     * Adds back button in toolbar functionallity
     */
    override fun onSupportNavigateUp(): Boolean { // back btn functionality
        finish()
        return true
    }

    /**
     * calls removeElements function from the view model on button click
     */
    fun onDeleteElements(view: View) {
        viewModelCreateRank.removeElements(view)

    }

    /**
     *  calls removeFields function from the view model on button click
     */
    fun onDeleteFields(view: View) {
        view.id = View.generateViewId()
        var v = view.id
        viewModelCreateRank.removeFields(view, v)
    }

    /**
     *  calls addElements function from the view model on button click
     */
    fun onAddElements(view: View) {
        viewModelCreateRank.addElements()
    }

    /**
     *  calls create function from the view model on button click
     */
    fun onCreate(view: View) {
        viewModelCreateRank.create(view)
        finish()
    }

    /**
     *   calls addFields function from the view model on button click
     */
    fun onAddFields(view: View) {
        view.id = View.generateViewId()
        var v = view.id
        viewModelCreateRank.addFields(v, view)
    }

    /**
     *  calls addImage function from the view model on button click
     */
    fun onImageAdd() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, imageCode)
    }


}