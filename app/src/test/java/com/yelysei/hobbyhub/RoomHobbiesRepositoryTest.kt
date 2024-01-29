
import android.database.sqlite.SQLiteConstraintException
import com.yelysei.hobbyhub.model.HobbyAlreadyExistsException
import com.yelysei.hobbyhub.model.hobbies.RoomHobbiesRepository
import com.yelysei.hobbyhub.model.hobbies.entities.Hobby
import com.yelysei.hobbyhub.model.hobbies.room.HobbiesDao
import com.yelysei.hobbyhub.model.hobbies.room.entities.HobbyDbEntity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RoomHobbiesRepositoryTest {

    @MockK(relaxed = true)
    private lateinit var mockHobbiesDao: HobbiesDao

    private lateinit var roomHobbiesRepository: RoomHobbiesRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        roomHobbiesRepository = RoomHobbiesRepository(mockHobbiesDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }


    @Test
    fun `test getCurrentHobbies`() = runBlocking {
        // Arrange
        val hobbyEntities = listOf(HobbyDbEntity(1, "Coding", "Programming", null, null, null))
        coEvery { mockHobbiesDao.getHobbies() } returns flow { emit(hobbyEntities) }

        // Act
        val result = roomHobbiesRepository.getCurrentHobbies().first()

        // Assert
        assertEquals(hobbyEntities.map { it.toHobby() }, result)
    }

    @Test
    fun `test getCurrentCategories`() = runBlocking {
        // Arrange
        val categories = listOf("Programming", "Reading", "Gaming")
        coEvery { mockHobbiesDao.getCategories() } returns flow { emit(categories) }

        // Act
        val result = roomHobbiesRepository.getCurrentCategories().first()

        // Assert
        assertEquals(categories, result)
    }

    @Test
    fun `test getAvailableHobbiesForCategory`() = runBlocking {
        // Arrange
        val categoryName = "Programming"
        val hobbies = listOf(Hobby(0, "Coding", categoryName, null, null, null))
        coEvery { mockHobbiesDao.findHobbiesByCategoryName(categoryName) } returns hobbies.map {
            HobbyDbEntity.fromHobby(
                it
            )
        }

        // Act
        val result = roomHobbiesRepository.getAvailableHobbiesForCategory(categoryName)

        // Assert
        assertEquals(hobbies, result)
    }

    @Test
    fun `test addCustomHobby`() = runBlocking {
        // Arrange
        val hobby = Hobby(
            hobbyName = "Drawing",
            categoryName = "Art",
            cost = "Free",
            place = null,
            people = "Individual"
        )

        // Mock the DAO behavior
        coEvery { mockHobbiesDao.insertCustomHobby(any()) } returns 1

        // Act
        val result = roomHobbiesRepository.addCustomHobby(hobby)

        // Assert
        assertEquals(1, result)
    }

    @Test
    fun `testAddCustomHobbyWithExistingHobbyName`() = runBlocking {
        // Arrange
        val existingHobbyName = "Coding"
        val hobby = Hobby(
            hobbyName = existingHobbyName,
            categoryName = "Programming",
            cost = null,
            place = null,
            people = null
        )

        // Mock the DAO behavior
        coEvery { mockHobbiesDao.insertCustomHobby(any()) } throws SQLiteConstraintException()

        // Act and Assert
        try {
            roomHobbiesRepository.addCustomHobby(hobby)
            fail("Expected HobbyAlreadyExistsException")
        } catch (e: HobbyAlreadyExistsException) {
            // Expected exception
            assertEquals(existingHobbyName, e.hobbyName)
        }
    }

    @Test
    fun `test getHobbiesByHobbyName`() = runBlocking {
        // Arrange
        val hobbyNameSearchInput = "C"

        // Mock the DAO behavior
        coEvery { mockHobbiesDao.findHobbiesByHobbyName(any()) } returns listOf(
            HobbyDbEntity(1, "Coding", "Programming", null, null, null),
            HobbyDbEntity(2, "Cooking", "Culinary", null, null, null)
        )

        // Act
        val result = roomHobbiesRepository.getHobbiesByHobbyName(hobbyNameSearchInput)

        // Assert
        assertEquals(2, result.size)
        assertTrue(result.all { it.hobbyName.contains(hobbyNameSearchInput, ignoreCase = true) })
    }

    @Test
    fun `test hobbyExists`() = runBlocking {
        // Arrange
        val hobbyName = "Reading"

        // Mock the DAO behavior
        coEvery { mockHobbiesDao.hobbyExists(any()) } returns true

        // Act
        val result = roomHobbiesRepository.hobbyExists(hobbyName)

        // Assert
        assertTrue(result)
    }
}
