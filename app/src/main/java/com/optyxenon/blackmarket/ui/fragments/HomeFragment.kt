package com.optyxenon.blackmarket.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.optyxenon.blackmarket.R
import com.optyxenon.blackmarket.databinding.FragmentHomeBinding
import com.optyxenon.blackmarket.ui.adapters.CategoriesAdapter
import com.optyxenon.blackmarket.ui.adapters.HorizontalAdapter
import com.optyxenon.blackmarket.ui.adapters.SliderAdapter
import com.optyxenon.blackmarket.ui.viewModels.MainViewModel
import com.optyxenon.blackmarket.utilities.Constants
import com.optyxenon.blackmarket.utilities.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {
    private val TAG = HomeFragment::class.java.name

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: MainViewModel by viewModels()

    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var featuredAdapter: HorizontalAdapter
    private lateinit var bestAdapter: HorizontalAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        checkOnBoardingState()
        checkAuthenticationState()

        setupAdapters()

        binding.apply {
            homeRecyclerFeatured.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            homeRecyclerBest.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            homeRecyclerCategories.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

            homePager.adapter = sliderAdapter
            homeRecyclerCategories.adapter = categoriesAdapter
            homeRecyclerFeatured.adapter = featuredAdapter
            homeRecyclerBest.adapter = bestAdapter

            TabLayoutMediator(homeTabDots, homePager) { tab, position -> }.attach()

            homePager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // save position
                    viewModel.sliderCurrentPage = position
                }
            })

            homeImgMenu.setOnClickListener(this@HomeFragment)
            homeImgCart.setOnClickListener(this@HomeFragment)
            homeTxtFeaturedMore.setOnClickListener(this@HomeFragment)
            homeTxtBestMore.setOnClickListener(this@HomeFragment)
        }

        getData()

        return binding.root
    }

    private fun setupSlider() {
        val handler = Handler(Looper.getMainLooper())

        handler.post(object : Runnable {
            override fun run() {
                binding.homePager.doOnAttach {
                    Log.d(
                        "*************",
                        "current page is : ${viewModel.sliderCurrentPage} and item count is : ${sliderAdapter.itemCount}"
                    )
                    binding.homePager.setCurrentItem(viewModel.sliderCurrentPage, true)
                }

                viewModel.sliderCurrentPage = viewModel.sliderCurrentPage + 1
                // check for last item
                if (viewModel.sliderCurrentPage >= sliderAdapter.itemCount)
                    viewModel.sliderCurrentPage = 0

                handler.postDelayed(this, Constants.HOME_SLIDER_DELAY)
            }
        })
    }

    private fun setupAdapters() {
        sliderAdapter = SliderAdapter()

        categoriesAdapter = CategoriesAdapter(CategoriesAdapter.OnClickListener { position, item ->
            // TODO item click listener
        })
        featuredAdapter = HorizontalAdapter(HorizontalAdapter.OnClickListener { position, item ->
            // TODO item click listener
        })
        bestAdapter = HorizontalAdapter(HorizontalAdapter.OnClickListener { position, item ->
            // TODO item click listener
        })
    }

    private fun getData() {
        // Banners
        viewModel.banners.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    // TODO Do shimmer effect
                }

                is Resource.Success -> {
                    sliderAdapter.submitList(it.data)
                    setupSlider()
                }

                is Resource.Error -> {

                }
            }
        })

        // Categories
        viewModel.categories.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    // TODO Do shimmer effect
                }

                is Resource.Success -> {
                    categoriesAdapter.submitList(it.data)
                }

                is Resource.Error -> {

                }
            }
        })

        // Featured Products
        viewModel.featuredProducts.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    Log.d(TAG, "Loading")
                    // TODO Do shimmer effect
                }

                is Resource.Success -> {
                    featuredAdapter.submitList(it.data)
                    for (product in it.data!!) {
                        Log.d(TAG, product.name)
                    }
                }

                is Resource.Error -> {
                    Log.d(TAG, it.message!!)
                }
            }
        })

        // Best Sell Products
        viewModel.bestSellProducts.observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    // TODO Do shimmer effect
                }

                is Resource.Success -> {
                    bestAdapter.submitList(it.data)
                }

                is Resource.Error -> {

                }
            }
        })
    }

    override fun onClick(v: View) {
        when (v) {
            binding.homeImgMenu -> {
                val action = HomeFragmentDirections.actionHomeFragmentToMenuFragment()
                val navController = NavHostFragment.findNavController(this)
                val navDestination = navController.currentDestination
                if (navDestination != null && navDestination.id == R.id.homeFragment) navController.navigate(
                    action
                )
            }

            binding.homeTxtFeaturedMore -> {
                val action = HomeFragmentDirections.actionHomeFragmentToFeaturedFragment()
                val navController = NavHostFragment.findNavController(this)
                val navDestination = navController.currentDestination
                if (navDestination != null && navDestination.id == R.id.homeFragment) navController.navigate(
                    action
                )
            }

            binding.homeTxtBestMore -> {
                val action = HomeFragmentDirections.actionHomeFragmentToBestSellFragment()
                val navController = NavHostFragment.findNavController(this)
                val navDestination = navController.currentDestination
                if (navDestination != null && navDestination.id == R.id.homeFragment) navController.navigate(
                    action
                )
            }

            binding.homeImgCart -> {
                val action = HomeFragmentDirections.actionHomeFragmentToCartFragment()
                val navController = NavHostFragment.findNavController(this)
                val navDestination = navController.currentDestination
                if (navDestination != null && navDestination.id == R.id.homeFragment) navController.navigate(
                    action
                )
            }

        }
    }

    private fun checkOnBoardingState() {
        Log.d(TAG, "check onboarding ${viewModel.onBoardingState}")
        // Check if we need to display our OnBoarding Fragment
        if (!viewModel.onBoardingState) {
            // The user hasn't seen the OnBoardingFragment yet, so show it
            Log.d(TAG, "The user hasn't seen the OnBoardingFragment yet, so show it")

            openOnBoardingFragment()
        }
    }

    private fun checkAuthenticationState() {
        // Check if the user didn't sign in
        if (viewModel.firebaseAuth.currentUser == null ||
            viewModel.firebaseAuth.currentUser?.isEmailVerified == false
        ) {

            // The user hasn't signed in , so WelcomeFragment
            openWelcomeFragment()

        }
    }

    private fun openWelcomeFragment() {
        val action =
            HomeFragmentDirections.actionHomeFragmentToEntryFragments()
        val navController = NavHostFragment.findNavController(this)
        val navDestination = navController.currentDestination
        if (navDestination != null && navDestination.id == R.id.homeFragment) navController.navigate(
            action
        )
    }

    private fun openOnBoardingFragment() {
        Log.d(TAG, "opening onBoarding Fragment")
        val action =
            HomeFragmentDirections.actionHomeFragmentToOnBoardingFragment()
        val navController = NavHostFragment.findNavController(this)
        val navDestination = navController.currentDestination
        if (navDestination != null && navDestination.id == R.id.homeFragment) {
            Log.d(TAG, "open onBoarding")
            navController.navigate(
                action
            )
        }
    }
}