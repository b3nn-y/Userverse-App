package com.bennysamuel.userverse.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bennysamuel.userverse.R
import com.bennysamuel.userverse.animation.startAnimation
import com.bennysamuel.userverse.databinding.FragmentHomeScreenBinding
import com.bennysamuel.userverse.homescreenrecyclerview.HomeScreenRecyclerViewAdapter
import com.bennysamuel.userverse.userdetailsretrofit.ArrayListOfData
import com.bennysamuel.userverse.userdetailsretrofit.DualUser
import com.bennysamuel.userverse.userdetailsretrofit.User
import com.bennysamuel.userverse.viewmodels.UserDataViewModel
import com.bennysamuel.userverse.viewmodels.UserDataViewModelFactory
import com.bennysamuel.weatherify.weatherApi.WeatherData
import com.bennysamuel.whatsmynextcountry.database.UserDatabase
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.launch


class HomeScreenFragment: Fragment() {

    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var viewModel: UserDataViewModel
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var recyclerView: RecyclerView
    private lateinit var userDataList: ArrayList<DualUser>
    private lateinit var homeScreenRecyclerViewAdapter: HomeScreenRecyclerViewAdapter
    private lateinit var animation:Animation
    private var isButtonVisible = false
    private var userCount = 26

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDatabaseDAO
        val viewModelFactory = UserDataViewModelFactory(dataSource,application)
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(UserDataViewModel::class.java)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        animation = AnimationUtils.loadAnimation(context, R.anim.background_explosion_animation).apply {
            duration = 900
            interpolator = AccelerateDecelerateInterpolator()
        }



        binding.changelayout.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_staggerdHomeFragment)
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())



        binding.searchBar.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                println("submitted")
                binding.searchBar.clearFocus()
                sortArrayAndGenRecyclerView(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                println("Text is changing")
                sortArrayAndGenRecyclerView(newText)
                return false
            }



        })

        if (!checkLocationPermission()){
            binding.weatherImage.setImageResource(R.drawable.nolocation)
            binding.placeName.text = "No Location"
            binding.forecast.text = "---"
            binding.temperature.text = "--deg"

        }

        binding.loadMore.setOnClickListener{
            lifecycleScope.launch {
                binding.loadMore.visibility = View.GONE
                viewModel.getUserInfo(userCount)
                homeScreenRecyclerViewAdapter.updateList(viewModel.apiDataList)
            }
        }

        binding.weatherImage.setOnClickListener{
            getLocation()
//            fetchLocation()
        }



        if (isInternetAvailable()){
            lifecycleScope.launch {
                makeApiCallToGetUserData()
                if (!viewModel.checkIfDataIsAvailableInDB()){
                    println("Data is saved")
                    var outputList: ArrayList<User> = ArrayList()
                    for (i in viewModel.apiDataListStaggerd){
                        outputList.add(i.user1)
                    }
                    viewModel.insertDataIntoDataBase(ArrayListOfData(outputList))
                }
            }

            if (viewModel.currentWeather != null){
                fillCurrentWeather(viewModel.currentWeather!!)
            }
            else{
                fetchLocation()
            }


        }
        else{
            println("No internet")
            binding.weatherImage.setImageResource(R.drawable.nolocation)
            binding.placeName.text = "No Internet"
            binding.forecast.text = "---"
            binding.temperature.text = "--deg"
            lifecycleScope.launch{
                if (viewModel.checkIfDataIsAvailableInDB()){
                    var a = viewModel.getDataFromDB()
                    println(a)
                    var dataToLoad = (Gson().fromJson(a, ArrayListOfData::class.java))
                    viewModel.apiDataList = viewModel.createRecyclerList(dataToLoad.data)
                    viewModel.apiDataListStaggerd =  viewModel.createStaggerdRecyclerList(dataToLoad.data)
                    println(dataToLoad.data::class.java)
                    genRecyclerView()


                }
                else{
                    binding.forecast.text = "No Data In DB"
                }

            }

        }


    }

    suspend fun makeApiCallToGetUserData(){
        if (viewModel.apiDataList.size < 10){
            viewModel.getUserInfo(userCount)
            genRecyclerView()
        }
        else{
            genRecyclerView()
        }
    }

    fun genRecyclerView(){
        homeScreenRecyclerViewAdapter = HomeScreenRecyclerViewAdapter(viewModel.apiDataList)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = homeScreenRecyclerViewAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (lastVisibleItemPosition == totalItemCount - 1 && !isButtonVisible) {
                    if (totalItemCount > 10){
                        binding.loadMore?.visibility = View.VISIBLE
                        isButtonVisible = true
                    }
                } else if (lastVisibleItemPosition < totalItemCount - 1 && isButtonVisible) {
                    binding.loadMore?.visibility = View.GONE
                    isButtonVisible = false
                }
            }
        })



        homeScreenRecyclerViewAdapter.onItemClick = {

            lifecycleScope.launch {
                viewModel.detailedFragmentUser = it
                println(it)
                binding.animBack?.isVisible = false
                binding.topBar?.isVisible = false
                binding.searchBar.isVisible = false
                binding.recyclerView.isVisible = false
                binding.animBack?.startAnimation(animation){
                    findNavController().navigate(R.id.action_homeScreenFragment_to_userDetailsFragment)
                }
            }

        }

        homeScreenRecyclerViewAdapter.onItemClick2 = { user, visible ->
            if (visible){
                lifecycleScope.launch{
                    println(user)
                    viewModel.detailedFragmentUser = user
                    println(viewModel.detailedFragmentUser)
                    binding.animBack?.isVisible = false
                    binding.topBar?.isVisible = false
                    binding.searchBar.isVisible = false
                    binding.recyclerView.isVisible = false
                    binding.animBack?.startAnimation(animation){
                        findNavController().navigate(R.id.action_homeScreenFragment_to_userDetailsFragment)
                    }

                }
            }
        }


    }

    fun sortArrayAndGenRecyclerView(query: String?){
        if (query == null || query == ""){
            genRecyclerView()
        }

        else{
            var tempList: ArrayList<DualUser> = viewModel.apiDataList
            var sortedUsersByStartingLetters: ArrayList<User> = ArrayList()
            var sortedUsersByContains: ArrayList<User> = ArrayList()
            for (i in tempList){
                if ((i.user1.name.first + " " + i.user1.name.last).lowercase().startsWith(query, true)){
                    sortedUsersByStartingLetters.add(i.user1)
                }

                if ((i.user2.name.first + " " + i.user2.name.last).lowercase().startsWith(query, true)){
                    sortedUsersByStartingLetters.add(i.user2)
                }

                if ((i.user1.name.title + ". " + i.user1.name.first + " " + i.user1.name.last).lowercase().contains(query, true)){
                    if (!sortedUsersByStartingLetters.contains(i.user1)){
                        sortedUsersByContains.add(i.user1)
                    }
                }

                if ((i.user2.name.title + ". " + i.user2.name.first + " " + i.user2.name.last).lowercase().contains(query,true)){
                    if (!sortedUsersByStartingLetters.contains(i.user2)){
                        sortedUsersByContains.add(i.user2)
                    }
                }

            }

            for (i in sortedUsersByContains){
                sortedUsersByStartingLetters.add(i)
            }
            println(sortedUsersByStartingLetters.size)

            if (query.length > 0){
                homeScreenRecyclerViewAdapter.updateList(viewModel.createRecyclerList(sortedUsersByStartingLetters))
            }
            else{
                homeScreenRecyclerViewAdapter.updateList(viewModel.apiDataList)
            }


        }

    }
    fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }




    fun getLocation() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION

        if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            if (true) {
                // Show a rationale dialog explaining why the permission is needed
                AlertDialog.Builder(requireContext())
                    .setTitle("Location Permission Needed")
                    .setMessage("We need your location to show the weather in your location.")
                    .setPositiveButton("OK") { _, _ ->
                        requestLocationPermission()
                    }
                    .setNegativeButton("Cancel") { _, _ ->
                        // Handle denial or show a message
                        showToast("Location permission denied.")
                    }
                    .create()
                    .show()
            } else {
                requestLocationPermission()
            }
        } else {
            showToast("Fetching Current Weather if internet is available.")
            // Permission already granted, get location
            fetchLocation()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            100
        )
    }

    private fun fetchLocation() {
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location ->
                    // Got last known location. In some situations, this can be null.
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        println(latitude.toString() + ": Latitude")
                        println(longitude.toString() + ": Longitude")

                        lifecycleScope.launch {
                            var weather = viewModel.getWeather(""+latitude+","+longitude)
                            if (weather != null) {
                                viewModel.currentWeather = weather
                            }
                            if (weather != null) {
                                fillCurrentWeather(weather)
                            }


                        }
                        // Use latitude and longitude as needed
//                        showToast("Latitude: $latitude, Longitude: $longitude")
                    }
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    showToast("Error fetching location: ${e.message}")
                }

        }
        else{
            showToast("Location permission not granted")
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun fillCurrentWeather(weather: WeatherData){
        binding.temperature.text = weather.current.tempC.toString() + "deg"
        binding.forecast.text = weather.current.condition.text
        binding.placeName.text = weather.location.name
        when (weather?.current!!.condition.code) {
            in 1000..1002 -> binding.weatherImage.setImageResource(R.drawable.sun)
            in 1114..1119 -> binding.weatherImage.setImageResource(R.drawable.blizzard)
            in 1180..1198 -> binding.weatherImage.setImageResource(R.drawable.rain)
            1072 -> binding.weatherImage.setImageResource(R.drawable.overcast)
            in 1273..1282 -> binding.weatherImage.setImageResource(R.drawable.thunder)
            1003, 1006 -> binding.weatherImage.setImageResource(R.drawable.cloudy2)
            else -> binding.weatherImage.setImageResource(R.drawable.sun)
        }

        println(weather.current.condition.icon)
        println(weather.current.condition.text)
    }

    @SuppressLint("ServiceCast")
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}