package com.patloew.rxfit

import android.content.Context

import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope

import java.util.concurrent.TimeUnit

import io.reactivex.Completable
import io.reactivex.Observable

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
 * limitations under the License.
 *
 * -----------------------------
 *
 * Make sure to include all the APIs and Scopes that you need for your app.
 * Also make sure to have the Location and Body Sensors permission on
 * Marshmallow, if they are needed by your Fit API requests.
 */
class RxFit {

    var timeoutTime: Long? = null
    var timeoutUnit: TimeUnit? = null

    val ctx: Context
    val apis: Array<Api<out Api.ApiOptions.NotRequiredOptions>>
    val scopes: Array<Scope>
    val handleResolutions: Boolean

    private val ble = Ble(this)
    private val config = Config(this)
    private val goals = Goals(this)
    private val history = History(this)
    private val recording = Recording(this)
    private val sensors = Sensors(this)
    private val sessions = Sessions(this)


    /* Creates a new RxFit instance, which handles resolutions.
     *
     * @param ctx Context.
     * @param apis An array of Fitness APIs to be used in your app.
     * @param scopes An array of the Scopes to be requested for your app.
     */
    constructor(ctx: Context, apis: Array<Api<out Api.ApiOptions.NotRequiredOptions>>, scopes: Array<Scope>) {
        this.ctx = ctx.applicationContext
        this.apis = apis
        this.scopes = scopes
        this.handleResolutions = true
    }

    /* Creates a new RxFit instance.
     *
     * @param ctx Context.
     * @param apis An array of Fitness APIs to be used in your app.
     * @param scopes An array of the Scopes to be requested for your app.
     * @param handleResolutions Set whether the library should try to handle resolutions
     *                          (by showing the resolution dialog) or not. Setting this
     *                          to false can be useful for background services.
     */
    constructor(ctx: Context, apis: Array<Api<out Api.ApiOptions.NotRequiredOptions>>, scopes: Array<Scope>, handleResolutions: Boolean) {
        this.ctx = ctx.applicationContext
        this.apis = apis
        this.scopes = scopes
        this.handleResolutions = handleResolutions
    }

    /* Set a default timeout for all requests to the Fit API made in the lib.
     * When a timeout occurs, onError() is called with a StatusException.
     */
    fun setDefaultTimeout(time: Long, timeUnit: TimeUnit?) {
        timeoutTime = time
        timeoutUnit = timeUnit
    }

    /* Reset the default timeout.
     */
    fun resetDefaultTimeout() {
        timeoutTime = null
        timeoutUnit = null
    }

    /* Can be used to check whether connection to Fit API was successful.
     * For example, a wear app might need to be notified, if the user
     * allowed accessing fitness data (which means that the connection
     * was successful). As an alternative, use doOnCompleted(...) and
     * doOnError(...) on any other RxFit Observable.
     *
     * This Completable completes if the connection was successful.
     */
    fun checkConnection(): Completable {
        return Completable.fromObservable(Observable.create(CheckConnectionObservable(this)))
    }

    fun ble(): Ble {
        return ble
    }

    fun config(): Config {
        return config
    }

    fun goals(): Goals {
        return goals
    }

    fun history(): History {
        return history
    }

    fun recording(): Recording {
        return recording
    }

    fun sensors(): Sensors {
        return sensors
    }

    fun sessions(): Sessions {
        return sessions
    }
}
