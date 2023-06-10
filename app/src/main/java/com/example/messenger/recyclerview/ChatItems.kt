package com.example.messenger.recyclerview

import android.content.ClipData
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.messenger.R


class ChatItems : Item{




    override fun getLayout() = R.layout.recycler_view_item

    override fun bind(viewHolder : ViewHolder, position: Int) {

    }


}




/*
import android.content.ClipData
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.messenger.R


class ChatItems : ClipData.Item() {

    override fun bind(viewHolder : ViewHolder, position : Int) {

     }

    override fun getLayout()  = R.layout.recycler_view_item

}

 */



/*

   override fun bind(viewHolder : ViewHolder , position : Int) {

    }
        override fun getLayout()  =R.layout.recycler_view_item

 */