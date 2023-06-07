package com.example.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class GetFlickrJsonData(private val listener: OnDataAvailable) :
    AsyncTask<String, Void, ArrayList<photo>>() {

    private val TAG = "GetFlickrJsonData"

    interface OnDataAvailable {
        fun onDataAvailable(data: List<photo>)
        fun onError(exception: Exception)
    }

    override fun doInBackground(vararg params: String?): ArrayList<photo> {
        Log.d(TAG, "doInBackground started")

        val photoList = ArrayList<photo>()
        try{
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")
            for( i in 0 until  itemsArray.length()){
                val jsonPhoto = itemsArray.getJSONObject(i)
                val title  = jsonPhoto.getString("title")
                val author  = jsonPhoto.getString("author")
                val authorId  = jsonPhoto.getString("author_id")
                val tags  = jsonPhoto.getString("tags")
                val jsonMedia = jsonPhoto.getJSONObject("media")
                val photourl  = jsonMedia.getString("m")
                val link = photourl.replaceFirst("_m.jpg","_b.jpg")

                val photoObject = photo(title,author,authorId,link,tags,photourl)          // setting object for the photo class
                photoList.add(photoObject)
                Log.d(TAG,"doInBackground $photoObject")
            }
        }catch (e:JSONException){
          e.printStackTrace()
            Log.d(TAG,"doInBackground error processing json data ${e.message}")
            cancel(true)
            listener.onError(e)
        }
        Log.d(TAG,"doInBackground ends")
        return photoList
    }

    override fun onPostExecute(result: ArrayList<photo>) {
        Log.d(TAG, "onPostExecute started")
        super.onPostExecute(result)
        listener.onDataAvailable(result)
        Log.d(TAG ,"onPostExecute ends")
    }

}
