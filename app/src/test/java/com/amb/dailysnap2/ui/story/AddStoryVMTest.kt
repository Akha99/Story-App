package com.amb.dailysnap2.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.amb.dailysnap2.DataDummy
import com.amb.dailysnap2.data.Result
import com.amb.dailysnap2.data.networking.response.BaseResponse
import com.amb.dailysnap2.data.repository.StoryRepository
import com.amb.dailysnap2.getOrAwaitValue
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AddStoryVMTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var postViewModel: AddStoryViewModel
    private val Token = DataDummy.generateToken()
    private val Photo = DataDummy.generateImages()
    private val Description = DataDummy.generateDescription()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        postViewModel = AddStoryViewModel(storyRepository)
    }

    @Test
    fun `post success should return a non-null result`() {
        val expectedAddStory = MutableLiveData<Result<BaseResponse>>().apply {
            value = Result.Success(BaseResponse(false, "Upload Success"))
        }
        `when`(storyRepository.addStory(Token, Photo, Description, null, null)).thenReturn(expectedAddStory)
        val actualAddStory = postViewModel.addStory(Token, Photo, Description).getOrAwaitValue()
        verify(storyRepository).addStory(Token, Photo, Description, null, null)
        assertNotNull(actualAddStory)
    }


    @Test
    fun `post error should return an Error result`() {
        val expectedAddStory = MutableLiveData<Result<BaseResponse>>().apply {
            value = Result.Error("Error")
        }
        `when`(storyRepository.addStory(Token, Photo, Description, null, null)).thenReturn(expectedAddStory)
        val actualAddStory = postViewModel.addStory(Token, Photo, Description).getOrAwaitValue()
        verify(storyRepository).addStory(Token, Photo, Description, null, null)
        assertNotNull(actualAddStory)
        assertTrue(actualAddStory is Result.Error)
    }
}


