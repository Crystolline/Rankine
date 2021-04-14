package edu.uc.group.rankine.ui.main

import android.app.Activity
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.uc.group.rankine.R
import edu.uc.group.rankine.dto.ElementObject
import edu.uc.group.rankine.dto.ObjectSet
import edu.uc.group.rankine.utilities.DynamicFieldUtil
import edu.uc.group.rankine.utilities.GetAllViewChildren

open class CreateRankSetFragment : Fragment() {
    companion object {
        fun newInstance() = CreateRankSetFragment()
    }

    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_create_rank_set, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmFactory = activity?.let { MainViewModelFactory(it) }!!
        vm = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        val addBtn = activity?.findViewById<AppCompatButton>(R.id.add_new_field_btn)
        val createBtn = activity?.findViewById<AppCompatButton>(R.id.create_btn)


        addBtn?.setOnClickListener {
            vm.addElements()


        }

        createBtn?.setOnClickListener {
            val dynamicFieldUtil = DynamicFieldUtil(requireActivity())
            val getAllViewChildren = GetAllViewChildren()
            val scrollContainer = requireActivity().findViewById<LinearLayout>(R.id.scroll_Container)
            val allViews: ArrayList<View> = getAllViewChildren.getAllChildren(scrollContainer!!)
            if (dynamicFieldUtil.userFilter(allViews)) {
                vm.create()
                (activity as MainActivity).moveToMain()
            } else {
                Toast.makeText(context, "Fill Out All Fields", Toast.LENGTH_SHORT).show()
            }

        }


    }

}

