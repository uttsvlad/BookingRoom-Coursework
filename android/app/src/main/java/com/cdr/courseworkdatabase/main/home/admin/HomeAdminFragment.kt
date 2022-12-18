package com.cdr.courseworkdatabase.main.home.admin

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.databinding.FragmentHomeAdminBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomIconMain
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain

class HomeAdminFragment : Fragment(), HasCustomTitleMain, HasCustomIconMain {

    private lateinit var binding: FragmentHomeAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext()) // Анимация
        exitTransition = inflater.inflateTransition(R.transition.fade) // Анимация "выход"
        enterTransition = inflater.inflateTransition(R.transition.slide_left) // Анимация "вход"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeAdminBinding.inflate(inflater, container, false)

        with(binding) {
            allClientsButton.setOnClickListener { clickAllClientsButton() }
            allBidsAdminButton.setOnClickListener { clickAllBidsAdminButton() }
            topServiceButton.setOnClickListener { clickTopServiceButton() }
        }

        return binding.root
    }

    private fun clickAllBidsAdminButton() = navigatorMain().showAllBidsAdminScreen()
    private fun clickAllClientsButton() = navigatorMain().showAllClientsScreen()
    private fun clickTopServiceButton() = navigatorMain().showTopServiceScreen()

    override fun getResIcon(): Int = R.drawable.ic_home
    override fun getRestTitle(): Int = R.string.titleHome
}