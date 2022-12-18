package com.cdr.courseworkdatabase.main.home.superAdmin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.checkValid
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentAddAdminBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.authentication.AdministratorDTO
import com.cdr.courseworkdatabase.model.authentication.UserDTO
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddAdminFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentAddAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAdminBinding.inflate(inflater, container, false)

        binding.createAdminButton.setOnClickListener { clickCreateAdminButton() }

        return binding.root
    }

    // Обработка нажатия на кнопку "Создать администратора":
    private fun clickCreateAdminButton() {
        val isFirstname = checkValidDataFirstName()
        val isSurname = checkValidDataSurname()
        val isUsername = checkValidDataUsername()
        val isPassword = checkValidDataHardPassword()

        val isFirstnameValid = binding.firstnameAdminEditText.text.toString().uppercase()
            .checkValid(binding.firstnameAdminEditText)
        val isSurnameValid = binding.surnameAdminEditText.text.toString().uppercase()
            .checkValid(binding.surnameAdminEditText)
        val isMiddleNameValid = binding.middleNameAdminEditText.text.toString().uppercase()
            .checkValid(binding.middleNameAdminEditText)
        val isUsernameValid = binding.usernameAdminEditText.text.toString().uppercase()
            .checkValid(binding.usernameAdminEditText)
        val isPasswordValid = binding.passwordAdminEditText.text.toString().uppercase()
            .checkValid(binding.passwordAdminEditText)

        if (isFirstname && isSurname && isUsername && isPassword &&
            isFirstnameValid && isSurnameValid && isMiddleNameValid && isUsernameValid && isPasswordValid
        ) {
            with(binding) {
                val firstname = firstnameAdminEditText.text.toString()
                val surname = surnameAdminEditText.text.toString()
                val middleName = middleNameAdminEditText.text.toString()
                val username = usernameAdminEditText.text.toString()
                val password = passwordAdminEditText.text.toString()
                sendDataToServer(
                    NewAdministratorDTO(
                        administratorDTO = AdministratorDTO(
                            firstName = firstname,
                            surname = surname,
                            middleName = middleName
                        ),
                        userDTO = UserDTO(
                            username = username,
                            password = password
                        )
                    )
                )
            }
        }
    }

    // Отправка нового пользователя на сервер:
    private fun sendDataToServer(newAdministrator: NewAdministratorDTO) {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        binding.createAdminButton.isEnabled =
            false // Запрет на повторное нажатие кнопки "Создать аккаунт"
        binding.downloadingProgressBar.visibility = View.VISIBLE // Отображение ProgressBar

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val createAdminAPI = retrofitClient.create(API::class.java)
        val call: Call<ResponseBody> =
            createAdminAPI.createAdministrator("Bearer ${user.jwtToken}", newAdministrator)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) requireContext().createToastResponse(true)
                else requireContext().createToastResponse(false)
                navigatorMain().goBack()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                requireContext().createToastResponse(false)
            }
        })
    }

    // -------
    // Проверка корректности всех введенных данных
    // -------

    // Проверка корректности введенного поля "Имя":
    private fun checkValidDataFirstName(): Boolean =
        if (binding.firstnameAdminEditText.text!!.matches(Regex("^[a-zA-Zа-яА-Я-]+$")) || binding.firstnameAdminEditText.text!!.length > 150) true
        else {
            binding.firstnameAdminEditText.error = getString(R.string.textComplaintError)
            false
        }

    // Проверка корректности введенного поля "Фамилия":
    private fun checkValidDataSurname(): Boolean =
        if (binding.surnameAdminEditText.text!!.matches(Regex("^[a-zA-Zа-яА-Я-]+$")) || binding.surnameAdminEditText.text!!.length > 150) true
        else {
            binding.surnameAdminEditText.error = getString(R.string.textComplaintError)
            false
        }

    // Проверка корректности введенного поля "Username":
    private fun checkValidDataUsername(): Boolean =
        if (binding.usernameAdminEditText.text!!.matches(Regex("^[a-zA-Z0-9]+$")) || binding.usernameAdminEditText.text!!.length > 30) true
        else {
            binding.usernameAdminEditText.error = getString(R.string.textComplaintError)
            false
        }

    // Проверка корректности введенного поля "Password":
    private fun checkValidDataHardPassword(): Boolean =
        if (binding.passwordAdminEditText.text!!.matches(Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~\$^+=<>]).{8,28}\$"))) {
            binding.passwordIsSimpleTextView.visibility = View.GONE
            true
        } else {
            binding.passwordAdminEditText.error = getString(R.string.textComplaintError)
            binding.passwordIsSimpleTextView.visibility = View.VISIBLE
            false
        }

    // -------
    // -------
    // -------

    override fun getRestTitle(): Int = R.string.textAddAdminTitle
}

data class NewAdministratorDTO(
    val administratorDTO: AdministratorDTO,
    val userDTO: UserDTO
)