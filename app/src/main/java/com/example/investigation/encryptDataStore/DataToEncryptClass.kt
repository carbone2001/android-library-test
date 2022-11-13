package com.example.investigation.encryptDataStore

import kotlinx.serialization.Serializable


@Serializable
data class DataToEncryptClass(
    val data: String? = null
)
