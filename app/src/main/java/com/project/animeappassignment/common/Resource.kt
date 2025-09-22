package com.project.animeappassignment.common


sealed class Resource<T> (val data:T?=null, val errorMessage: String?=null) {
    class Loading<T>(message: String?="Loading..."): Resource<T>(null,message)
    class Error<T>(message: String?="Error.."): Resource<T>(null,message)
    class Success<T>(data:T?): Resource<T>(data,null)
}