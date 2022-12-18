package com.cdr.courseworkdatabase.main.home.client

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.checkValid
import com.cdr.courseworkdatabase.createToastCustom
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentComplaintBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class ComplaintFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentComplaintBinding

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
        binding = FragmentComplaintBinding.inflate(inflater, container, false)

        binding.sendComplaintButton.setOnClickListener { clickSendComplaintButton() }

        return binding.root
    }

    // Нажатие на кнопку "Отправить":
    private fun clickSendComplaintButton() {
        if (binding.complaintEditText.text.toString().uppercase().checkValid(binding.complaintEditText)){
            if (binding.complaintEditText.text.isBlank()) binding.complaintEditText.error =
                getString(R.string.textComplaintError)
            else {
                val listener = DialogInterface.OnClickListener { _, clickedButton ->
                    when (clickedButton) {
                        androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE -> sendComplaintToServer()
                    }
                }

                val dialog = AlertDialog.Builder(requireContext())
                    .setIcon(R.drawable.ic_complaint)
                    .setTitle(R.string.textTitleComplaintAlertDialog)
                    .setMessage(R.string.textComplaintAlertDialog)
                    .setPositiveButton(R.string.yes, listener)
                    .setNegativeButton(R.string.no, listener)
                dialog.show()
            }
        }
    }

    // Отправка жалобы на сервер:
    private fun sendComplaintToServer() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя
        val content = binding.complaintEditText.text.toString()

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val complaintAPI = retrofitClient.create(API::class.java)
        val call: Call<ResponseBody> =
            complaintAPI.createComplaint("Bearer ${user.jwtToken}", ComplaintRequest(content))

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) requireContext().createToastCustom("Жалоба отправлена!")
                else requireContext().createToastResponse(false)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                requireContext().createToastCustom("Неизвестная ошибка!")
            }
        })

        navigatorMain().goBack()
    }

    override fun getRestTitle(): Int = R.string.titleComplaint
}

class ComplaintRequest(val content: String)