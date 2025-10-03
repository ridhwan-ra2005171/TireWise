package com.mrm.tirewise.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mrm.tirewise.model.Vehicle
import com.mrm.tirewise.repository.VehicleRepository
import com.mrm.tirewise.utils.deleteImageFromStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class VehicleViewModel(application: Application) : AndroidViewModel(application) {
    
    // Vehicle repo
    private val vehicleRepository = VehicleRepository(application)

    // edit vehicle set to true if a vehicle is to be edited
    private var _editVehicle: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val editVehicleState = _editVehicle.asStateFlow()

    private val _vehicleList = MutableStateFlow<List<Vehicle>>(emptyList())
    val vehicleList: StateFlow<List<Vehicle>> = _vehicleList.asStateFlow()

    private val _currentVehicle : MutableStateFlow<Vehicle?> = MutableStateFlow(
        if (_vehicleList.value.isNotEmpty())
            _vehicleList.value[0]
        else
            null
    )

    var currentVehicle = _currentVehicle.asStateFlow()

    private var _waitingForResult = MutableStateFlow(false)
    var waitingForResult = _waitingForResult.asStateFlow()



    fun setEditVehicle(edit: Boolean) {
        _editVehicle.value = edit
    }

    private fun setWaitingForResult(value: Boolean) {
        _waitingForResult.value = value
    }


    fun assignCurrentVehicle(vehicleId: String) {
        var vehicle = _vehicleList.value.firstOrNull { it.vehicleId == vehicleId }
        if (vehicle == null) {
            vehicle = Vehicle(vehicleBrand = "test1", vehicleMake = "test1", plateNo = "0000")
            _currentVehicle.value = vehicle
            return
        }
        _currentVehicle.value = vehicle
    }

    fun resetCurrentVehicle() {
        _currentVehicle.value = null
    }

    fun getVehicles(userId: String) {
        setWaitingForResult(true)
        viewModelScope.launch(Dispatchers.IO) {
            vehicleRepository.getVehicles(userId).collect {
                _vehicleList.value = it
            }.also {
                setWaitingForResult(false)
            }
        }
    }

    // Add/Update a vehicle
    fun upsertVehicle(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO) {
            vehicleRepository.upsertVehicle(vehicle)
            refreshVehicleList(userId = vehicle.userId)
        }
    }

    fun deleteVehicle(vehicle: Vehicle) {
        viewModelScope.launch(Dispatchers.IO) {
            vehicleRepository.deleteVehicle(vehicle)
            refreshVehicleList(userId = vehicle.userId)
        }
    }

    fun deleteImageFromFirebaseStorage(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            deleteImageFromStorage(imageUrl)
        }
    }

    private fun refreshVehicleList(userId: String) {
        getVehicles(userId = userId)
    }

}