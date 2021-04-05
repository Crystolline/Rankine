package edu.uc.group.rankine.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import edu.uc.group.rankine.R
import kotlinx.android.synthetic.main.main_fragment.*


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
        btnCreateRank.setOnClickListener {
            (activity as MainActivity).moveToCreateRankSet()
        }
    }
}