package com.example.myapplication

import com.example.myapplication.Entities.*

object LocalData {
    val aircraftList = mutableListOf<Aircraft>()
    val airportList = mutableListOf<Airport>()
    val cabinTypeList = mutableListOf<CabinType>()
    val campaignTypeList = mutableListOf<CampaignType>()
    val categoryList = mutableListOf<Category>()
    val countryList = mutableListOf<Country>()
    val discountCampaignList = mutableListOf<DiscountCampaign>()
    val questionList = mutableListOf<Question>()
    val routeList = mutableListOf<Route>()
    val scheduleList = mutableListOf<Schedule>()
    val statusList = mutableListOf<Status>()
    val ticketList = mutableListOf<Ticket>()
    val ticketDetailList = mutableListOf<TicketDetail>()
    val videoList = mutableListOf<Video>()

    fun load() {
        aircraftList.clear()
        airportList.clear()
        cabinTypeList.clear()
        campaignTypeList.clear()
        categoryList.clear()
        countryList.clear()
        discountCampaignList.clear()
        questionList.clear()
        routeList.clear()
        scheduleList.clear()
        statusList.clear()
        ticketList.clear()
        ticketDetailList.clear()
        videoList.clear()



        aircraftList.addAll(DataBase.select("Aircraft"))
        airportList.addAll(DataBase.select("Airports"))
        cabinTypeList.addAll(DataBase.select("CabinTypes"))
        campaignTypeList.addAll(DataBase.select("CampaignTypes"))
        categoryList.addAll(DataBase.select("Categories"))
        countryList.addAll(DataBase.select("Countries"))
        discountCampaignList.addAll(DataBase.select("DiscountCampaigns"))
        questionList.addAll(DataBase.select("Questions"))
        routeList.addAll(DataBase.select("Routes"))
        scheduleList.addAll(DataBase.select("Schedules"))
        statusList.addAll(DataBase.select("Status"))
        ticketList.addAll(DataBase.select("Tickets"))
        ticketDetailList.addAll(DataBase.select("TicketDetails"))
        videoList.addAll(DataBase.select("Videos"))
    }
}