package com.mrm.tirewise.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mrm.tirewise.model.TirePosition
import com.mrm.tirewise.model.TireScan
import com.mrm.tirewise.repository.TireScanRepository
import com.mrm.tirewise.view.screens.tireList.SortBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TireScanViewModel(application: Application) : AndroidViewModel(application ) {

    private val tireScanRepository by lazy {
        TireScanRepository(application)
    }

    var currentTirePosition = TirePosition.ALL
        private set

    private var _currentTireScan = MutableStateFlow(TireScan())
    val currentTireScan = _currentTireScan.asStateFlow()

    private val _tireScansList = MutableStateFlow<List<TireScan>>(emptyList())
    val tireScansList = _tireScansList.asStateFlow()

    private var _waitingForResult = MutableStateFlow(false)
    var waitingForResult = _waitingForResult.asStateFlow()
    fun setWaitingForResult(waiting: Boolean) {
        _waitingForResult.value = waiting
    }

    fun setCurrentTirePosition(position: TirePosition) {
        currentTirePosition =
//            .value
        position
        Log.d ( "__POSITION", currentTirePosition.toString())
    }

    fun setCurrentTireScan(tireScan: TireScan) {
        _currentTireScan.value = tireScan
    }

    @SuppressLint("SuspiciousIndentation")
    fun getTireScanList(vehicleId: String ) {
        viewModelScope.launch(Dispatchers.IO) {
             _tireScansList.value = tireScanRepository.getTireScans(vehicleId, currentTirePosition.getPositionAsString()
//                    .value.getPositionAsString()
                )
                Log.d( "__SCANS", "tireRepo: ${tireScansList.value}")

        }
    }

    fun sortTireScans(vehicleId: String, sortBy: SortBy) {
        viewModelScope.launch(Dispatchers.IO) {
            _tireScansList.value = tireScanRepository.sortTireScans(vehicleId, currentTirePosition.getPositionAsString(), sortBy)
        }
    }


    fun uploadTireScan(tireScan: TireScan) {
        setWaitingForResult(true)
           viewModelScope.launch(Dispatchers.IO) {
              tireScanRepository.uploadTireScan(tireScan)
           }.also { setWaitingForResult(false) }
    }

    fun deleteTireScan(tireScan: TireScan) {
        viewModelScope.launch(Dispatchers.IO) {
            tireScanRepository.deleteTireScan(tireScan)
        }
    }
}