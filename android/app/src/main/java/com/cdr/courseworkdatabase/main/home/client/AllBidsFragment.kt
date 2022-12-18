package com.cdr.courseworkdatabase.main.home.client

import android.os.Bundle
import android.os.Parcelable
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentAllBidsBinding
import com.cdr.courseworkdatabase.main.adapter.AllBidsAction
import com.cdr.courseworkdatabase.main.adapter.AllBidsAdapter
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import kotlinx.android.parcel.Parcelize
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class AllBitsFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentAllBidsBinding
    private var bids = mutableListOf<AllBidsAdminObject>()

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
        binding = FragmentAllBidsBinding.inflate(inflater, container, false)

        getAllBids()

        return binding.root
    }

    // Метод получения всех заявок клиента:
    private fun getAllBids() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val allBidsAPI = retrofitClient.create(API::class.java)
        val call: Call<List<AllBidsAdminObject>> =
            allBidsAPI.getAllBids("Bearer ${user.jwtToken}", user.username)

        call.enqueue(object : Callback<List<AllBidsAdminObject>> {
            override fun onResponse(
                call: Call<List<AllBidsAdminObject>>,
                response: Response<List<AllBidsAdminObject>>
            ) {
                if (response.isSuccessful) {
                    binding.downloadingProgressBar.visibility = View.GONE
                    binding.downloadingTextView.visibility = View.GONE
                    requireContext().createToastResponse(true)
                    bids = response.body() as MutableList<AllBidsAdminObject>
                    renderUI()
                } else requireContext().createToastResponse(false)
            }

            override fun onFailure(call: Call<List<AllBidsAdminObject>>, t: Throwable) {
                requireContext().createToastResponse(false)
            }
        })
    }

    // Отрисовка списка со всеми комнатами:
    private fun renderUI() {
        binding.listView.visibility = View.VISIBLE

        val adapter = AllBidsAdapter(object : AllBidsAction {
            override fun showBidInfo(bid: AllBidsAdminObject) =
                navigatorMain().showTreatmentBidScreen(bid)
        })
        adapter.data = bids
        binding.listView.adapter = adapter
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun getRestTitle(): Int = R.string.titleAllBids
}

@Parcelize
data class AllBidsAdminObject(
    var registrationId: Long,
    var roomId: Long,
    var categoryName: String,
    var checkIn: String,
    var checkOut: String,
    var isAccepted: Boolean?,
    val comment: String?,
    val paymentId: Long,
    val servicesNames: List<String>?,
    val totalPrice: Double
) : Parcelable