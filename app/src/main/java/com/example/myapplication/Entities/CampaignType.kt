package com.example.myapplication.Entities

data class CampaignType(
    var ID: Long = 0,
    var Name: String = ""

) {
    override fun toString(): String {
        return Name
    }
}
