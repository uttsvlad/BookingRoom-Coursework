package com.cdr.courseworkdatabase.main.contract

import androidx.annotation.DrawableRes

interface HasCustomIconMain {
    @DrawableRes
    fun getResIcon(): Int
}