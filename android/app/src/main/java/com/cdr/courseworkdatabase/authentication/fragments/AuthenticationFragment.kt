package com.cdr.courseworkdatabase.authentication.fragments

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.authentication.contract.HasCustomIconAuthentication
import com.cdr.courseworkdatabase.authentication.contract.HasCustomTitleAuthentication
import com.cdr.courseworkdatabase.authentication.contract.navigator
import com.cdr.courseworkdatabase.checkValid
import com.cdr.courseworkdatabase.createToastCustom
import com.cdr.courseworkdatabase.model.authentication.UserDTO
import com.cdr.courseworkdatabase.databinding.FragmentAuthenticationBinding
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.authentication.AuthenticationResponse
import com.cdr.courseworkdatabase.model.storage.StorageUser
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthenticationFragment : Fragment(), HasCustomTitleAuthentication,
    HasCustomIconAuthentication {

    private lateinit var binding: FragmentAuthenticationBinding
    private var usernameFromRegistration = ""
    private var passwordFromRegistration = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext()) // Анимация
        exitTransition = inflater.inflateTransition(R.transition.fade) // Анимация "выход"
        enterTransition = inflater.inflateTransition(R.transition.fade) // Аниамция "вход"

        usernameFromRegistration = arguments?.getString(ARG_KEY_USERNAME) ?: ""
        passwordFromRegistration = arguments?.getString(ARG_KEY_PASSWORD) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthenticationBinding.inflate(inflater, container, false)

        binding.registrationTextView.setOnClickListener { clickRegistrationButton() } // Регистрация
        binding.signInButton.setOnClickListener { clickSignInButton() } // Войти

        if (arguments != null) {
            binding.usernameEditText.setText(usernameFromRegistration)
            binding.passwordEditText.setText(passwordFromRegistration)
        }

        return binding.root
    }

    private fun clickRegistrationButton() = navigator().showRegistrationScreen()
    private fun clickSignInButton() {
        val isUsername = checkValidDataUsername()
        val isPassword = checkValidDataPassword()

        val isUsernameValid = binding.usernameEditText.text.toString().uppercase()
            .checkValid(binding.usernameEditText)
        val isPasswordValid = binding.passwordEditText.text.toString().uppercase()
            .checkValid(binding.passwordEditText)

        if (isUsername && isPassword && isUsernameValid && isPasswordValid) {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            sendDataToServer(
                UserDTO(
                    username = username, password = password
                )
            )
        }
    }

    // Проверка введенных данных на сервере:
    private fun sendDataToServer(userDTO: UserDTO) {
        // TODO: уничтожить при финальной версии:
//        navigator().gotoMainActivity()
//        saveUser(
//            "Чекунков",
//            "Александр",
//            "Владимирович",
////            "Администратор",
//            "Клиент",
//            "AlexandrChekunkov",
//            "Загранпаспорт",
//            "1815182677",
//            "20 ноября 2022",
//            "unknown"
//        )
//------------------------
//------------------------
//------------------------
        binding.signInButton.isEnabled = false // Запрет на повторное нажатие кнопки "Вход"
        binding.downloadingProgressBar.visibility = View.VISIBLE // Отображение ProgressBar
        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val authenticationAPI = retrofitClient.create(API::class.java)
        val call: Call<AuthenticationResponse?>? = authenticationAPI.checkUser(userDTO)

        call?.enqueue(object : Callback<AuthenticationResponse?> {
            override fun onResponse(
                call: Call<AuthenticationResponse?>, response: Response<AuthenticationResponse?>
            ) {
                binding.signInButton.isEnabled =
                    true // Разрешение на повторное нажатие кнопки "Вход"
                binding.downloadingProgressBar.visibility = View.INVISIBLE // Скрытие ProgressBar
                if (response.isSuccessful) {
                    requireContext().createToastCustom("Вход выполнен!")
                    saveUser( // Сохранение пользователя в память устройства
                        surname = response.body()?.clientDTO?.surname
                            ?: response.body()?.administratorDTO?.surname ?: "Error",
                        firstName = response.body()?.clientDTO?.firstName
                            ?: response.body()?.administratorDTO?.firstName ?: "Error",
                        middleName = response.body()?.clientDTO?.middleName
                            ?: response.body()?.administratorDTO?.middleName ?: "",
                        role = response.body()?.role ?: "Error",
                        username = userDTO.username,
                        documentName = response.body()?.clientDTO?.documentName ?: "-",
                        documentID = response.body()?.clientDTO?.documentNumber ?: "-",
                        dateOfCreation = response.body()?.registrationDate ?: "Unknown",
                        jwtToken = response.body()?.jwtToken ?: ""
                    )
                    navigator().gotoMainActivity() // Переход в основное меню
                } else if (response.code() == 400) requireContext().createToastCustom("Данный пользователь не зарегистрирован!")
                else if (response.code() == 401) requireContext().createToastCustom("Неверный пароль!")
                else requireContext().createToastCustom("Неизвестная ошибка!")
            }

            override fun onFailure(call: Call<AuthenticationResponse?>, t: Throwable) {
                binding.signInButton.isEnabled =
                    true // Разрешение на повторное нажатие кнопки "Вход"
                binding.downloadingProgressBar.visibility = View.INVISIBLE // Скрытие ProgressBar
                requireContext().createToastCustom("Неизвестная ошибка!")
            }
        })
//------------------------
//------------------------
//------------------------
    }

    // -------
    // Проверка корректности всех введенных данных
    // -------

    // Проверка корректности введенного поля "Username":
    private fun checkValidDataUsername(): Boolean =
        if (binding.usernameEditText.text!!.matches(Regex("^[a-zA-Z0-9]+$")) || binding.usernameEditText.text!!.length > 30) true
        else {
            binding.usernameEditText.error = "Недействительный!"
            false
        }

    // Проверка корректности введенного поля "Пароль":
    private fun checkValidDataPassword(): Boolean =
        if (binding.passwordEditText.text!!.matches(Regex("^[a-zA-Z0-9-@%&{}()<>*+-=!?^\$|]+$")) || binding.passwordEditText.text!!.length > 30) true
        else {
            binding.passwordEditText.error = "Недействительный!"
            false
        }

    // -------
    // -------
    // -------

    override fun getResTitle(): Int = R.string.titleAuthentication
    override fun getResIcon(): Int = R.drawable.ic_sign_in

    // Метод сохранения пользователя:
    private fun saveUser(
        surname: String,
        firstName: String,
        middleName: String,
        role: String,
        username: String,
        documentName: String,
        documentID: String,
        dateOfCreation: String,
        jwtToken: String
    ) {
        val user = StorageUser(
            surname,
            firstName,
            middleName,
            role,
            username,
            documentName,
            documentID,
            dateOfCreation,
            jwtToken
        )
        UserStorageWorker().saveUserToStorage(requireContext(), user)
    }

    companion object {
        @JvmStatic
        private val ARG_KEY_USERNAME = "KEY_USERNAME"

        @JvmStatic
        private val ARG_KEY_PASSWORD = "KEY_PASSWORD"

        @JvmStatic
        fun newInstance(username: String, password: String): AuthenticationFragment {
            val bundle = Bundle()
            bundle.putString(ARG_KEY_USERNAME, username)
            bundle.putString(ARG_KEY_PASSWORD, password)

            val fragment = AuthenticationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}