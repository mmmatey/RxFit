package com.patloew.rxfit

import android.app.PendingIntent
import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.Session
import com.google.android.gms.fitness.request.SessionInsertRequest
import com.google.android.gms.fitness.request.SessionReadRequest
import com.google.android.gms.fitness.result.SessionReadResult

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner

import io.reactivex.Observable
import io.reactivex.Single

import junit.framework.Assert.assertEquals
import org.mockito.Mockito.atLeast
import org.mockito.Mockito.times
import org.powermock.api.mockito.PowerMockito.doReturn

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(Observable::class, Single::class, ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class)
class SessionsTest : BaseTest() {

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.spy(Single::class.java)
        PowerMockito.mockStatic(Observable::class.java)
        doReturn(100).`when`(Observable::class.java, "bufferSize")
        super.setup()
    }


    // Insert

    @Test
    @Throws(Exception::class)
    fun sessions_Insert() {
        val captor = ArgumentCaptor.forClass(SessionInsertSingle::class.java)

        val request = Mockito.mock(SessionInsertRequest::class.java)
        rxFit.sessions().insert(request)
        rxFit.sessions().insert(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(SessionInsertRequest::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.sessionInsertRequest)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.sessionInsertRequest)
        BaseTest.assertTimeoutSet(single)
    }

    // Read

    @Test
    @Throws(Exception::class)
    fun sessions_Read() {
        val captor = ArgumentCaptor.forClass(SessionReadSingle::class.java)

        val request = Mockito.mock(SessionReadRequest::class.java)
        rxFit.sessions().read(request)
        rxFit.sessions().read(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)


        PowerMockito.verifyStatic(SessionReadRequest::class.java, times(2))
        Single.create<SessionReadResult>(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.sessionReadRequest)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.sessionReadRequest)
        BaseTest.assertTimeoutSet(single)
    }

    // Register For Sessions

    @Test
    @Throws(Exception::class)
    fun sessions_RegisterForSessions() {
        val captor = ArgumentCaptor.forClass(SessionRegisterSingle::class.java)

        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        rxFit.sessions().registerForSessions(pendingIntent)
        rxFit.sessions().registerForSessions(pendingIntent, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(PendingIntent::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertTimeoutSet(single)
    }

    // Unregister For Sessions

    @Test
    @Throws(Exception::class)
    fun sessions_UnregisterForSessions() {
        val captor = ArgumentCaptor.forClass(SessionUnregisterSingle::class.java)

        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        rxFit.sessions().unregisterForSessions(pendingIntent)
        rxFit.sessions().unregisterForSessions(pendingIntent, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(PendingIntent::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertTimeoutSet(single)
    }

    // Start

    @Test
    @Throws(Exception::class)
    fun sessions_Start() {
        val captor = ArgumentCaptor.forClass(SessionStartSingle::class.java)

        val session = Mockito.mock(Session::class.java)
        rxFit.sessions().start(session)
        rxFit.sessions().start(session, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Session::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(session, single.session)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(session, single.session)
        BaseTest.assertTimeoutSet(single)
    }


    // Stop

    @Test
    @Throws(Exception::class)
    fun sessions_Stop() {
        val captor = ArgumentCaptor.forClass(SessionStopSingle::class.java)

        val identifier = "identifier"
        rxFit.sessions().stop(identifier)
        rxFit.sessions().stop(identifier, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)


        PowerMockito.verifyStatic(Session::class.java, atLeast(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(identifier, single.identifier)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(identifier, single.identifier)
        BaseTest.assertTimeoutSet(single)
    }

}
