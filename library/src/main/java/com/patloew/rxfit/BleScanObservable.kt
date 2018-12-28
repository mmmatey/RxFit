package com.patloew.rxfit

import androidx.annotation.RequiresPermission

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.BleDevice
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.BleScanCallback
import com.google.android.gms.fitness.request.StartBleScanRequest

import java.util.concurrent.TimeUnit

import io.reactivex.ObservableEmitter

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
internal class BleScanObservable @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
constructor(rxFit: RxFit, val dataTypes: Array<DataType>?, val stopTimeSecs: Int?, timeout: Long?, timeUnit: TimeUnit?) : BaseObservable<BleDevice>(rxFit, timeout, timeUnit) {

    private var bleScanCallback: BleScanCallback? = null

    @Suppress("DEPRECATION")
    @RequiresPermission("android.permission.BLUETOOTH_ADMIN")
    override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: ObservableEmitter<BleDevice>) {
        bleScanCallback = object : BleScanCallback() {
            override fun onDeviceFound(bleDevice: BleDevice) {
                subscriber.onNext(bleDevice)
            }

            override fun onScanStopped() {
                subscriber.onComplete()
            }
        }

        val startBleScanRequest = StartBleScanRequest.Builder().setBleScanCallback(bleScanCallback)
        if (dataTypes != null) {
            startBleScanRequest.setDataTypes(*dataTypes)
        }
        if (stopTimeSecs != null) {
            startBleScanRequest.setTimeoutSecs(stopTimeSecs)
        }

        setupFitnessPendingResult<Status>(
                Fitness.BleApi.startBleScan(apiClient, startBleScanRequest.build()),
                StatusErrorResultCallBack(subscriber)
        )
    }

    override fun onUnsubscribed(apiClient: GoogleApiClient) {
        Fitness.BleApi.stopBleScan(apiClient, bleScanCallback)
    }
}
