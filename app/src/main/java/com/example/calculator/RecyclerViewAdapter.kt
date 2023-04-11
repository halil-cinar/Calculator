package com.example.calculator

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.calculator.View.TAG
import com.example.calculator.databinding.RecyclerViewLayoutBinding

class RecyclerViewAdapter(private var items:ArrayList<String>,private val recyclerView: RecyclerView):RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    var instance:RecyclerViewLayoutBinding?=null
    class ViewHolder(var view:RecyclerViewLayoutBinding):RecyclerView.ViewHolder(view.root){
        fun changeText(text:String){
            view.text=text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var inflater=LayoutInflater.from(parent.context)
         instance=DataBindingUtil.inflate<RecyclerViewLayoutBinding>(inflater, R.layout.recycler_view_layout,parent,false)
        return ViewHolder(instance!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.changeText(items[position])

    }

    override fun getItemCount(): Int {
        return items.size
    }

   @JvmName("setItems1")
   public fun setItems(items: ArrayList<String>){
       this.items.clear()
        this.items.addAll(items)
       Log.e(TAG, "setItems: "+this.items.size, )
       notifyDataSetChanged()

       Log.e(TAG, "setItems: ", )
      recyclerView.scrollToPosition(items.size-1)

    }
}