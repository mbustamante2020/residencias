package com.residencias.es.data.oauth.datasource

import android.content.Context


class SessionManager(context: Context) {

    private val sharedPreferencesName = "sessionPreferences"
    private val sharedPreferences = context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)

    private val accessTokenKey = "accessTokeKey"
    private val refreshTokenKey = "refreshTokenKey"

    private val idKey = "idKey"
    private val nameKey = "nameKey"
    private val emailKey = "emailKey"
    private val roleKey = "roleKey"

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

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(refreshTokenKey, null)
    }

    fun saveRefreshToken(refreshToken: String) {
        val editor = sharedPreferences.edit()
        editor.putString(refreshTokenKey, refreshToken)
        editor.apply()
    }

    fun clearRefreshToken() {
        val editor = sharedPreferences.edit()
        editor.remove(refreshTokenKey)
        editor.apply()
    }

    fun getId(): String? {
        return sharedPreferences.getString(idKey, null)
    }

    fun getName(): String? {
        return sharedPreferences.getString(nameKey, null)
    }

    fun getEmail(): String? {
        return sharedPreferences.getString(emailKey, null)
    }

    fun getRole(): String? {
        return sharedPreferences.getString(roleKey, null)
    }

    fun saveUserData(id: String?, name: String?, email: String?, role: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(idKey, id)
        editor.putString(nameKey, name)
        editor.putString(emailKey, email)
        editor.putString(roleKey, role)
        editor.apply()
    }

    fun cleaUserData() {
        val editor = sharedPreferences.edit()
        editor.remove(idKey)
        editor.remove(nameKey)
        editor.remove(emailKey)
        editor.remove(roleKey)
        editor.apply()
    }
}