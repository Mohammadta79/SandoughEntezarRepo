package com.example.sandoughentezar.models

data class PaymentModel(
    var id: String,
    var date: String,
    var amount: String,
    var authority: String,
    var user_id: String,
    var description:String
)
