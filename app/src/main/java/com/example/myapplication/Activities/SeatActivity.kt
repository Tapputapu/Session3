package com.example.myapplication.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Toast
import com.example.myapplication.Adapters.GridListViewAdapter
import com.example.myapplication.CabinType
import com.example.myapplication.Entities.Schedule
import com.example.myapplication.Entities.Ticket
import com.example.myapplication.Adapters.ListViewAdapter
import com.example.myapplication.Entities.TicketDetail
import com.example.myapplication.LocalData
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivitySeatBinding
import java.time.LocalDate
import java.util.function.BinaryOperator

class SeatActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    private lateinit var b: ActivitySeatBinding
    private lateinit var adapter: BaseAdapter
    private lateinit var ticketDetail: TicketDetail
    private lateinit var seatAdapter: GridListViewAdapter
    private var ticket = mutableListOf<Ticket>()
    private val items = mutableListOf<Schedule>()
    private val seatList = mutableListOf<SeatInfo>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivitySeatBinding.inflate(layoutInflater)
        setContentView(b.apply {

            adapter = ListViewAdapter(this@SeatActivity, R.layout.list_confirm, items, false)
            listView.adapter = adapter


            showButton.setOnClickListener(this@SeatActivity)
            cancelButton.setOnClickListener(this@SeatActivity)
            submitButton.setOnClickListener(this@SeatActivity)
            userSpinner.onItemSelectedListener = this@SeatActivity
        }.root)
    }

    override fun onClick(v: View?) {
        when (v) {
            b.showButton -> {
                ticket =
                    LocalData.ticketList.filter { it.BookingReference == b.refTextView.text.toString() }
                        .toMutableList()
                if (ticket.size == 0) {
                    Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show()
                    return
                }
                items.clear()
                ticketDetail =
                    LocalData.ticketDetailList.first { a -> a.ID == ticket[0].TicketDetailID }
                CabinType = LocalData.cabinTypeList.first { it.ID == ticketDetail.CabinTypeID }
                LocalData.scheduleList.filter { it.ID == ticketDetail.ScheduleID }
                    .forEach {
                        items.add(it)
                    }

                adapter.notifyDataSetChanged()
                b.userSpinner.requestFocus()

                val list = ticket.groupBy { it.BookingReference }
                val userList = mutableListOf<String>()
                list[b.refTextView.text.toString()]?.forEach {
                    userList.add("${it.Firstname} ${it.Lastname}")
                }



                seatAdapter = GridListViewAdapter(
                    this,
                    R.layout.list_seat,
                    seatList
                )
                b.gridView.adapter = seatAdapter

                b.userSpinner.adapter =
                    ArrayAdapter(
                        this@SeatActivity, R.layout.support_simple_spinner_dropdown_item, userList
                    )

//                createSeat(ticket[0].Firstname, ticket[0].Lastname, ticket[0].getBirthDate())
            }
            b.cancelButton -> finish()
            b.submitButton -> {

            }
        }
    }

    private fun getReserved(number: String, ticketDetailId: Long): Boolean {
        val reservedList =
            LocalData.ticketList.filter { it.TicketDetailID == ticketDetailId && it.SeatNumber != null }
        reservedList.forEach {
            val seatNum = it.SeatNumber!!
            val array = number
            val row = array[0].toString().toInt()
            val col = array[1].toString()
            if (seatNum / 4.0 > row - 1.0) {
                val reservedCol = seatNum % 4 //1->1,2->2,3->3,0->4
                if (reservedCol == 1 && col == "A") return true
                if (reservedCol == 2 && col == "B") return true
                if (reservedCol == 3 && col == "C") return true
                if (reservedCol == 0 && col == "D") return true
            }
        }
        return false
    }

    private fun getMyReserved(
        isReserved: Boolean,
        bookingReference: String,
        firstName: String,
        lastName: String,
        birthDate: LocalDate
    ): Boolean {
        val myReservation =
            LocalData.ticketList.first { it.BookingReference == bookingReference && it.Firstname == firstName && it.Lastname == lastName && it.getBirthDate() == birthDate }
        if (isReserved && myReservation.SeatNumber != null) return true
        return false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val user = b.userSpinner.selectedItemPosition
        createSeat(ticket[user].Firstname, ticket[user].Lastname, ticket[user].getBirthDate())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    private fun createSeat(firstName: String, lastName: String, birthDate: LocalDate) {
        val aircraft =
            LocalData.aircraftList.first { it.ID == LocalData.scheduleList.first { a -> a.ID == ticketDetail.ScheduleID }.AircraftID }
        val seatNum = when (CabinType?.ID) {
            1L -> aircraft.EconomySeats
            2L -> aircraft.BusinessSeats
            3L -> aircraft.FirstSeats
            else -> 0
        }
        seatList.clear()
        var ID = 1
        for (i in 1..seatNum / 4) {
            for (c in listOf('A', 'B', 'T', 'C', 'D')) {
                seatList.add(
                    SeatInfo(
                        ID,
                        "${i}${c}",
                        getMyReserved(
                            getReserved("${i}${c}", ticketDetail.ID),
                            ticket[0].BookingReference,
                            firstName,
                            lastName,
                            birthDate
                        ),
                        getReserved("${i}${c}", ticketDetail.ID)
                    )
                )
                ID++
            }
        }
        seatAdapter.notifyDataSetChanged()
    }
}


data class SeatInfo(
    var ID: Int = 0,
    var Number: String = "",
    var IsSelected: Boolean = false,
    var IsReserved: Boolean = false
)