package com.example.sandoughentezar.models

data class LoanModel(
    var id: String,
    var row: String,
    var start_date: String,
    var amount: String,
    var end_date: String,
    var due_date: String,
    var status: String,
    var num_of_installment: String,
    var num_of_paid_installment: String,
    var num_of_unpaid_installment: String,
    var installment_amount: String
)