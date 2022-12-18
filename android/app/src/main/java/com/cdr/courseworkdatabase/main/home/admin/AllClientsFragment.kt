package com.cdr.courseworkdatabase.main.home.admin

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentAllClientsBinding
import com.cdr.courseworkdatabase.main.adapter.AllClientsAdapter
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.authentication.ClientDTO
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AllClientsFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentAllClientsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext()) // Анимация
        exitTransition = inflater.inflateTransition(R.transition.fade) // Анимация "выход"
        enterTransition = inflater.inflateTransition(R.transition.slide_bottom) // Анимация "вход"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAllClientsBinding.inflate(inflater, container, false)

        getAllClients()

        return binding.root
    }

    private fun getAllClients() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()
        val allClientsAPI = retrofitClient.create(API::class.java)
        val call: Call<List<AllClientObject>> =
            allClientsAPI.getAllClients("Bearer ${user.jwtToken}")

        call.enqueue(object : Callback<List<AllClientObject>> {
            override fun onResponse(
                call: Call<List<AllClientObject>>,
                response: Response<List<AllClientObject>>
            ) {
                if (response.isSuccessful) {
                    requireContext().createToastResponse(true)
                    binding.downloadingProgressBar.visibility = View.GONE
                    binding.downloadingTextView.visibility = View.GONE
                    renderUI(response.body())
                } else requireContext().createToastResponse(false)
            }

            override fun onFailure(call: Call<List<AllClientObject>>, t: Throwable) {
                requireContext().createToastResponse(false)
            }
        })
    }

    private fun renderUI(allClients: List<AllClientObject>?) {
        val adapter = AllClientsAdapter()
        adapter.data = allClients!!
        binding.listView.visibility = View.VISIBLE
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
        binding.listView.adapter = adapter
    }

    override fun getRestTitle(): Int = R.string.titleAllClients
}

data class AllClientObject(
    val clientDTO: ClientDTO,
    val username: String,
    val registrationDate: String
)