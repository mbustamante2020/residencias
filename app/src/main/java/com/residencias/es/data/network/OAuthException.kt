package com.residencias.es.data.network

sealed class OAuthException : Throwable()

object UnauthorizedException : OAuthException()