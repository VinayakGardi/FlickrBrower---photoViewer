package com.example.flickrbrowser

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.example.flickrbrowser.databinding.ActivityMainBinding
import java.lang.Exception


class MainActivity : AppCompatActivity() , GetFlickrJsonData.OnDataAvailable {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG, "On create started")
        val getRawData = GetRawData()

        var url : String = createUri("https://api.flickr.com/services/feeds/photos_public.gne","android,oreo","en-us",true)
        getRawData.downloadCompleteListener(this)

        getRawData.execute(url)
        Log.d(TAG, "On create ended")




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

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
            Log.d(TAG, "Downloaded successfully ")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            Log.d(TAG, "Download failed $status error message $data")
        }
    }

    override fun onDataAvailable(data: List<photo>) {
        Log.d(TAG ,"onDataAvailable called $data")

        Log.d(TAG,"onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG,"Error is ${exception.message}")
    }
}






