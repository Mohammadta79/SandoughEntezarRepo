package com.example.sandoughentezar.models

import saman.zamani.persiandate.PersianDate
import java.util.*

data class PaymentModel(
    var id: String,
    var date: List<Int>,
    var amount: String,
    var authority: String,
    var member_id: String,
    var description:String
)
