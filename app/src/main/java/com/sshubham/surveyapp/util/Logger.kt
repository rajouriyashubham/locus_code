package com.sshubham.surveyapp.util.Logger

import android.util.Log

/**
 * Created by Shubham Rajouriya on 7/11/2022.
 * all dev modification @Copyrigthts reserved to code owner/Shubham Rajouriya
 */
class Logger {
    companion object{
        fun log(tag: String, value: String){
            Log.d(tag, value)
        }
    }
}