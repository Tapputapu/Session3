package com.example.myapplication.Entities

data class DiscountCampaign(
    var ID: Long = 0,
    var Name: String = "",
    var StartDate: String = "",
    var EndDate: String = "",
    var Photo: String = "",
    var Description: String = "",
    var DiscountRate: Int = 0,
    var Condition: String = "",
    var CampaignTypeID: Long = 0

) {
    override fun toString(): String {
        return Name
    }
}
