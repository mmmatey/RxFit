package com.patloew.rxfit

import com.google.android.gms.fitness.data.Goal
import com.google.android.gms.fitness.request.GoalsReadRequest
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
class Goals internal constructor(private val rxFit: RxFit) {


    // read current

    fun readCurrent(goalsReadRequest: GoalsReadRequest): Observable<Goal> {
        return readCurrentInternal(goalsReadRequest, null, null)
    }

    fun readCurrent(goalsReadRequest: GoalsReadRequest, timeout: Long, timeUnit: TimeUnit): Observable<Goal> {
        return readCurrentInternal(goalsReadRequest, timeout, timeUnit)
    }

    private fun readCurrentInternal(goalsReadRequest: GoalsReadRequest, timeout: Long?, timeUnit: TimeUnit?): Observable<Goal> {
        return Single.create(GoalsReadCurrentSingle(rxFit, goalsReadRequest, timeout, timeUnit))
                .flatMapObservable { Observable.fromIterable(it) }
    }

}
