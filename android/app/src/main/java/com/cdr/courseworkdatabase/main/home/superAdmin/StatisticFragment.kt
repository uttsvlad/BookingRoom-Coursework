package com.cdr.courseworkdatabase.main.home.superAdmin

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.SimpleAdapter
import androidx.annotation.RequiresApi
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.createToastCustom
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentStatisticBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class StatisticFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentStatisticBinding
    private var isSelectedStartDate = false // Выбранна ли дата заселения
    private var isSelectedFinishDate = false // Выбранна ли дата выселения
    private var isSelectedAdministrator = false

    private var day: Int? = null // Минимальный день - для конца
    private var month: Int? = null // Минимальный месяц - для конца
    private var year: Int? = null // Минимальный год - для конца
    private var administratorId: Long? = null

    private var allAdmins = mutableListOf<Administrator>()

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)

        binding.selectStartDateTextView.setOnClickListener { clickSelectStartDateTextView() }
        binding.selectFinishDateTextView.setOnClickListener {
            if (isSelectedStartDate) clickSelectFinishDateTextView()
            else requireContext().createToastCustom("Для начала выберите дату начала!")
        }
        binding.createStatisticButton.setOnClickListener { clickCreateStatisticButton() }

        binding.root.forEach { it.visibility = View.GONE }
        binding.progressBar.visibility = View.VISIBLE

        getAllAdmins()

        return binding.root
    }

    private fun getAllAdmins() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val allAdminsAPI = retrofitClient.create(API::class.java)
        val call: Call<List<Administrator>> = allAdminsAPI.getAllAdmins("Bearer ${user.jwtToken}")

        call.enqueue(object : Callback<List<Administrator>> {
            override fun onResponse(
                call: Call<List<Administrator>>,
                response: Response<List<Administrator>>
            ) {
                if (response.isSuccessful) {
                    allAdmins = response.body() as MutableList<Administrator>
                    renderUI()
                } else requireContext().createToastResponse(false)
            }

            override fun onFailure(call: Call<List<Administrator>>, t: Throwable) {
                requireContext().createToastResponse(false)
            }

        })
    }

    private fun getStatistic() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val downloadStatisticAPI = retrofitClient.create(API::class.java)
        val call: Call<StatisticResponse> = downloadStatisticAPI.getStatistic(
            "Bearer ${user.jwtToken}",
            StatisticRequest(
                administratorId = this@StatisticFragment.administratorId!!,
                start = binding.selectStartDateTextView.text.toString(),
                end = binding.selectFinishDateTextView.text.toString()
            )
        )

        call.enqueue(object : Callback<StatisticResponse> {
            override fun onResponse(
                call: Call<StatisticResponse>,
                response: Response<StatisticResponse>
            ) {
                if (response.isSuccessful) {
                    showStatistic(response.body()!!)
                    requireContext().createToastCustom("Статистика была загружена!")
                } else {
                    requireContext().createToastResponse(false)
                }
            }

            override fun onFailure(call: Call<StatisticResponse>, t: Throwable) {
                requireContext().createToastResponse(false)
            }
        })

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showStatistic(statisticResponse: StatisticResponse) {
        with(binding) {
            titleAllCheckIn.visibility = View.VISIBLE
            allCheckInTextView.text = ""
            titleLuxCount.visibility = View.VISIBLE
            luxCountTextView.text = ""
            titleTotalRevenue.visibility = View.VISIBLE
            totalRevenueTextView.text = ""

            allCheckInTextView.text = statisticResponse.allCheckinsCount.toString()
            luxCountTextView.text = statisticResponse.luxCount.toString()
            totalRevenueTextView.text = statisticResponse.totalRevenue.toString()
        }
    }

    private fun renderUI() {
        binding.root.forEach { it.visibility = View.VISIBLE }
        binding.progressBar.visibility = View.GONE
        binding.titleAllCheckIn.visibility = View.GONE
        binding.titleLuxCount.visibility = View.GONE
        binding.titleTotalRevenue.visibility = View.GONE

        val data = (0 until allAdmins.size).map {
            mapOf(
                KEY_ID_ADMIN to allAdmins[it].id,
                KEY_NAME_ADMIN to "${allAdmins[it].surname} ${allAdmins[it].firstName}"
            )
        }

        val adapter = SimpleAdapter(
            requireContext(),
            data,
            android.R.layout.simple_list_item_1,
            arrayOf(KEY_NAME_ADMIN),
            intArrayOf(android.R.id.text1)
        )

        binding.adminsSpinner.adapter = adapter
        binding.adminsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                administratorId = data[p2][KEY_ID_ADMIN] as Long?
                isSelectedAdministrator = true
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    // Нажатие на текст "Выбор даты заселения":
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun clickSelectStartDateTextView() {
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, monthOfYear, dayOfMonth ->
                isSelectedStartDate = true
                binding.selectStartDateTextView.setTextColor(requireContext().getColor(R.color.blue))

                val m = if (monthOfYear + 1 < 10) "0${monthOfYear + 1}" else (monthOfYear + 1).toString()
                val d = if (dayOfMonth < 10) "0${dayOfMonth}" else dayOfMonth.toString()

                binding.selectStartDateTextView.text = "$year-$m-$d"

                this@StatisticFragment.day = dayOfMonth + 1
                this@StatisticFragment.month = monthOfYear
                this@StatisticFragment.year = year

            }, mYear, mMonth, mDay
        )

        datePickerDialog.show()
    }

    // Нажатие на текст "Выбор даты выселения":
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun clickSelectFinishDateTextView() {
        val c: Calendar = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, monthOfYear, dayOfMonth ->
                isSelectedFinishDate = true
                binding.selectFinishDateTextView.setTextColor(requireContext().getColor(R.color.blue))

                val m = if (monthOfYear + 1 < 10) "0${monthOfYear + 1}" else (monthOfYear + 1).toString()
                val d = if (dayOfMonth < 10) "0${dayOfMonth}" else dayOfMonth.toString()

                binding.selectFinishDateTextView.text = "$year-$m-$d"
            }, mYear, mMonth, mDay
        )
        val mCalendar = Calendar.getInstance()
        mCalendar.set(year!!, month!!, day!!)
        datePickerDialog.datePicker.minDate = mCalendar.timeInMillis

        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun clickCreateStatisticButton() {
        with(binding) { // Проверка выбранных дат:
            if (!isSelectedStartDate && !isSelectedFinishDate && !isSelectedAdministrator) { // Обе даты пропушенны
                selectStartDateTextView.setTextColor(requireContext().getColor(R.color.red))
                selectFinishDateTextView.setTextColor(requireContext().getColor(R.color.red))
            } else if (!isSelectedStartDate) selectStartDateTextView.setTextColor( // Дата заселения
                requireContext().getColor(R.color.red)
            )
            else if (!isSelectedFinishDate) selectFinishDateTextView.setTextColor( // Дата выселения
                requireContext().getColor(R.color.red)
            )
            else getStatistic()
        }
    }

    override fun getRestTitle(): Int = R.string.titleStatistic

    companion object {
        @JvmStatic
        val KEY_NAME_ADMIN = "KEY_NAME_ADMIN"

        @JvmStatic
        val KEY_ID_ADMIN = "KEY_ID_ADMIN"
    }
}

data class Administrator(
    val id: Long,
    var surname: String,
    var firstName: String,
    var middleName: String?
)

data class StatisticRequest(
    val administratorId: Long,
    val start: String,
    val end: String
)

data class StatisticResponse(
    val allCheckinsCount: Int,
    val luxCount: Int,
    val totalRevenue: Double
)
