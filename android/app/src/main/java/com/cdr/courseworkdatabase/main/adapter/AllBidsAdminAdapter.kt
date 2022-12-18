package com.cdr.courseworkdatabase.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cdr.courseworkdatabase.databinding.ItemAllBidsAdminBinding
import com.cdr.courseworkdatabase.main.home.admin.AllBidsAdmin
import com.cdr.courseworkdatabase.main.home.admin.AllClientObject

interface BidsAdminActionListener {
    fun showBidInfoAdminScreen(bid: AllBidsAdmin)
    fun showBidDamageInfoAdminScreen(bid: AllBidsAdmin)
    fun showMessage()
}

class AllBidsAdminAdapter(
    private val bidsAdminActionListener: BidsAdminActionListener
) : RecyclerView.Adapter<AllBidsAdminAdapter.AllBidsAdminViewHolder>(),
    View.OnClickListener {

    var data: List<AllBidsAdmin> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllBidsAdminViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAllBidsAdminBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return AllBidsAdminViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AllBidsAdminViewHolder, position: Int) {
        val bid: AllBidsAdmin = data[position]

        val name = "Номер \"${bid.categoryName}\" - №${bid.roomId}"
        val status =
            if (bid.isAccepted == null) "В обработке" else if (bid.isAccepted) "Одобренно" else "Отказ"

        holder.binding.root.tag = bid

        with(holder.binding) {
            nameTextView.text = name
            statusTextView.text = status
            clientTextView.text = bid.clientFullName
            priceTitle.text = "${bid.totalPrice} рублей"
        }
    }

    override fun onClick(view: View) {
        val bid: AllBidsAdmin = view.tag as AllBidsAdmin
        when (bid.isAccepted) {
            null -> bidsAdminActionListener.showBidInfoAdminScreen(bid)
            true -> bidsAdminActionListener.showBidDamageInfoAdminScreen(bid)
            else -> bidsAdminActionListener.showMessage()
        }
    }

    class AllBidsAdminViewHolder(val binding: ItemAllBidsAdminBinding) :
        RecyclerView.ViewHolder(binding.root)
}
