package com.cdr.courseworkdatabase.main.home.client

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.databinding.FragmentBidBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomIconMain
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain

class TreatmentBidFragment : Fragment(), HasCustomTitleMain, HasCustomIconMain {

    private lateinit var binding: FragmentBidBinding
    private lateinit var bookedRoom: BookRoom // Информация о забронированной комнате

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bookedRoom = arguments?.getParcelable(ARG_KEY_ROOM_BOOKED_INFO)!!
        val inflater = TransitionInflater.from(requireContext()) // Анимация
        exitTransition = inflater.inflateTransition(R.transition.fade) // Анимация "выход"
        enterTransition = inflater.inflateTransition(R.transition.slide_bottom) // Анимация "вход"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBidBinding.inflate(inflater, container, false)
        binding.acceptButton.setOnClickListener { clickButtonAccept() } // Нажатие на кнопку "Ок"

        renderUI() // Отрисовка информации о забронированной комнате
        startEmojiRain() // Запуск дождя с Emoji

        return binding.root
    }

    // Отрисовка информации о забронированной комнате
    @SuppressLint("SetTextI18n")
    private fun renderUI() {
        with(binding) {
            resultTextView.text = "В обработке"
            roomIdTextView.text = "№${bookedRoom.roomId}"
            dateInTextView.text = bookedRoom.checkIn
            dateOutTextView.text = bookedRoom.checkOut
            paymentTextView.text =
                if (bookedRoom.paymentId.toInt() == 1) "Наличные (при заселении)" else "Банковская карта"
        }
    }

    // Запуск дождя с Emoji
    private fun startEmojiRain() {
        val emojiContainer = binding.emojiContainer

        emojiContainer.addEmoji(R.drawable.ic_clock) // Добавление нового эмоджи
        emojiContainer.addEmoji(R.drawable.ic_lock_close) // Добавление нового эмоджи

        emojiContainer.setPer(5) // Количество (по умолчанию 6)
        emojiContainer.setDuration(6000) // Длительность в милисекундах (по умолчанию 8000)
        emojiContainer.setDropDuration(3400) // Длительность падения в милисекундах (по умолчанию 2400)
        emojiContainer.setDropFrequency(500) // Частота падения (по умолчанию 500)

        emojiContainer.startDropping()
    }

    // Нажатие на кнопку "Ок":
    private fun clickButtonAccept() = navigatorMain().showHomeScreen()

    override fun getResIcon(): Int = R.drawable.ic_treatment
    override fun getRestTitle(): Int = R.string.titleTreatmentOfBookedRoom

    companion object {
        @JvmStatic
        private val ARG_KEY_ROOM_BOOKED_INFO = "ROOM_BOOKED_INFO"

        @JvmStatic
        fun newInstance(bookedRoom: BookRoom): TreatmentBidFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARG_KEY_ROOM_BOOKED_INFO, bookedRoom)

            val fragment = TreatmentBidFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}