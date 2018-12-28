package com.patloew.rxfit

import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSet
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataDeleteRequest
import com.google.android.gms.fitness.request.DataReadRequest
import com.google.android.gms.fitness.request.DataUpdateListenerRegistrationRequest
import com.google.android.gms.fitness.request.DataUpdateRequest
import com.google.android.gms.fitness.result.DailyTotalResult
import com.google.android.gms.fitness.result.DataReadResult

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

import io.reactivex.Single

import org.mockito.Mockito.`when`

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, DataType::class, DataSet::class, BaseRx::class)
class HistoryOnSubscribeTest : BaseOnSubscribeTest() {

    @Mock
    internal lateinit var dataType: DataType
    @Mock
    internal lateinit var dataSet: DataSet
    @Mock
    internal lateinit var dataSource: DataSource

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }

    // HistoryDeleteDataObservable

    @Test
    fun historyDeleteDataObservable_Success() {
        val dataDeleteRequest = Mockito.mock(DataDeleteRequest::class.java)
        val single = PowerMockito.spy(HistoryDeleteDataSingle(rxFit, dataDeleteRequest, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(historyApi.deleteData(apiClient, dataDeleteRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun historyDeleteDataObservable_StatusException() {
        val dataDeleteRequest = Mockito.mock(DataDeleteRequest::class.java)
        val single = PowerMockito.spy(HistoryDeleteDataSingle(rxFit, dataDeleteRequest, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(historyApi.deleteData(apiClient, dataDeleteRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // HistoryInsertDataObservable

    @Test
    fun historyInsertDataObservable_Success() {
        val single = PowerMockito.spy(HistoryInsertDataSingle(rxFit, dataSet, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(historyApi.insertData(apiClient, dataSet)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun historyInsertDataObservable_StatusException() {
        val single = PowerMockito.spy(HistoryInsertDataSingle(rxFit, dataSet, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(historyApi.insertData(apiClient, dataSet)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // HistoryReadDailyTotalObservable

    @Test
    fun historyReadDailyTotalObservable_Success() {
        val dailyTotalResult = Mockito.mock(DailyTotalResult::class.java)
        val single = PowerMockito.spy(HistoryReadDailyTotalSingle(rxFit, dataType, null, null))

        setPendingResultValue(dailyTotalResult)
        `when`(dailyTotalResult.status).thenReturn(status)
        `when`<DataSet>(dailyTotalResult.total).thenReturn(dataSet)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(historyApi.readDailyTotal(apiClient, dataType)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), dataSet)
    }

    @Test
    fun historyReadDailyTotalObservable_StatusException() {
        val dailyTotalResult = Mockito.mock(DailyTotalResult::class.java)
        val single = PowerMockito.spy(HistoryReadDailyTotalSingle(rxFit, dataType, null, null))

        setPendingResultValue(dailyTotalResult)
        `when`(dailyTotalResult.status).thenReturn(status)
        `when`<DataSet>(dailyTotalResult.total).thenReturn(dataSet)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(historyApi.readDailyTotal(apiClient, dataType)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // HistoryReadDataObservable

    @Test
    fun historyReadDataObservable_Success() {
        val dataReadRequest = Mockito.mock(DataReadRequest::class.java)
        val dataReadResult = Mockito.mock(DataReadResult::class.java)
        val single = PowerMockito.spy(HistoryReadDataSingle(rxFit, dataReadRequest, null, null))

        setPendingResultValue(dataReadResult)
        `when`(dataReadResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(historyApi.readData(apiClient, dataReadRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), dataReadResult)
    }

    @Test
    fun historyReadDataObservable_StatusException() {
        val dataReadRequest = Mockito.mock(DataReadRequest::class.java)
        val dataReadResult = Mockito.mock(DataReadResult::class.java)
        val single = PowerMockito.spy(HistoryReadDataSingle(rxFit, dataReadRequest, null, null))

        setPendingResultValue(dataReadResult)
        `when`(dataReadResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(historyApi.readData(apiClient, dataReadRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // HistoryUpdateDataObservable

    @Test
    fun historyUpdateDataObservable_Success() {
        val dataUpdateRequest = Mockito.mock(DataUpdateRequest::class.java)
        val single = PowerMockito.spy(HistoryUpdateDataSingle(rxFit, dataUpdateRequest, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(historyApi.updateData(apiClient, dataUpdateRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun historyUpdateDataObservable_StatusException() {
        val dataUpdateRequest = Mockito.mock(DataUpdateRequest::class.java)
        val single = PowerMockito.spy(HistoryUpdateDataSingle(rxFit, dataUpdateRequest, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(historyApi.updateData(apiClient, dataUpdateRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // HistoryRegisterDataUpdateListenerSingle

    @Test
    fun historyRegisterDataUpdateListenerSingle_DataSource_Success() {
        val single = PowerMockito.spy(HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(historyApi.registerDataUpdateListener(Matchers.eq<GoogleApiClient>(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest::class.java))).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun historyRegisterDataUpdateListenerSingle_DataSource_StatusException() {
        val single = PowerMockito.spy(HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(historyApi.registerDataUpdateListener(Matchers.eq<GoogleApiClient>(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest::class.java))).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun historyRegisterDataUpdateListenerSingle_DataType_Success() {
        val single = PowerMockito.spy(HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, null, dataType, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(historyApi.registerDataUpdateListener(Matchers.eq<GoogleApiClient>(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest::class.java))).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun historyRegisterDataUpdateListenerSingle_DataType_StatusException() {
        val single = PowerMockito.spy(HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, null, dataType, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(historyApi.registerDataUpdateListener(Matchers.eq<GoogleApiClient>(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest::class.java))).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun historyRegisterDataUpdateListenerSingle_DataType_DataSource_Success() {
        val single = PowerMockito.spy(HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, dataType, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(historyApi.registerDataUpdateListener(Matchers.eq<GoogleApiClient>(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest::class.java))).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun historyRegisterDataUpdateListenerSingle_DataType_DataSource_StatusException() {
        val single = PowerMockito.spy(HistoryRegisterDataUpdateListenerSingle(rxFit, pendingIntent, dataSource, dataType, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(historyApi.registerDataUpdateListener(Matchers.eq<GoogleApiClient>(apiClient), Matchers.any(DataUpdateListenerRegistrationRequest::class.java))).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // HistoryUnregisterDataUpdateListenerSingle

    @Test
    fun historyUnregisterDataUpdateListenerSingle_Success() {
        val single = PowerMockito.spy(HistoryUnregisterDataUpdateListenerSingle(rxFit, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(historyApi.unregisterDataUpdateListener(apiClient, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun historyUnregisterDataUpdateListenerSingle_StatusException() {
        val single = PowerMockito.spy(HistoryUnregisterDataUpdateListenerSingle(rxFit, pendingIntent, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(historyApi.unregisterDataUpdateListener(apiClient, pendingIntent)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }


}
