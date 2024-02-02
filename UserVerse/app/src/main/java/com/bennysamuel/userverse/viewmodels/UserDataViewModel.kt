package com.bennysamuel.userverse.viewmodels
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.bennysamuel.userverse.R
import com.bennysamuel.userverse.userdetailsretrofit.ArrayListOfData
import com.bennysamuel.userverse.userdetailsretrofit.DualUser
import com.bennysamuel.userverse.userdetailsretrofit.RetrofitInstance
import com.bennysamuel.userverse.userdetailsretrofit.StaggerdUser
import com.bennysamuel.userverse.userdetailsretrofit.User
import com.bennysamuel.userverse.userdetailsretrofit.UserDataApiService
import com.bennysamuel.weatherify.weatherApi.WeatherApiService
import com.bennysamuel.weatherify.weatherApi.WeatherData
import com.bennysamuel.weatherify.weatherApi.WeatherRetroFitInstance
import com.bennysamuel.whatsmynextcountry.database.UserDataForTable
import com.bennysamuel.whatsmynextcountry.database.UserDatabaseDAO
import com.google.gson.Gson
import retrofit2.create

class UserDataViewModel(
    val dataBase: UserDatabaseDAO,
    application: Application
): AndroidViewModel(application) {
    var apiDataList: ArrayList<DualUser> = ArrayList()
    var apiDataListStaggerd: ArrayList<StaggerdUser> = ArrayList()
    var currentWeather: WeatherData? = null

    lateinit var detailedFragmentUser: User
    suspend fun getUserInfo(count:Int){
        val retrofitService = RetrofitInstance.getRestrofitInstance().create(UserDataApiService::class.java)
        val response = retrofitService.getUserData(count.toString())
        if (response.isSuccessful){
            println("GOT RESPONSE")
            var a = 0
            var tempNum = 0
            for (i in 0 until (response.body()?.results?.size!!)/2){
                var temp = response.body()!!.results
                var ind1 = a++
                var ind2 = a++
                apiDataListStaggerd.add(StaggerdUser(temp[ind1]))
                apiDataListStaggerd.add(StaggerdUser(temp[ind2]))
                if (tempNum == 0){

                    try {
                        apiDataList.add(DualUser(temp[ind1], temp[ind2], R.drawable.round_back_user1, R.drawable.round_back_user2, true, true))
                    }
                    catch (e: Exception){
                        apiDataList.add(DualUser(temp[ind1], temp[ind1], R.drawable.round_back_user1, R.drawable.round_back_user2, true, false))

                    }
                    tempNum = 1
                }

                else{
                    try {
                        apiDataList.add(DualUser(temp[ind1], temp[ind2], R.drawable.round_back_user2, R.drawable.round_back_user1, true, true))
                    }
                    catch (e: Exception){
                        apiDataList.add(DualUser(temp[ind1], temp[ind1], R.drawable.round_back_user2, R.drawable.round_back_user1, true, false))

                    }
                    tempNum = 0
                }
            }
            if (response.body()!!.results.size % 2 != 0){
                apiDataList.add(DualUser(response.body()!!.results[response.body()!!.results.size -1], response.body()!!.results[0], R.drawable.round_back_user2, R.drawable.round_back_user1, true, false))
                apiDataListStaggerd.add(StaggerdUser(response.body()!!.results[response.body()!!.results.size -1]))
            }
        }
        else{
            println(response.code())
        }

    }

    fun createRecyclerList(userList: ArrayList<User>): ArrayList<DualUser> {
        var outputList: ArrayList<DualUser> = ArrayList()
        var a = 0
        var tempNum = 0
        for (i in 0 until (userList.size!!)/2){
            if(tempNum == 0 ){
                outputList.add(DualUser(userList[a++], userList[a++], R.drawable.round_back_user1, R.drawable.round_back_user2, true, true))
                tempNum = 1
            }
            else{
                outputList.add(DualUser(userList[a++], userList[a++], R.drawable.round_back_user2, R.drawable.round_back_user1, true, true))
                tempNum = 0
            }
        }

        if (userList.size!! % 2 != 0){
            outputList.add(DualUser(userList[userList.size!! -1], userList[0] , R.drawable.round_back_user2, R.drawable.round_back_user1, true, false))
        }

        return outputList
    }



    fun createStaggerdRecyclerList(userList: ArrayList<User>): ArrayList<StaggerdUser> {
        var outputList: ArrayList<StaggerdUser> = ArrayList()
        var a = 0
        var tempNum = 0
        for (i in userList){
            outputList.add(StaggerdUser(i))
        }

        return outputList
    }

    suspend fun getWeather(q: String): WeatherData? {
        var retrofitInstance2 = WeatherRetroFitInstance.getRetrofitInstance().create(WeatherApiService::class.java)
        var response2 = retrofitInstance2.getWeather(q)

        if (response2.isSuccessful){
            println("Weather Data Received")
            return response2.body()
        }
        else{
            println(response2.code())
        }
        return null


    }


    suspend fun checkIfDataIsAvailableInDB(): Boolean {
        var data = dataBase.getData()
        if (data != null) {
            return true
        }
        else {
           return false
        }
    }


    fun insertDataIntoDataBase(data: ArrayListOfData){
        dataBase.insert(UserDataForTable(apiInfo = Gson().toJson(data)))
    }

    suspend fun getDataFromDB(): String? {
        var a = dataBase.getData()
        return a
    }


    //https://flagcdn.com/w640/ua.png

}