package com.example.myapplication.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.myapplication.*
import com.example.myapplication.Activities.ConfirmActivity
import com.example.myapplication.Activities.FlightActivity
import com.example.myapplication.Entities.Schedule
import com.example.myapplication.databinding.ListConfirmBinding
import com.example.myapplication.databinding.ListFlightBinding
import com.example.myapplication.databinding.ListUserBinding
import java.time.LocalTime

class ListViewAdapter(
    private val context: Context,
    private val resourceID: Int,
    private val items: List<Any>,
    private val isConfirm: Boolean = true,
    private val notify: NotifyChange? = null
) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return -1
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return when (resourceID) {
            R.layout.list_flight -> ListFlightBinding.inflate(LayoutInflater.from(context)).apply {
                val item = items[position] as Schedule
                val ticketDetail =
                    LocalData.ticketDetailList.first { it.ScheduleID == item.ID && it.CabinTypeID == CabinType?.ID }
                val route = LocalData.routeList.first { it.ID == item.RouteID }
                fromTextView.text =
                    LocalData.airportList.first { it.ID == route.DepartureAirportID }.Name
                toTextView.text =
                    LocalData.airportList.first { it.ID == route.ArrivalAirportID }.Name
                startTimeTextView.text = LocalTime.parse(item.Time).plusMinutes(0L).toString()
                toTimeTextView.text =
                    LocalTime.parse(item.Time).plusMinutes(route.FlightTime.toLong()).toString()
                costTextView.text =
                    "$${(item.EconomyPrice * ticketDetail.Rate * Adult + (item.EconomyPrice * ticketDetail.Rate * Child / 2)).toInt()}"
                val aircraft = LocalData.aircraftList.first { it.ID == item.AircraftID }
                val reservedTicket =
                    LocalData.ticketList.filter { it.TicketDetailID == ticketDetail.ID && LocalData.ticketDetailList.first { a -> a.ID == it.TicketDetailID }.CabinTypeID == CabinType?.ID }
                        .count()
                val seatCount = when (CabinType?.ID) {
                    1L -> aircraft.EconomySeats
                    2L -> aircraft.BusinessSeats
                    3L -> aircraft.FirstSeats
                    else -> 1
                }
                val result = calc(seatCount, reservedTicket)
                emptyTextView.setTextColor(Color.BLACK)
                emptyTextView.text = when {
                    result == 100 -> {
                        emptyTextView.setTextColor(Color.RED)
                        "Full"
                    }
                    result == 0
                    -> "Vacant"
                    else -> "${result} Left"
                }
                if (!isConfirm) {
                    root.setOnClickListener {
                        if (IsReturn && !IsLast) {
                            outboundID = item.ID
                            IsLast = true
                            context.startActivity(Intent(context, FlightActivity::class.java))
                        } else {
                            returnID = item.ID
                            context.startActivity(Intent(context, ConfirmActivity::class.java))
                        }
                    }
                }
            }.root
            R.layout.list_user -> ListUserBinding.inflate(LayoutInflater.from(context)).apply {
                val item = items[position] as ConfirmActivity.UserInfo
                userTypeTextView.text = item.Type
                userInfoTextView.text = "${item.FirstName} ${item.LastName} ${item.BirthDate}"
                emailTextView.text = "Email:${item.Email}"
                phoneTextView.text = "Phone:${item.Phone}"
                pnTextView.text = "PN:${item.PN}"
                countryTextView.text =
                    "Country:${LocalData.countryList.first { it.ID == item.CountryID }.Name}"
                removeButton.setOnClickListener {
                    notify?.remove(item)
                }
            }.root
            R.layout.list_confirm -> ListConfirmBinding.inflate(LayoutInflater.from(context))
                .apply {
                    val item = items[position] as Schedule
                    val ticketDetail =
                        LocalData.ticketDetailList.first { it.ScheduleID == item.ID && it.CabinTypeID == CabinType?.ID }
                    val route = LocalData.routeList.first { it.ID == item.RouteID }
                    fromTextView.text =
                        LocalData.airportList.first { it.ID == route.DepartureAirportID }.Name
                    toTextView.text =
                        LocalData.airportList.first { it.ID == route.ArrivalAirportID }.Name
                    startTimeTextView.text = LocalTime.parse(item.Time).plusMinutes(0L).toString()
                    toTimeTextView.text =
                        LocalTime.parse(item.Time).plusMinutes(route.FlightTime.toLong()).toString()
                    if (isConfirm) {
                        costTextView.text =
                            "$${(item.EconomyPrice * ticketDetail.Rate * Adult + (item.EconomyPrice * ticketDetail.Rate * Child / 2)).toInt()}"
                    }
                    cabinTypeTextView.text =
                        LocalData.cabinTypeList.first { it.ID == ticketDetail.CabinTypeID }.Name
                    dateTextView.text = item.getDate().toString()

                }.root
            else -> View(context)
        }
    }

    private fun calc(seatNum: Int, reserved: Int): Int {
        val rate = reserved.toFloat() / seatNum * 100
        val rateInt = Math.round(rate)
        return when {
            seatNum == reserved -> 100
            rateInt >= 81 -> seatNum - reserved
            else -> 0
        }
    }
}