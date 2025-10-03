package com.mrm.tirewise.testing

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class vehicleUtilTest {


    @Test
    fun `upsertVehicle - valid input for all vehicle field return true`(){
        val result= vehicleUtil.validateVehicleInput(
            vehicleId = "ABC123",
            userId = "htrs-fdgh-8793",
            plateNo = "ABC1234",
            vehicleModel = "Model X",
            vehicleMake = "Tesla",
            vehicleName = "My Tesla Model X",
            yearMake = "2022",
            vehicleColor = "Red",
            additionalInfo = "Some additional information",
            imageUri = "https://example.com/image.jpg"

        )
        assertThat(result).isTrue()
    }

    @Test
    fun `upsertVehicle - userId does not exist from existing user returns false`(){
        val result= vehicleUtil.validateVehicleInput(
            vehicleId = "ABC123",
            userId = "",
            plateNo = "ABC1234",
            vehicleModel = "Model X",
            vehicleMake = "Tesla",
            vehicleName = "My Tesla Model X",
            yearMake = "2022",
            vehicleColor = "Red",
            additionalInfo = "Some additional information",
            imageUri = "https://example.com/image.jpg"

        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertVehicle - plateNo is empty returns false`(){
        val result= vehicleUtil.validateVehicleInput(
            vehicleId = "ABC123",
            userId = "htrs-fdgh-8793",
            plateNo = "",
            vehicleModel = "Model X",
            vehicleMake = "Tesla",
            vehicleName = "My Tesla Model X",
            yearMake = "2022",
            vehicleColor = "Red",
            additionalInfo = "Some additional information",
            imageUri = "https://example.com/image.jpg"

        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertVehicle - vehicleModel is empty returns false`(){
        val result= vehicleUtil.validateVehicleInput(
            vehicleId = "ABC123",
            userId = "htrs-fdgh-8793",
            plateNo = "ABC1243",
            vehicleModel = "",
            vehicleMake = "Tesla",
            vehicleName = "My Tesla Model X",
            yearMake = "2022",
            vehicleColor = "Red",
            additionalInfo = "Some additional information",
            imageUri = "https://example.com/image.jpg"

        )
        assertThat(result).isFalse()
    }

    @Test
    fun `upsertVehicle - vehicleMake is empty returns false`(){
        val result= vehicleUtil.validateVehicleInput(
            vehicleId = "ABC123",
            userId = "htrs-fdgh-8793",
            plateNo = "ABC1243",
            vehicleModel = "Model X",
            vehicleMake = "",
            vehicleName = "My Tesla Model X",
            yearMake = "2022",
            vehicleColor = "Red",
            additionalInfo = "Some additional information",
            imageUri = "https://example.com/image.jpg"

        )
        assertThat(result).isFalse()
    }

    @Test
    fun `getVehicles - non existing userId returns false`(){
        val result = vehicleUtil.getVehicles(
            userId=""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `getVehicles - valid existing userId returns true`(){
        val result = vehicleUtil.getVehicles(
            userId="htrs-fdgh-8793"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `deleteVehicle - non existing vehicleId returns false`(){
        val result = vehicleUtil.deleteVehicle(
            vehicleId=""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `deleteVehicle - valid existing vehicleId returns true`(){
        val result = vehicleUtil.deleteVehicle(
            vehicleId="sdfasdf1321"
        )
        assertThat(result).isTrue()
    }

}
