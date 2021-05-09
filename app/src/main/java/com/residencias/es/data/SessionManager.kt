package com.residencias.es.data

import android.content.Context


class SessionManager(context: Context) {

    private val sharedPreferencesName = "sessionPreferences"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    private val accessTokenKey = "accessTokenKey"
    private val idKey = "idKey"
    private val nameKey = "nameKey"
    private val emailKey = "emailKey"

    fun isUserAvailable(): Boolean {
        return getAccessToken() != null
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(accessTokenKey, null)
    }

    fun saveAccessToken(accessToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString(accessTokenKey, accessToken)
        editor.apply()
    }

    fun clearAccessToken() {
        val editor = sharedPreferences.edit()
        editor.remove(accessTokenKey)
        editor.apply()
    }

    fun getIdData(): String? {
        return sharedPreferences.getString(idKey, null)
    }

    fun getNameData(): String? {
        return sharedPreferences.getString(nameKey, null)
    }

    fun getEmailData(): String? {
        return sharedPreferences.getString(emailKey, null)
    }

    fun saveUserData(id: String?, name: String?, email: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(idKey, id)
        editor.putString(nameKey, name)
        editor.putString(emailKey, email)
        editor.apply()
    }

    fun cleaUserData() {
        val editor = sharedPreferences.edit()
        editor.remove(idKey)
        editor.remove(nameKey)
        editor.remove(emailKey)
        editor.apply()
    }

}