package com.cdr.courseworkdatabase.main.home.superAdmin

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.databinding.FragmentHomeSuperadminBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomIconMain
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain

class HomeSuperAdminFragment : Fragment(), HasCustomTitleMain, HasCustomIconMain {

    private lateinit var binding: FragmentHomeSuperadminBinding

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
        binding = FragmentHomeSuperadminBinding.inflate(inflater, container, false)

        binding.addAdminButton.setOnClickListener { clickAddAdminButton() }
        binding.downloadStatisticCSVButton.setOnClickListener { clickCreateStatisticButton() }

        return binding.root
    }

    private fun clickAddAdminButton() = navigatorMain().showAddAdminScreen()
    private fun clickCreateStatisticButton() = navigatorMain().showStatisticScreen()

    override fun getResIcon(): Int = R.drawable.ic_home
    override fun getRestTitle(): Int = R.string.titleHome
}