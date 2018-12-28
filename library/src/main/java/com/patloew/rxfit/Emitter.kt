package com.patloew.rxfit

import io.reactivex.ObservableEmitter
import io.reactivex.SingleEmitter

open class Emitter<T>(val subscriber: T) {

    fun onError(throwable: Throwable) {
        when(subscriber) {
            is ObservableEmitter<*> -> {
                subscriber.onError(throwable)
            }
            is SingleEmitter<*> -> {
                subscriber.onError(throwable)
            }
        }
    }

    fun setCancelable(cancellable: () -> Unit) {
        when(subscriber) {
            is ObservableEmitter<*> -> {
                subscriber.setCancellable{
                    cancellable()
                }
            }
            is SingleEmitter<*> -> {
                subscriber.setCancellable {
                    cancellable()
                }
            }
        }
    }

    fun isDisposed() : Boolean {
        return when(subscriber) {
            is ObservableEmitter<*> -> {
                subscriber.isDisposed
            }
            is SingleEmitter<*> -> {
                subscriber.isDisposed
            }
            else -> false
        }
    }
}