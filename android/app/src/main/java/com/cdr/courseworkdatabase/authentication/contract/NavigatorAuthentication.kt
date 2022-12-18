package com.cdr.courseworkdatabase.authentication.contract

import androidx.fragment.app.Fragment

fun Fragment.navigator(): NavigatorAuthentication = requireActivity() as NavigatorAuthentication

interface NavigatorAuthentication {
    fun showAuthenticationScreen()
    fun showRegistrationScreen()
    fun showAuthenticationScreenAfterRegistration(username: String, password: String)
    fun showNoInternetScreen()
    fun gotoMainActivity()
}