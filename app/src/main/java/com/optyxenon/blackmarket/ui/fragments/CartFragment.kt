package com.optyxenon.blackmarket.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.optyxenon.blackmarket.R
import com.optyxenon.blackmarket.databinding.FragmentCartBinding
import com.optyxenon.blackmarket.ui.viewModels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartFragment : Fragment() {
    private val TAG = CartFragment::class.java.name

    private lateinit var binding: FragmentCartBinding
    private val viewModel: MainViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false)

        binding.featuredImgBack.setOnClickListener { requireActivity().onBackPressed() }

        return binding.root
    }
}