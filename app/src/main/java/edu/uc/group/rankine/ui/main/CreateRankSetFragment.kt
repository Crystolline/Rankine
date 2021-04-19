package edu.uc.group.rankine.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.uc.group.rankine.R
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

        return inflater.inflate(R.layout.create_rank_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        vmFactory = activity?.let { MainViewModelFactory(it) }!!
        vm = ViewModelProvider(this, vmFactory).get(MainViewModel::class.java)
        val addBtn = activity?.findViewById<AppCompatButton>(R.id.create_rank_fragment_addBtn)
        val createBtn = activity?.findViewById<AppCompatButton>(R.id.create_rank_fragment_createBtn)
        val parentView = activity?.findViewById<LinearLayout>(R.id.create_rank_fragment_scrollContainer) as ViewGroup
        val toolbar = view?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.create_rank_fragment_toolbar)
        toolbar?.navigationIcon = ResourcesCompat.getDrawable(resources, R.drawable.ic_back_icon, null)
        toolbar!!.setNavigationOnClickListener { (activity as MainActivity).moveToMain() }

        addBtn?.setOnClickListener {
            vm.addElements(parentView)
        }

        createBtn?.setOnClickListener {
            val dynamicFieldUtil = DynamicFieldUtil(requireActivity())
            val getAllViewChildren = GetAllViewChildren()
            val scrollContainer =
                    requireActivity().findViewById<LinearLayout>(R.id.create_rank_fragment_scrollContainer)
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

