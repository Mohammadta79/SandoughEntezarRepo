package com.example.sandoughentezar.models



data class InstallmentModel(
    val id: String,
    val date: String,
    val status: String,
    val amount: String,
    val member_id:String,
    val loan_id:String
)
