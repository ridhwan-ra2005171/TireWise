package com.mrm.tirewise.testing

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import org.junit.Rule
import org.junit.Test
import java.util.Date

class TireScanUtilTest{

//    @Mock
//    lateinit var firestore: FirebaseFirestore
//
//    @Mock
//    lateinit var context: Context
//
//    private lateinit var testing: MyTestClass
//
//    @Before
//    fun setUp() {
//        // Initialize repository with mocked dependencies
//        testing = MyTestClass(context, firestore) // Assuming a constructor for DI
//    }
//
//    @Test
//    fun testFetchTireScans() {
//        val mockSnapshot = mock(QuerySnapshot::class.java)
//        Mockito.`when`(firestore.collection("tireScans").get())
//
//        val tireScans = testing.fetchTireScans() // Assuming a method to fetch scans
//
//        // Assert using Truth
//        assertThat(tireScans).isEqualTo(mockSnapshot) // Or perform custom assertions on the result
//    }


    @Test
    fun `upsertTireScan - valid input for all tire scan field return true`() {
        val result = TireScanUtil.validateTireScanInput(
            vehicleId = "1234567890", // Example vehicle ID
            tireImageUri = "https://example.com/tire-images/placeholder.jpg", // Dummy image URI
            tireCondition = "Good", // Example tire condition
            tireConditionDesc = "Moderate Wear.", // Example condition description
            tirePosition = "Front Left", // Example tire position
            additionalNotes = "Some additional notes about the tire.", // Optional notes
            date = System.currentTimeMillis() // Current timestamp
        )
        assertThat(result).isTrue()
    }

    @Test
     fun `upsertTireScan - invalid input vehicleId return false`() {
        val result = TireScanUtil.validateTireScanInput(
            vehicleId = "4wqr231", // Example vehicle ID
            tireImageUri = "https://example.com/tire-images/placeholder.jpg", // Dummy image URI
            tireCondition = "Good", // Example tire condition
            tireConditionDesc = "Moderate Wear.", // Example condition description
            tirePosition = "Front Left", // Example tire position
            additionalNotes = "Some additional notes about the tire.", // Optional notes
            date = System.currentTimeMillis() // Current timestamp
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertTireScan - invalid tireImageuri  return false`() {
        val result = TireScanUtil.validateTireScanInput(
            vehicleId = "1234567890", // Example vehicle ID
            tireImageUri = "", // Dummy image URI
            tireCondition = "Good", // Example tire condition
            tireConditionDesc = "Moderate Wear.", // Example condition description
            tirePosition = "Front Left", // Example tire position
            additionalNotes = "Some additional notes about the tire.", // Optional notes
            date = System.currentTimeMillis() // Current timestamp
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertTireScan - invalid tireCondition return false`() {
        val result = TireScanUtil.validateTireScanInput(
            vehicleId = "1234567890", // Example vehicle ID
            tireImageUri = "https://example.com/tire-images/placeholder.jpg", // Dummy image URI
            tireCondition = "Lol", // Example tire condition
            tireConditionDesc = "Moderate Wear", // Example condition description
            tirePosition = "Front Left", // Example tire position
            additionalNotes = "Some additional notes about the tire.", // Optional notes
            date = System.currentTimeMillis() // Current timestamp
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `upsertTireScan - Empty tireCondition return false`() {
        val result = TireScanUtil.validateTireScanInput(
            vehicleId = "1234567890", // Example vehicle ID
            tireImageUri = "https://example.com/tire-images/placeholder.jpg", // Dummy image URI
            tireCondition = "", // Example tire condition
            tireConditionDesc = "Moderate Wear", // Example condition description
            tirePosition = "Front Left", // Example tire position
            additionalNotes = "Some additional notes about the tire.", // Optional notes
            date = System.currentTimeMillis() // Current timestamp
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertTireScan - empty tirePosition return false`() {
        val result = TireScanUtil.validateTireScanInput(
            vehicleId = "1234567890", // Example vehicle ID
            tireImageUri = "https://example.com/tire-images/placeholder.jpg", // Dummy image URI
            tireCondition = "Good", // Example tire condition
            tireConditionDesc = "Moderate Wear", // Example condition description
            tirePosition = "", // Example tire position
            additionalNotes = "Some additional notes about the tire.", // Optional notes
            date = System.currentTimeMillis() // Current timestamp
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertTireScan - invalid tirePosition return false`() {
        val result = TireScanUtil.validateTireScanInput(
            vehicleId = "1234567890", // Example vehicle ID
            tireImageUri = "https://example.com/tire-images/placeholder.jpg", // Dummy image URI
            tireCondition = "Good", // Example tire condition
            tireConditionDesc = "Moderate Wear", // Example condition description
            tirePosition = "Middle Left", // Example tire position
            additionalNotes = "Some additional notes about the tire.", // Optional notes
            date = System.currentTimeMillis() // Current timestamp
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertTireScan - invalid Tirescan Date return false`() {
        val result = TireScanUtil.validateTireScanInput(
            vehicleId = "1234567890", // Example vehicle ID
            tireImageUri = "https://example.com/tire-images/placeholder.jpg", // Dummy image URI
            tireCondition = "Good", // Example tire condition
            tireConditionDesc = "Moderate Wear", // Example condition description
            tirePosition = "Middle Left", // Example tire position
            additionalNotes = "Some additional notes about the tire.", // Optional notes
            date = 0L// Current timestamp
        )
        assertThat(result).isFalse()
    }



    @Test
    fun `getTires - valid userId and tire position return True`() {
        val result = TireScanUtil.getTires(
            userId = "htrs-fdgh-8793", // Example vehicle ID
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `getTires - invalid userId return false`() {
        val result = TireScanUtil.getTires(
            userId = "1", // Example vehicle ID
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `deleteTireScan - invalid tireScanId return false`() {
        val result = TireScanUtil.deleteTireScan(
            tireId = "1"
        )
        assertThat(result).isFalse()
    }
    @Test
    fun `deleteTireScan - valid tireScanId return true`() {
        val result = TireScanUtil.deleteTireScan(
            tireId = "tirescan213"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `filterTiresByPosition - invalid tirePosition return false`() {
        val result = TireScanUtil.filterTiresByPosition(
            tirePosition = "1"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `filterTiresByPosition - valid tirePosition return false`() {
        val data = listOf(
            TireScanUtil.MyTireScan(
                tireId = "T12345",
                vehicleId = "V1000",
                tireImageUri = "content://...",
                tireCondition = "Good",
                tireConditionDesc = "Looks good, no visible wear",
                tirePosition = "Front Left",
                date = Date(2024, 2, 28).time//March 28th
            ), TireScanUtil.MyTireScan(
                tireId = "T54321",
                vehicleId = "V2001",
                tireImageUri = "content://...",
                tireCondition = "Worn",
                tireConditionDesc = "Needs replacement soon",
                tirePosition = "Rear Right",
                date = Date(2024, 2, 29).time//March 29th
            ),
            TireScanUtil.MyTireScan(
                tireId = "T54321",
                vehicleId = "V2001",
                tireImageUri = "content://...",
                tireCondition = "Worn",
                tireConditionDesc = "Needs replacement soon",
                tirePosition = "Rear Right",
                date = Date(2024, 2, 30).time //March 30th
            )

        )

        val filteredData = filterByTirePosition(data, "Rear Right")

        assertThat(filteredData).isEqualTo(data.filter { it.tirePosition == "Rear Right" })
    }

    fun filterByTirePosition(data: List<TireScanUtil.MyTireScan>, tirePosition: String): List<TireScanUtil.MyTireScan> {
        return data.filter { it.tirePosition == tirePosition }
    }

    @Test
    fun `sortByDate sorts objects in ascending order by date`(){
        val data = listOf(
            TireScanUtil.MyTireScan(
                tireId = "T12345",
                vehicleId = "V1000",
                tireImageUri = "content://...",
                tireCondition = "Good",
                tireConditionDesc = "Looks good, no visible wear",
                tirePosition = "Front Left",
                date = Date(2024, 2, 28).time//March 28th
            ), TireScanUtil.MyTireScan(
                tireId = "T54321",
                vehicleId = "V2001",
                tireImageUri = "content://...",
                tireCondition = "Worn",
                tireConditionDesc = "Needs replacement soon",
                tirePosition = "Rear Right",
                date = Date(2024, 2, 29).time//March 29th
            ),
            TireScanUtil.MyTireScan(
                tireId = "T54321",
                vehicleId = "V2001",
                tireImageUri = "content://...",
                tireCondition = "Worn",
                tireConditionDesc = "Needs replacement soon",
                tirePosition = "Rear Right",
                date = Date(2024, 2, 30).time //March 30th
            )

        )

        val sortedData = sortByDate(data)

        assertThat(sortedData)
            .isEqualTo(data.sortedBy { it.date })
    }

    fun sortByDate(data: List<TireScanUtil.MyTireScan>): List<TireScanUtil.MyTireScan> {
        return data.sortedBy { it.date }
    }

    @Test
    fun `sortByDate sorts objects in descending order by date`(){
        val data = listOf(
            TireScanUtil.MyTireScan(
                tireId = "T12345",
                vehicleId = "V1000",
                tireImageUri = "content://...",
                tireCondition = "Good",
                tireConditionDesc = "Looks good, no visible wear",
                tirePosition = "Front Left",
                date = Date(2024, 2, 28).time//March 28th
            ), TireScanUtil.MyTireScan(
                tireId = "T54321",
                vehicleId = "V2001",
                tireImageUri = "content://...",
                tireCondition = "Worn",
                tireConditionDesc = "Needs replacement soon",
                tirePosition = "Rear Right",
                date = Date(2024, 2, 29).time//March 29th
            ),
            TireScanUtil.MyTireScan(
                tireId = "T54321",
                vehicleId = "V2001",
                tireImageUri = "content://...",
                tireCondition = "Worn",
                tireConditionDesc = "Needs replacement soon",
                tirePosition = "Rear Right",
                date = Date(2024, 2, 30).time //March 30th
            )

        )

        val sortedData = sortByDateDescending(data)

        assertThat(sortedData)
            .isEqualTo(data.sortedByDescending { it.date })
    }

    fun sortByDateDescending(data: List<TireScanUtil.MyTireScan>): List<TireScanUtil.MyTireScan> {
        return data.sortedByDescending { it.date }
    }






}