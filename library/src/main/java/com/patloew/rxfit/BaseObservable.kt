package com.patloew.rxfit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe

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
abstract class BaseObservable<T> : BaseRxObservable<T, ObservableEmitter<T>, Emitter<ObservableEmitter<T>>>, ObservableOnSubscribe<T> {
    protected constructor(rxFit: RxFit, timeout: Long?, timeUnit: TimeUnit?) : super(rxFit, timeout, timeUnit)

    protected constructor(ctx: Context, services: Array<Api<out Api.ApiOptions.NotRequiredOptions>>, scopes: Array<Scope>?) : super(ctx, services, scopes)

    @Throws(Exception::class)
    override fun subscribe(subscriber: ObservableEmitter<T>) {
        subscribeEmitter(Emitter(subscriber))
    }
}
