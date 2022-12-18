package com.cdr.courseworkdatabase.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cdr.courseworkdatabase.databinding.ItemAllClientsBinding
import com.cdr.courseworkdatabase.main.home.admin.AllClientObject


class AllClientsAdapter : RecyclerView.Adapter<AllClientsAdapter.AllClientsViewHolder>() {

    var data: List<AllClientObject> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllClientsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAllClientsBinding.inflate(inflater, parent, false)

        return AllClientsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllClientsViewHolder, position: Int) {
        val client = data[position]

        with(holder.binding) {
            val name =
                "${client.clientDTO.surname} ${client.clientDTO.firstName} ${client.clientDTO.middleName}"
            nameTextView.text = name
            documentIDTextView.text = client.clientDTO.documentNumber
            dateOfCreationTextView.text = client.registrationDate
        }
    }

    class AllClientsViewHolder(val binding: ItemAllClientsBinding) :
        RecyclerView.ViewHolder(binding.root)
}


//
//class AllClientsAdapter(private val data: List<AllClientObject>) : BaseAdapter() {
//    override fun getCount(): Int = data.size
//
//    override fun getItem(p0: Int): AllClientObject = data[p0]
//
//    override fun getItemId(p0: Int): Long = 0
//
//    override fun getView(p0: Int, p1: View?, p2: ViewGroup): View {
//        val binding = p1?.tag as ItemAllClientsBinding? ?: createBinding(p2.context)
//
//        val client = getItem(p0)
//        val name =
//            "${client.clientDTO.surname} ${client.clientDTO.firstName} ${client.clientDTO.middleName}"
//        binding.nameTextView.text = name
//        binding.documentIDTextView.text = client.clientDTO.documentNumber
//        binding.dateOfCreationTextView.text = client.registrationDate
//
//        return binding.root
//    }
//
//    private fun createBinding(context: Context): ItemAllClientsBinding {
//        val binding = ItemAllClientsBinding.inflate(LayoutInflater.from(context))
//        binding.root.tag = binding
//        return binding
//    }
//}