package com.patloew.rxfit

import androidx.annotation.RequiresPermission
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.data.BleDevice
import com.google.android.gms.fitness.data.DataType
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.TimeUnit

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
class Ble internal constructor(private val rxFit: RxFit) {

    // getClaimedDevices

    val claimedDevices: Observable<BleDevice>
        get() = getClaimedDeviceListInternal(null, null, null)

    // claimDevice

    fun claimDevice(bleDevice: BleDevice): Single<Status> {
        return claimDeviceInternal(bleDevice, null, null, null)
    }

    fun claimDevice(bleDevice: BleDevice, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return claimDeviceInternal(bleDevice, null, timeout, timeUnit)
    }

    fun claimDevice(deviceAddress: String): Single<Status> {
        return claimDeviceInternal(null, deviceAddress, null, null)
    }

    fun claimDevice(deviceAddress: String, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return claimDeviceInternal(null, deviceAddress, timeout, timeUnit)
    }

    private fun claimDeviceInternal(bleDevice: BleDevice?, deviceAddress: String?, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(BleClaimDeviceSingle(rxFit, bleDevice, deviceAddress, timeout, timeUnit))
    }

    fun getClaimedDevices(timeout: Long, timeUnit: TimeUnit): Observable<BleDevice> {
        return getClaimedDeviceListInternal(null, timeout, timeUnit)
    }

    fun getClaimedDevices(dataType: DataType): Observable<BleDevice> {
        return getClaimedDeviceListInternal(dataType, null, null)
    }

    fun getClaimedDevices(dataType: DataType, timeout: Long, timeUnit: TimeUnit): Observable<BleDevice> {
        return getClaimedDeviceListInternal(dataType, timeout, timeUnit)
    }

    private fun getClaimedDeviceListInternal(dataType: DataType?, timeout: Long?, timeUnit: TimeUnit?): Observable<BleDevice> {
        return Single.create(BleListClaimedDevicesSingle(rxFit, dataType, timeout, timeUnit))
                .flatMapObservable { Observable.fromIterable(it) }
    }

    // scan

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    fun scan(): Observable<BleDevice> {
        return scanInternal(null, null, null, null)
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    fun scan(timeout: Long, timeUnit: TimeUnit): Observable<BleDevice> {
        return scanInternal(null, null, timeout, timeUnit)
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    fun scan(dataTypes: Array<DataType>?): Observable<BleDevice> {
        return scanInternal(dataTypes, null, null, null)
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    fun scan(dataTypes: Array<DataType>, timeout: Long, timeUnit: TimeUnit): Observable<BleDevice> {
        return scanInternal(dataTypes, null, timeout, timeUnit)
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    fun scan(stopTimeSecs: Int): Observable<BleDevice> {
        return scanInternal(null, stopTimeSecs, null, null)
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    fun scan(stopTimeSecs: Int, timeout: Long, timeUnit: TimeUnit): Observable<BleDevice> {
        return scanInternal(null, stopTimeSecs, timeout, timeUnit)
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    fun scan(dataTypes: Array<DataType>, stopTimeSecs: Int): Observable<BleDevice> {
        return scanInternal(dataTypes, stopTimeSecs, null, null)
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    fun scan(dataTypes: Array<DataType>, stopTimeSecs: Int, timeout: Long, timeUnit: TimeUnit): Observable<BleDevice> {
        return scanInternal(dataTypes, stopTimeSecs, timeout, timeUnit)
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    private fun scanInternal(dataTypes: Array<DataType>? = arrayOf(), stopTimeSecs: Int?, timeout: Long?, timeUnit: TimeUnit?): Observable<BleDevice> {
        return Observable.create(BleScanObservable(rxFit, dataTypes, stopTimeSecs, timeout, timeUnit))
    }

    // unclaim Device
    fun unclaimDevice(bleDevice: BleDevice): Single<Status> {
        return unclaimDeviceInternal(bleDevice, null, null, null)
    }

    fun unclaimDevice(bleDevice: BleDevice, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return unclaimDeviceInternal(bleDevice, null, timeout, timeUnit)
    }

    fun unclaimDevice(deviceAddress: String): Single<Status> {
        return unclaimDeviceInternal(null, deviceAddress, null, null)
    }

    fun unclaimDevice(deviceAddress: String, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return unclaimDeviceInternal(null, deviceAddress, timeout, timeUnit)
    }

    private fun unclaimDeviceInternal(bleDevice: BleDevice?, deviceAddress: String?, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(BleUnclaimDeviceSingle(rxFit, bleDevice, deviceAddress, timeout, timeUnit))
    }

}
