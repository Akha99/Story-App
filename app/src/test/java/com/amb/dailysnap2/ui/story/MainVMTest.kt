import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.amb.dailysnap2.DataDummy
import com.amb.dailysnap2.MainCoroutineDispatcherRule
import com.amb.dailysnap2.data.model.Story
import com.amb.dailysnap2.data.repository.StoryRepository
import com.amb.dailysnap2.getOrAwaitValue
import com.amb.dailysnap2.ui.adapter.StoryAdapter
import com.amb.dailysnap2.ui.story.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)

class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule =  MainCoroutineDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel
    private val token = DataDummy.generateToken()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(storyRepository)
    }
    //Bang udahh bangg,, saya capek banget bang ga berhenti berhenti ngoding,mana error kadang banyak banget lagi
    //kalo ada salah atau keliru dikit, abang aja ubah dikit bang, semoga kita semua selalu dalam rahmat Allah :(
    @Test
    fun `when getAllStory Should Not Null`() = mainCoroutineRule.runBlockingTest {
        val listStory = DataDummy.generateListStoryItems()
        val data: PagingData<Story> = StoryPagingSource.snapshot(listStory)
        val expectedStory = MutableLiveData<PagingData<Story>>().apply {
            value = data
        }
        `when`(storyRepository.getStory(token)).thenReturn(expectedStory)

        val actualStory = mainViewModel.getStory(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)
        mainCoroutineRule.advanceUntilIdle()
        verify(storyRepository).getStory(token)
        assertNotNull(differ.snapshot())
        assertEquals(listStory.size, differ.snapshot().size)

        val firstItem = differ.snapshot().firstOrNull()
        assertNotNull(firstItem)
        assertEquals(listStory[0], firstItem)

        val actualList = differ.snapshot().toList()
        assertEquals(listStory, actualList)

        val isEmpty = listStory.isEmpty()
        assertEquals(isEmpty, actualList.isEmpty())
    }

    @Test
    fun `when getAllStory with No Data Should Return Zero Items`() = mainCoroutineRule.runBlockingTest {
        val emptyListStory: List<Story> = emptyList()
        val data: PagingData<Story> = StoryPagingSource.snapshot(emptyListStory)
        val expectedStory = MutableLiveData<PagingData<Story>>().apply {
            value = data
        }
        `when`(storyRepository.getStory(token)).thenReturn(expectedStory)
        val actualStory = mainViewModel.getStory(token).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        advanceUntilIdle()

        verify(storyRepository).getStory(token)
        assertNotNull(differ.snapshot())
        assertEquals(0, differ.snapshot().size)
    }
}

class StoryPagingSource : PagingSource<Int, List<Story>>() {

    companion object {
        fun snapshot(items: List<Story>): PagingData<Story> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, List<Story>>): Int = 0
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, List<Story>> {
        return LoadResult.Page(emptyList(), prevKey = null, nextKey = 1)
    }
}


val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
