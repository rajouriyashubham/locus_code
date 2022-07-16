package com.sshubham.surveyapp.logsurvey.userinterface.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.sshubham.surveyapp.R

/**
 * Created by Shubham Rajouriya on 7/12/2022.
 * all dev modification @Copyrigthts reserved to code owner/Shubham Rajouriya
 */
class LargeImageDialog(private val context: Context) {
    lateinit var imageDialog: Dialog

    fun showLargeImage(bitmap: Bitmap){
        imageDialog = Dialog(context)
        imageDialog.setContentView(R.layout.enlarge_image_view)
        imageDialog.setCancelable(false)
        val closeButton = imageDialog.findViewById<Button>(R.id.imageCloseBtn)
        val largeImage = imageDialog.findViewById<ImageView>(R.id.enlargeImage)
        largeImage.setImageBitmap(bitmap)
        closeButton.setOnClickListener {
            imageDialog.dismiss()
        }
        imageDialog.show()
    }
}