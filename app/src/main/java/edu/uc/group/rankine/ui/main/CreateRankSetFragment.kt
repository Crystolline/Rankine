package edu.uc.group.rankine.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R
import edu.uc.group.rankine.utilities.DynamicFieldUtil
import edu.uc.group.rankine.utilities.GetAllViewChildren

class CreateRankSetFragment : Fragment() {
    companion object {
        fun newInstance() = CreateRankSetFragment()
    }

    private lateinit var vm: MainViewModel
    private lateinit var vmFactory: MainViewModelFactory
    private val ma: MainActivity = MainActivity()

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
                ma.moveToMain()
            } else {
                Toast.makeText(activity, "Fill Out All Fields", Toast.LENGTH_SHORT).show()
            }

        }
        

    }
}