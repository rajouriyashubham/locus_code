package com.sshubham.surveyapp.logsurvey.model

import com.google.gson.annotations.SerializedName


data class DataMap (

  @SerializedName("options" ) var options : ArrayList<String> = arrayListOf()

)