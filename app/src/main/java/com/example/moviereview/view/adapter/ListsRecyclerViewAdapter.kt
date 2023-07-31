package com.example.moviereview.view.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.moviereview.R
import com.example.moviereview.db.local.entities.*
import com.example.moviereview.db.local.viewmodel.FavListsViewModel
import com.example.moviereview.db.local.viewmodel.LikedListViewModel
import com.example.moviereview.db.local.viewmodel.UsersViewModel
import com.example.moviereview.utils.HelperFunction
import com.example.moviereview.view.list_screen_clean.presentation.view.ListActivity
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.*

class ListsRecyclerViewAdapter(var context: Context,val viewOwner: ViewModelStoreOwner,val lifeOwner:LifecycleOwner) :
    RecyclerView.Adapter<ListsRecyclerViewAdapter.MyViewHolder>() {

    private lateinit var listsLog : java.util.function.Function<Int,Int>
    private var lists : ArrayList<Lists> = arrayListOf()
    private var listMovies: ArrayList<List<ListMovies>> = arrayListOf()

    lateinit var usersViewModel:UsersViewModel
    lateinit var favListsViewModel: FavListsViewModel
    lateinit var likedListViewModel: LikedListViewModel
    lateinit var sharedPreferences: SharedPreferences

    private var email = ""
    class MyViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView){
        val listName = itemView.findViewById<TextView>(R.id.your_list_name)
        val ownerName = itemView.findViewById<TextView>(R.id.your_list_owner_name)
        val profile = itemView.findViewById<ShapeableImageView>(R.id.your_list_profile)
        val poster1 = itemView.findViewById<ImageView>(R.id.your_list_image1)
        val poster2 = itemView.findViewById<ImageView>(R.id.your_list_image2)
        val poster3 = itemView.findViewById<ImageView>(R.id.your_list_image3)
        val poster4 = itemView.findViewById<ImageView>(R.id.your_list_image4)
        val date = itemView.findViewById<TextView>(R.id.your_list_date)
        val count = itemView.findViewById<TextView>(R.id.your_list_number)
        val likeBtn = itemView.findViewById<ImageView>(R.id.your_list_like_iv)
        val likeCount = itemView.findViewById<TextView>(R.id.your_list_like_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var inflater: LayoutInflater = LayoutInflater.from(context)
        var view: View = inflater.inflate(R.layout.item_recyclerview_lists,parent,false)

        usersViewModel = ViewModelProvider(viewOwner).get(UsersViewModel::class.java)
        favListsViewModel = ViewModelProvider(viewOwner).get(FavListsViewModel::class.java)
        likedListViewModel = ViewModelProvider(viewOwner).get(LikedListViewModel::class.java)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        email = sharedPreferences.getString("email", "").toString()

        return ListsRecyclerViewAdapter.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentList = lists[position]
        val currentMovies = listMovies[position]

        if(currentMovies.size==0)
        {
            holder.poster1.scaleType = ImageView.ScaleType.FIT_CENTER

        }
        else{
            holder.poster1.scaleType =ImageView.ScaleType.FIT_XY
        }
        holder.poster1.clipToOutline = true
        holder.poster2.clipToOutline = true
        holder.poster3.clipToOutline = true
        holder.poster4.clipToOutline = true

        holder.listName.text = currentList.title
        holder.date.text = currentList.dateCreated
        if(currentMovies.size>=4) {
            holder.poster1.visibility = View.VISIBLE
            holder.poster2.visibility = View.VISIBLE
            holder.poster3.visibility = View.VISIBLE
            holder.poster4.visibility = View.VISIBLE

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(0).poster,holder.poster1)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(0).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster1.setImageBitmap(bitmap)
//                }
//            }

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(1).poster,holder.poster2)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(1).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster2.setImageBitmap(bitmap)
//                }
//            }

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(2).poster,holder.poster3)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(2).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster3.setImageBitmap(bitmap)
//                }
//            }

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(3).poster,holder.poster4)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(3).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster4.setImageBitmap(bitmap)
//                }
//            }
        }
        else if(currentMovies.size==3) {
            holder.poster1.visibility = View.VISIBLE
            holder.poster2.visibility = View.VISIBLE
            holder.poster3.visibility = View.VISIBLE

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(0).poster,holder.poster1)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(0).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster1.setImageBitmap(bitmap)
//                }
//            }

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(1).poster,holder.poster2)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(1).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster2.setImageBitmap(bitmap)
//                }
//            }

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(2).poster,holder.poster3)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(2).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster3.setImageBitmap(bitmap)
//                }
//            }
            holder.poster4.visibility = View.GONE
        }
        else if(currentMovies.size == 2)
        {
            holder.poster1.visibility = View.VISIBLE
            holder.poster2.visibility = View.VISIBLE

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(0).poster,holder.poster1)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(0).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster1.setImageBitmap(bitmap)
//                }
//            }

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(1).poster,holder.poster2)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(1).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster2.setImageBitmap(bitmap)
//                }
//            }
            holder.poster3.visibility = View.GONE
            holder.poster4.visibility = View.GONE
        }
        else if(currentMovies.size == 1)
        {
            holder.poster1.visibility = View.VISIBLE

            HelperFunction.loadImage(context,"https://image.tmdb.org/t/p/original/" + currentMovies.get(0).poster,holder.poster1)
//            CoroutineScope(Dispatchers.IO).launch {
//                val bitmap = HelperFunction.downloadImage("https://image.tmdb.org/t/p/original/" + currentMovies.get(0).poster)
//                withContext(Dispatchers.Main) {
//                    holder.poster1.setImageBitmap(bitmap)
//                }
//            }
            holder.poster2.visibility = View.GONE
            holder.poster3.visibility = View.GONE
            holder.poster4.visibility = View.GONE
        }
        else{
//            holder.poster1.scaleType = ImageView.ScaleType.FIT_XY

        }
        holder.count.text = currentMovies.size.toString()+" item(s)"
        usersViewModel.getUserByEmail(currentList.email)
        usersViewModel.userByEmail.observe(lifeOwner)
        {
            holder.profile.setImageBitmap(it.get(0).image)
            holder.ownerName.text = it.get(0).name
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ListActivity::class.java)
            intent.putExtra("listName",currentList.title)
            intent.putExtra("listId",currentList.listId.toLong())
            intent.putExtra("listDate",currentList.dateCreated)
            context.startActivity(intent)
        }

        var isLikedList= false

        likedListViewModel.getLikedListsByListId(currentList.listId)
        likedListViewModel.likedListsByListId.observe(lifeOwner)
        {
            holder.likeCount.text = it.size.toString()
            for (i in it)
            {
                if(i.email == email)
                {
                    holder.likeBtn.setImageResource(R.drawable.like_filled)
                    isLikedList = true
                    setUpLikeListBtn(holder.likeBtn,currentList,isLikedList)
                }
            }
        }
        setUpLikeListBtn(holder.likeBtn,currentList,isLikedList)

    }

    private fun setUpLikeListBtn(likeBtn: ImageView, currentList: Lists, isLikedList: Boolean)
    {
        var isLiked = isLikedList
        likeBtn.setOnClickListener {
            if(!isLiked)
            {
                val toast = Toast.makeText(context,"Liked", Toast.LENGTH_SHORT)
                HelperFunction.showToast(toast,context.resources)
                likedListViewModel.addLikedList(LikedLists(email,currentList.listId))
                likeBtn.setImageResource(R.drawable.like_filled)
                isLiked = true
            }
            else{
                val toast  = Toast.makeText(context,"Unliked", Toast.LENGTH_SHORT)
                HelperFunction.showToast(toast,context.resources)
                likedListViewModel.deleteLikedList(email,currentList.listId)
                likeBtn.setImageResource(R.drawable.heart)
                isLiked = false
            }
        }
    }
    override fun getItemCount(): Int {
        return lists.size
    }

    fun setNewData(listAndMovies:LinkedHashMap<Lists,HashSet<ListMovies>>)
    {
        lists.clear()
        listMovies.clear()
        for(i in listAndMovies.keys)
        {
//            Log.i("Lists",i.title)
            lists.add(i)
        }
        for(j in listAndMovies.values)
        {
            listMovies.add(j.toList())
        }

        notifyDataSetChanged()
    }
}