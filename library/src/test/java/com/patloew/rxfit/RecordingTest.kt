package com.patloew.rxfit

import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Subscription
import io.reactivex.Observable

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

import io.reactivex.Single

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.mockito.Mockito.atLeast
import org.mockito.Mockito.times

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(Single::class, ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, DataType::class)
class RecordingTest : BaseTest() {

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.spy(Single::class.java)
        super.setup()
    }


    // List Subscriptions

    @Test
    @Throws(Exception::class)
    fun recording_ListSubscriptions() {
        val captor = ArgumentCaptor.forClass(RecordingListSubscriptionsSingle::class.java)

        rxFit.recording().listSubscriptions()
        rxFit.recording().listSubscriptions(BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, atLeast(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertNull(single.dataType)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertNull(single.dataType)
        BaseTest.assertTimeoutSet(single)
    }


    @Test
    @Throws(Exception::class)
    fun recording_ListSubscriptions_DataType() {
        val captor = ArgumentCaptor.forClass(RecordingListSubscriptionsSingle::class.java)

        val dataType = Mockito.mock(DataType::class.java)
        rxFit.recording().listSubscriptions(dataType)
        rxFit.recording().listSubscriptions(dataType, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, atLeast(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(dataType, single.dataType)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(dataType, single.dataType)
        BaseTest.assertTimeoutSet(single)
    }

    // Unsubscribe

    @Test
    @Throws(Exception::class)
    fun recording_Unsubscribe_DataSource() {
        val captor = ArgumentCaptor.forClass(RecordingUnsubscribeSingle::class.java)

        val dataSource = Mockito.mock(DataSource::class.java)
        rxFit.recording().unsubscribe(dataSource)
        rxFit.recording().unsubscribe(dataSource, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Single::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertNull(single.dataType)
        assertEquals(dataSource, single.dataSource)
        assertNull(single.subscription)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertNull(single.dataType)
        assertEquals(dataSource, single.dataSource)
        assertNull(single.subscription)
        BaseTest.assertTimeoutSet(single)
    }

    @Test
    @Throws(Exception::class)
    fun recording_Unsubscribe_DataType() {
        val captor = ArgumentCaptor.forClass(RecordingUnsubscribeSingle::class.java)

        val dataType = Mockito.mock(DataType::class.java)
        rxFit.recording().unsubscribe(dataType)
        rxFit.recording().unsubscribe(dataType, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Single::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(dataType, single.dataType)
        assertNull(single.dataSource)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(dataType, single.dataType)
        assertNull(single.dataSource)
        assertNull(single.subscription)
        BaseTest.assertTimeoutSet(single)
    }

    @Test
    @Throws(Exception::class)
    fun recording_Unsubscribe_Subscription() {
        val captor = ArgumentCaptor.forClass(RecordingUnsubscribeSingle::class.java)

        val subscription = Mockito.mock(Subscription::class.java)
        rxFit.recording().unsubscribe(subscription)
        rxFit.recording().unsubscribe(subscription, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Single::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertNull(single.dataType)
        assertNull(single.dataSource)
        assertEquals(subscription, single.subscription)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertNull(single.dataType)
        assertNull(single.dataSource)
        assertEquals(subscription, single.subscription)
        BaseTest.assertTimeoutSet(single)
    }


    // Subscribe

    @Test
    @Throws(Exception::class)
    fun recording_Subscribe_DataSource() {
        val captor = ArgumentCaptor.forClass(RecordingSubscribeSingle::class.java)

        val dataSource = Mockito.mock(DataSource::class.java)
        rxFit.recording().subscribe(dataSource)
        rxFit.recording().subscribe(dataSource, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Single::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertNull(single.dataType)
        assertEquals(dataSource, single.dataSource)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertNull(single.dataType)
        assertEquals(dataSource, single.dataSource)
        BaseTest.assertTimeoutSet(single)
    }

    @Test
    @Throws(Exception::class)
    fun recording_Subscribe_DataType() {
        val captor = ArgumentCaptor.forClass(RecordingSubscribeSingle::class.java)

        val dataType = Mockito.mock(DataType::class.java)
        rxFit.recording().subscribe(dataType)
        rxFit.recording().subscribe(dataType, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Single::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(dataType, single.dataType)
        assertNull(single.dataSource)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(dataType, single.dataType)
        assertNull(single.dataSource)
        BaseTest.assertTimeoutSet(single)
    }

}
