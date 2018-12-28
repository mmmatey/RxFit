package com.patloew.rxfit

import android.app.PendingIntent
import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.SensorsApi
import com.google.android.gms.fitness.data.DataPoint
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataSourcesRequest
import com.google.android.gms.fitness.request.OnDataPointListener
import com.google.android.gms.fitness.request.SensorRequest
import com.google.android.gms.fitness.result.DataSourcesResult

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner

import java.util.ArrayList

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver

import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, DataPoint::class, DataType::class, BaseRx::class)
class SensorsOnSubscribeTest : BaseOnSubscribeTest() {

    @Mock
    internal lateinit var dataType: DataType
    @Mock
    internal lateinit var dataSource: DataSource
    @Mock
    internal lateinit var sensorRequest: SensorRequest

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }


    // SensorsAddDataPointIntentObservable

    @Test
    fun SensorsAddDataPointIntentObservable_Success() {
        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val single = PowerMockito.spy(SensorsAddDataPointIntentSingle(rxFit, sensorRequest, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sensorsApi.add(apiClient, sensorRequest, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun SensorsAddDataPointIntentObservable_StatusException() {
        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val single = PowerMockito.spy(SensorsAddDataPointIntentSingle(rxFit, sensorRequest, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sensorsApi.add(apiClient, sensorRequest, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }


    // SensorsRemoveDataPointIntentObservable

    @Test
    fun SensorsRemoveDataPointIntentObservable_Success() {
        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val single = PowerMockito.spy(SensorsRemoveDataPointIntentSingle(rxFit, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sensorsApi.remove(apiClient, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun SensorsRemoveDataPointIntentObservable_StatusException() {
        val pendingIntent = Mockito.mock(PendingIntent::class.java)
        val single = PowerMockito.spy(SensorsRemoveDataPointIntentSingle(rxFit, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sensorsApi.remove(apiClient, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // SensorsDataPointObservable

    @Test
    fun SensorsDataPointObservable_Success() {
        val dataPoint = Mockito.mock(DataPoint::class.java)
        val observable = PowerMockito.spy(SensorsDataPointObservable(rxFit, sensorRequest, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sensorsApi.add(Matchers.any(GoogleApiClient::class.java), Matchers.any(SensorRequest::class.java), Matchers.any(OnDataPointListener::class.java))).thenReturn(pendingResult)
        `when`(apiClient.isConnected).thenReturn(true)

        setupBaseObservableSuccess(observable)
        val sub = Observable.create(observable).test()
        BaseOnSubscribeTest.getSubscriber(observable, apiClient)?.onNext(dataPoint)

        verify<SensorsApi>(sensorsApi, never()).remove(Matchers.any(GoogleApiClient::class.java), Matchers.any(OnDataPointListener::class.java))
        sub.dispose()
        verify<SensorsApi>(sensorsApi).remove(Matchers.any(GoogleApiClient::class.java), Matchers.any(OnDataPointListener::class.java))

        sub.assertNotTerminated()
        sub.assertValue(dataPoint)
    }

    @Test
    fun SensorsDataPointObservable_StatusException() {
        val observable = PowerMockito.spy(SensorsDataPointObservable(rxFit, sensorRequest, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sensorsApi.add(Matchers.any(GoogleApiClient::class.java), Matchers.any(SensorRequest::class.java), Matchers.any(OnDataPointListener::class.java))).thenReturn(pendingResult)

        setupBaseObservableSuccess(observable)

        BaseOnSubscribeTest.assertError(Observable.create(observable).test(), StatusException::class.java)
    }

    // SensorsFindDataSourcesObservable

    @Test
    fun SensorsFindDataSourcesObservable_Success() {
        val dataSourcesRequest = Mockito.mock(DataSourcesRequest::class.java)
        val dataSourcesResult = Mockito.mock(DataSourcesResult::class.java)
        val single = PowerMockito.spy(SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, null, null, null))

        val dataSourceList = ArrayList<DataSource>()
        dataSourceList.add(dataSource)

        `when`(dataSourcesResult.dataSources).thenReturn(dataSourceList)

        setPendingResultValue(dataSourcesResult)
        `when`(dataSourcesResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), dataSourceList)
    }

    @Test
    fun SensorsFindDataSourcesObservable_StatusException() {
        val dataSourcesRequest = Mockito.mock(DataSourcesRequest::class.java)
        val dataSourcesResult = Mockito.mock(DataSourcesResult::class.java)
        val single = PowerMockito.spy(SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, null, null, null))

        val dataSourceList = ArrayList<DataSource>()
        dataSourceList.add(dataSource)

        `when`(dataSourcesResult.dataSources).thenReturn(dataSourceList)

        setPendingResultValue(dataSourcesResult)
        `when`(dataSourcesResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun SensorsFindDataSourcesObservable_WithDataType_Success() {
        val dataSourcesRequest = Mockito.mock(DataSourcesRequest::class.java)
        val dataSourcesResult = Mockito.mock(DataSourcesResult::class.java)
        val single = PowerMockito.spy(SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, dataType, null, null))

        val dataSourceList = ArrayList<DataSource>()
        dataSourceList.add(dataSource)

        `when`(dataSourcesResult.getDataSources(dataType)).thenReturn(dataSourceList)

        setPendingResultValue(dataSourcesResult)
        `when`(dataSourcesResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), dataSourceList)
    }

    @Test
    fun SensorsFindDataSourcesObservable_WithDataType_StatusException() {
        val dataSourcesRequest = Mockito.mock(DataSourcesRequest::class.java)
        val dataSourcesResult = Mockito.mock(DataSourcesResult::class.java)
        val single = PowerMockito.spy(SensorsFindDataSourcesSingle(rxFit, dataSourcesRequest, dataType, null, null))

        val dataSourceList = ArrayList<DataSource>()
        dataSourceList.add(dataSource)

        `when`(dataSourcesResult.getDataSources(dataType)).thenReturn(dataSourceList)

        setPendingResultValue(dataSourcesResult)
        `when`(dataSourcesResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(sensorsApi.findDataSources(apiClient, dataSourcesRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }
}
