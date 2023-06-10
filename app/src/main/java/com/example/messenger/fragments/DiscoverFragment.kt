package com.example.messenger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.messenger.R


class DiscoverFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val textViewTitle = activity?.findViewById<TextView>(R.id.title_toolbar_textview)
        if (textViewTitle != null) {
            textViewTitle.text = "Discover"
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }


}