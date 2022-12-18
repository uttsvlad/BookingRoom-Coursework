package com.cdr.courseworkdatabase.authentication.contract

import androidx.annotation.DrawableRes

interface HasCustomIconAuthentication {
    @DrawableRes
    fun getResIcon(): Int
}