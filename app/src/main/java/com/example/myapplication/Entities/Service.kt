package com.example.myapplication.Entities

data class Service(
    var ID: Long = 0,
    var Name: String = "",
    var ParentID: Long? = null,
    var CategoryID: Long? = null

) {
    override fun toString(): String {
        return Name
    }
}
