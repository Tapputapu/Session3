package com.example.myapplication.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.Toast
import com.example.myapplication.*
import com.example.myapplication.Adapters.ListViewAdapter
import com.example.myapplication.Entities.Country
import com.example.myapplication.Entities.Schedule
import com.example.myapplication.Entities.Ticket
import com.example.myapplication.databinding.ActivityConfirmBinding
import java.lang.StringBuilder
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.random.Random

class ConfirmActivity : AppCompatActivity(), View.OnClickListener, NotifyChange {
    private lateinit var b: ActivityConfirmBinding
    private lateinit var adapter: BaseAdapter
    private val userInfos = mutableListOf<UserInfo>()
    private val items = mutableListOf<Schedule>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(b.apply {
            supportActionBar?.hide()
            setSupportActionBar(toolBar)
            titleTextView.text = "Confirm"
            val returnItem = LocalData.scheduleList.first { it.ID == returnID }
            val outboundItem = LocalData.scheduleList.firstOrNull { it.ID == outboundID }
            if (outboundItem == null) {
                items.add(returnItem)
            } else {
                items.addAll(listOf(outboundItem, returnItem))
            }
            ticketListView.adapter = ListViewAdapter(
                this@ConfirmActivity,
                R.layout.list_confirm,
                items,
                true
            )
            adapter = ListViewAdapter(
                this@ConfirmActivity,
                R.layout.list_user,
                userInfos,
                true,
                this@ConfirmActivity
            )

            passportCountrySpinner.adapter = ArrayAdapter(
                this@ConfirmActivity,
                R.layout.support_simple_spinner_dropdown_item,
                LocalData.countryList
            )
            adultcountTextView.text = Adult.toString()
            childCountTextView.text = Child.toString()
            babyTextView.text = Baby.toString()
            var cost = 0
            items.forEach {
                val ticketDetail =
                    LocalData.ticketDetailList.first { a -> a.ScheduleID == it.ID && a.CabinTypeID == CabinType?.ID }
                cost += ((it.EconomyPrice * ticketDetail.Rate * Adult) + (it.EconomyPrice * ticketDetail.Rate * Child / 2)).toInt()
            }
            costTextView.text = "$$cost"

            usersListView.adapter = adapter

            birthDateEditText.setOnClickListener(this@ConfirmActivity)

            addButton.setOnClickListener(this@ConfirmActivity)
            submitButton.setOnClickListener(this@ConfirmActivity)
            backButton.setOnClickListener(this@ConfirmActivity)
        }.root)

    }

    override fun onClick(v: View?) {
        when (v) {
            b.addButton -> {
                if (b.firstNameEditText.text.isEmpty() || b.lastNameEditText.text.isEmpty()
                    || b.birthDateEditText.text.isEmpty() || b.passportNumberTextView.text.isEmpty() || b.passportCountrySpinner.selectedItemPosition <= -1
                ) {
                    Toast.makeText(this, "Input error.", Toast.LENGTH_SHORT).show()
                    return
                }
                val userInfo = UserInfo(
                    getType(LocalDate.parse(b.birthDateEditText.text.toString())),
                    b.firstNameEditText.text.toString(),
                    b.lastNameEditText.text.toString(),
                    b.birthDateEditText.text.toString(),
                    b.emailEditText.text.toString(),
                    b.phoneTextView.text.toString(),
                    b.passportNumberTextView.text.toString(),
                    (b.passportCountrySpinner.selectedItem as Country).ID
                )

                userInfos.add(userInfo)
                adapter.notifyDataSetChanged()

                b.firstNameEditText.text.clear()
                b.lastNameEditText.text.clear()
                b.phoneTextView.text.clear()
                b.emailEditText.text.clear()
                b.birthDateEditText.text.clear()
                b.passportNumberTextView.text.clear()
                b.passportCountrySpinner.setSelection(0)
            }
            b.submitButton -> {
                if (Adult + Child + Baby > userInfos.size) {
                    Toast.makeText(this, "Require all user information.", Toast.LENGTH_SHORT).show()
                    return
                }
                val refList = mutableListOf<String>()
                items.forEach { schedule ->
                    val refString = createRefStr()
                    refList.add(refString)
                    userInfos.forEach { user ->
                        val ticket = Ticket(
                            0,
                            LocalData.ticketDetailList.first { a -> a.CabinTypeID == CabinType?.ID && a.ScheduleID == schedule.ID }.ID,
                            refString,
                            user.FirstName,
                            user.LastName,
                            user.Phone,
                            user.BirthDate,
                            user.Email,
                            user.PN,
                            user.CountryID,
                            LocalDate.now().toString(),
                            null,
                            1
                        )
                        DataBase.insert("Tickets", ticket)
                    }
                }
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                AlertDialog.Builder(this).apply {
                    val str = StringBuilder()
                    refList.forEach { str.append("$it\r\n") }
                    setTitle("Reference Number")
                    setMessage(str.toString())
                    setPositiveButton("OK") { _, _ ->
                        startActivity(Intent(context, SearchActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        })
                    }
                    setCancelable(false)
                    create()
                    show()
                }
            }
            b.backButton -> {
                finish()
            }
            b.birthDateEditText -> {
                DatePickerDialog(this).apply {
                    setOnDateSetListener { view, year, month, dayOfMonth ->
                        val editText = v as EditText
                        editText.setText(
                            String.format(
                                "%04d-%02d-%02d",
                                year,
                                month + 1,
                                dayOfMonth
                            )
                        )
                    }
                    show()
                }
            }
        }
    }

    data class UserInfo(
        var Type: String = "",
        var FirstName: String = "",
        var LastName: String = "",
        var BirthDate: String = "",
        var Email: String = "",
        var Phone: String = "",
        var PN: String = "",
        var CountryID: Long = 0
    )

    override fun remove(info: UserInfo) {
        userInfos.remove(info)
        adapter.notifyDataSetChanged()
    }

    private fun createRefStr(): String {
        val refID = Random.nextInt(1, 999999)
        val refStr = String.format("%06d", refID)
        if (LocalData.ticketList.firstOrNull { it.BookingReference == refStr } != null) {
            return createRefStr()
        }
        return refStr
    }

    private fun getType(birthDate: LocalDate): String {
        val now = LocalDate.now()
        val years = ChronoUnit.YEARS.between(birthDate, now)
        return when {
            years in 0..2 -> "Infants"
            years in 3..11 -> "Children"
            else -> "Adults"
        }
    }
}