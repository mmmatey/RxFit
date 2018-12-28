package com.patloew.rxfit

import com.google.android.gms.common.api.Result
import com.google.android.gms.common.api.ResultCallback

import io.reactivex.SingleEmitter
import io.reactivex.functions.Function

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
internal class SingleResultCallBack<T : Result, R> private constructor(private val subscriber: SingleEmitter<R>, private val mapper: Function<T, R>) : ResultCallback<T> {

    override fun onResult(result: T) {
        if (!result.status.isSuccess) {
            subscriber.onError(StatusException(result.status))
        } else {
            try {
                subscriber.onSuccess(mapper.apply(result))
            } catch (e: Exception) {
                subscriber.onError(e)
            }

        }
    }

    companion object {

        operator fun <T : Result, R> get(subscriber: SingleEmitter<R>, mapper: Function<T, R>): ResultCallback<T> {
            return SingleResultCallBack(subscriber, mapper)
        }

        operator fun <T : Result> get(subscriber: SingleEmitter<T>): ResultCallback<T> {
            return SingleResultCallBack(subscriber, Function { input -> input })
        }
    }
}
