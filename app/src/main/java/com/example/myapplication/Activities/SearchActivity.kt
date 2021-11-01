package com.example.myapplication.Activities

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.myapplication.*
import com.example.myapplication.Entities.Airport
import com.example.myapplication.Entities.CabinType
import com.example.myapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import java.lang.Exception
import java.time.LocalDate

class SearchActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var b: ActivityMainBinding
    private lateinit var fromAdapter: BaseAdapter
    private lateinit var toAdapter: BaseAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.apply {
            supportActionBar?.hide()
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            LocalData.load()
            setSupportActionBar(toolBar)
            fromAdapter = ArrayAdapter(
                this@SearchActivity,
                R.layout.support_simple_spinner_dropdown_item,
                LocalData.airportList
            )
            toAdapter = ArrayAdapter(
                this@SearchActivity,
                R.layout.support_simple_spinner_dropdown_item,
                LocalData.airportList
            )
            fromSpinner.adapter = fromAdapter
            toSpinner.adapter = toAdapter
            val adults = listOf(0, 1, 2, 3, 4, 5)
            val children = listOf(0, 1, 2, 3, 4, 5)
            adultCountSpinner.adapter = ArrayAdapter(
                this@SearchActivity,
                R.layout.support_simple_spinner_dropdown_item,
                adults
            )
            childCountSpinner.adapter = ArrayAdapter(
                this@SearchActivity,
                R.layout.support_simple_spinner_dropdown_item,
                children
            )
            seatTypeSpinner.adapter = ArrayAdapter(
                this@SearchActivity,
                R.layout.support_simple_spinner_dropdown_item,
                LocalData.cabinTypeList
            )

            startDateEditText.setOnClickListener(this@SearchActivity)
            endDateEditText.setOnClickListener(this@SearchActivity)
            searchButton.setOnClickListener(this@SearchActivity)
            fromSpinner.onItemSelectedListener = this@SearchActivity
            toSpinner.onItemSelectedListener = this@SearchActivity
            adultCountSpinner.onItemSelectedListener = this@SearchActivity
            changeButton.setOnClickListener(this@SearchActivity)
        }.root)
    }

    override fun onClick(v: View?) {
        when (v) {
            b.changeButton -> {
                val fromCurrent = b.fromSpinner.selectedItemPosition
                val toCurrent = b.toSpinner.selectedItemPosition
                b.fromSpinner.adapter = b.toSpinner.adapter
                b.toSpinner.adapter = b.fromSpinner.adapter
                b.fromSpinner.setSelection(toCurrent)
                b.toSpinner.setSelection(fromCurrent)
            }
            b.searchButton -> {
                if (b.startDateEditText.text.isEmpty()) {
                    Toast.makeText(this, "Input error.", Toast.LENGTH_SHORT).show()
                    return
                }
                if (b.fromSpinner.selectedItemPosition == b.toSpinner.selectedItemPosition) {
                    Toast.makeText(this, "Input error.", Toast.LENGTH_SHORT).show()
                    return
                }

                DepartureAirport = b.fromSpinner.selectedItem as Airport
                ArrivalAirport = b.toSpinner.selectedItem as Airport
                IsReturn = b.toggleButton.isChecked
                DepartureDate = LocalDate.parse(b.startDateEditText.text.toString())
                ReturnDate = try {
                    LocalDate.parse(b.endDateEditText.text.toString())
                } catch (e: Exception) {
                    null
                }
                Adult = b.adultCountSpinner.selectedItem as Int
                Child = b.childCountSpinner.selectedItem as Int
                Baby = b.babyCountSpinner.selectedItem as Int
                CabinType = b.seatTypeSpinner.selectedItem as CabinType
                if (IsReturn && b.endDateEditText.text.isEmpty()) {
                    Toast.makeText(this, "Input error.", Toast.LENGTH_SHORT).show()
                    return
                }
                if (Adult == 0 && Child == 0) {
                    Toast.makeText(this, "Input error.", Toast.LENGTH_SHORT).show()
                    return
                }
                if (ReturnDate != null && DepartureDate!! >= ReturnDate) {
                    Toast.makeText(this, "Input error.", Toast.LENGTH_SHORT).show()
                    return
                }
                if (IsReturn) {
                    IsLast = false
                    startActivity(Intent(this, FlightActivity::class.java))
                } else {
                    startActivity(Intent(this, FlightActivity::class.java))
                }
            }
            else -> {
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent) {
            b.fromSpinner -> {
                val item = b.fromSpinner.selectedItem as Airport
                b.fromIataTextView.text = item.IATACode
            }
            b.toSpinner -> {
                val item = b.toSpinner.selectedItem as Airport
                b.toIataTextView.text = item.IATACode
            }
            b.adultCountSpinner -> {
                val item = b.adultCountSpinner.selectedItem as Int
                val babys = mutableListOf<Int>()
                for (i in 0..item) {
                    babys.add(i)
                }
                b.babyCountSpinner.adapter =
                    ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, babys)
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this, SeatActivity::class.java))
        return super.onOptionsItemSelected(item)
    }
}