package com.gappein.app.data

import android.content.Context
import android.content.SharedPreferences
import com.gappein.sdk.model.User
import com.google.gson.Gson

object AppPreference {

    private var sharedPreferences: SharedPreferences? = null
    private const val NAME = "sample-app"
    private const val USER_REGISTER = "user_register"

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences =
                context.applicationContext.getSharedPreferences(NAME, Context.MODE_PRIVATE)
        }
    }


    fun setUser(user: User) {
        val editor = sharedPreferences?.edit()
        editor?.putString(USER_REGISTER, Gson().toJson(user))
        editor?.apply()
    }

    fun getUser(): User? {
        val userString = sharedPreferences?.getString(USER_REGISTER, "")
        return if (userString != null) {
            Gson().fromJson(userString, User::class.java)
        } else null
    }

}