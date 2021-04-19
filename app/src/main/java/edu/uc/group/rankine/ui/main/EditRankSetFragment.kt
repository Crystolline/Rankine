package edu.uc.group.rankine.ui.main

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R
import edu.uc.group.rankine.utilities.DynamicFieldUtil
import edu.uc.group.rankine.utilities.GetAllViewChildren
import java.io.File

class EditRankSetFragment : Fragment() {
    companion object {
        fun newInstance() = EditRankSetFragment()
    }

    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_rank_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmFactory = activity?.let { MainViewModelFactory(it) }!!
        vm = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        val addBtn = activity?.findViewById<AppCompatButton>(R.id.edit_rank_fragment_addBtn)
        val saveBtn = activity?.findViewById<AppCompatButton>(R.id.edit_rank_fragment_saveBtn)
        val name = activity?.findViewById<EditText>(R.id.edit_rank_fragment_nameEditText)
        val parentView = activity?.findViewById<LinearLayout>(R.id.edit_rank_fragment_scrollView) as ViewGroup
        val image = activity?.findViewById<ImageView>(R.id.edit_rank_fragment_image)
        val getAllViewChildren = GetAllViewChildren()
        var counter = 0
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.edit_rank_fragment_toolbar)
        toolbar?.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_back_icon, null)
        toolbar!!.setNavigationOnClickListener { (activity as MainActivity).moveToMain() }

        //Set Image
        val file = File(MainViewModel.getData!!.localUri)
        image!!.clipToOutline = true
        if (file.exists()) {
            image.setImageURI(Uri.parse(MainViewModel.getData?.localUri))
        } else {
            image.setImageResource(R.drawable.ic_launcher_foreground)
        }

        //set name
        name?.setText(MainViewModel.getData!!.name)

        //add elements
        for (i in 0 until MainViewModel.getData!!.element.size) {
            vm.addElements(parentView)
        }

        //set elements
        val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(parentView)
        for (child: View in allViews) {

            if (child is EditText) {

                child.setText(MainViewModel.getData!!.element[counter])
                counter++
            }
        }

        //click listener for add btn
        addBtn?.setOnClickListener {
            vm.addElements(parentView)

        }

        //click listener for save btn
        saveBtn?.setOnClickListener {
            val dynamicFieldUtil = DynamicFieldUtil()
            if (dynamicFieldUtil.userFilter(allViews, name!!)) {
                vm.editSet(name, parentView as LinearLayout)
                (activity as MainActivity).moveToMain()
            } else {
                Toast.makeText(context, "Fill Out All Fields", Toast.LENGTH_SHORT).show()
            }

        }

    }

}