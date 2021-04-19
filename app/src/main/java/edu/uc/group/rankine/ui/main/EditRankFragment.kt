package edu.uc.group.rankine.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.utilities.DynamicFieldUtil
import edu.uc.group.rankine.utilities.GetAllViewChildren

class EditRankFragment : Fragment() {
    companion object {
        fun newInstance() = EditRankFragment()
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
        val deleteBtn = activity?.findViewById<ImageButton>(R.id.dynamic_elements_deleteAttribute)
        val name = activity?.findViewById<EditText>(R.id.edit_rank_fragment_nameEditText)
        val parentView = activity?.findViewById<LinearLayout>(R.id.edit_rank_fragment_scrollView) as ViewGroup
        val getAllViewChildren = GetAllViewChildren()
        var counter = 0
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.edit_rank_fragment_toolbar)


        toolbar?.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_back_icon, null)
        toolbar!!.setNavigationOnClickListener { (activity as MainActivity).moveToMain() }

        name?.setText(MainViewModel.setData!!.name)
        for (i in 0 until MainViewModel.setData!!.element.size) {
            vm.addElements(parentView)
        }

        val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(parentView)
        for (child: View in allViews) {
            if (child is EditText) {

                child.setText(MainViewModel.setData!!.element[counter])
                counter++
            }
        }

        addBtn?.setOnClickListener {
            vm.addElements(parentView)

        }

        saveBtn?.setOnClickListener {
            val dynamicFieldUtil = DynamicFieldUtil(requireActivity())
            val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(parentView)
            if (dynamicFieldUtil.userFilter(allViews)) {
                vm.editSet()
                (activity as MainActivity).moveToMain()
            } else {
                Toast.makeText(context, "Fill Out All Fields", Toast.LENGTH_SHORT).show()
            }

        }

    }

}