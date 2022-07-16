package com.sshubham.surveyapp.logsurvey.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sshubham.surveyapp.logsurvey.model.SurveyData
import com.sshubham.surveyapp.logsurvey.usecase.FetchSurveyDetailUseCase
import kotlinx.coroutines.launch

/**
 * Created by Shubham Rajouriya on 7/11/2022.
 * all dev modification @Copyrigthts reserved to code owner/Shubham Rajouriya
 */
class FetchSurveyDetailViewModel: ViewModel() {
    var surveyLiveData = MutableLiveData<List<SurveyData>>()

    fun getSurveyData(): LiveData<List<SurveyData>>{
        return surveyLiveData
    }

    /**
     * fetchData() - fetch the data and from use case layer
     *and post the data to UI interface using live data
     */
    fun fetchData(fetchSurveyDetailUseCase: FetchSurveyDetailUseCase){
        viewModelScope.launch {
            val surveyData = fetchSurveyDetailUseCase.fetchAppSurveyData()
            surveyLiveData.postValue(surveyData)
        }
    }
}