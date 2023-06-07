package com.example.flickrbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSION_ERROR, ERROR
}

class GetRawData : AsyncTask<String,Void,String>()  {
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    private var listener : MainActivity? =null
    fun downloadCompleteListener (callbackObject : MainActivity){
        listener = callbackObject
    }

    override fun doInBackground(vararg params: String?): String {
        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No URL specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground : Invalid URL ${e.message}"
                }

                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground :IO Exception reading data ${e.message}"
                }

                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSION_ERROR
                    "doInBackground : SecurityException  ${e.message}"
                }

                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "Unknown error : ${e.message}"
                }


            }
            Log.e(TAG,errorMessage)
            println(errorMessage)

            return errorMessage

        }
    }

    override fun onPostExecute(result: String) {
        Log.d(TAG,"onPostMethodCalled")
        listener?.onDownloadComplete(result,downloadStatus)

    }


}