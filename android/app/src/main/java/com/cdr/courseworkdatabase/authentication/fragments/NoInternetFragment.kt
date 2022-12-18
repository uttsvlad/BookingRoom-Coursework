package com.cdr.courseworkdatabase.authentication.fragments

import android.annotation.SuppressLint
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.authentication.contract.HasCustomTitleAuthentication
import com.cdr.courseworkdatabase.authentication.contract.navigator
import com.cdr.courseworkdatabase.createToastCustom
import com.cdr.courseworkdatabase.databinding.FragmentNoInternetBinding

class NoInternetFragment : Fragment(), HasCustomTitleAuthentication {

    private lateinit var binding: FragmentNoInternetBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoInternetBinding.inflate(inflater, container, false)
        binding.tryAgainButton.setOnClickListener { clickUpdateButton() }

        return binding.root
    }

    // Нажатие на кнопку "Обновить":
    private fun clickUpdateButton() {
        if (isConnection()) navigator().showAuthenticationScreen()
        else requireContext().createToastCustom(getString(R.string.textNoInternetConnectionTextView))
    }

    // Проверка подключения к интернету:
    @SuppressLint("NewApi")
    private fun isConnection(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }

    override fun getResTitle(): Int = R.string.titleNoInternet
}