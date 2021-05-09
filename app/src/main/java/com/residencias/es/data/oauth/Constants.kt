package com.residencias.es.data.oauth

import java.util.*


object Constants {

    // OAuth2 Variables
    const val clientID = ""
    const val clientSecret = "7agwmvQK9IvMonQEHi8xOBEGPiJFm9KjnP6LB2qBQgXzJYcSkPMwR2QuZigGkMCn"
    const val redirectUri = "https://residencesysalud.es/"

    val scopes = listOf("user:read:email user:edit")
    val uniqueState = UUID.randomUUID().toString()
}