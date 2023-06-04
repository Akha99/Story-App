import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.amb.dailysnap2.DataDummy
import com.amb.dailysnap2.data.Result
import com.amb.dailysnap2.data.model.Story
import com.amb.dailysnap2.data.repository.StoryRepository
import com.amb.dailysnap2.data.networking.response.StoryResponse
import com.amb.dailysnap2.getOrAwaitValue
import com.amb.dailysnap2.ui.maps.MapsViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

//@RunWith(MockitoJUnitRunner::class)
class MapsVMTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var storyMapsViewModel: MapsViewModel
    private val token = DataDummy.generateToken()
    private val storyList = DataDummy.generateListStoryItems()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        storyMapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `getStory with location is not null and return success`() {
        val expectedResponse = StoryResponse(false, null, storyList)
        val expectedMaps = MutableLiveData<Result<StoryResponse>>().apply {
            value = Result.Success(expectedResponse)
        }

        `when`(storyRepository.getStoryLocation(token)).thenReturn(expectedMaps)

        val actualMaps = storyMapsViewModel.getStoryLocation(token).getOrAwaitValue()

        verify(storyRepository).getStoryLocation(token)
        assertNotNull(actualMaps)
        assertTrue(actualMaps is Result.Success)

        val actualResponse = (actualMaps as Result.Success).data
        assertEquals(expectedResponse, actualResponse)

        val actualStoryList = actualResponse?.listStory
        assertNotNull(actualStoryList)
        assertEquals(101, actualStoryList?.size)

        val actualStory = actualStoryList?.get(0)
        assertNotNull(actualStory)

        assertEquals(storyList[0].id, actualStory?.id)
        assertEquals(storyList[0].name, actualStory?.name)
        assertEquals(storyList[0].description, actualStory?.description)
        assertEquals(storyList[0].photoUrl, actualStory?.photoUrl)
        assertEquals(storyList[0].lat, actualStory?.lat ?: 0.0, 0.001)
        assertEquals(storyList[0].lon, actualStory?.lon ?: 0.0, 0.001)
    }
}