package com.sshubham.surveyapp.util

import android.content.Context

/**
 * Created by Shubham Rajouriya on 7/11/2022.
 * all dev modification @Copyrigthts reserved to code owner/Shubham Rajouriya
 */
class FileReader(private val context: Context) {

    fun readFile(): String{
        val file_name = "app_content"
        val fileData = context.assets.open(file_name).bufferedReader().use{
            it.readText()
        }
       return fileData
    }

}