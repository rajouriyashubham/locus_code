package com.sshubham.surveyapp

import com.sshubham.surveyapp.logsurvey.model.ItemType
import com.sshubham.surveyapp.logsurvey.repository.FetchSurveyDetailRepository
import com.sshubham.surveyapp.util.FileReader
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * Created by Shubham Rajouriya on 7/12/2022.
 * all dev modification @Copyrigthts reserved to code owner/Shubham Rajouriya
 */
class FetchSurveyDetailRepositoryTest {

    private lateinit var fetchSurveyDetailRepository: FetchSurveyDetailRepository

    @MockK
    lateinit var fileReader: FileReader

    @Before
    fun setUp(){
        fileReader = mockk(relaxed = true)
        fetchSurveyDetailRepository = FetchSurveyDetailRepository(fileReader)
    }

    @Test
    fun fetchAppSurveyDataSuccessTest() = runBlocking{

        val testString = "[\n" +
                "{\n" +
                "\"type\": \"PHOTO\",\n" +
                "\"id\": \"pic1\",\n" +
                "\"title\": \"Photo 1\",\n" +
                "\"dataMap\": {}\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"SINGLE_CHOICE\",\n" +
                "\"id\": \"choice1\",\n" +
                "\"title\": \"Photo 1 choice\",\n" +
                "\"dataMap\": {\n" +
                "\"options\": [\n" +
                "\"Good\",\n" +
                "\"OK\",\n" +
                "\"Bad\"\n" +
                "]\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"COMMENT\",\n" +
                "\"id\": \"comment1\",\n" +
                "\"title\": \"Photo 1 comments\",\n" +
                "\"dataMap\": {}\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"PHOTO\",\n" +
                "\"id\": \"pic2\",\n" +
                "\"title\": \"Photo 2\",\n" +
                "\"dataMap\": {}\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"SINGLE_CHOICE\",\n" +
                "\"id\": \"choice2\",\n" +
                "\"title\": \"Photo 2 choice\",\n" +
                "\"dataMap\": {\n" +
                "\"options\": [\n" +
                "\"Good\",\n" +
                "\"OK\",\n" +
                "\"Bad\"\n" +
                "]\n" +
                "}\n" +
                "}]"

        every { fileReader.readFile() } answers {testString}
        val result = fetchSurveyDetailRepository.fetchAppSurveyData()
        Assert.assertEquals("Response has 2 photo data", 2, result.filter { it.itemType == ItemType.PHOTO }.size)
        Assert.assertEquals("Response has 2 choice selection data", 2, result.filter { it.itemType == ItemType.OPTION }.size)
        Assert.assertEquals("Response has 1 comment data", 1, result.filter { it.itemType == ItemType.COMMENT }.size)
    }

    @Test
    fun fetchAppSurveyDataWhenEmptyDataRecvivedTest()= runBlocking{
        val testString = ""
        every { fileReader.readFile() } answers {testString}
        val result = fetchSurveyDetailRepository.fetchAppSurveyData()
        Assert.assertEquals("Response has 0 data", 0, result.filter { it.itemType == ItemType.PHOTO }.size)
    }

    @Test
    fun fetchAppSurveyDataWhenIncorrectFormatDataRecivedTest()= runBlocking{

        val testString = "[\n" +
                "{\n" +
                "\"type\": \"PHOTO\",\n" +
                "\"id\": \"pic1\",\n" +
                "\"title\": \"Photo 1\",\n" +
                "\"dataMap\": {}\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"SINGLE_CHOICE\",\n" +
                "\"id\": \"choice1\",\n" +
                "\"title\": \"Photo 1 choice\",\n" +
                "\"dataMap\": {\n" +
                "\"options\": [\n" +
                "\"Good\",\n" +
                "\"Bad\"\n" +
                "]\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"COMMENT\",\n" +
                "\"id\": \"comment1\",\n" +
                "\"title\": \"Photo 1 comments\",\n" +
                "\"dataMap\": {}\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"PHOTO\",\n" +
                "\"id\": \"pic2\",\n" +
                "\"title\": \"Photo 2\",\n" +
                "\"dataMap\": {}\n" +
                "},\n" +
                "{\n" +
                "\"type\": \"SINGLE_CHOICE\",\n" +
                "\"id\": \"choice2\",\n" +
                "\"title\": \"Photo 2 choice\",\n" +
                "\"dataMap\": {\n" +
                "\"options\": [\n" +
                "\"Good\",\n" +
                "\"OK\",\n" +
                "\"Bad\"\n" +
                "]\n" +
                "}\n"

        every { fileReader.readFile() } answers {testString}
        val result = fetchSurveyDetailRepository.fetchAppSurveyData()
        Assert.assertEquals("Response has 0 photo data", 0, result.filter { it.itemType == ItemType.PHOTO }.size)
    }

}