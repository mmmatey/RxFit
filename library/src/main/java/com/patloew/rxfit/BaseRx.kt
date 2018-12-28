package com.patloew.rxfit

import android.content.Context

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.*
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.result.DataReadResponse
import com.google.android.gms.tasks.Task

import java.util.HashSet
import java.util.concurrent.TimeUnit

/* Copyright (C) 2015 Michał Charmas (http://blog.charmas.pl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---------------
 *
 * FILE MODIFIED by Patrick Löwenstein, 2016
 *
 */
@Suppress("unused")
abstract class BaseRx<T> {

    protected val ctx: Context
    private val services: Array<Api<out Api.ApiOptions.NotRequiredOptions>>
    protected val scopes: Array<Scope>?
    protected val handleResolution: Boolean
    val timeoutTime: Long?
    val timeoutUnit: TimeUnit?

    val apiClientBuilder: GoogleApiClient.Builder
        get() = GoogleApiClient.Builder(ctx)

    protected constructor(rxFit: RxFit, timeout: Long?, timeUnit: TimeUnit?) {
        this.ctx = rxFit.ctx
        this.services = rxFit.apis
        this.scopes = rxFit.scopes
        this.handleResolution = rxFit.handleResolutions

        if (timeout != null && timeUnit != null) {
            this.timeoutTime = timeout
            this.timeoutUnit = timeUnit
        } else {
            this.timeoutTime = rxFit.timeoutTime
            this.timeoutUnit = rxFit.timeoutUnit
        }
    }

    protected constructor(ctx: Context, services: Array<Api<out Api.ApiOptions.NotRequiredOptions>>, scopes: Array<Scope>?) {
        this.ctx = ctx
        this.services = services
        this.scopes = scopes
        this.handleResolution = false
        timeoutTime = null
        timeoutUnit = null
    }

    fun <T : Result> setupFitnessPendingResult(pendingResult: PendingResult<T>, resultCallback: ResultCallback<T>) {
        if (timeoutTime != null && timeoutUnit != null) {
            pendingResult.setResultCallback(resultCallback, timeoutTime, timeoutUnit)
        } else {
            pendingResult.setResultCallback(resultCallback)
        }
    }

    fun createApiClient(apiClientConnectionCallbacks: ApiClientConnectionCallbacks): GoogleApiClient {
        val apiClientBuilder = apiClientBuilder
        for (service in services) {
            apiClientBuilder.addApi(service)
        }

        if (scopes != null) {
            for (scope in scopes) {
                apiClientBuilder.addScope(scope)
            }
        }

        apiClientBuilder.addConnectionCallbacks(apiClientConnectionCallbacks)
        apiClientBuilder.addOnConnectionFailedListener(apiClientConnectionCallbacks)

        val apiClient = apiClientBuilder.build()

        apiClientConnectionCallbacks.setClient(apiClient)

        return apiClient
    }

    protected open fun onUnsubscribed(apiClient: GoogleApiClient) {}

    abstract fun handleResolutionResult(resultCode: Int, connectionResult: ConnectionResult?)

    abstract class ApiClientConnectionCallbacks protected constructor() : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

        protected open var apiClient: GoogleApiClient? = null

        open fun setClient(client: GoogleApiClient) {
            this.apiClient = client
        }
    }

    companion object {
        val observableSet: MutableSet<BaseRx<*>> = HashSet()

        fun onResolutionResult(resultCode: Int, connectionResult: ConnectionResult?) {
            for (observable in observableSet) {
                observable.handleResolutionResult(resultCode, connectionResult)
            }
            observableSet.clear()
        }
    }
}
