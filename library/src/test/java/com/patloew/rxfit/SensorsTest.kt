package com.patloew.rxfit

import android.app.PendingIntent
import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataSourcesRequest
import com.google.android.gms.fitness.request.SensorRequest

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
@PrepareOnlyThisForTest(Observable::class, Single::class, ContextCompat::class, Fitness::class, Status::class, DataType::class, ConnectionResult::class)
class SensorsTest : BaseTest() {

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.spy(Single::class.java)
        PowerMockito.mockStatic(Observable::class.java)
        doReturn(100).`when`(Observable::class.java, "bufferSize")
        super.setup()
    }

    // Add DataPoint Intent

    @Test
    @Throws(Exception::class)
    fun sensors_AddDataPointIntent() {
        val captor = ArgumentCaptor.forClass(SensorsAddDataPointIntentSingle::class.java)

        val request = Mockito.mock(SensorRequest::class.java)
        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        rxFit.sensors().addDataPointIntent(request, pendingIntent)
        rxFit.sensors().addDataPointIntent(request, pendingIntent, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)


        PowerMockito.verifyStatic(PendingIntent::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.sensorRequest)
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.sensorRequest)
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertTimeoutSet(single)
    }

    // Remove DataPoint Intent

    @Test
    @Throws(Exception::class)
    fun sensors_RemoveDataPointIntent() {
        val captor = ArgumentCaptor.forClass(SensorsRemoveDataPointIntentSingle::class.java)

        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        rxFit.sensors().removeDataPointIntent(pendingIntent)
        rxFit.sensors().removeDataPointIntent(pendingIntent, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(PendingIntent::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(pendingIntent, single.pendingIntent)
        BaseTest.assertTimeoutSet(single)
    }

    // GetDataPoints

    @Test
    @Throws(Exception::class)
    fun sensors_GetDataPoints() {
        val captor = ArgumentCaptor.forClass(SensorsDataPointObservable::class.java)

        val request = Mockito.mock(SensorRequest::class.java)
        rxFit.sensors().getDataPoints(request)
        rxFit.sensors().getDataPoints(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(SensorRequest::class.java, times(2))
        Observable.create<DataPoint>(captor.capture())

        var observable = captor.allValues[0]
        assertEquals(request, observable.sensorRequest)
        BaseTest.assertNoTimeoutSet(observable)

        observable = captor.allValues[1]
        assertEquals(request, observable.sensorRequest)
        BaseTest.assertTimeoutSet(observable)
    }

    // Remove DataPoint Intent

    @Test
    @Throws(Exception::class)
    fun sensors_FindDataSources() {
        val captor = ArgumentCaptor.forClass(SensorsFindDataSourcesSingle::class.java)

        val request = Mockito.mock(DataSourcesRequest::class.java)
        rxFit.sensors().findDataSources(request)
        rxFit.sensors().findDataSources(request, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)


        PowerMockito.verifyStatic(DataSourcesRequest::class.java, atLeast(2))
        Single.create<List<DataSource>>(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.dataSourcesRequest)
        assertNull(single.dataType)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.dataSourcesRequest)
        assertNull(single.dataType)
        BaseTest.assertTimeoutSet(single)
    }

    @Test
    @Throws(Exception::class)
    fun sensors_FindDataSources_DataType() {
        val captor = ArgumentCaptor.forClass(SensorsFindDataSourcesSingle::class.java)

        val request = Mockito.mock(DataSourcesRequest::class.java)
        val dataType = Mockito.mock(DataType::class.java)
        rxFit.sensors().findDataSources(request, dataType)
        rxFit.sensors().findDataSources(request, dataType, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)


        PowerMockito.verifyStatic(DataSourcesRequest::class.java, atLeast(2))
        Single.create<List<DataSource>>(captor.capture())

        var single = captor.allValues[0]
        assertEquals(request, single.dataSourcesRequest)
        assertEquals(dataType, single.dataType)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(request, single.dataSourcesRequest)
        assertEquals(dataType, single.dataType)
        BaseTest.assertTimeoutSet(single)
    }

}
