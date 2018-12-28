package com.patloew.rxfit

import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataTypeCreateRequest
import com.google.android.gms.fitness.result.DataTypeResult

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

import io.reactivex.Single

import org.mockito.Mockito.`when`

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, DataType::class, BaseRx::class)
class ConfigOnSubscribeTest : BaseOnSubscribeTest() {

    @Mock
    internal lateinit var dataType: DataType

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }


    // ConfigCreateCustomDataTypeObservable

    @Test
    fun ConfigCreateCustomDataTypeObservable_Success() {
        val dataTypeCreateRequest = Mockito.mock(DataTypeCreateRequest::class.java)
        val dataTypeResult = Mockito.mock(DataTypeResult::class.java)
        val single = PowerMockito.spy(ConfigCreateCustomDataTypeSingle(rxFit, dataTypeCreateRequest, null, null))

        setPendingResultValue(dataTypeResult)
        `when`(dataTypeResult.status).thenReturn(status)
        `when`(dataTypeResult.dataType).thenReturn(dataType)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(configApi.createCustomDataType(apiClient, dataTypeCreateRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), dataType)
    }

    @Test
    fun ConfigCreateCustomDataTypeObservable_StatusException() {
        val dataTypeCreateRequest = Mockito.mock(DataTypeCreateRequest::class.java)
        val dataTypeResult = Mockito.mock(DataTypeResult::class.java)
        val single = PowerMockito.spy(ConfigCreateCustomDataTypeSingle(rxFit, dataTypeCreateRequest, null, null))

        setPendingResultValue(dataTypeResult)
        `when`(dataTypeResult.status).thenReturn(status)
        `when`(dataTypeResult.dataType).thenReturn(dataType)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(configApi.createCustomDataType(apiClient, dataTypeCreateRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // ConfigDisableFitObservable

    @Test
    fun ConfigDisableFitObservable_Success() {
        val single = PowerMockito.spy(ConfigDisableFitSingle(rxFit, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(configApi.disableFit(apiClient)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun ConfigDisableFitObservable_StatusException() {
        val single = PowerMockito.spy(ConfigDisableFitSingle(rxFit, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(configApi.disableFit(apiClient)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // ConfigReadDataTypeObservable

    @Test
    fun ConfigReadDataTypeObservable_Success() {
        val dataTypeName = "dataTypeName"
        val dataTypeResult = Mockito.mock(DataTypeResult::class.java)
        val single = PowerMockito.spy(ConfigReadDataTypeSingle(rxFit, dataTypeName, null, null))

        setPendingResultValue(dataTypeResult)
        `when`(dataTypeResult.status).thenReturn(status)
        `when`(dataTypeResult.dataType).thenReturn(dataType)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(configApi.readDataType(apiClient, dataTypeName)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), dataType)
    }

    @Test
    fun ConfigReadDataTypeObservable_StatusException() {
        val dataTypeName = "dataTypeName"
        val dataTypeResult = Mockito.mock(DataTypeResult::class.java)
        val single = PowerMockito.spy(ConfigReadDataTypeSingle(rxFit, dataTypeName, null, null))

        setPendingResultValue(dataTypeResult)
        `when`(dataTypeResult.status).thenReturn(status)
        `when`(dataTypeResult.dataType).thenReturn(dataType)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(configApi.readDataType(apiClient, dataTypeName)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

}
