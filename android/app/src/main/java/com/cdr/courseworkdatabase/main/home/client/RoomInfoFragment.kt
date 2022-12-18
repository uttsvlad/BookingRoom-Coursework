package com.cdr.courseworkdatabase.main.home.client

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.createToastCustom
import com.cdr.courseworkdatabase.createToastResponse
import com.cdr.courseworkdatabase.databinding.FragmentRoomInfoBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain
import com.cdr.courseworkdatabase.model.API
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import com.synnapps.carouselview.ImageListener
import kotlinx.android.parcel.Parcelize
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.properties.Delegates

class RoomInfoFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentRoomInfoBinding
    private var isSelectedStartDate = false // Выбранна ли дата заселения
    private var isSelectedFinishDate = false // Выбранна ли дата выселения
    private lateinit var roomInfo: RoomInfoObject
    private var roomId by Delegates.notNull<Long>() // Уникальный номер комнаты

    private var day: Int? = null
    private var month: Int? = null
    private var year: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomId = arguments?.getLong(ARG_KEY_ROOM_ID) ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoomInfoBinding.inflate(inflater, container, false)

        getRoomInfo() // Получение информации о комнате
        binding.selectStartDateTextView.setOnClickListener { clickSelectStartDateTextView() }
        binding.selectFinishDateTextView.setOnClickListener {
            if (!isSelectedStartDate) requireContext().createToastCustom("Для начала выберите дату заселения!")
            else clickSelectFinishDateTextView()
        }
        binding.bookRoomButton.setOnClickListener { clickBookRoomButton() }

        return binding.root
    }

    // Метод получения информации о комнате:
    private fun getRoomInfo() {
        val user =
            UserStorageWorker().createUserFromStorage(requireContext()) // Получение пользователя

        val url = getString(R.string.baseURL)
        val retrofitClient =
            Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                .build()

        val roomInfoAPI = retrofitClient.create(API::class.java)
        val call: Call<RoomInfoObject> = roomInfoAPI.getRoomInfo("Bearer ${user.jwtToken}", roomId)

        call.enqueue(object : Callback<RoomInfoObject> {
            override fun onResponse(
                call: Call<RoomInfoObject>, response: Response<RoomInfoObject>
            ) {
                if (response.isSuccessful) {
                    requireContext().createToastResponse(true)
                    roomInfo = response.body()!!
                    renderUI()
                } else requireContext().createToastResponse(false)
            }

            override fun onFailure(call: Call<RoomInfoObject>, t: Throwable) {
                requireContext().createToastResponse(false)
            }
        })
    }

    // Метод отправка данных о "Бронировании комнаты":
    private fun sendDataToStorage() {
        with(binding) {
            val user = UserStorageWorker().createUserFromStorage(requireContext())

            val username = user.username
            val roomIdBooked = roomId
            val checkIn = selectStartDateTextView.text.toString()
            val checkOut = selectFinishDateTextView.text.toString()

            var servicesIds: MutableList<Long>? = mutableListOf()
            if (roomInfo.categoryName == "Люкс") {
                servicesIds = null
            } else {
                if (bonusOneCheckBox.isChecked) servicesIds?.add(roomInfo.services?.get(0)?.id!!)
                if (bonusTwoCheckBox.isChecked) servicesIds?.add(roomInfo.services?.get(1)?.id!!)
                if (bonusThreeCheckBox.isChecked) servicesIds?.add(roomInfo.services?.get(2)?.id!!)
            }

            val paymentId: Long = if (cashRadioButton.isChecked) 1 else 2

            val bookRoom = BookRoom(
                username = username,
                roomId = roomIdBooked,
                checkIn = checkIn,
                checkOut = checkOut,
                servicesIds = servicesIds,
                paymentId = paymentId
            )

            val url = getString(R.string.baseURL)
            val retrofitClient =
                Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create())
                    .build()

            val bookRoomAPI = retrofitClient.create(API::class.java)
            val call: Call<ResponseBody> =
                bookRoomAPI.sendBookRoom("Bearer ${user.jwtToken}", bookRoom)

            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>, response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        requireContext().createToastCustom("Заявление отправлено на обработку!")
                        navigatorMain().showTreatmentBidScreen(bookRoom)
                    } else requireContext().createToastResponse(false)
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    requireContext().createToastCustom("Неизвестная ошибка!")
                }
            })
        }
    }

    // Отрисовка информации о комнате:
    @SuppressLint("SetTextI18n")
    private fun renderUI() {
        if (roomInfo.categoryName == "Люкс") {
            binding.titleBonus.visibility = View.GONE
            binding.bonusOneCheckBox.visibility = View.GONE
            binding.bonusTwoCheckBox.visibility = View.GONE
            binding.bonusThreeCheckBox.visibility = View.GONE
        } else {
            binding.titleBonus.visibility = View.VISIBLE
            binding.bonusOneCheckBox.visibility = View.VISIBLE
            binding.bonusTwoCheckBox.visibility = View.VISIBLE
            binding.bonusThreeCheckBox.visibility = View.VISIBLE
        }


        val imageListener = ImageListener { position, imageView ->
            Glide.with(requireContext()).load(roomInfo.photos!![position]).into(imageView)
        }
        with(binding) {
            carouselView.setImageListener(imageListener)
            carouselView.pageCount = roomInfo.photos!!.size
            titleInfoRoom.text = "Номер \"${roomInfo.categoryName}\" №${roomInfo.id}"
            descriptionTextView.text = roomInfo.description
            costRoomTextView.text = "${roomInfo.price} рублей"
            capacityRoomTextView.text = "${roomInfo.capacity} человек"
            floorRoomTextView.text = "${roomInfo.floor} этаж"
            windowViewRoomTextView.text = roomInfo.windowViewName
            conditionerRoomTextView.text = if (roomInfo.conditionerAvailable) "Да" else "Нет"
            hairDrierRoomTextView.text = if (roomInfo.hairDryerAvailable) "Да" else "Нет"
            bonusOneCheckBox.text =
                "${roomInfo.services?.get(0)?.serviceName} (${roomInfo.services?.get(0)?.servicePrice} рублей)"
            bonusTwoCheckBox.text =
                "${roomInfo.services?.get(1)?.serviceName} (${roomInfo.services?.get(1)?.servicePrice} рублей)"
            bonusThreeCheckBox.text =
                "${roomInfo.services?.get(2)?.serviceName} (${roomInfo.services?.get(2)?.servicePrice} рублей)"
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
                binding.selectStartDateTextView.text = "$dayOfMonth/${monthOfYear + 1}/$year"

                this@RoomInfoFragment.day = dayOfMonth + 1
                this@RoomInfoFragment.month = monthOfYear
                this@RoomInfoFragment.year = year

            }, mYear, mMonth, mDay
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
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
                binding.selectFinishDateTextView.text = "$dayOfMonth/${monthOfYear + 1}/$year"
            }, mYear, mMonth, mDay
        )

        val mCalendar = Calendar.getInstance()
        mCalendar.set(year!!, month!!, day!!)
        datePickerDialog.datePicker.minDate = mCalendar.timeInMillis

        datePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun clickBookRoomButton() {
        with(binding) { // Проверка выбранных дат:
            if (!isSelectedStartDate && !isSelectedFinishDate) { // Обе даты пропушенны
                selectStartDateTextView.setTextColor(requireContext().getColor(R.color.red))
                selectFinishDateTextView.setTextColor(requireContext().getColor(R.color.red))
            } else if (!isSelectedStartDate) selectStartDateTextView.setTextColor( // Дата заселения
                requireContext().getColor(R.color.red)
            )
            else if (!isSelectedFinishDate) selectFinishDateTextView.setTextColor( // Дата выселения
                requireContext().getColor(R.color.red)
            )
            else sendDataToStorage() // Все проверки пройденны
        }
    }

    override fun getRestTitle(): Int = R.string.titleClientInfoRoom

    companion object {
        @JvmStatic
        private val ARG_KEY_ROOM_ID = "KEY_ROOM_ID"

        @JvmStatic
        fun newInstance(newRoomId: Long): RoomInfoFragment {
            val bundle = Bundle()
            bundle.putLong(ARG_KEY_ROOM_ID, newRoomId)

            val fragment = RoomInfoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}

data class RoomInfoObject(
    var id: Long? = null,
    var description: String = "",
    var categoryName: String = "",
    var windowViewName: String = "",
    var price: Double = 0.0,
    var capacity: Int = 0,
    var floor: Int = 0,
    var conditionerAvailable: Boolean = false,
    var hairDryerAvailable: Boolean = false,
    var services: List<BonusService>? = null,
    var photos: List<String>? = null
)

data class BonusService(var id: Long, var serviceName: String = "", var servicePrice: Double = 0.0)

@Parcelize
data class BookRoom(
    val username: String,
    val roomId: Long,
    val checkIn: String,
    val checkOut: String,
    val servicesIds: List<Long>?,
    val paymentId: Long
) : Parcelable