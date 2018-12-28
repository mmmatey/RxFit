package com.patloew.rxfit

import android.app.PendingIntent
import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.Session
import com.google.android.gms.fitness.request.SessionInsertRequest
import com.google.android.gms.fitness.request.SessionReadRequest
import com.google.android.gms.fitness.result.SessionReadResult
import com.google.android.gms.fitness.result.SessionStopResult

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner

import java.util.ArrayList

import io.reactivex.Single

import org.mockito.Mockito.`when`

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, BaseRx::class)
class SessionOnSubScribeTest : BaseOnSubscribeTest() {

    @Mock
    internal lateinit var session: Session

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }


    // SessionInsertObservable

    @Test
    fun SessionInsertObservable_Success() {
        val sessionInsertRequest = Mockito.mock(SessionInsertRequest::class.java)
        val single = PowerMockito.spy(SessionInsertSingle(rxFit, sessionInsertRequest, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sessionsApi.insertSession(apiClient, sessionInsertRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun SessionInsertObservable_StatusException() {
        val sessionInsertRequest = Mockito.mock(SessionInsertRequest::class.java)
        val single = PowerMockito.spy(SessionInsertSingle(rxFit, sessionInsertRequest, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sessionsApi.insertSession(apiClient, sessionInsertRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // SessionRegisterObservable

    @Test
    fun SessionRegisterObservable_Success() {
        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val single = PowerMockito.spy(SessionRegisterSingle(rxFit, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sessionsApi.registerForSessions(apiClient, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun SessionRegisterObservable_StatusException() {
        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val single = PowerMockito.spy(SessionRegisterSingle(rxFit, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sessionsApi.registerForSessions(apiClient, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // SessionUnregisterObservable

    @Test
    fun SessionUnregisterObservable_Success() {
        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val single = PowerMockito.spy(SessionUnregisterSingle(rxFit, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sessionsApi.unregisterForSessions(apiClient, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun SessionUnregisterObservable_StatusException() {
        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val single = PowerMockito.spy(SessionUnregisterSingle(rxFit, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sessionsApi.unregisterForSessions(apiClient, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // SessionStartObservable

    @Test
    fun SessionStartObservable_Success() {
        val session = Mockito.mock(Session::class.java)
        val single = PowerMockito.spy(SessionStartSingle(rxFit, session, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sessionsApi.startSession(apiClient, session)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun SessionStartObservable_StatusException() {
        val single = PowerMockito.spy(SessionStartSingle(rxFit, session, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sessionsApi.startSession(apiClient, session)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // SessionStopObservable

    @Test
    fun SessionStopObservable_Success() {
        val identifier = "identifier"
        val sessionStopResult = Mockito.mock(SessionStopResult::class.java)
        val single = PowerMockito.spy(SessionStopSingle(rxFit, identifier, null, null))

        val sessionList = ArrayList<Session>()
        sessionList.add(session)

        `when`(sessionStopResult.sessions).thenReturn(sessionList)

        setPendingResultValue(sessionStopResult)
        `when`(sessionStopResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sessionsApi.stopSession(apiClient, identifier)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), sessionList)
    }

    @Test
    fun SessionStopObservable_StatusException() {
        val identifier = "identifier"
        val sessionStopResult = Mockito.mock(SessionStopResult::class.java)
        val single = PowerMockito.spy(SessionStopSingle(rxFit, identifier, null, null))

        val sessionList = ArrayList<Session>()
        sessionList.add(session)

        `when`(sessionStopResult.sessions).thenReturn(sessionList)

        setPendingResultValue(sessionStopResult)
        `when`(sessionStopResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sessionsApi.stopSession(apiClient, identifier)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // SessionReadObservable

    @Test
    fun SessionReadObservable_Success() {
        val sessionReadRequest = Mockito.mock(SessionReadRequest::class.java)
        val sessionReadResult = Mockito.mock(SessionReadResult::class.java)
        val single = PowerMockito.spy(SessionReadSingle(rxFit, sessionReadRequest, null, null))

        setPendingResultValue(sessionReadResult)
        `when`(sessionReadResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sessionsApi.readSession(apiClient, sessionReadRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), sessionReadResult)
    }

    @Test
    fun SessionReadObservable_StatusException() {
        val sessionReadRequest = Mockito.mock(SessionReadRequest::class.java)
        val sessionReadResult = Mockito.mock(SessionReadResult::class.java)
        val single = PowerMockito.spy(SessionReadSingle(rxFit, sessionReadRequest, null, null))

        setPendingResultValue(sessionReadResult)
        `when`(sessionReadResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sessionsApi.readSession(apiClient, sessionReadRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }


}
