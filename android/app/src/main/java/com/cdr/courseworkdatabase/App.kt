package com.cdr.courseworkdatabase

import android.app.Application
import android.content.Context
import android.widget.EditText
import android.widget.Toast

class App : Application() {

}

fun String.checkValid(editText: EditText): Boolean {
    INVALID_DICTIONARY.forEach {
        if (this.contains(it)) {
            editText.error = "Недопустимый символ или слово!"
            return false
        }
    }
    return true
}

fun Context.createToastResponse(isSuccess: Boolean) {
    val message = if (isSuccess) "Успешно!" else "Произошла ошибка!"
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.createToastCustom(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

private val INVALID_DICTIONARY = listOf(
    "--", "'", "DATABASE", "UNION", "DROP", "DELETE", "USE", "SOURCE", "SHOW", "CREATE", "INSERT",
    "UPDATE", "SELECT", "WHERE", "GROUP", "ORDER", "JOIN", "INIT", "PG_DUMP", "PG_RESTORE", ";"
)