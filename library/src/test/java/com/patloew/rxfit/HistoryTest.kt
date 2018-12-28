package com.patloew.rxfit

import android.app.PendingIntent
import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataDeleteRequest
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataUpdateRequest
import com.google.android.gms.fitness.result.DataReadResult

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
import junit.framework.Assert.assertNull
import org.mockito.Mockito.atLeast
import org.mockito.Mockito.times
import org.powermock.api.mockito.PowerMockito.doReturn

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(Observable::class, Single::class, ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, DataType::class, DataSet::class)
class HistoryTest : BaseTest() {

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.spy(Single::class.java)
        PowerMockito.mockStatic(Observable::class.java)
        doReturn(100).`when`(Observable::class.java, "bufferSize")
        super.setup()
    }

    // Data Delete Request

    @Test
    @Throws(Exception::class)
    fun History_DataDeleteRequest() {
        val captor = ArgumentCaptor.forClass(HistoryDeleteDataSingle::class.java)

        val request = Mockito.mock(DataDeleteRequest::class.java)
        rxFit.history().delete(request)
        rxFit.history().delete(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataDeleteRequest::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.dataDeleteRequest)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.dataDeleteRequest)
        BaseTest.assertTimeoutSet(single)
    }

    // Insert Data Set

    @Test
    @Throws(Exception::class)
    fun History_InsertDataSet() {
        val captor = ArgumentCaptor.forClass(HistoryInsertDataSingle::class.java)

        val dataSet = Mockito.mock(DataSet::class.java)
        rxFit.history().insert(dataSet)
        rxFit.history().insert(dataSet, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataSet::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(dataSet, single.dataSet)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(dataSet, single.dataSet)
        BaseTest.assertTimeoutSet(single)
    }

    // Read Daily Total

    @Test
    @Throws(Exception::class)
    fun History_ReadDailyTotal() {
        val captor = ArgumentCaptor.forClass(HistoryReadDailyTotalSingle::class.java)

        val dataType = Mockito.mock(DataType::class.java)
        rxFit.history().readDailyTotal(dataType)
        rxFit.history().readDailyTotal(dataType, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataSet::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(dataType, single.dataType)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(dataType, single.dataType)
        BaseTest.assertTimeoutSet(single)
    }

    // Read

    @Test
    @Throws(Exception::class)
    fun History_Read() {
        val captor = ArgumentCaptor.forClass(HistoryReadDataSingle::class.java)

        val request = Mockito.mock(DataReadRequest::class.java)
        rxFit.history().read(request)
        rxFit.history().read(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataSet::class.java, times(2))
        Single.create<DataReadResult>(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.dataReadRequest)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.dataReadRequest)
        BaseTest.assertTimeoutSet(single)
    }

    // Read Buckets

    @Test
    @Throws(Exception::class)
    fun History_ReadBuckets() {
        val captor = ArgumentCaptor.forClass(HistoryReadDataSingle::class.java)

        val request = Mockito.mock(DataReadRequest::class.java)
        rxFit.history().readBuckets(request)
        rxFit.history().readBuckets(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataReadRequest::class.java, atLeast(2))
        Single.create<DataReadResult>(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.dataReadRequest)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.dataReadRequest)
        BaseTest.assertTimeoutSet(single)
    }

    // Read DataSets

    @Test
    @Throws(Exception::class)
    fun History_ReadDataSets() {
        val captor = ArgumentCaptor.forClass(HistoryReadDataSingle::class.java)

        val request = Mockito.mock(DataReadRequest::class.java)
        rxFit.history().readDataSets(request)
        rxFit.history().readDataSets(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataReadRequest::class.java, atLeast(2))
        Single.create<DataReadResult>(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.dataReadRequest)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.dataReadRequest)
        BaseTest.assertTimeoutSet(single)
    }


    // Update

    @Test
    @Throws(Exception::class)
    fun History_Update() {
        val captor = ArgumentCaptor.forClass(HistoryUpdateDataSingle::class.java)

        val request = Mockito.mock(DataUpdateRequest::class.java)
        rxFit.history().update(request)
        rxFit.history().update(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataUpdateRequest::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.dataUpdateRequest)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.dataUpdateRequest)
        BaseTest.assertTimeoutSet(single)
    }

    // RegisterDataUpdateListener

    @Test
    @Throws(Exception::class)
    fun History_RegisterDataUpdateListener_DataSource() {
        val captor = ArgumentCaptor.forClass(HistoryRegisterDataUpdateListenerSingle::class.java)

        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val dataSource = Mockito.mock(DataSource::class.java)
        rxFit.history().registerDataUpdateListener(pendingIntent, dataSource)
        rxFit.history().registerDataUpdateListener(pendingIntent, dataSource, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(PendingIntent::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(pendingIntent, single.request.intent)
        assertEquals(dataSource, single.request.dataSource)
        assertNull(single.request.dataType)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(pendingIntent, single.request.intent)
        assertEquals(dataSource, single.request.dataSource)
        assertNull(single.request.dataType)
        BaseTest.assertTimeoutSet(single)
    }

    @Test
    @Throws(Exception::class)
    fun History_RegisterDataUpdateListener_DataType() {
        val captor = ArgumentCaptor.forClass(HistoryRegisterDataUpdateListenerSingle::class.java)

        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val dataType = Mockito.mock(DataType::class.java)
        rxFit.history().registerDataUpdateListener(pendingIntent, dataType)
        rxFit.history().registerDataUpdateListener(pendingIntent, dataType, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataType::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(pendingIntent, single.request.intent)
        assertNull(single.request.dataSource)
        assertEquals(dataType, single.request.dataType)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(pendingIntent, single.request.intent)
        assertNull(single.request.dataSource)
        assertEquals(dataType, single.request.dataType)
        BaseTest.assertTimeoutSet(single)
    }


    @Test
    @Throws(Exception::class)
    fun History_RegisterDataUpdateListener_DataSource_DataType() {
        val captor = ArgumentCaptor.forClass(HistoryRegisterDataUpdateListenerSingle::class.java)

        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val dataSource = Mockito.mock(DataSource::class.java)
        val dataType = Mockito.mock(DataType::class.java)
        rxFit.history().registerDataUpdateListener(pendingIntent, dataSource, dataType)
        rxFit.history().registerDataUpdateListener(pendingIntent, dataSource, dataType, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(PendingIntent::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(pendingIntent, single.request.intent)
        assertEquals(dataSource, single.request.dataSource)
        assertEquals(dataType, single.request.dataType)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(pendingIntent, single.request.intent)
        assertEquals(dataSource, single.request.dataSource)
        assertEquals(dataType, single.request.dataType)
        BaseTest.assertTimeoutSet(single)
    }

    // UnregisterDataUpdateListener

    @Test
    @Throws(Exception::class)
    fun History_UnregisterDataUpdateListener() {
        val captor = ArgumentCaptor.forClass(HistoryUnregisterDataUpdateListenerSingle::class.java)

        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        rxFit.history().unregisterDataUpdateListener(pendingIntent)
        rxFit.history().unregisterDataUpdateListener(pendingIntent, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(PendingIntent::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertTimeoutSet(single)
    }


}
