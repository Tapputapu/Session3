package com.example.myapplication.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.BaseAdapter
import com.example.myapplication.*
import com.example.myapplication.Adapters.ListViewAdapter
import com.example.myapplication.Entities.Route
import com.example.myapplication.Entities.Schedule
import com.example.myapplication.databinding.ActivityFlightBinding

class FlightActivity() : AppCompatActivity() {
    private lateinit var b: ActivityFlightBinding
    private lateinit var adapter: BaseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityFlightBinding.inflate(layoutInflater)
        setContentView(b.apply {
            supportActionBar?.hide()
            setSupportActionBar(toolBar)
            titleTextView.text = "Outbound"
            var route: Route? = null
            var schedule = listOf<Schedule>()
            if (IsLast && IsReturn) {
                titleTextView.text = "Return"
                route =
                    LocalData.routeList.firstOrNull { it.ArrivalAirportID == DepartureAirport?.ID && it.DepartureAirportID == ArrivalAirport?.ID }
                schedule =
                    LocalData.scheduleList.filter { it.RouteID == route?.ID && it.getDate() == ReturnDate }
            } else {
                route =
                    LocalData.routeList.firstOrNull { it.DepartureAirportID == DepartureAirport?.ID && it.ArrivalAirportID == ArrivalAirport?.ID }
                schedule =
                    LocalData.scheduleList.filter { it.RouteID == route?.ID && it.getDate() == DepartureDate }
            }
            adapter = ListViewAdapter(
                this@FlightActivity,
                R.layout.list_flight,
                schedule,
                false
            )
            listView.adapter = adapter

            backButton.setOnClickListener {
                finish()
            }
        }.root)

    }
}