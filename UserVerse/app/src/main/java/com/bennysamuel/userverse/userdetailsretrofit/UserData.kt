package com.bennysamuel.userverse.userdetailsretrofit

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("results")
    val results: List<User>
)

data class User(
    @SerializedName("gender")
    val gender: String,

    @SerializedName("name")
    val name: Name,

    @SerializedName("location")
    val location: Location,

    @SerializedName("email")
    val email: String,

    @SerializedName("dob")
    val dob: Dob,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("cell")
    val cell: String,

    @SerializedName("picture")
    val picture: Picture,

    @SerializedName("nat")
    val nat: String
)

data class Name(
    @SerializedName("title")
    val title: String,

    @SerializedName("first")
    val first: String,

    @SerializedName("last")
    val last: String
)

data class Location(
    @SerializedName("city")
    val city: String,

    @SerializedName("state")
    val state: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("postcode")
    val postcode: String,

    @SerializedName("coordinates")
    val coordinates: Coordinates
)

data class Coordinates(
    @SerializedName("latitude")
    val latitude: String,

    @SerializedName("longitude")
    val longitude: String
)

data class Dob(
    @SerializedName("date")
    val date: String,

    @SerializedName("age")
    val age: String
)

data class Picture(
    @SerializedName("large")
    val large: String,

    @SerializedName("medium")
    val medium: String,

    @SerializedName("thumbnail")
    val thumbnail: String
)

data class DualUser(
    val user1: User,
    val user2: User,
    val background1: Int,
    val background2: Int,
    val u1Visible: Boolean,
    val u2Visible: Boolean
)


data class StaggerdUser(
    val user1: User
)

data class ArrayListOfData(
    val data: ArrayList<User>
)


