package com.example.moviereview.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import java.net.URL

class HelperFunction {
    companion object
    {
        fun isValidEmail(email: String): Boolean {
            Log.i("valididate",
                email
            )
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun showToast(toast: Toast,resources:Resources)
        {
//            val view : View? = toast.view
//            view?.getBackground()?.setColorFilter(resources.getColor(R.color.manga), PorterDuff.Mode.SRC_IN);
//            val text = view!!.findViewById<View>(android.R.id.message) as TextView
//            text.setTextColor(resources.getColor(R.color.white))
            toast.show()
        }
        fun loadImage(context : Context,url: String,imageView: ImageView)
        {
            Glide.with(context).load(url).into(imageView)
        }
        fun downloadImage(imageUrl: String): Bitmap? {
            return try {
                val conn = URL(imageUrl).openConnection()
//                conn.setRequestProperty("Connection", "close");
//                System.setProperty("http.keepAlive", "false");
//                System.setProperty("java.net.preferIPv4Stack" , "true");
//                conn.useCaches = true;
//                conn.connectTimeout = 5000; // Set a reasonable timeout value

                conn.connect()
                val inputStream = conn.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()
                bitmap
            } catch (e: Exception) {
                Log.e("ImageDownload", "Exception $e")
                null
            }
        }
    }
}