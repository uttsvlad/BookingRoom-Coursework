package com.cdr.courseworkdatabase.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cdr.courseworkdatabase.databinding.ItemAllRoomsBinding
import com.cdr.courseworkdatabase.main.home.client.AllRoomsObject


interface AllRoomsAction {
    fun onRoomInfo(roomId: Long)
}

class AllRoomsAdapter(private val allRoomsAction: AllRoomsAction) :
    RecyclerView.Adapter<AllRoomsAdapter.AllRoomsViewHolder>(), View.OnClickListener {

    var data: List<AllRoomsObject> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRoomsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAllRoomsBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return AllRoomsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllRoomsViewHolder, position: Int) {
        val room: AllRoomsObject = data[position]

        holder.binding.root.tag = room

        with(holder.binding) {
            val name = "Номер \"${room.categoryName}\" - №${room.id}"
            val cost = "${room.price} рублей"
            val capacity = "${room.capacity} человек"

            nameTextView.text = name
            costTextView.text = cost
            capacityTextView.text = capacity
        }
    }

    class AllRoomsViewHolder(val binding: ItemAllRoomsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onClick(view: View) {
        val room: AllRoomsObject = view.tag as AllRoomsObject

        allRoomsAction.onRoomInfo(room.id!!)
    }
}


/*
class AllRoomsAdapter(private val date: List<AllRoomsObject>) : BaseAdapter() {

    override fun getCount(): Int = date.size

    override fun getItem(p0: Int): AllRoomsObject = date[p0]

    override fun getItemId(p0: Int): Long = 0

    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
        val binding = p1?.tag as ItemAllRoomsBinding? ?: createBinding(p2.context)

        val room = getItem(p0)
        val name = "Номер \"${room.categoryName}\" - №${room.id}"
        val cost = "${room.price} рублей"
        val capacity = "${room.capacity} человек"

        binding.nameTextView.text = name
        binding.costTextView.text = cost
        binding.capacityTextView.text = capacity

        return binding.root
    }

    private fun createBinding(context: Context): ItemAllRoomsBinding {
        val binding = ItemAllRoomsBinding.inflate(LayoutInflater.from(context))
        binding.root.tag = binding
        return binding
    }
}*/
