package com.sshubham.surveyapp.logsurvey.usecase

import com.google.gson.Gson
import com.sshubham.surveyapp.logsurvey.model.SurveyData
import com.sshubham.surveyapp.logsurvey.repository.FetchSurveyDetailRepository

/**
 * Created by Shubham Rajouriya on 7/11/2022.
 * all dev modification @Copyrigthts reserved to code owner/Shubham Rajouriya
 */

/**
 * FetchSurveyDetailUseCase implements fetchAppSurveyData method from repository
 */
class FetchSurveyDetailUseCase(private val fetchSurveyDetailRepository: FetchSurveyDetailRepository) {

    /**
     * fetchAppSurveyData() - fetch the data and map for specific use case requirement
     * @return List<SurveyData> - fetched data from data source
     */
    suspend fun fetchAppSurveyData(): List<SurveyData> {
        val surveyData = fetchSurveyDetailRepository.fetchAppSurveyData()
        return surveyData
    }
}