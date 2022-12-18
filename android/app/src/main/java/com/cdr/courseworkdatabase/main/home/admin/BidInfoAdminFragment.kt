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
import com.cdr.courseworkdatabase.databinding.FragmentBidAdminBinding
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

class BidInfoAdminFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentBidAdminBinding
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
        binding = FragmentBidAdminBinding.inflate(inflater, container, false)

        renderUI()
        binding.acceptButton.setOnClickListener { clickAcceptButton() }
        binding.notAcceptButton.setOnClickListener { clickNotAcceptButton() }

        return binding.root
    }

    private fun clickAcceptButton() {
        if (binding.commentEditText.text.toString().uppercase().checkValid(binding.commentEditText)){
            if (isCommentEmpty()) binding.commentEditText.error = getString(R.string.textComplaintError)
            else sendInfoToServer(true)
        }
    }

    private fun clickNotAcceptButton() {
        if (binding.commentEditText.text.toString().uppercase().checkValid(binding.commentEditText)) {
            if (isCommentEmpty()) binding.commentEditText.error = getString(R.string.textComplaintError)
            else sendInfoToServer(false)
        }
    }

    private fun isCommentEmpty(): Boolean = binding.commentEditText.text.isBlank()

    private fun sendInfoToServer(isAccept: Boolean) {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val handleBid = AdminBidHandleRequest(
            registrationId = bid.registrationId,
            adminUsername = user.username,
            isAccepted = isAccept,
            comment = binding.commentEditText.text.toString()
        )

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val handleBidsAPI = retrofitClient.create(API::class.java)
        val call: Call<ResponseBody> =
            handleBidsAPI.sendHandleBid("Bearer ${user.jwtToken}", handleBid)

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

    companion object {
        @JvmStatic
        private val KEY_ARG_BID_INFO = "BID_INFO"

        @JvmStatic
        fun newInstance(newBidInfo: AllBidsAdmin): BidInfoAdminFragment {
            val bundle = Bundle()
            bundle.putParcelable(KEY_ARG_BID_INFO, newBidInfo)

            val fragment = BidInfoAdminFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getRestTitle(): Int = R.string.titleBidInfo
}

data class AdminBidHandleRequest(
    val registrationId: Long,
    val adminUsername: String,
    val isAccepted: Boolean,
    val comment: String
)