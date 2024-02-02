package com.bennysamuel.userverse.homescreenrecyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bennysamuel.userverse.R
import com.bennysamuel.userverse.userdetailsretrofit.DualUser
import com.bennysamuel.userverse.userdetailsretrofit.User
import com.bumptech.glide.Glide

class HomeScreenRecyclerViewAdapter (private var userDataList: ArrayList<DualUser>): RecyclerView.Adapter<HomeScreenRecyclerViewAdapter.UserViewHolder>(){
    var onItemClick: ((User) -> Unit)? = null
    var onItemClick2: ((User, Boolean) -> Unit)? = null


    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profileImg1: ImageView = itemView.findViewById(R.id.profileImg1)
        val profileImg2: ImageView = itemView.findViewById(R.id.profileImg2)
        val flagImage1: ImageView = itemView.findViewById(R.id.flagImage1)
        val flagImage2: ImageView = itemView.findViewById(R.id.flagImage2)
        val user1Name: TextView = itemView.findViewById(R.id.userName1)
        val user2Name: TextView = itemView.findViewById(R.id.userName2)
        val email1: TextView = itemView.findViewById(R.id.email1)
        val email2: TextView = itemView.findViewById(R.id.email2)
        val phone1: TextView = itemView.findViewById(R.id.phone1)
        val phone2: TextView = itemView.findViewById(R.id.phone2)
        val user1Back: ConstraintLayout = itemView.findViewById(R.id.user1)
        val user2Back: ConstraintLayout = itemView.findViewById(R.id.user2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userDataList.size
    }

    override fun getItemViewType(position: Int): Int  = position

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        var data = userDataList[position]
        var flagLink1 = "https://flagcdn.com/w320/${data.user1.nat.lowercase()}.png"
        var flagLink2 = "https://flagcdn.com/w320/${data.user2.nat.lowercase()}.png"
        Glide.with(holder.itemView.context)
            .load(flagLink1)
            .error(R.drawable.error)
            .placeholder(R.drawable.load)
            .into(holder.flagImage1)

        Glide.with(holder.itemView.context)
            .load(flagLink2)
            .error(R.drawable.error)
            .placeholder(R.drawable.load)
            .into(holder.flagImage2)

        Glide.with(holder.itemView.context)
            .load(data.user1.picture.large)
            .error(R.drawable.error)
            .placeholder(R.drawable.load)
            .into(holder.profileImg1)


        Glide.with(holder.itemView.context)
            .load(data.user2.picture.large)
            .error(R.drawable.error)
            .placeholder(R.drawable.load)
            .into(holder.profileImg2)

        holder.user1Name.text = data.user1.name.title + ". " + data.user1.name.first + " " + data.user1.name.last
        holder.user2Name.text = data.user2.name.title + ". " + data.user2.name.first + " " + data.user2.name.last

        holder.email1.text = data.user1.email
        holder.email2.text = data.user2.email

        holder.phone1.text = data.user1.cell
        holder.phone2.text = data.user2.cell

        holder.user1Back.setBackgroundResource(data.background1)
        holder.user2Back.setBackgroundResource(data.background2)

        holder.user1Back.setOnClickListener {
            onItemClick?.invoke(data.user1)
        }

        holder.user2Back.setOnClickListener {
            onItemClick2?.invoke(data.user2, data.u2Visible)
        }

        if (!data.u2Visible){
            holder.user2Back.visibility = View.INVISIBLE
        }
    }

    fun updateList(newList: ArrayList<DualUser>){
        userDataList = newList
        notifyDataSetChanged()
    }

}