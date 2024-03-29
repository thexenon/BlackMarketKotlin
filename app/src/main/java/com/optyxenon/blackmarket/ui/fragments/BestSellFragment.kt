package com.optyxenon.blackmarket.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.optyxenon.blackmarket.R
import com.optyxenon.blackmarket.databinding.FragmentBestSellBinding
import com.optyxenon.blackmarket.ui.adapters.ProductsAdapter
import com.optyxenon.blackmarket.ui.viewModels.MainViewModel
import com.optyxenon.blackmarket.utilities.Resource
import com.optyxenon.blackmarket.utilities.VisualUtils.SpacingItemDecoration
import com.optyxenon.blackmarket.utilities.VisualUtils.calculateNoOfColumns
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BestSellFragment : Fragment() {
    private val TAG = BestSellFragment::class.java.name

    private lateinit var binding: FragmentBestSellBinding
    private lateinit var adapter: ProductsAdapter
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_best_sell, container, false)
        adapter = ProductsAdapter(ProductsAdapter.OnClickListener { position, item ->
            // TODO item click listener
        })

        binding.apply {
            bestRecycler.layoutManager = GridLayoutManager(
                requireActivity(), calculateNoOfColumns(
                    requireActivity(), 180f, 8
                )
            )

            bestRecycler.addItemDecoration(
                SpacingItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.grid_spacing),
                    calculateNoOfColumns(requireActivity(), 180f, 8)
                )
            )

            bestRecycler.adapter = adapter
            bestImgBack.setOnClickListener { requireActivity().onBackPressed() }
        }




        return binding.root
    }

    override fun onStart() {
        super.onStart()
        getBestSellList()
    }

    private fun getBestSellList() {
        viewModel.bestSellProducts.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    // TODO Do shimmer effect
                }

                is Resource.Success -> {
                    adapter.submitList(it.data)
                }

                is Resource.Error -> {

                }
            }
        })
    }
}