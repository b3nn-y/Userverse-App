package com.bennysamuel.userverse.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bennysamuel.userverse.R
import com.bennysamuel.userverse.animation.startAnimation
import com.bennysamuel.userverse.databinding.FragmentUserDetailsBinding
import com.bennysamuel.userverse.viewmodels.UserDataViewModel
import com.bennysamuel.userverse.viewmodels.UserDataViewModelFactory
import com.bennysamuel.whatsmynextcountry.database.UserDatabase
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch


class UserDetailsFragment: Fragment() {
    private lateinit var binding: FragmentUserDetailsBinding
    private lateinit var viewModel: UserDataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserDetailsBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = UserDatabase.getInstance(application).userDatabaseDAO
        val viewModelFactory = UserDataViewModelFactory(dataSource,application)
        viewModel = ViewModelProvider(requireActivity(),viewModelFactory).get(UserDataViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var animation = AnimationUtils.loadAnimation(context, R.anim.background_explosion_animation).apply {
            duration = 100
            interpolator = AccelerateDecelerateInterpolator()
        }
        binding.back.setOnClickListener {
            binding.userAnimation?.isVisible = false
            binding.userAnimation?.startAnimation(animation){
                findNavController().navigate(R.id.action_userDetailsFragment_to_homeScreenFragment)
            }
        }
        lifecycleScope.launch {
            fillUserDetails()
        }
    }

    suspend fun fillUserDetails() {

        try{
            val data = viewModel.detailedFragmentUser

            Glide.with(this)
                .load(viewModel.detailedFragmentUser.picture.large)
                .error(R.drawable.error)
                .placeholder(R.drawable.load)
                .into(binding.userImage)


            binding.age.text = "Age: " + data.dob.age
            binding.dob.text = data.dob.date.subSequence(0, 10)
            binding.personName.text = data.name.title + ". " + data.name.first + " " + data.name.last
            binding.emailText.text = data.email
            binding.location.text =
                data.location.city + ", " + data.location.state + ", " + data.location.country
            binding.phoneNumber.text = data.cell
            var weather = viewModel.getWeather(data.location.coordinates.latitude + "," + data.location.coordinates.longitude)

            when (weather?.current!!.condition.code) {
                in 1000..1002 -> binding.weatherImage2.setImageResource(R.drawable.sun)
                in 1114..1119 -> binding.weatherImage2.setImageResource(R.drawable.blizzard)
                in 1180..1198 -> binding.weatherImage2.setImageResource(R.drawable.rain)
                1072 -> binding.weatherImage2.setImageResource(R.drawable.overcast)
                in 1273..1282 -> binding.weatherImage2.setImageResource(R.drawable.thunder)
                1003, 1006 -> binding.weatherImage2.setImageResource(R.drawable.cloudy2)
                else -> binding.weatherImage2.setImageResource(R.drawable.sun)
            }

            if ((weather?.current!!.tempC.toInt() < 3)){
                binding.weatherImage2.setImageResource(R.drawable.cold)
            }


            if (weather != null) {
                binding.temperature2.text = weather.current.tempC.toInt().toString() + "°"
                binding.humidity2.text = "Humidity: " + weather.current.humidity.toString()
            }
        }
        catch (e:Exception){
            println(e.message)
        }




    }
}