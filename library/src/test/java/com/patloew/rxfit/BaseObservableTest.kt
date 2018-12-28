package com.patloew.rxfit

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.MockitoAnnotations
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner

import io.reactivex.Observable
import io.reactivex.ObservableEmitter

import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@RunWith(PowerMockRunner::class)
class BaseObservableTest : BaseOnSubscribeTest() {

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }

    @Test
    fun baseObservable_ApiClient_Connected() {
        val next = Any()
        val observable = spy(object : BaseObservable<Any>(ctx, arrayOf(), null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: ObservableEmitter<Any>) {
                subscriber.onNext(next)
                subscriber.onComplete()
            }
        })

        doAnswer {
            val callbacks = it.getArgument<BaseRx.ApiClientConnectionCallbacks>(0)
            callbacks.setClient(apiClient)
            callbacks.onConnected(null)
            apiClient
        }.`when`<BaseObservable<Any>>(observable).createApiClient(Matchers.any<BaseRx.ApiClientConnectionCallbacks>(BaseRx.ApiClientConnectionCallbacks::class.java))

        val sub = Observable.create<Any>(observable).test()

        sub.assertValue(next)
        sub.assertComplete()
    }

    @Test
    fun baseObservable_ApiClient_ConnectionSuspended() {
        val next = Any()
        val observable = spy(object : BaseObservable<Any>(ctx, arrayOf(), null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: ObservableEmitter<Any>) {
                subscriber.onNext(next)
                subscriber.onComplete()
            }
        })

        doAnswer {
            val callbacks = it.getArgument<BaseRx.ApiClientConnectionCallbacks>(0)
            callbacks.setClient(apiClient)
            callbacks.onConnectionSuspended(0)
            apiClient
        }.`when`<BaseObservable<Any>>(observable).createApiClient(Matchers.any<BaseRx.ApiClientConnectionCallbacks>(BaseRx.ApiClientConnectionCallbacks::class.java))

        val sub = Observable.create<Any>(observable).test()

        sub.assertNoValues()
        sub.assertError(GoogleAPIConnectionSuspendedException::class.java)
    }

    @Test
    fun baseObservable_ApiClient_ConnectionFailed_NoResulution() {
        val `object` = Any()
        val observable = spy(object : BaseObservable<Any>(ctx, arrayOf(), null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: ObservableEmitter<Any>) {
                subscriber.onNext(`object`)
                subscriber.onComplete()
            }
        })

        doReturn(false).`when`(connectionResult).hasResolution()

        doAnswer {
            val callbacks = it.getArgument<BaseRx.ApiClientConnectionCallbacks>(0)
            callbacks.setClient(apiClient)
            callbacks.onConnectionFailed(connectionResult)
            apiClient
        }.`when`<BaseObservable<Any>>(observable).createApiClient(Matchers.any<BaseRx.ApiClientConnectionCallbacks>(BaseRx.ApiClientConnectionCallbacks::class.java))

        val sub = Observable.create<Any>(observable).test()

        sub.assertNoValues()
        sub.assertError(GoogleAPIConnectionException::class.java)
    }

    @Test
    fun baseObservable_ApiClient_ConnectionFailed_Resolution() {
        val `object` = Any()
        val observable = spy(object : BaseObservable<Any>(rxFit, null, null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: ObservableEmitter<Any>) {
                subscriber.onNext(`object`)
                subscriber.onComplete()
            }
        })

        doReturn(true).`when`(connectionResult).hasResolution()

        doAnswer {
            val callbacks = it.getArgument<BaseRx.ApiClientConnectionCallbacks>(0)
            callbacks.setClient(apiClient)
            callbacks.onConnectionFailed(connectionResult)
            apiClient
        }.`when`<BaseObservable<Any>>(observable).createApiClient(Matchers.any<BaseRx.ApiClientConnectionCallbacks>(BaseRx.ApiClientConnectionCallbacks::class.java))

        val sub = Observable.create<Any>(observable).test()

        sub.assertNoValues()
        sub.assertNotTerminated()

        verify<Context>(ctx).startActivity(Matchers.any(Intent::class.java))
    }

}
