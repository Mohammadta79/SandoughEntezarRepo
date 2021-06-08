package com.example.sandoughentezar.data

import com.example.sandoughentezar.models.InstallmentModel

object LocalData {
    fun provideInstallment(): ArrayList<InstallmentModel> {
        var list = ArrayList<InstallmentModel>()
        list.add(InstallmentModel("1", "۱۴۰۰/۰۵/۰۳", "پرداخت شده", "۵۰۰۰۰۰"))
        list.add(InstallmentModel("2", "۱۴۰۰/۰۵/۰۴", "پرداخت نشده", "۵۰۰۰۰۰"))
        list.add(InstallmentModel("3", "۱۴۰۰/۰۵/۰۵", "پرداخت شده", "۵۰۰۰۰۰"))
        list.add(InstallmentModel("4", "۱۴۰۰/۰۵/۰۶", "پرداخت نشده", "۵۰۰۰۰۰"))
        return list
    }
}