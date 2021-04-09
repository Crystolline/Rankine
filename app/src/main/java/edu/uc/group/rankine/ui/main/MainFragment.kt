package edu.uc.group.rankine.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.uc.group.rankine.R

class MainFragment : Fragment() {
    internal lateinit var viewModel: MainViewModel

    companion object {
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val btnOpenActivity: FloatingActionButton? = activity?.findViewById(R.id.btnCreateRank)
        btnOpenActivity?.setOnClickListener {
            (activity as MainActivity).moveToCreateRankSet()
        }
    }
}