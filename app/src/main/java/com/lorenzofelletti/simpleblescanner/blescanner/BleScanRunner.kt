package com.lorenzofelletti.simpleblescanner.blescanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.util.Log
import com.lorenzofelletti.simpleblescanner.BuildConfig.DEBUG
import com.lorenzofelletti.simpleblescanner.blescanner.model.BleScanCallback
import kotlin.math.pow

class BleScanRunner(private val btManager: BluetoothManager) {
    private lateinit var bleScanManager: BleScanManager

    private lateinit var foundDevices: MutableList<List<String>>

    @SuppressLint("MissingPermission")
    fun searchDevices() {

        if (DEBUG) Log.e(TAG, "Running Scan")

        foundDevices = emptyList<List<String>>().toMutableList()

        bleScanManager = BleScanManager(btManager, 5000, scanCallback = BleScanCallback({
            val macAdress = it?.device?.address.toString()
            val rssi = it?.rssi
            val txPower = it?.txPower
            val plZero = 0.0

            // verificar e documentar essa função
            val distance = 10.0001

            val device:MutableList<String> = mutableListOf(macAdress, rssi.toString(), String.format("%.3f", distance))

            var hasDevice = false
            foundDevices.forEach{ it ->
                if (it[0].equals(macAdress)) {
                    true.also { hasDevice = true }
                }

            }
            if (DEBUG) Log.e(TAG, "Device: $device $hasDevice")

            if (!hasDevice) foundDevices += device

        }))

        bleScanManager.afterScanActions.add {
            if (DEBUG) Log.e(TAG, "Devices: $foundDevices")
        }

        bleScanManager.scanBleDevices()

    }

    companion object {
        private val TAG = BleScanRunner::class.java.simpleName
    }
}