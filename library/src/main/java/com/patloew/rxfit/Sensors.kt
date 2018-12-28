package com.patloew.rxfit

import android.app.PendingIntent
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataSourcesRequest
import com.google.android.gms.fitness.request.SensorRequest
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
class Sensors internal constructor(private val rxFit: RxFit) {

    // addDataPointIntent

    fun addDataPointIntent(sensorRequest: SensorRequest, pendingIntent: PendingIntent): Single<Status> {
        return addDataPointIntentInternal(sensorRequest, pendingIntent, null, null)
    }

    fun addDataPointIntent(sensorRequest: SensorRequest, pendingIntent: PendingIntent, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return addDataPointIntentInternal(sensorRequest, pendingIntent, timeout, timeUnit)
    }

    private fun addDataPointIntentInternal(sensorRequest: SensorRequest, pendingIntent: PendingIntent, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(SensorsAddDataPointIntentSingle(rxFit, sensorRequest, pendingIntent, timeout, timeUnit))
    }

    // removeDataPointIntent

    fun removeDataPointIntent(pendingIntent: PendingIntent): Single<Status> {
        return removeDataPointIntentInternal(pendingIntent, null, null)
    }

    fun removeDataPointIntent(pendingIntent: PendingIntent, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return removeDataPointIntentInternal(pendingIntent, timeout, timeUnit)
    }

    private fun removeDataPointIntentInternal(pendingIntent: PendingIntent, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(SensorsRemoveDataPointIntentSingle(rxFit, pendingIntent, timeout, timeUnit))
    }

    // getDataPoints

    fun getDataPoints(sensorRequest: SensorRequest): Observable<DataPoint> {
        return getDataPointsInternal(sensorRequest, null, null)
    }

    fun getDataPoints(sensorRequest: SensorRequest, timeout: Long, timeUnit: TimeUnit): Observable<DataPoint> {
        return getDataPointsInternal(sensorRequest, timeout, timeUnit)
    }

    private fun getDataPointsInternal(sensorRequest: SensorRequest, timeout: Long?, timeUnit: TimeUnit?): Observable<DataPoint> {
        return Observable.create(SensorsDataPointObservable(rxFit, sensorRequest, timeout, timeUnit))
    }

    // findDataSources

    fun findDataSources(dataSourcesRequest: DataSourcesRequest): Observable<DataSource> {
        return findDataSourcesInternal(dataSourcesRequest, null, null, null)
    }

    fun findDataSources(dataSourcesRequest: DataSourcesRequest, timeout: Long, timeUnit: TimeUnit): Observable<DataSource> {
        return findDataSourcesInternal(dataSourcesRequest, null, timeout, timeUnit)
    }

    fun findDataSources(dataSourcesRequest: DataSourcesRequest, dataType: DataType): Observable<DataSource> {
        return findDataSourcesInternal(dataSourcesRequest, dataType, null, null)
    }

    fun findDataSources(dataSourcesRequest: DataSourcesRequest, dataType: DataType, timeout: Long, timeUnit: TimeUnit): Observable<DataSource> {
        return findDataSourcesInternal(dataSourcesRequest, dataType, timeout, timeUnit)
    }

    private fun findDataSourcesInternal(dataSourcesRequest: DataSourcesRequest, dataType: DataType?, timeout: Long?, timeUnit: TimeUnit?): Observable<DataSource> {
        return Single.create(SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, dataType, timeout, timeUnit))
                .flatMapObservable { Observable.fromIterable(it) }
    }

}
