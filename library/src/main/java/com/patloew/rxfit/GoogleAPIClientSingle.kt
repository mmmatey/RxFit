package com.patloew.rxfit

import android.content.Context

import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope

import io.reactivex.Single
import io.reactivex.SingleEmitter

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
internal class GoogleAPIClientSingle internal constructor(ctx: Context, apis: Array<Api<out Api.ApiOptions.NotRequiredOptions>>, scopes: Array<Scope>?) : BaseSingle<GoogleApiClient>(ctx, apis, scopes) {

    override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: SingleEmitter<GoogleApiClient>) {
        apiClient?.let {
            subscriber.onSuccess(it)
        }
    }

    companion object {

        @SafeVarargs
        fun create(context: Context, apis: Array<Api<out Api.ApiOptions.NotRequiredOptions>>): Single<GoogleApiClient> {
            return Single.create(GoogleAPIClientSingle(context, apis, null))
        }

        fun create(context: Context, apis: Array<Api<out Api.ApiOptions.NotRequiredOptions>>, scopes: Array<Scope>): Single<GoogleApiClient> {
            return Single.create(GoogleAPIClientSingle(context, apis, scopes))
        }
    }
}
