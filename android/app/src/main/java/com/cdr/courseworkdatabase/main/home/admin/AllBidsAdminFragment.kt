package com.cdr.courseworkdatabase.main.home.admin

import android.os.Bundle
import android.os.Parcelable
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.createToastCustom
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentAllBidsAdminBinding
import com.cdr.courseworkdatabase.main.adapter.AllBidsAdminAdapter
import com.cdr.courseworkdatabase.main.adapter.BidsAdminActionListener
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain
import com.cdr.courseworkdatabase.main.home.client.AllBidsAdminObject
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import kotlinx.android.parcel.Parcelize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AllBidsAdminFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentAllBidsAdminBinding
    private var bids = mutableListOf<AllBidsAdmin>()

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
        binding = FragmentAllBidsAdminBinding.inflate(inflater, container, false)
        getAllBids()

        return binding.root
    }

    private fun getAllBids() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val allBidsAPI = retrofitClient.create(API::class.java)
        val call: Call<List<AllBidsAdmin>> =
            allBidsAPI.getAllBidsAdmin("Bearer ${user.jwtToken}")

        call.enqueue(object : Callback<List<AllBidsAdmin>> {
            override fun onResponse(
                call: Call<List<AllBidsAdmin>>,
                response: Response<List<AllBidsAdmin>>
            ) {
                if (response.isSuccessful) {
                    binding.downloadingProgressBar.visibility = View.GONE
                    binding.downloadingTextView.visibility = View.GONE
                    requireContext().createToastResponse(true)
                    bids = response.body() as MutableList<AllBidsAdmin>
                    renderUI()
                } else requireContext().createToastResponse(false)
            }

            override fun onFailure(call: Call<List<AllBidsAdmin>>, t: Throwable) {
                requireContext().createToastResponse(false)
            }
        })
    }

    private fun renderUI() {
        val adapter = AllBidsAdminAdapter(object : BidsAdminActionListener {
            override fun showBidInfoAdminScreen(bid: AllBidsAdmin) =
                navigatorMain().showBidInfoAdminScreen(bid)

            override fun showBidDamageInfoAdminScreen(bid: AllBidsAdmin) =
                navigatorMain().showBidDamageInfoAdminScreen(bid)

            override fun showMessage() =
                requireContext().createToastCustom("Заявка уже была обработана. Отказ!")

        })
        adapter.data = bids
        binding.listView.visibility = View.VISIBLE
        binding.listView.adapter = adapter
        binding.listView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun getRestTitle(): Int = R.string.titleAllBidsAdmin
}

@Parcelize
data class AllBidsAdmin(
    val registrationId: Long,
    val roomId: Long,
    val categoryName: String,
    val checkIn: String,
    val checkOut: String,
    val isAccepted: Boolean?,
    val paymentId: Long,
    val servicesNames: List<String>?,
    val totalPrice: Double,
    val clientFullName: String
) : Parcelable