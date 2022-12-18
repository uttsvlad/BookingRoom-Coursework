package com.cdr.courseworkdatabase.authentication.contract

import androidx.annotation.StringRes

interface HasCustomTitleAuthentication {
    @StringRes
    fun getResTitle(): Int
}