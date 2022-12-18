package com.cdr.courseworkdatabase.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cdr.courseworkdatabase.databinding.ItemAllBidsBinding
import com.cdr.courseworkdatabase.main.home.client.AllBidsAdminObject

interface AllBidsAction {
    fun showBidInfo(bid: AllBidsAdminObject)
}

class AllBidsAdapter(private val allBidsAction: AllBidsAction) :
    RecyclerView.Adapter<AllBidsAdapter.AllBidsViewHolder>(), View.OnClickListener {

    var data: List<AllBidsAdminObject> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllBidsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAllBidsBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return AllBidsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllBidsViewHolder, position: Int) {
        val bid = data[position]

        val name = "Номер \"${bid.categoryName}\" - №${bid.roomId}"
        val status =
            if (bid.isAccepted == null) "В обработке" else if (bid.isAccepted!!) "Одобренно" else "Отказ"

        with(holder.binding) {
            root.tag = bid
            nameTextView.text = name
            statusTextView.text = status
            dateInTextView.text = bid.checkIn
            dateOutTextView.text = bid.checkOut
        }
    }

    override fun onClick(view: View) {
        val bid: AllBidsAdminObject = view.tag as AllBidsAdminObject
        allBidsAction.showBidInfo(bid)
    }

    class AllBidsViewHolder(val binding: ItemAllBidsBinding) : RecyclerView.ViewHolder(binding.root)
}
