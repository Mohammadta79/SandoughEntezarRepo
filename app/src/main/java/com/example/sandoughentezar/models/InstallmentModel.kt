package com.example.sandoughentezar.models

import java.sql.Timestamp

data class InstallmentModel(
    val id: String,
    val date: Timestamp,
    val status: String,
    val amount: String
)
