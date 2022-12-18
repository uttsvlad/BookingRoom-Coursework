package com.cdr.courseworkdatabase.authentication.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.authentication.contract.HasCustomTitleAuthentication
import com.cdr.courseworkdatabase.authentication.contract.navigator
import com.cdr.courseworkdatabase.checkValid
import com.cdr.courseworkdatabase.createToastCustom
import com.cdr.courseworkdatabase.databinding.FragmentRegistrationBinding
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.authentication.ClientDTO
import com.cdr.courseworkdatabase.model.authentication.RegistrationDTO
import com.cdr.courseworkdatabase.model.authentication.RegistrationResponse
import com.cdr.courseworkdatabase.model.authentication.UserDTO
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegistrationFragment : Fragment(), HasCustomTitleAuthentication {

    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext()) // Анимация
        exitTransition = inflater.inflateTransition(R.transition.fade)// Аниамция "выход"
        enterTransition = inflater.inflateTransition(R.transition.fade) // Аниамция "вход"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        binding.signUpButton.setOnClickListener { clickSignUpButton() }

        return binding.root
    }

    // Обработка нажатия на кнопку "Создать аккаунт":
    private fun clickSignUpButton() {
        val isFirstname = checkValidDataFirstName()
        val isSurname = checkValidDataSurname()
        val isMiddleName = checkValidDataMiddleName()
        val isPassportID = checkValidDataPassportID()
        val isUsername = checkValidDataUsername()
        val isPassword = checkValidDataHardPassword()

        val isFirstnameValid = binding.firstnameSignUpEditText.text.toString().uppercase()
            .checkValid(binding.firstnameSignUpEditText)
        val isSurnameValid = binding.surnameSignUpEditText.text.toString().uppercase()
            .checkValid(binding.surnameSignUpEditText)
        val isMiddleNameValid = binding.middleNameSignUpEditText.text.toString().uppercase()
            .checkValid(binding.middleNameSignUpEditText)
        val isPassportIdValid = binding.passIDSignUpEditText.text.toString().uppercase()
            .checkValid(binding.passIDSignUpEditText)
        val isUsernameValid = binding.usernameSignUpEditText.text.toString().uppercase()
            .checkValid(binding.usernameSignUpEditText)
        val isPasswordValid = binding.passwordSignUpEditText.text.toString().uppercase()
            .checkValid(binding.passwordSignUpEditText)

        if (isFirstname && isSurname && isMiddleName && isPassportID && isUsername && isPassword &&
            isFirstnameValid && isSurnameValid && isMiddleNameValid && isPassportIdValid &&
            isUsernameValid && isPasswordValid
        ) {
            with(binding) {
                val firstname = firstnameSignUpEditText.text.toString()
                val surname = surnameSignUpEditText.text.toString()
                val middleName = middleNameSignUpEditText.text.toString()
                val username = usernameSignUpEditText.text.toString()
                val password = passwordSignUpEditText.text.toString()
                val documentID = passIDSignUpEditText.text.toString()
                val documentName = if (passportRadioButton.isChecked) "ПАСПОРТ" else "ЗАГРАНПАСПОРТ"

                sendDataToServer(
                    RegistrationDTO(
                        userDTO = UserDTO(
                            username = username, password = password
                        ), clientDTO = ClientDTO(
                            surname = surname,
                            firstName = firstname,
                            middleName = middleName,
                            documentName = documentName,
                            documentNumber = documentID,
                        )
                    )
                )
            }
        }
    }

    // Проверка введенных данных на сервере:
    private fun sendDataToServer(registrationDTO: RegistrationDTO) {
        binding.signUpButton.isEnabled =
            false // Запрет на повторное нажатие кнопки "Создать аккаунт"
        binding.downloadingProgressBar.visibility = View.VISIBLE // Отображение ProgressBar
        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val registrationAPI = retrofitClient.create(API::class.java)
        val call: Call<RegistrationResponse?>? = registrationAPI.createUser(registrationDTO)

        call?.enqueue(object : Callback<RegistrationResponse?> {
            override fun onResponse(
                call: Call<RegistrationResponse?>, response: Response<RegistrationResponse?>
            ) {
                binding.signUpButton.isEnabled =
                    true // Разрешение на повторное нажатие кнопки "Создать аккаунт"
                binding.downloadingProgressBar.visibility = View.INVISIBLE // Скрытие ProgressBar
                if (response.isSuccessful) {
                    requireContext().createToastCustom("Аккаунт создан!")
                    navigator().showAuthenticationScreenAfterRegistration(
                        username = registrationDTO.userDTO.username,
                        password = registrationDTO.userDTO.password
                    )
                } else {
                    val gson = GsonBuilder().create()
                    val mError: RegistrationResponse = gson.fromJson(
                        response.errorBody()!!.string(), RegistrationResponse::class.java
                    )
                    if (mError.errors?.contains("username") == true) requireContext().createToastCustom(
                        "Логин занят!"
                    )
                }
            }

            override fun onFailure(call: Call<RegistrationResponse?>, t: Throwable) {
                binding.signUpButton.isEnabled =
                    true // Разрешение на повторное нажатие кнопки "Создать аккаунт"
                binding.downloadingProgressBar.visibility = View.INVISIBLE // Скрытие ProgressBar
                requireContext().createToastCustom("Не известная ошибка!")
            }
        })
    }

    // -------
    // Проверка корректности всех введенных данных
    // -------

    // Проверка корректности введенного поля "Имя":
    private fun checkValidDataFirstName(): Boolean =
        if (binding.firstnameSignUpEditText.text!!.matches(Regex("^[a-zA-Zа-яА-Я-]+$")) || binding.firstnameSignUpEditText.text!!.length > 150) true
        else {
            binding.firstnameSignUpEditText.error = ERROR_MESSAGE
            false
        }

    // Проверка корректности введенного поля "Фамилия":
    private fun checkValidDataSurname(): Boolean =
        if (binding.surnameSignUpEditText.text!!.matches(Regex("^[a-zA-Zа-яА-Я-]+$")) || binding.surnameSignUpEditText.text!!.length > 150) true
        else {
            binding.surnameSignUpEditText.error = ERROR_MESSAGE
            false
        }

    private fun checkValidDataMiddleName(): Boolean =
        if (binding.middleNameSignUpEditText.text!!.matches(Regex("^[a-zA-Zа-яА-Я-]+$")) || binding.firstnameSignUpEditText.text!!.length > 150) true
        else {
            binding.firstnameSignUpEditText.error = ERROR_MESSAGE
            false
        }

    // Проверка корректности введенного поля "Номер документа":
    private fun checkValidDataPassportID(): Boolean =
        if (binding.passIDSignUpEditText.text!!.matches(Regex("^[0-9]+$")) || binding.passIDSignUpEditText.text!!.length > 50) true
        else {
            binding.passIDSignUpEditText.error = ERROR_MESSAGE
            false
        }

    // Проверка корректности введенного поля "Username":
    private fun checkValidDataUsername(): Boolean =
        if (binding.usernameSignUpEditText.text!!.matches(Regex("^[a-zA-Z0-9]+$")) || binding.usernameSignUpEditText.text!!.length > 30) true
        else {
            binding.usernameSignUpEditText.error = ERROR_MESSAGE
            false
        }

    // Проверка корректности введенного поля "Password":
    private fun checkValidDataHardPassword(): Boolean =
        if (binding.passwordSignUpEditText.text!!.matches(Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~\$^+=<>]).{8,28}\$"))) {
            binding.passwordIsSimpleTextView.visibility = View.GONE
            true
        } else {
            binding.passwordSignUpEditText.error = SIMPLE_MESSAGE
            binding.passwordIsSimpleTextView.visibility = View.VISIBLE
            false
        }

    // -------
    // -------
    // -------

    override fun getResTitle(): Int = R.string.titleRegistration

    companion object {
        private const val SIMPLE_MESSAGE = "Недостаточно сложный!"
        private const val ERROR_MESSAGE = "Недействительный!"
    }
}