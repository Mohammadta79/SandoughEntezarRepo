package com.example.sandoughentezar.api.state

data class Resource<out T>(val status: Status, val msg: String, val data: T?, val e: Exception?) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(Status.Success, "Success!", data, null)
        }

        fun <T> failure(msg: String): Resource<T> {
            return Resource(Status.Failure, msg, null, null)
        }


        fun <T> loading(): Resource<T> {
            return Resource(Status.Loading, "", null, null)
        }
    }
}
