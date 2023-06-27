package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(val accessToken: String, val createdAt: Long, val expireInMs: Long)
