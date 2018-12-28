package com.patloew.rxfit

import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.*
import com.google.android.gms.fitness.Fitness

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner

import junit.framework.Assert.assertEquals
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.never
import org.mockito.Mockito.spy
import org.mockito.Mockito.verify

@RunWith(PowerMockRunner::class)
@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, GoogleApiClient.Builder::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
class BaseRxTest : BaseOnSubscribeTest() {

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }

    @Test
    fun setupFitnessPendingResult_NoTimeout() {
        val baseRx = spy(object : BaseRx<Any>(rxFit, null, null) {
            override fun handleResolutionResult(resultCode: Int, connectionResult: ConnectionResult?) {}
        })

        val resultCallback = Mockito.mock(ResultCallback::class.java) as ResultCallback<Result>

        baseRx.setupFitnessPendingResult(pendingResult, resultCallback)

        verify<PendingResult<*>>(pendingResult).setResultCallback(resultCallback)
    }

    @Test
    fun setupFitnessPendingResult_Timeout() {
        val baseRx = spy(object : BaseRx<Any>(rxFit, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT) {
            override fun handleResolutionResult(resultCode: Int, connectionResult: ConnectionResult?) {}
        })

        val resultCallback = Mockito.mock(ResultCallback::class.java) as ResultCallback<Result>

        baseRx.setupFitnessPendingResult(pendingResult, resultCallback)

        verify<PendingResult<*>>(pendingResult).setResultCallback(resultCallback, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)
    }

    @Test
    fun createApiClient_NoScopes() {
        val builder = Mockito.mock(GoogleApiClient.Builder::class.java)

        val baseRx = spy(object : BaseRx<Any>(ctx, arrayOf<Api<out Api.ApiOptions.NotRequiredOptions>>(Fitness.HISTORY_API), null) {
            override fun handleResolutionResult(resultCode: Int, connectionResult: ConnectionResult?) {}
        })

        doReturn(builder).`when`<BaseRx<Any>>(baseRx).apiClientBuilder
        doReturn(apiClient).`when`<GoogleApiClient.Builder>(builder).build()

        val callbacks = Mockito.mock(BaseRx.ApiClientConnectionCallbacks::class.java)

        assertEquals(apiClient, baseRx.createApiClient(callbacks))
        verify<GoogleApiClient.Builder>(builder).addApi(Fitness.HISTORY_API)
        verify<GoogleApiClient.Builder>(builder).addConnectionCallbacks(callbacks)
        verify<GoogleApiClient.Builder>(builder).addOnConnectionFailedListener(callbacks)
        verify<GoogleApiClient.Builder>(builder, never()).addScope(Matchers.any(Scope::class.java))
        verify<BaseRx.ApiClientConnectionCallbacks>(callbacks).setClient(Matchers.any(GoogleApiClient::class.java))
    }

    @Test
    fun createApiClient_Scopes() {
        val builder = Mockito.mock(GoogleApiClient.Builder::class.java)

        val baseRx = spy(object : BaseRx<Any>(ctx, arrayOf<Api<out Api.ApiOptions.NotRequiredOptions>>(Fitness.HISTORY_API), arrayOf(Fitness.SCOPE_ACTIVITY_READ)) {
            override fun handleResolutionResult(resultCode: Int, connectionResult: ConnectionResult?) {}
        })

        doReturn(builder).`when`<BaseRx<Any>>(baseRx).apiClientBuilder
        doReturn(apiClient).`when`<GoogleApiClient.Builder>(builder).build()

        val callbacks = Mockito.mock(BaseRx.ApiClientConnectionCallbacks::class.java)

        assertEquals(apiClient, baseRx.createApiClient(callbacks))
        verify<GoogleApiClient.Builder>(builder).addApi(Fitness.HISTORY_API)
        verify<GoogleApiClient.Builder>(builder).addScope(Fitness.SCOPE_ACTIVITY_READ)
        verify<GoogleApiClient.Builder>(builder).addConnectionCallbacks(callbacks)
        verify<GoogleApiClient.Builder>(builder).addOnConnectionFailedListener(callbacks)
        verify<BaseRx.ApiClientConnectionCallbacks>(callbacks).setClient(Matchers.any(GoogleApiClient::class.java))
    }

    @Test
    fun onResolutionResult() {
        val baseRx = spy(object : BaseRx<Any>(ctx, arrayOf(), null) {
            override fun handleResolutionResult(resultCode: Int, connectionResult: ConnectionResult?) {}
        })

        BaseRx.observableSet.add(baseRx)

        BaseRx.onResolutionResult(0, connectionResult)
        verify<BaseRx<Any>>(baseRx).handleResolutionResult(0, connectionResult)
        assertEquals(0, BaseRx.observableSet.size)
    }

}
