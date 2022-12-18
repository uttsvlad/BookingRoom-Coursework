package com.cdr.courseworkdatabase.model.storage

import android.content.Context
import com.google.gson.Gson
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class UserStorageWorker {

    // Сохранение пользователя в память устройства:
    fun saveUserToStorage(context: Context, user: StorageUser) {
        val data = Gson().toJson(user)

        val writer =
            OutputStreamWriter(context.openFileOutput(USER_FILE_NAME, Context.MODE_PRIVATE))
        writer.write(data)
        writer.close()
    }

    // Создание объекта пользователя:
    fun createUserFromStorage(context: Context): StorageUser {
        val data = readUserFromStorage(context)
        val jsonRoot = JSONObject(data)

        return StorageUser(
            surname = jsonRoot.getString("surname"),
            firstName = jsonRoot.getString("firstName"),
            middleName = jsonRoot.getString("middleName"),
            role = jsonRoot.getString("role"),
            username = jsonRoot.getString("username"),
            documentName = jsonRoot.getString("documentName"),
            documentID = jsonRoot.getString("documentID"),
            dateOfCreation = jsonRoot.getString("dateOfCreation"),
            jwtToken = jsonRoot.getString("jwtToken")
        )
    }

    // Чтения сохраненного пользователя в памяти устройства:
    private fun readUserFromStorage(context: Context): String {
        val file = context.openFileInput(USER_FILE_NAME)
        val inputStream = InputStreamReader(file)
        val reader = BufferedReader(inputStream)

        val data = StringBuilder()
        var line: String?

        while (reader.readLine().also { line = it } != null) data.append(line)

        return data.toString()
    }

    companion object {
        @JvmStatic
        private val USER_FILE_NAME = "user.json"
    }

}