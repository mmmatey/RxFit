package com.patloew.rxfit

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner

import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Cancellable
import io.reactivex.observers.TestObserver

import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.verifyZeroInteractions

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, SingleEmitter::class)
class BaseSingleTest : BaseOnSubscribeTest() {

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }

    @Test
    fun baseObservable_ApiClient_Connected() {
        val `object` = Any()
        val single = spy(object : BaseSingle<Any>(ctx, arrayOf(), null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: SingleEmitter<Any>) {
                subscriber.onSuccess(`object`)
            }
        })

        doAnswer { invocation ->
            val callbacks = invocation.getArgument<BaseRx.ApiClientConnectionCallbacks>(0)
            callbacks.setClient(apiClient)
            callbacks.onConnected(null)
            apiClient
        }.`when`<BaseSingle<Any>>(single).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks::class.java))

        val sub = Single.create<Any>(single).test()

        sub.assertValue(`object`)
        sub.assertComplete()
    }

    @Test
    fun baseObservable_ApiClient_ConnectionSuspended() {
        val `object` = Any()
        val single = spy(object : BaseSingle<Any>(ctx, arrayOf(), null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: SingleEmitter<Any>) {
                subscriber.onSuccess(`object`)
            }
        })

        doAnswer { invocation ->
            val callbacks = invocation.getArgument<BaseRx.ApiClientConnectionCallbacks>(0)
            callbacks.setClient(apiClient)
            callbacks.onConnectionSuspended(0)
            apiClient
        }.`when`<BaseSingle<Any>>(single).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks::class.java))

        val sub = Single.create<Any>(single).test()

        sub.assertNoValues()
        sub.assertError(GoogleAPIConnectionSuspendedException::class.java)
    }

    @Test
    fun baseObservable_ApiClient_ConnectionFailed_NoResulution() {
        val `object` = Any()
        val single = spy(object : BaseSingle<Any>(ctx, arrayOf(), null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: SingleEmitter<Any>) {
                subscriber.onSuccess(`object`)
            }
        })

        doReturn(false).`when`(connectionResult).hasResolution()

        doAnswer { invocation ->
            val callbacks = invocation.getArgument<BaseRx.ApiClientConnectionCallbacks>(0)
            callbacks.setClient(apiClient)
            callbacks.onConnectionFailed(connectionResult)
            apiClient
        }.`when`<BaseSingle<Any>>(single).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks::class.java))

        val sub = Single.create<Any>(single).test()

        sub.assertNoValues()
        sub.assertError(GoogleAPIConnectionException::class.java)
    }

    @Test
    fun baseObservable_ApiClient_ConnectionFailed_Resolution() {
        val `object` = Any()
        val single = spy(object : BaseSingle<Any>(rxFit, null, null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: SingleEmitter<Any>) {
                subscriber.onSuccess(`object`)
            }
        })

        doReturn(true).`when`(connectionResult).hasResolution()

        doAnswer { invocation ->
            val callbacks = invocation.getArgument<BaseRx.ApiClientConnectionCallbacks>(0)
            callbacks.setClient(apiClient)
            callbacks.onConnectionFailed(connectionResult)
            apiClient
        }.`when`<BaseSingle<Any>>(single).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks::class.java))

        val sub = Single.create<Any>(single).test()

        sub.assertNoValues()
        sub.assertNotTerminated()

        verify<Context>(ctx).startActivity(Matchers.any(Intent::class.java))
    }

    @Test
    fun handleResolutionResult_ResultOK() {
        val `object` = Any()
        val single = spy(object : BaseSingle<Any>(rxFit, null, null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: SingleEmitter<Any>) {
                subscriber.onSuccess(`object`)
            }
        })

        val sub = PowerMockito.spy(object : SingleEmitter<Any> {
            override fun tryOnError(t: Throwable): Boolean {
                return false
            }
            override fun onSuccess(value: Any) {}
            override fun onError(error: Throwable) {}
            override fun setDisposable(s: Disposable?) {}
            override fun setCancellable(c: Cancellable?) {}
            override fun isDisposed(): Boolean {
                return false
            }
        })

        PowerMockito.doReturn(false).`when`<SingleEmitter<*>>(sub).isDisposed
        single.subscriptionInfoMap[apiClient] = Emitter<SingleEmitter<Any>>(sub)

        single.handleResolutionResult(Activity.RESULT_OK, connectionResult)

        verify(apiClient).connect()
        verifyNoMoreInteractions(apiClient)
        verify<SingleEmitter<*>>(sub, never()).onError(Matchers.any(Throwable::class.java))
    }

    @Test
    fun handleResolutionResult_ResultOK_ConnectionException() {
        val `object` = Any()
        val single = spy(object : BaseSingle<Any>(rxFit, null, null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: SingleEmitter<Any>) {
                subscriber.onSuccess(`object`)
            }
        })

        val sub = PowerMockito.spy(object : SingleEmitter<Any> {
            override fun tryOnError(t: Throwable): Boolean {
                return false
            }
            override fun onSuccess(value: Any) {}
            override fun onError(error: Throwable) {}
            override fun setDisposable(s: Disposable?) {}
            override fun setCancellable(c: Cancellable?) {}
            override fun isDisposed(): Boolean {
                return false
            }
        })

        val exception = RuntimeException()
        doThrow(exception).`when`(apiClient).connect()
        PowerMockito.doReturn(false).`when`<SingleEmitter<*>>(sub).isDisposed
        single.subscriptionInfoMap[apiClient] = Emitter<SingleEmitter<Any>>(sub)

        single.handleResolutionResult(Activity.RESULT_OK, connectionResult)

        verify(apiClient).connect()
        verifyNoMoreInteractions(apiClient)
        verify<SingleEmitter<*>>(sub).onError(exception)
    }

    @Test
    fun handleResolutionResult_ResultCanceled() {
        val `object` = Any()
        val single = spy(object : BaseSingle<Any>(rxFit, null, null) {
            override fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: SingleEmitter<Any>) {
                subscriber.onSuccess(`object`)
            }
        })

        val sub = PowerMockito.spy(object : SingleEmitter<Any> {
            override fun tryOnError(t: Throwable): Boolean {
                return false
            }
            override fun onSuccess(value: Any) {}
            override fun onError(error: Throwable) {}
            override fun setDisposable(s: Disposable?) {}
            override fun setCancellable(c: Cancellable?) {}
            override fun isDisposed(): Boolean {
                return false
            }
        })

        PowerMockito.doReturn(false).`when`<SingleEmitter<*>>(sub).isDisposed
        single.subscriptionInfoMap[apiClient] = Emitter<SingleEmitter<Any>>(sub)

        single.handleResolutionResult(Activity.RESULT_CANCELED, connectionResult)

        verifyZeroInteractions(apiClient)
        verify<SingleEmitter<*>>(sub).onError(Matchers.any(GoogleAPIConnectionException::class.java))
    }

}
