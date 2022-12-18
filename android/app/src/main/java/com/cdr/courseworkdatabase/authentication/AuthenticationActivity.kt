package com.cdr.courseworkdatabase.authentication

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.authentication.contract.HasCustomIconAuthentication
import com.cdr.courseworkdatabase.authentication.contract.HasCustomTitleAuthentication
import com.cdr.courseworkdatabase.authentication.contract.NavigatorAuthentication
import com.cdr.courseworkdatabase.authentication.fragments.AuthenticationFragment
import com.cdr.courseworkdatabase.authentication.fragments.NoInternetFragment
import com.cdr.courseworkdatabase.authentication.fragments.RegistrationFragment
import com.cdr.courseworkdatabase.databinding.ActivityAuthenticationBinding
import com.cdr.courseworkdatabase.main.MainActivity

class AuthenticationActivity : AppCompatActivity(), NavigatorAuthentication {

    private lateinit var binding: ActivityAuthenticationBinding // Authentication-Binding
    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!
    private val fragmentLifecycleCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            renderToolbar()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityAuthenticationBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setSupportActionBar(binding.toolbar) // Инициализация Toolbar
        if (savedInstanceState == null) { // Запуск фрагмента
            if (isConnection()) showAuthenticationScreen()
            else showNoInternetScreen()
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    // Отображение фрагмента с "отсутсвием интернета":
    override fun showNoInternetScreen() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, NoInternetFragment()).commit()
    }

    // Отображение фрагмента с аутентификацией:
    override fun showAuthenticationScreen() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, AuthenticationFragment()).commit()
    }

    // Отображение фрагмента с регистрацией:
    override fun showRegistrationScreen() {
        supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.fragmentContainer, RegistrationFragment()).commit()
    }

    // Отображение фрагмента с аутентификацией после регистрации нового аккаунта:
    override fun showAuthenticationScreenAfterRegistration(username: String, password: String) {
        val fragment = AuthenticationFragment.newInstance(username, password)
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    // Переход на основное меню:
    override fun gotoMainActivity() {
        supportFragmentManager.popBackStack()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finishAfterTransition()
    }

    // Отрисовываем объекты в Toolbar:
    private fun renderToolbar() {
        val fragment = currentFragment

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.titleMarginStart = 0
        } else supportActionBar?.setDisplayHomeAsUpEnabled(false)

        if (fragment is HasCustomTitleAuthentication) supportActionBar?.title =
            getString(fragment.getResTitle())
        else supportActionBar?.title = getString(R.string.app_name)

        if (fragment is HasCustomIconAuthentication) {
            supportActionBar?.setIcon(fragment.getResIcon())
            binding.toolbar.titleMarginStart = 75
        } else supportActionBar?.setIcon(ColorDrawable(resources.getColor(R.color.transparent)))
    }

    // Проверка подключения к интернету:
    @SuppressLint("NewApi")
    private fun isConnection(): Boolean {
        val connectivityManager = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
}