package com.patloew.rxfit

import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.request.SessionReadRequest
import com.google.android.gms.fitness.result.SessionReadResult

import java.util.concurrent.TimeUnit

import io.reactivex.SingleEmitter

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
internal class SessionReadSingle(rxFit: RxFit, val sessionReadRequest: SessionReadRequest, timeout: Long?, timeUnit: TimeUnit?) : BaseSingle<SessionReadResult>(rxFit, timeout, timeUnit) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: SingleEmitter<SessionReadResult>) {
        setupFitnessPendingResult(
                Fitness.SessionsApi.readSession(apiClient, sessionReadRequest),
                SingleResultCallBack[subscriber]
        )
    }
}
