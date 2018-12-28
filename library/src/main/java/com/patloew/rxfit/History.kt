package com.patloew.rxfit

import android.app.PendingIntent

import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.data.Bucket
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataDeleteRequest
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataUpdateRequest
import com.google.android.gms.fitness.result.DataReadResult

import java.util.concurrent.TimeUnit

import io.reactivex.Observable
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
class History internal constructor(private val rxFit: RxFit) {

    // delete

    fun delete(dataDeleteRequest: DataDeleteRequest): Single<Status> {
        return deleteInternal(dataDeleteRequest, null, null)
    }

    fun delete(dataDeleteRequest: DataDeleteRequest, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return deleteInternal(dataDeleteRequest, timeout, timeUnit)
    }

    private fun deleteInternal(dataDeleteRequest: DataDeleteRequest, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(HistoryDeleteDataSingle(rxFit, dataDeleteRequest, timeout, timeUnit))
    }

    // insert

    fun insert(dataSet: DataSet): Single<Status> {
        return insertInternal(dataSet, null, null)
    }

    fun insert(dataSet: DataSet, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return insertInternal(dataSet, timeout, timeUnit)
    }

    private fun insertInternal(dataSet: DataSet, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(HistoryInsertDataSingle(rxFit, dataSet, timeout, timeUnit))
    }

    // readDailyTotal

    fun readDailyTotal(dataType: DataType): Single<DataSet> {
        return readDailyTotalInternal(dataType, null, null)
    }

    fun readDailyTotal(dataType: DataType, timeout: Long, timeUnit: TimeUnit): Single<DataSet> {
        return readDailyTotalInternal(dataType, timeout, timeUnit)
    }

    private fun readDailyTotalInternal(dataType: DataType, timeout: Long?, timeUnit: TimeUnit?): Single<DataSet> {
        return Single.create(HistoryReadDailyTotalSingle(rxFit, dataType, timeout, timeUnit))
    }

    // read

    fun read(dataReadRequest: DataReadRequest): Single<DataReadResult> {
        return readInternal(dataReadRequest, null, null)
    }

    fun read(dataReadRequest: DataReadRequest, timeout: Long, timeUnit: TimeUnit): Single<DataReadResult> {
        return readInternal(dataReadRequest, timeout, timeUnit)
    }

    private fun readInternal(dataReadRequest: DataReadRequest, timeout: Long?, timeUnit: TimeUnit?): Single<DataReadResult> {
        return Single.create(HistoryReadDataSingle(rxFit, dataReadRequest, timeout, timeUnit))
    }

    fun readBuckets(dataReadRequest: DataReadRequest): Observable<Bucket> {
        return readBucketsInternal(dataReadRequest, null, null)
    }

    fun readBuckets(dataReadRequest: DataReadRequest, timeout: Long, timeUnit: TimeUnit): Observable<Bucket> {
        return readBucketsInternal(dataReadRequest, timeout, timeUnit)
    }

    private fun readBucketsInternal(dataReadRequest: DataReadRequest, timeout: Long?, timeUnit: TimeUnit?): Observable<Bucket> {
        return readInternal(dataReadRequest, timeout, timeUnit)
                .flatMapObservable { dataReadResult -> Observable.fromIterable(dataReadResult.buckets) }
    }

    fun readDataSets(dataReadRequest: DataReadRequest): Observable<DataSet> {
        return readDataSetsInternal(dataReadRequest, null, null)
    }

    fun readDataSets(dataReadRequest: DataReadRequest, timeout: Long, timeUnit: TimeUnit): Observable<DataSet> {
        return readDataSetsInternal(dataReadRequest, timeout, timeUnit)
    }

    private fun readDataSetsInternal(dataReadRequest: DataReadRequest, timeout: Long?, timeUnit: TimeUnit?): Observable<DataSet> {
        return readInternal(dataReadRequest, timeout, timeUnit)
                .flatMapObservable { dataReadResult -> Observable.fromIterable(dataReadResult.dataSets) }
    }


    // update

    fun update(dataUpdateRequest: DataUpdateRequest): Single<Status> {
        return updateInternal(dataUpdateRequest, null, null)
    }

    fun update(dataUpdateRequest: DataUpdateRequest, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return updateInternal(dataUpdateRequest, timeout, timeUnit)
    }

    private fun updateInternal(dataUpdateRequest: DataUpdateRequest, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(HistoryUpdateDataSingle(rxFit, dataUpdateRequest, timeout, timeUnit))
    }

    // register data update listener

    fun registerDataUpdateListener(pendingIntent: PendingIntent, dataSource: DataSource): Single<Status> {
        return registerDataUpdateListenerInternal(pendingIntent, dataSource, null, null, null)
    }

    fun registerDataUpdateListener(pendingIntent: PendingIntent, dataSource: DataSource, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return registerDataUpdateListenerInternal(pendingIntent, dataSource, null, timeout, timeUnit)
    }

    fun registerDataUpdateListener(pendingIntent: PendingIntent, dataType: DataType): Single<Status> {
        return registerDataUpdateListenerInternal(pendingIntent, null, dataType, null, null)
    }

    fun registerDataUpdateListener(pendingIntent: PendingIntent, dataType: DataType, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return registerDataUpdateListenerInternal(pendingIntent, null, dataType, timeout, timeUnit)
    }

    fun registerDataUpdateListener(pendingIntent: PendingIntent, dataSource: DataSource, dataType: DataType): Single<Status> {
        return registerDataUpdateListenerInternal(pendingIntent, dataSource, dataType, null, null)
    }

    fun registerDataUpdateListener(pendingIntent: PendingIntent, dataSource: DataSource, dataType: DataType, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return registerDataUpdateListenerInternal(pendingIntent, dataSource, dataType, timeout, timeUnit)
    }

    private fun registerDataUpdateListenerInternal(pendingIntent: PendingIntent, dataSource: DataSource?, dataType: DataType?, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, dataType, timeout, timeUnit))
    }

    // unregister data update listener

    fun unregisterDataUpdateListener(pendingIntent: PendingIntent): Single<Status> {
        return unregisterDataUpdateListenerInternal(pendingIntent, null, null)
    }

    fun unregisterDataUpdateListener(pendingIntent: PendingIntent, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return unregisterDataUpdateListenerInternal(pendingIntent, timeout, timeUnit)
    }

    private fun unregisterDataUpdateListenerInternal(pendingIntent: PendingIntent, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(HistoryUnregisterDataUpdateListenerSingle(rxFit, pendingIntent, timeout, timeUnit))
    }

}
