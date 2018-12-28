package com.patloew.rxfit

import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.Goal
import com.google.android.gms.fitness.request.GoalsReadRequest

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
import org.powermock.api.mockito.PowerMockito.doReturn

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(Observable::class, Single::class, ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class)
class GoalsTest : BaseTest() {

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.spy(Single::class.java)
        PowerMockito.mockStatic(Observable::class.java)
        doReturn(100).`when`(Observable::class.java, "bufferSize")
        super.setup()
    }

    // Read Current

    @Test
    @Throws(Exception::class)
    fun goals_ReadCurrent() {
        val captor = ArgumentCaptor.forClass(GoalsReadCurrentSingle::class.java)

        val request = Mockito.mock(GoalsReadRequest::class.java)
        rxFit.goals().readCurrent(request)
        rxFit.goals().readCurrent(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, atLeast(2))
        Single.create<List<Goal>>(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.goalsReadRequest)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.goalsReadRequest)
        BaseTest.assertTimeoutSet(single)
    }

}
