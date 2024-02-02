package com.bennysamuel.userverse.staggerdRecyclerView



import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.bennysamuel.userverse.R
import com.bennysamuel.userverse.userdetailsretrofit.DualUser
import com.bennysamuel.userverse.userdetailsretrofit.StaggerdUser
import com.bennysamuel.userverse.userdetailsretrofit.User
import com.bumptech.glide.Glide

class StaggerdRecyclerViewAdapter (private var userDataList: ArrayList<StaggerdUser>): RecyclerView.Adapter<StaggerdRecyclerViewAdapter.StaggerdViewHolder>(){
    var onItemClick: ((User) -> Unit)? = null
    var onItemClick2: ((User, Boolean) -> Unit)? = null


    class StaggerdViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val singleUser: ConstraintLayout = itemView.findViewById(R.id.singleUser)

        init {

            val displayMetrics = DisplayMetrics()
            val display = itemView.context.resources.displayMetrics
            val screenHeight = display.heightPixels
            val cardHeight = screenHeight / 2

            val constraintSet = ConstraintSet()
            constraintSet.clone(singleUser)
            constraintSet.constrainHeight(R.id.singleUser, cardHeight)
            constraintSet.applyTo(singleUser)
        }


        val staggerdName: TextView = itemView.findViewById(R.id.smallUserName)
        val staggerdImage: ImageView = itemView.findViewById(R.id.profImage)
        val staggerdName2: TextView = itemView.findViewById(R.id.smallUserName2)
        val staggerdImage2: ImageView = itemView.findViewById(R.id.profImage2)
        val staggerdEmail: TextView = itemView.findViewById(R.id.staggerdEmail)
        val staggerdPhone: TextView = itemView.findViewById(R.id.staggerdPhone)
        val staggerdFlag: ImageView = itemView.findViewById(R.id.staggerdFlag)
        val bigUser: ConstraintLayout = itemView.findViewById(R.id.bigUser)
        val smallUser: ConstraintLayout = itemView.findViewById(R.id.smallUser)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaggerdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_card_layout, parent, false)
        return StaggerdViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userDataList.size
    }

    override fun getItemViewType(position: Int): Int  = position

    override fun onBindViewHolder(holder: StaggerdViewHolder, position: Int) {
        var data = userDataList[position]
        var flagLink1 = "https://flagcdn.com/w320/${data.user1.nat.lowercase()}.png"
        Glide.with(holder.itemView.context)
            .load(flagLink1)
            .error(R.drawable.error)
            .placeholder(R.drawable.load)
            .into(holder.staggerdFlag)


        Glide.with(holder.itemView.context)
            .load(data.user1.picture.large)
            .error(R.drawable.error)
            .placeholder(R.drawable.load)
            .into(holder.staggerdImage)


        Glide.with(holder.itemView.context)
            .load(data.user1.picture.large)
            .error(R.drawable.error)
            .placeholder(R.drawable.load)
            .into(holder.staggerdImage2)


        holder.staggerdName.text = data.user1.name.title + ". " + data.user1.name.first + " " + data.user1.name.last
        holder.staggerdName2.text = data.user1.name.title + ". " + data.user1.name.first + " " + data.user1.name.last

        holder.staggerdEmail.text = data.user1.email

        holder.staggerdPhone.text = data.user1.cell


        holder.smallUser.setOnClickListener {
            onItemClick?.invoke(data.user1)
        }
        holder.bigUser.setOnClickListener {
            onItemClick?.invoke(data.user1)
        }

        if (position % 2 == 0){
            holder.smallUser.visibility = View.GONE
            holder.bigUser.setBackgroundResource(R.drawable.round_back_user1)
        }
        else{
            holder.bigUser.visibility = View.GONE
            holder.smallUser.setBackgroundResource(R.drawable.round_back_user2)
        }



    }

    fun updateStaggerdList(newList: ArrayList<StaggerdUser>){
        userDataList = newList
        notifyDataSetChanged()
    }

}