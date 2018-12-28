package com.patloew.rxfit

import android.app.Activity
import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Matchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.exceptions.CompositeException
import io.reactivex.observers.TestObserver

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doThrow
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`

@RunWith(PowerMockRunner::class)
@PrepareOnlyThisForTest(Observable::class, ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, BaseRx::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
class RxFitTest : BaseOnSubscribeTest() {

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }

    // RxFit

    @Test
    fun setTimeout() {
        rxFit.setDefaultTimeout(BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)
        assertEquals(BaseTest.TIMEOUT_TIME, rxFit.timeoutTime as Long)
        assertEquals(BaseTest.TIMEOUT_TIMEUNIT, rxFit.timeoutUnit)
    }

    @Test(expected = IllegalArgumentException::class)
    fun setTimeout_TimeUnitMissing() {
        rxFit.setDefaultTimeout(BaseTest.TIMEOUT_TIME, null)
        assertNull(rxFit.timeoutTime)
        assertNull(rxFit.timeoutUnit)
    }

    @Test
    fun resetDefaultTimeout() {
        rxFit.setDefaultTimeout(BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)
        rxFit.resetDefaultTimeout()
        assertNull(rxFit.timeoutTime)
        assertNull(rxFit.timeoutUnit)
    }


    // Check Connection

    @Test
    fun checkConnection() {
        val observable = Observable.empty<Void>()

        PowerMockito.mockStatic(Observable::class.java) { observable }
        val captor = ArgumentCaptor.forClass(CheckConnectionObservable::class.java)

        rxFit.checkConnection()

        PowerMockito.verifyStatic(Observable::class.java, times(1))
        Observable.create(captor.capture())

        val checkConnectionObservable = captor.value
        assertNotNull(checkConnectionObservable)
    }

    // GoogleApiClientObservable

    @Test
    fun googleAPIClientObservable_Success() {
        val single = PowerMockito.spy(GoogleAPIClientSingle(ctx, arrayOf(), arrayOf()))

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), apiClient)
    }

    @Test
    fun googleAPIClientObservable_ConnectionException() {
        val single = PowerMockito.spy(GoogleAPIClientSingle(ctx, arrayOf(), arrayOf()))

        setupBaseSingleError(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), GoogleAPIConnectionException::class.java)
    }

    // CheckConnectionCompletable

    @Test
    fun checkConnectionCompletable_Success() {
        val observable = PowerMockito.spy(CheckConnectionObservable(rxFit))

        setupBaseObservableSuccess(observable)
        val sub = Completable.fromObservable(Observable.create(observable)).test()

        sub.assertComplete()
        sub.assertNoValues()
    }

    @Test
    fun checkConnectionCompletable_Error() {
        val observable = PowerMockito.spy(CheckConnectionObservable(rxFit))

        setupBaseObservableError(observable)
        val sub = Completable.fromObservable(Observable.create(observable)).test()

        sub.assertError(GoogleAPIConnectionException::class.java)
        sub.assertNoValues()
    }


    @Test
    fun checkConnectionCompletable_Resolution_Success() {
        val apiClient2 = Mockito.mock(GoogleApiClient::class.java)
        val observable = PowerMockito.spy(CheckConnectionObservable(rxFit))

        val completable = Completable.fromObservable(Observable.create(observable))

        setupBaseObservableResolution(observable, apiClient)
        val sub1 = completable.test()

        setupBaseObservableResolution(observable, apiClient2)
        val sub2 = completable.test()

        doAnswer {
            BaseOnSubscribeTest.getSubscriber(observable, apiClient)?.apply {
                observable.onGoogleApiClientReady(apiClient, this)
            }
            null
        }.`when`(apiClient).connect()

        doAnswer {
            BaseOnSubscribeTest.getSubscriber(observable, apiClient2)?.apply {
                observable.onGoogleApiClientReady(apiClient2, this)
            }
            null
        }.`when`(apiClient2).connect()

        BaseRx.onResolutionResult(Activity.RESULT_OK, Mockito.mock(ConnectionResult::class.java))

        sub1.assertComplete()
        sub1.assertNoValues()

        sub2.assertComplete()
        sub2.assertNoValues()
    }

    @Test
    fun checkConnectionCompletable_Resolution_Error() {
        val apiClient2 = Mockito.mock(GoogleApiClient::class.java)
        val observable = PowerMockito.spy(CheckConnectionObservable(rxFit))

        val completable = Completable.fromObservable(Observable.create(observable))

        setupBaseObservableResolution(observable, apiClient)
        val sub1 = completable.test()

        setupBaseObservableResolution(observable, apiClient2)
        val sub2 = completable.test()

        BaseRx.onResolutionResult(Activity.RESULT_CANCELED, Mockito.mock(ConnectionResult::class.java))

        sub1.assertError(GoogleAPIConnectionException::class.java)
        sub1.assertNoValues()

        sub2.assertError(GoogleAPIConnectionException::class.java)
        sub2.assertNoValues()
    }

    @Test
    fun checkConnectionCompletable_Resolution_Error_ResumeNext_Resolution_Success() {
        val connectionResult = Mockito.mock(ConnectionResult::class.java)
        val observable = PowerMockito.spy(CheckConnectionObservable(rxFit))

        setupBaseObservableResolution(observable, apiClient)
        val sub = Completable.fromObservable<Void>(Observable.create(observable).compose(RxFitOnExceptionResumeNext.with(Observable.empty()))).test()

        `when`(connectionResult.hasResolution()).thenReturn(true)
        BaseRx.onResolutionResult(Activity.RESULT_CANCELED, connectionResult)

        sub.assertError { throwable -> (throwable as CompositeException).exceptions[0] is GoogleAPIConnectionException }
        sub.assertNoValues()
    }

    @Test
    fun checkConnectionCompletable_Resolution_Error_ResumeNext_NoResolution_Success() {
        val connectionResult = Mockito.mock(ConnectionResult::class.java)
        val observable = PowerMockito.spy(CheckConnectionObservable(rxFit))

        setupBaseObservableResolution(observable, apiClient)
        val sub = Completable.fromObservable<Void>(Observable.create(observable).compose(RxFitOnExceptionResumeNext.with(Observable.empty()))).test()

        `when`(connectionResult.hasResolution()).thenReturn(false)
        BaseRx.onResolutionResult(Activity.RESULT_CANCELED, connectionResult)

        sub.assertComplete()
        sub.assertNoValues()
    }

    @Test
    fun checkConnectionCompletable_Resolution_Success_ResumeNext_Error() {
        val connectionResult = Mockito.mock(ConnectionResult::class.java)
        val observable = PowerMockito.spy(CheckConnectionObservable(rxFit))

        setupBaseObservableResolution(observable, apiClient)
        val sub = Completable.fromObservable<Void>(Observable.create(observable).compose(RxFitOnExceptionResumeNext.with(Observable.empty()))).test()

        doAnswer {
            BaseOnSubscribeTest.getSubscriber(observable, apiClient)?.apply {
                observable.onGoogleApiClientReady(apiClient, this)
            }
            null
        }.`when`(apiClient).connect()

        val error = Error("Generic error")
        doThrow(error).`when`(observable).onGoogleApiClientReady(Matchers.any(GoogleApiClient::class.java), Matchers.any())
        BaseRx.onResolutionResult(Activity.RESULT_OK, connectionResult)

        sub.assertError { throwable -> (throwable as CompositeException).exceptions[0] === error }
        sub.assertNoValues()
    }

}
