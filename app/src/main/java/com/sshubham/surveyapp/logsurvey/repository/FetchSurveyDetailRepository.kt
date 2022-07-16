package com.sshubham.surveyapp.logsurvey.repository

import android.util.Log
import com.google.gson.Gson
import com.sshubham.surveyapp.logsurvey.model.ItemType
import com.sshubham.surveyapp.logsurvey.model.SurveyData
import com.sshubham.surveyapp.util.FileReader
import com.sshubham.surveyapp.util.Logger.Logger

/**
 * Created by Shubham Rajouriya on 7/11/2022.
 * all dev modification @Copyrigthts reserved to code owner/Shubham Rajouriya
 */

/**
 * FetchSurveyDetailRepository implements fetchAppSurveyData method
 * for fetching the data from data source(FileReader) and map the data in required formate
 */
class FetchSurveyDetailRepository(private val fileReader: FileReader) {
    private val TAG = "FetchSurveyDetailRepository"
    private val PHOTO_TYPE = "PHOTO"
    private val CHOICE_TYPE = "SINGLE_CHOICE"
    private val COMMENT_TYPE = "COMMENT"

    /**
     * fetchAppSurveyData() - fetch the data and map for specific use case requirment
     * @return List<SurveyData> - fetched data from data source
     */
    suspend fun fetchAppSurveyData(): List<SurveyData>{
        val appContent = fileReader.readFile()
        try {
            val surveyData: Array<SurveyData> = Gson().fromJson(appContent, Array<SurveyData>::class.java)
            surveyData.forEach {
               when(it.type){
                   PHOTO_TYPE -> {
                       it.itemType = ItemType.PHOTO
                   }
                   CHOICE_TYPE -> {
                       it.itemType = ItemType.OPTION
                   }
                   COMMENT_TYPE -> {
                       it.itemType = ItemType.COMMENT
                   }
               }

            }
            return surveyData.asList()
        } catch (e: Exception) {
            return arrayListOf()
        }
    }
}