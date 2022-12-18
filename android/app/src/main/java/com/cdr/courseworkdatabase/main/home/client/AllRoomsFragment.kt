package com.cdr.courseworkdatabase.main.home.client

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.createToastCustom
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentAllRoomsBinding
import com.cdr.courseworkdatabase.main.adapter.AllRoomsAction
import com.cdr.courseworkdatabase.main.adapter.AllRoomsAdapter
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AllRoomsFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentAllRoomsBinding
    private var rooms = mutableListOf<AllRoomsObject>()

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
        binding = FragmentAllRoomsBinding.inflate(inflater, container, false)

        getAllRooms()

        return binding.root
    }

    // Метод получения все комнат:
    private fun getAllRooms() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val allRoomsAPI = retrofitClient.create(API::class.java)
        val call: Call<List<AllRoomsObject>> = allRoomsAPI.getAllRooms("Bearer ${user.jwtToken}")

        call.enqueue(object : Callback<List<AllRoomsObject>> {
            override fun onResponse(
                call: Call<List<AllRoomsObject>>,
                response: Response<List<AllRoomsObject>>
            ) {
                if (response.isSuccessful) {
                    requireContext().createToastResponse(true)
                    binding.downloadingProgressBar.visibility = View.GONE
                    binding.downloadingTextView.visibility = View.GONE
                    rooms = response.body() as MutableList<AllRoomsObject>
                    renderUI(response.body())
                } else requireContext().createToastResponse(false)
            }

            override fun onFailure(call: Call<List<AllRoomsObject>>, t: Throwable) {
                requireContext().createToastCustom("Неизвестная ошибка!")
            }
        })
    }

    // Отрисовка списка со всеми комнатами:
    private fun renderUI(allRooms: List<AllRoomsObject>?) {
        binding.listView.visibility = View.VISIBLE

        val adapter = AllRoomsAdapter(object : AllRoomsAction {
            override fun onRoomInfo(roomId: Long) = navigatorMain().showInfoRoomScreen(roomId)
        })
        adapter.data = allRooms!!
        binding.listView.adapter = adapter
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun getRestTitle(): Int = R.string.titleAllRooms
}

data class AllRoomsObject(
    var id: Long? = null,
    var categoryName: String = "",
    var price: Double = 0.0,
    var capacity: Int = 0,
)