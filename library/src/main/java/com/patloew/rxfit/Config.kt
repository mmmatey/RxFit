package com.patloew.rxfit

import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataTypeCreateRequest

import java.util.concurrent.TimeUnit

import io.reactivex.Single

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
class Config internal constructor(private val rxFit: RxFit) {

    // createCustomDataType

    fun createCustomDataType(dataTypeCreateRequest: DataTypeCreateRequest): Single<DataType> {
        return createCustomDataTypeInternal(dataTypeCreateRequest, null, null)
    }

    fun createCustomDataType(dataTypeCreateRequest: DataTypeCreateRequest, timeout: Long, timeUnit: TimeUnit): Single<DataType> {
        return createCustomDataTypeInternal(dataTypeCreateRequest, timeout, timeUnit)
    }

    private fun createCustomDataTypeInternal(dataTypeCreateRequest: DataTypeCreateRequest, timeout: Long?, timeUnit: TimeUnit?): Single<DataType> {
        return Single.create(ConfigCreateCustomDataTypeSingle(rxFit, dataTypeCreateRequest, timeout, timeUnit))
    }

    // disableFit

    fun disableFit(): Single<Status> {
        return disableFitInternal(null, null)
    }

    fun disableFit(timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return disableFitInternal(timeout, timeUnit)
    }

    private fun disableFitInternal(timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(ConfigDisableFitSingle(rxFit, timeout, timeUnit))
    }

    // readDataType

    fun readDataType(dataTypeName: String): Single<DataType> {
        return readDataTypeInternal(dataTypeName, null, null)
    }

    fun readDataType(dataTypeName: String, timeout: Long, timeUnit: TimeUnit): Single<DataType> {
        return readDataTypeInternal(dataTypeName, timeout, timeUnit)
    }

    private fun readDataTypeInternal(dataTypeName: String, timeout: Long?, timeUnit: TimeUnit?): Single<DataType> {
        return Single.create(ConfigReadDataTypeSingle(rxFit, dataTypeName, timeout, timeUnit))
    }

}
