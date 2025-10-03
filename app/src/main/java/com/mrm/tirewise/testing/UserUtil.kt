package com.mrm.tirewise.testing

object UserUtil {

    fun login(username: String, password: String): Boolean {
        //If the login is successful if the username and password are "admin"
        return username == "admin" && password == "admin"
    }

    fun logout(isLoggedin: Boolean) : Boolean {
        if(isLoggedin!=true){
            return false
        }
        return true

    }
}