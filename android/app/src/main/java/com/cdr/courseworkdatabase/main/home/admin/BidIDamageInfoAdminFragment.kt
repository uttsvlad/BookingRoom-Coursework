package com.cdr.courseworkdatabase.main.home.admin

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.checkValid
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentBidAcceptedAdminBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain
import com.cdr.courseworkdatabase.main.home.client.AllBidsAdminObject
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BidIDamageInfoAdminFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentBidAcceptedAdminBinding
    private lateinit var bid: AllBidsAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bid = arguments?.getParcelable(KEY_ARG_BID_INFO)!!
        val inflater = TransitionInflater.from(requireContext()) // Анимация
        exitTransition = inflater.inflateTransition(R.transition.fade) // Анимация "выход"
        enterTransition = inflater.inflateTransition(R.transition.slide_bottom) // Анимация "вход"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBidAcceptedAdminBinding.inflate(inflater, container, false)

        renderUI()
        binding.sendButton.setOnClickListener { clickSendDamageButton() }

        return binding.root
    }

    private fun clickSendDamageButton() {
        if (binding.addMoneyEditText.text.toString().uppercase().checkValid(binding.addMoneyEditText)){
            if (isMoneyDamageEmpty()) binding.addMoneyEditText.error =
                getString(R.string.textComplaintError)
            else sendInfoToServer()
        }
    }

    private fun isMoneyDamageEmpty(): Boolean = binding.addMoneyEditText.text.isBlank()

    private fun sendInfoToServer() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val damageSum = AddDamageToRegistrationRequest(
            registrationId = bid.registrationId,
            damageSum = binding.addMoneyEditText.text.toString().toInt()
        )

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val damageSumAPI = retrofitClient.create(API::class.java)
        val call: Call<ResponseBody> =
            damageSumAPI.sendDamageSum("Bearer ${user.jwtToken}", damageSum)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) requireContext().createToastResponse(true)
                else requireContext().createToastResponse(false)
                navigatorMain().goBack()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                requireContext().createToastResponse(false)
                navigatorMain().goBack()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun renderUI() {
        with(binding) {
            titleInfoRoom.text = "Номер \"${bid.categoryName}\" №${bid.roomId}"
            clientTextView.text = bid.clientFullName
            paymentTextView.text =
                if (bid.paymentId.toInt() == 1) "Наличные (при заселении)" else "Банковская карта"
            startDateTextView.text = bid.checkIn
            finishDateTextView.text = bid.checkOut
            costRoomTextView.text = "${bid.totalPrice} рублей"
        }
    }


    override fun getRestTitle(): Int = R.string.damageTitle

    companion object {
        @JvmStatic
        private val KEY_ARG_BID_INFO = "BID_INFO"

        @JvmStatic
        fun newInstance(newBidInfo: AllBidsAdmin): BidIDamageInfoAdminFragment {
            val bundle = Bundle()
            bundle.putParcelable(KEY_ARG_BID_INFO, newBidInfo)

            val fragment = BidIDamageInfoAdminFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}

data class AddDamageToRegistrationRequest(
    val registrationId: Long,
    val damageSum: Int
)