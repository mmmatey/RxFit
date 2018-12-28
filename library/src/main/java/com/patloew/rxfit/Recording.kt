package com.patloew.rxfit

import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Subscription
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
class Recording internal constructor(private val rxFit: RxFit) {

    // listSubscriptions

    fun listSubscriptions(): Observable<Subscription> {
        return listSubscriptionsInternal(null, null, null)
    }

    fun listSubscriptions(timeout: Long, timeUnit: TimeUnit): Observable<Subscription> {
        return listSubscriptionsInternal(null, timeout, timeUnit)
    }

    fun listSubscriptions(dataType: DataType): Observable<Subscription> {
        return listSubscriptionsInternal(dataType, null, null)
    }

    fun listSubscriptions(dataType: DataType, timeout: Long, timeUnit: TimeUnit): Observable<Subscription> {
        return listSubscriptionsInternal(dataType, timeout, timeUnit)
    }

    private fun listSubscriptionsInternal(dataType: DataType?, timeout: Long?, timeUnit: TimeUnit?): Observable<Subscription> {
        return Single.create(RecordingListSubscriptionsSingle(rxFit, dataType, timeout, timeUnit))
                .flatMapObservable { Observable.fromIterable(it) }
    }

    // subscribe

    fun subscribe(dataSource: DataSource): Single<Status> {
        return subscribeInternal(dataSource, null, null, null)
    }

    fun subscribe(dataSource: DataSource, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return subscribeInternal(dataSource, null, timeout, timeUnit)
    }

    fun subscribe(dataType: DataType): Single<Status> {
        return subscribeInternal(null, dataType, null, null)
    }

    fun subscribe(dataType: DataType, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return subscribeInternal(null, dataType, timeout, timeUnit)
    }

    private fun subscribeInternal(dataSource: DataSource?, dataType: DataType?, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(RecordingSubscribeSingle(rxFit, dataSource, dataType, timeout, timeUnit))
    }

    // unsubscribe

    fun unsubscribe(dataSource: DataSource): Single<Status> {
        return unsubscribeInternal(dataSource, null, null, null, null)
    }

    fun unsubscribe(dataSource: DataSource, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return unsubscribeInternal(dataSource, null, null, timeout, timeUnit)
    }

    fun unsubscribe(dataType: DataType): Single<Status> {
        return unsubscribeInternal(null, dataType, null, null, null)
    }

    fun unsubscribe(dataType: DataType, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return unsubscribeInternal(null, dataType, null, timeout, timeUnit)
    }

    fun unsubscribe(subscription: Subscription): Single<Status> {
        return unsubscribeInternal(null, null, subscription, null, null)
    }

    fun unsubscribe(subscription: Subscription, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return unsubscribeInternal(null, null, subscription, timeout, timeUnit)
    }

    private fun unsubscribeInternal(dataSource: DataSource?, dataType: DataType?, subscription: Subscription?, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(RecordingUnsubscribeSingle(rxFit, dataSource, dataType, subscription, timeout, timeUnit))
    }

}
