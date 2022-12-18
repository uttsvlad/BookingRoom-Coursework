package com.cdr.courseworkdatabase.main.home.client

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.databinding.FragmentHomeClientBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomIconMain
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain

class HomeClientFragment : Fragment(), HasCustomTitleMain, HasCustomIconMain {

    private lateinit var binding: FragmentHomeClientBinding

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
        binding = FragmentHomeClientBinding.inflate(inflater, container, false)

        with(binding) {
            bookRoomButton.setOnClickListener { clickBookRoomButton() }
            clientOrdersButton.setOnClickListener { clickOrdersButton() }
            clientComplaintButton.setOnClickListener { clickComplaintButton() }
        }

        return binding.root
    }

    private fun clickBookRoomButton() = navigatorMain().showAllRoomsScreen()
    private fun clickOrdersButton() = navigatorMain().showAllBidsScreen()
    private fun clickComplaintButton() = navigatorMain().showComplaintScreen()

    override fun getResIcon(): Int = R.drawable.ic_home
    override fun getRestTitle(): Int = R.string.titleHome
}