package com.patloew.rxfit

import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.DataTypeCreateRequest

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner

import io.reactivex.Single

import junit.framework.Assert.assertEquals
import org.mockito.Mockito.times

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(Single::class, ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, DataType::class)
class ConfigTest : BaseTest() {

    @Mock
    internal lateinit var dataTypeCreateRequest: DataTypeCreateRequest

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.spy(Single::class.java)
        super.setup()
    }


    // Create Custom Data Type

    @Test
    @Throws(Exception::class)
    fun Config_CreateCustomDataType() {
        val captor = ArgumentCaptor.forClass(ConfigCreateCustomDataTypeSingle::class.java)

        rxFit.config().createCustomDataType(dataTypeCreateRequest)
        rxFit.config().createCustomDataType(dataTypeCreateRequest, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataTypeCreateRequest::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(dataTypeCreateRequest, single.dataTypeCreateRequest)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(dataTypeCreateRequest, single.dataTypeCreateRequest)
        BaseTest.assertTimeoutSet(single)
    }

    // Disable Fit


    @Test
    @Throws(Exception::class)
    fun Config_DisableFit() {
        val captor = ArgumentCaptor.forClass(ConfigDisableFitSingle::class.java)

        rxFit.config().disableFit()
        rxFit.config().disableFit(BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataTypeCreateRequest::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        BaseTest.assertTimeoutSet(single)
    }


    // Read Data Type

    @Test
    @Throws(Exception::class)
    fun Config_ReadDataType() {
        val captor = ArgumentCaptor.forClass(ConfigReadDataTypeSingle::class.java)

        val dataTypeName = "name"
        rxFit.config().readDataType(dataTypeName)
        rxFit.config().readDataType(dataTypeName, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(DataTypeCreateRequest::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(dataTypeName, single.dataTypeName)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(dataTypeName, single.dataTypeName)
        BaseTest.assertTimeoutSet(single)
    }

}
