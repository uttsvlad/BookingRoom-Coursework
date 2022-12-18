package com.cdr.courseworkdatabase.main.contract

import androidx.annotation.StringRes

interface HasCustomTitleMain {
    @StringRes
    fun getRestTitle(): Int
}