package com.patloew.rxfit

import android.app.PendingIntent
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.data.Session
import com.google.android.gms.fitness.request.SessionInsertRequest
import com.google.android.gms.fitness.request.SessionReadRequest
import com.google.android.gms.fitness.result.SessionReadResult
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
class Sessions internal constructor(private val rxFit: RxFit) {

    // insert

    fun insert(sessionInsertRequest: SessionInsertRequest): Single<Status> {
        return insertInternal(sessionInsertRequest, null, null)
    }

    fun insert(sessionInsertRequest: SessionInsertRequest, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return insertInternal(sessionInsertRequest, timeout, timeUnit)
    }

    private fun insertInternal(sessionInsertRequest: SessionInsertRequest, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(SessionInsertSingle(rxFit, sessionInsertRequest, timeout, timeUnit))
    }

    // read

    fun read(sessionReadRequest: SessionReadRequest): Single<SessionReadResult> {
        return readInternal(sessionReadRequest, null, null)
    }

    fun read(sessionReadRequest: SessionReadRequest, timeout: Long, timeUnit: TimeUnit): Single<SessionReadResult> {
        return readInternal(sessionReadRequest, timeout, timeUnit)
    }

    private fun readInternal(sessionReadRequest: SessionReadRequest, timeout: Long?, timeUnit: TimeUnit?): Single<SessionReadResult> {
        return Single.create(SessionReadSingle(rxFit, sessionReadRequest, timeout, timeUnit))
    }

    // registerForSessions

    fun registerForSessions(pendingIntent: PendingIntent): Single<Status> {
        return registerForSessionsInternal(pendingIntent, null, null)
    }

    fun registerForSessions(pendingIntent: PendingIntent, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return registerForSessionsInternal(pendingIntent, timeout, timeUnit)
    }

    private fun registerForSessionsInternal(pendingIntent: PendingIntent, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(SessionRegisterSingle(rxFit, pendingIntent, timeout, timeUnit))
    }

    // unregisterForSessions

    fun unregisterForSessions(pendingIntent: PendingIntent): Single<Status> {
        return unregisterForSessionsInternal(pendingIntent, null, null)
    }

    fun unregisterForSessions(pendingIntent: PendingIntent, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return unregisterForSessionsInternal(pendingIntent, timeout, timeUnit)
    }

    private fun unregisterForSessionsInternal(pendingIntent: PendingIntent, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(SessionUnregisterSingle(rxFit, pendingIntent, timeout, timeUnit))
    }

    // start

    fun start(session: Session): Single<Status> {
        return startInternal(session, null, null)
    }

    fun start(session: Session, timeout: Long, timeUnit: TimeUnit): Single<Status> {
        return startInternal(session, timeout, timeUnit)
    }

    private fun startInternal(session: Session, timeout: Long?, timeUnit: TimeUnit?): Single<Status> {
        return Single.create(SessionStartSingle(rxFit, session, timeout, timeUnit))
    }

    // stop

    fun stop(identifier: String): Observable<Session> {
        return stopInternal(identifier, null, null)
    }

    fun stop(identifier: String, timeout: Long, timeUnit: TimeUnit): Observable<Session> {
        return stopInternal(identifier, timeout, timeUnit)
    }

    private fun stopInternal(identifier: String, timeout: Long?, timeUnit: TimeUnit?): Observable<Session> {
        return Single.create(SessionStopSingle(rxFit, identifier, timeout, timeUnit))
                .flatMapObservable { Observable.fromIterable(it) }
    }


}
