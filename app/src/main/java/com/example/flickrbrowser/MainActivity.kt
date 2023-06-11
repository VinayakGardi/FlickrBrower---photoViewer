package com.example.flickrbrowser

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flickrbrowser.databinding.ActivityMainBinding
import java.lang.Exception


class MainActivity : AppCompatActivity() , GetFlickrJsonData.OnDataAvailable , RecyclerItemClickListener.OnRecyclerClickListener {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var flickrRecyclerViewAdapter = FlickrRecyclerViewAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "On create started")
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.addOnItemTouchListener(RecyclerItemClickListener(this,binding.recyclerView,this))
        binding.recyclerView.adapter = flickrRecyclerViewAdapter

        val getRawData = GetRawData()

        var url : String = createUri("https://api.flickr.com/services/feeds/photos_public.gne","actors","en-us",false)
        getRawData.downloadCompleteListener(this)

        getRawData.execute(url)
        Log.d(TAG, "On create ended")




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if(item.itemId == R.id.search_item){
           Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show()
       }
        if(item.itemId == R.id.app_exit) {
            Toast.makeText(this, "Exiting now", Toast.LENGTH_SHORT).show()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createUri(baseUrl : String , searchQueries : String , lang : String , matchAll : Boolean) : String{
         Log.d(TAG ,"Create uri starts")
        val uri = Uri.parse(baseUrl).buildUpon()
            .appendQueryParameter("tags",searchQueries)
            .appendQueryParameter("tagmode",if(matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang",lang)
            .appendQueryParameter("format","json")
            .appendQueryParameter("nojsoncallback","1").build()
        Log.d(TAG , "URL is $uri")

        return uri.toString()

    }

    fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "Downloaded successfully $data")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            Log.d(TAG, "Download failed $status error message $data")
        }
    }

    override fun onDataAvailable(data: List<photo>) {
        Log.d(TAG ,"onDataAvailable called $data")
      flickrRecyclerViewAdapter.loadNewData(data)
        Log.d(TAG,"onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG,"Error is ${exception.message}")
    }

    override fun onItemClicked(view: View, position: Int) {
        Log.d(TAG , "onItemClicked starts")
        Toast.makeText(this, "normal tap position $position", Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongCLicked(view: View, position: Int) {
        Log.d(TAG , "onItemLongCLicked starts")
        Toast.makeText(this, "long tap position $position", Toast.LENGTH_SHORT).show()
    }
}






