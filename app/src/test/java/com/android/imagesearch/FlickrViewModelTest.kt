package com.android.imagesearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.imagesearch.data.model.FlickrResponse
import com.android.imagesearch.data.model.Media
import com.android.imagesearch.data.model.PhotoItemList
import com.android.imagesearch.data.repository.FlickrRepository
import com.android.imagesearch.ui.viewmodel.FlickrViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class FlickrViewModelTest {

    // Rule to allow LiveData to execute on the same thread as the test
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Test dispatcher that allows us to control the execution of coroutines
    private val testDispatcher = TestCoroutineDispatcher()

    // Mocked instance of the FlickrRepository
    @Mock
    private lateinit var repository: FlickrRepository

    // The ViewModel we are testing
    private lateinit var viewModel: FlickrViewModel

    @Before
    fun setUp() {
        // Initialize mocks and set the Main dispatcher to the test dispatcher
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = FlickrViewModel(repository)
    }

    @Test
    fun `search photos with valid query updates photos`() = runTest(testDispatcher) {
        // Given a valid query and expected photos
        val query = "cats"
        val expectedPhotos = listOf(PhotoItemList(title = "Cat 1", link = "https://www.flickr.com//photos//ariabtw//53543917874//", date_taken = "2024-01-15T19:44:01-08:00", description = "<p><a href=\\\"https:\\/\\/www.flickr.com\\/people\\/ariabtw\\/\\\">ariabtw<\\/a> posted a photo:", published = "2024-02-21T18:56:55Z", author = "nobody@flickr.com (\\\"ariabtw\\\")", author_id = "198696210@N05", tags = "select cat pet photography shorthair tabby student", media = Media("https://live.staticflickr.com/65535/53543635659_36883006f0_m.jpg")), PhotoItemList(title = "Cat 2", link = "https://www.flickr.com//photos//187223221@N04//53543909240//", media = Media("https://live.staticflickr.com/65535/53543635659_36883006f0_m.jpg"), date_taken = "2024-02-21T11:06:30-08:00", description = "<p><a href=\\\"https:\\/\\/www.flickr.com\\/people\\/j", published = "2024-02-21T16:12:01Z", author = "nobody@flickr.com (\\\"Jezevec40 (formerly Badger 23)\\\")", author_id = "96636569@N00", tags = "2024 20240233 jezevec40 cat adurree bisad chatte gata gato gatto ikati"))
        whenever(repository.searchPhotos(query)).thenReturn(FlickrResponse(
            title = "",
            link = "",
            description = "",
            modified = "",
            generator = "",
            items = expectedPhotos
        ))

        // When searching for photos with the given query
        viewModel.searchPhotos(query)
        advanceUntilIdle()

        // Then verify photos are updated, no error message, and loading is false
        assertEquals(expectedPhotos, viewModel.photos.value)
        assertNull(viewModel.errorMessage.value)
        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun `search photos with empty query returns empty list`() = runTest(testDispatcher) {
        // Given an empty query string
        val query = ""

        // When searching with an empty query
        viewModel.searchPhotos(query)
        advanceUntilIdle()

        // Then verify the photos list is empty and no error message
        assertEquals(emptyList<PhotoItemList>(), viewModel.photos.value)
        assertNull(viewModel.errorMessage.value)
    }

    @Test
    fun `getPhotoItemByTitle returns correct item`() = runTest(testDispatcher) {
        // Given a list of photo items
        val photoItemList = listOf(PhotoItemList(title = "Photo 1", link = "https://www.flickr.com//photos//ariabtw//53543917874//", date_taken = "2024-01-15T19:44:01-08:00", description = "<p><a href=\\\"https:\\/\\/www.flickr.com\\/people\\/ariabtw\\/\\\">ariabtw<\\/a> posted a photo:", published = "2024-02-21T18:56:55Z", author = "nobody@flickr.com (\\\"ariabtw\\\")", author_id = "198696210@N05", tags = "select cat pet photography shorthair tabby student", media = Media("https://live.staticflickr.com/65535/53543635659_36883006f0_m.jpg")), PhotoItemList(title = "Photo 2", link = "https://www.flickr.com//photos//187223221@N04//53543909240//", media = Media("https://live.staticflickr.com/65535/53543635659_36883006f0_m.jpg"), date_taken = "2024-02-21T11:06:30-08:00", description = "<p><a href=\\\"https:\\/\\/www.flickr.com\\/people\\/j", published = "2024-02-21T16:12:01Z", author = "nobody@flickr.com (\\\"Jezevec40 (formerly Badger 23)\\\")", author_id = "96636569@N00", tags = "2024 20240233 jezevec40 cat adurree bisad chatte gata gato gatto ikati"))
        viewModel.photoItems = photoItemList

        // When retrieving a photo item by its title
        val result = viewModel.getPhotoItemByTitle("Photo 1")

        // Then verify the correct photo item is returned
        assertNotNull(result)
        assertEquals("Photo 1", result?.title)
    }

    @Test
    fun `getPhotoItemByTitle returns null when title not found`() = runTest(testDispatcher) {
        // Given a list of photo items
        val photoItemList = listOf(PhotoItemList(title = "Photo 1",  link = "https://www.flickr.com//photos//ariabtw//53543917874//", date_taken = "2024-01-15T19:44:01-08:00", description = "<p><a href=\\\"https:\\/\\/www.flickr.com\\/people\\/ariabtw\\/\\\">ariabtw<\\/a> posted a photo:", published = "2024-02-21T18:56:55Z", author = "nobody@flickr.com (\\\"ariabtw\\\")", author_id = "198696210@N05", tags = "select cat pet photography shorthair tabby student", media = Media("https://live.staticflickr.com/65535/53543635659_36883006f0_m.jpg")), PhotoItemList(title = "Photo 2", link = "https://www.flickr.com//photos//187223221@N04//53543909240//", media = Media("https://live.staticflickr.com/65535/53543635659_36883006f0_m.jpg"), date_taken = "2024-02-21T11:06:30-08:00", description = "<p><a href=\\\"https:\\/\\/www.flickr.com\\/people\\/j", published = "2024-02-21T16:12:01Z", author = "nobody@flickr.com (\\\"Jezevec40 (formerly Badger 23)\\\")", author_id = "96636569@N00", tags = "2024 20240233 jezevec40 cat adurree bisad chatte gata gato gatto ikati"))
        viewModel.photoItems = photoItemList

        // When retrieving a photo item with a title that does not exist
        val result = viewModel.getPhotoItemByTitle("Photo 3")

        // Then verify that the result is null
        assertNull(result)
    }

//  The tearDown method resets the dispatcher after each test to ensure that the main dispatcher is in its original state, preventing interference with other tests
    @After
    fun tearDown() {
        // Reset the Main dispatcher to the original state after the tests complete
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
