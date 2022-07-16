package com.sshubham.surveyapp.logsurvey.userinterface.activities

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sshubham.surveyapp.R
import com.sshubham.surveyapp.databinding.ActivityMainBinding
import com.sshubham.surveyapp.logsurvey.repository.FetchSurveyDetailRepository
import com.sshubham.surveyapp.logsurvey.usecase.FetchSurveyDetailUseCase
import com.sshubham.surveyapp.logsurvey.userinterface.adapter.SurveyListingAdapter
import com.sshubham.surveyapp.logsurvey.userinterface.dialog.LargeImageDialog
import com.sshubham.surveyapp.logsurvey.viewmodel.FetchSurveyDetailViewModel
import com.sshubham.surveyapp.util.FileReader
import com.sshubham.surveyapp.util.Logger.Logger


class MainActivity : AppCompatActivity(), SurveyListingAdapter.SurveyDataListner {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var surveyViewModel: FetchSurveyDetailViewModel
    private lateinit var surveyListingAdapter: SurveyListingAdapter
    private val CAMERA_REQUEST = 1888
    private lateinit var imageView: ImageView
    private val MY_CAMERA_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        binding.root
        init()
        observeSurveyData(surveyViewModel)
        getSurveyContentData(surveyViewModel)
    }

    private fun init() {
        surveyViewModel = ViewModelProvider.AndroidViewModelFactory(Application())
            .create(FetchSurveyDetailViewModel::class.java)
        surveyListingAdapter  = SurveyListingAdapter(this, this)
        binding.contentRecyclerView.adapter = surveyListingAdapter
        binding.contentRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun observeSurveyData(surveyViewModel: FetchSurveyDetailViewModel) {
        surveyViewModel.getSurveyData().observe(this, Observer {
            surveyListingAdapter.updateData(it)
        })
    }

    private fun getSurveyContentData(surveyViewModel: FetchSurveyDetailViewModel) {
        surveyViewModel.fetchData(FetchSurveyDetailUseCase(FetchSurveyDetailRepository(FileReader(this))))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_submit -> {
                Logger.log(TAG,"Log the captured response and id")
                val capturedIds = StringBuffer()
                surveyListingAdapter.surveyDataList.forEach {data ->
                    if (!data.selectedResponse.isNullOrEmpty()) {
                        capturedIds.append("${data.id} and the reponse is: ${data.selectedResponse}\n")
                        Logger.log(TAG,"content: ${data.selectedResponse}, id: ${data.id}")
                    }
                }
                if (!capturedIds.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, capturedIds.toString(), Toast.LENGTH_LONG).show()
                }
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }

    override fun onCommentCapture(content: String, commentId: String, position: Int) {
        Logger.log(TAG,"content: $content, id: $commentId , position: $position")
    }

    override fun onOptionSelection(optionContent: String, optionId: String, position: Int) {
        Logger.log(TAG,"content: $optionContent, id: $optionId , position: $position")
    }

    override fun onPhotoTouchEvent(isForDeletion: Boolean, selectedImageView: ImageView, itemId: String, itemPosition: Int) {
        Logger.log(TAG,"isDeletion: $isForDeletion, id: $itemId , position: $itemPosition")
        imageView = selectedImageView
        if (!isForDeletion) {
            if (surveyListingAdapter.surveyDataList.get(itemPosition).imageBitmap == null) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), MY_CAMERA_PERMISSION_CODE)
                } else {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST)
                }
            } else {
                val enlargeImageDialog = LargeImageDialog(this)
                enlargeImageDialog.showLargeImage(surveyListingAdapter.surveyDataList.get(itemPosition).imageBitmap!!)
            }
        }else{
            surveyListingAdapter.surveyDataList.get(itemPosition).imageBitmap = null
            imageView.setImageDrawable(getDrawable(R.drawable.palin_bg))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            var photo = data?.extras!!["data"] as Bitmap?
            if (photo != null) {
                photo = rotateImage(photo, 90f)
            }
            imageView.setImageBitmap(photo)
            val position = imageView.tag as Int
            surveyListingAdapter.surveyDataList.get(position).imageBitmap = photo
        }
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }
}