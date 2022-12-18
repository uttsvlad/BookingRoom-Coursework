package com.cdr.courseworkdatabase.main.profile

import android.content.DialogInterface
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.databinding.FragmentProfileBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomIconMain
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain
import com.cdr.courseworkdatabase.model.storage.StorageUser
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker

class ProfileFragment : Fragment(), HasCustomTitleMain, HasCustomIconMain {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var user: StorageUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val inflater = TransitionInflater.from(requireContext()) // Анимация
        exitTransition = inflater.inflateTransition(R.transition.fade) // Анимация "выход"
        enterTransition = inflater.inflateTransition(R.transition.slide_right) // Анимация "вход"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        renderUI() // Отрисовка всех объектов

        binding.signOutButton.setOnClickListener { clickSignOutButton() }

        return binding.root
    }

    // Нажатие на кнопку "Выйти":
    private fun clickSignOutButton() {
        val listener = DialogInterface.OnClickListener { _, clickedButton ->
            when (clickedButton) {
                AlertDialog.BUTTON_POSITIVE -> navigatorMain().signOut()
            }
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_sign_out)
            .setTitle(getString(R.string.textTitleSignOutAlertDialog))
            .setMessage(getString(R.string.textMessageSignOutAlertDialog))
            .setPositiveButton(getString(R.string.yes), listener)
            .setNegativeButton(getString(R.string.no), listener)

        dialog.show()
    }

    // Отрисовка всех данных о пользователе:
    private fun renderUI() {
        with(binding) {
            surnameTextView.text = user.surname
            firstnameTextView.text = user.firstName
            middleNameTextView.text = user.middleName
            roleTextView.text = user.role
            usernameTextView.text = user.username
            documentTextView.text = user.documentName
            documentIDTextView.text = user.documentID
            dateOfCreationTextView.text = user.dateOfCreation
        }
    }

    override fun getResIcon(): Int = R.drawable.ic_profile
    override fun getRestTitle(): Int = R.string.titleProfile
}