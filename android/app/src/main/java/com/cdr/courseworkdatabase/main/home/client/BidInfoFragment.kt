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
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.navigatorMain

class BidInfoFragment : Fragment(), HasCustomTitleMain {

    private lateinit var binding: FragmentBidBinding
    private lateinit var bidInfo: AllBidsAdminObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bidInfo = arguments?.getParcelable(ARG_KEY_BID)!!
        val inflater = TransitionInflater.from(requireContext()) // Анимация
        exitTransition = inflater.inflateTransition(R.transition.fade) // Анимация "выход"
        enterTransition = inflater.inflateTransition(R.transition.slide_bottom) // Анимация "вход"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBidBinding.inflate(inflater, container, false)

        binding.acceptButton.setOnClickListener { clickButtonAccept() }
        renderUI() // Отрисовка информации о заявке клиента:
        startEmojiRain() // Запуск дождя с Emoji

        return binding.root
    }

    // Отрисовка информации о заявке клиента:
    @SuppressLint("SetTextI18n")
    private fun renderUI() {
        with(binding) {
            roomIdTextView.text = "№${bidInfo.roomId}"
            dateInTextView.text = bidInfo.checkIn
            dateOutTextView.text = bidInfo.checkOut
            totalPriceTextView.text = "${bidInfo.totalPrice} рублей"
            paymentTextView.text =
                if (bidInfo.paymentId.toInt() == 1) "Наличные (при заселении)" else "Банковская карта"

            if (bidInfo.isAccepted == null) {
                resultTextView.text = "В обработке"
                commentTextView.text = "Отправленно на обработку"
            } else {
                if (bidInfo.isAccepted == true) {
                    resultImageView.setImageResource(R.drawable.ic_accepted_big)
                    resultTextView.text = "Обработано!"
                    commentTextView.text = bidInfo.comment
                } else {
                    resultImageView.setImageResource(R.drawable.ic_not_accepted_big)
                    resultTextView.text = "Отказ!"
                    commentTextView.text = bidInfo.comment
                }
            }
        }
    }

    // Запуск дождя с Emoji
    private fun startEmojiRain() {
        val emojiContainer = binding.emojiContainer
        if (bidInfo.isAccepted == null) {
            emojiContainer.addEmoji(R.drawable.ic_clock) // Добавление нового эмоджи
            emojiContainer.addEmoji(R.drawable.ic_lock_close) // Добавление нового эмоджи
        } else {
            if (bidInfo.isAccepted == true) {
                emojiContainer.addEmoji(R.drawable.ic_accepted) // Добавление нового эмоджи
                emojiContainer.addEmoji(R.drawable.ic_lock_open) // Добавление нового эмоджи
            } else {
                emojiContainer.addEmoji(R.drawable.ic_not_accepted) // Добавление нового эмоджи
                emojiContainer.addEmoji(R.drawable.ic_lock_close) // Добавление нового эмоджи
            }
        }

        emojiContainer.setPer(5) // Количество (по умолчанию 6)
        emojiContainer.setDuration(6000) // Длительность в милисекундах (по умолчанию 8000)
        emojiContainer.setDropDuration(3400) // Длительность падения в милисекундах (по умолчанию 2400)
        emojiContainer.setDropFrequency(500) // Частота падения (по умолчанию 500)

        emojiContainer.startDropping()
    }

    // Нажатие на кнопку "Ок":
    private fun clickButtonAccept() = navigatorMain().showHomeScreen()

    override fun getRestTitle(): Int = R.string.titleBidInfo

    companion object {
        @JvmStatic
        private val ARG_KEY_BID = "KEY_BID"

        @JvmStatic
        fun newInstance(bidInfo: AllBidsAdminObject): BidInfoFragment {
            val bundle = Bundle()
            bundle.putParcelable(ARG_KEY_BID, bidInfo)

            val fragment = BidInfoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}