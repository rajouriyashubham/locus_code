package com.sshubham.surveyapp.logsurvey.model

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName


data class SurveyData (

  @SerializedName("type"    ) var type    : String?  = null,
  @SerializedName("id"      ) var id      : String?  = null,
  @SerializedName("title"   ) var title   : String?  = null,
  @SerializedName("dataMap" ) var dataMap : DataMap? = DataMap(),
  var itemType: ItemType? = null,
  var selectedResponse: String? = null,
  var imageBitmap: Bitmap? = null

)