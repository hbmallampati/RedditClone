package edu.cs371m.reddit

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import edu.cs371m.reddit.databinding.ActivityMainBinding
import edu.cs371m.reddit.databinding.ActivityOnePostBinding
import edu.cs371m.reddit.ui.MainViewModel
import edu.cs371m.reddit.ui.OnePostViewModel

class OnePostActivity  : AppCompatActivity() {

    companion object {
       val adapterPosTag : String = "adapterPositionTag"
       val titleTag : String = "titleTag"
       val selfTextTag : String = "selfTextTag"
       val imageUrlTag : String = "imageUrlTag"
       val fallbackImageUrlTag : String = "fallbackImageUrlTag"
    }

    private val viewModel: OnePostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityOnePostBinding = ActivityOnePostBinding.inflate(layoutInflater)
        setContentView(activityOnePostBinding.root)
        setSupportActionBar(activityOnePostBinding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val activityThatCalled = intent
        val callingBundle = activityThatCalled.extras

        callingBundle?.let {
            viewModel.setParams(
                it.getString(titleTag)!!, it.getString(selfTextTag)!!, it.getString(imageUrlTag)!!,
                it.getString(fallbackImageUrlTag)!!

            )

        }

        viewModel.observeParamsUpdated().observe(this) {

            activityOnePostBinding.toolbar.title = viewModel.getTitle().value.toString()
            activityOnePostBinding.contentMain.onePostTitle2.text = viewModel.getTitle().value.toString()

            if(viewModel.getImgUrl().value.toString().isEmpty())
            {
                activityOnePostBinding.contentMain.onePostSelfText.text = viewModel.getSelfText().value.toString()
            }
            else if((viewModel.getFallbackImgUrl().value.toString().isEmpty() || viewModel.getFallbackImgUrl().value.toString() == "self")
                && viewModel.getImgUrl().value.toString().endsWith("/"))
            {
                activityOnePostBinding.contentMain.onePostSelfText.text = viewModel.getSelfText().value.toString()
            }
            else
            {
                viewModel.downloadImage(viewModel.getImgUrl().value.toString(),
                                viewModel.getFallbackImgUrl().value.toString(),
                                activityOnePostBinding.contentMain.onePostImg)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(item.itemId == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this)
            finish()
            true
        } else
            super.onOptionsItemSelected(item)
    }






}