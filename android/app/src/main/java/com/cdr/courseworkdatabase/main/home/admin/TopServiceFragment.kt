package com.cdr.courseworkdatabase.main.home.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentTopServiceBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TopServiceFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentTopServiceBinding
    private lateinit var topService: TopService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopServiceBinding.inflate(inflater, container, false)

        binding.okButton.setOnClickListener { navigatorMain().goBack() }
        getTopService()

        return binding.root
    }

    private fun getTopService() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val topServiceAPI = retrofitClient.create(API::class.java)
        val call: Call<TopService> = topServiceAPI.getTopService("Bearer ${user.jwtToken}")

        call.enqueue(object : Callback<TopService> {
            override fun onResponse(call: Call<TopService>, response: Response<TopService>) {
                if (response.isSuccessful) {
                    topService = response.body() as TopService
                    requireContext().createToastResponse(true)
                    renderUI()
                } else requireContext().createToastResponse(false)
            }

            override fun onFailure(call: Call<TopService>, t: Throwable) {
                requireContext().createToastResponse(false)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun renderUI() {
        with(binding) {
            nameTopService.text = topService.serviceName
            costTopService.text = "${topService.servicePrice} рублей"
            countTopService.text = "${topService.count} раз"
        }
    }

    override fun getRestTitle(): Int = R.string.titleTopService
}

data class TopService(
    val serviceName: String,
    val servicePrice: Double,
    val count: Long
)