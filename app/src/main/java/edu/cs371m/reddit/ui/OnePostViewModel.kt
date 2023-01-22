package edu.cs371m.reddit.ui

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.cs371m.reddit.glide.Glide

class OnePostViewModel : ViewModel() {

    private var title = MutableLiveData<String>()
    private var selfText = MutableLiveData<String>()
    private var imageUrl = MutableLiveData<String>()
    private var fallbackImageUrl = MutableLiveData<String>()

    private val paramsUpdated = MutableLiveData<Boolean>()


    fun setParams(x1: String, x2: String, x3: String, x4: String) {

        title.value = x1
        selfText.value = x2
        imageUrl.value = x3
        fallbackImageUrl.value = x4

        paramsUpdated.postValue(true)

    }

    fun getTitle() : LiveData<String> {
        return title
    }

    fun getSelfText() : LiveData<String> {
        return selfText
    }

    fun getImgUrl() : LiveData<String> {
        return imageUrl
    }

    fun getFallbackImgUrl() : LiveData<String> {
        return fallbackImageUrl
    }



    fun observeParamsUpdated() : LiveData<Boolean> {
        return paramsUpdated
    }


    fun downloadImage(urlImg:String, fallback : String, imageView: ImageView) {
       // Glide.fetch(urlImg, R.drawable.ic_cloud_download_black_24dp, imageView)
        Glide.fetch(urlImg, fallback, imageView)
    }


}