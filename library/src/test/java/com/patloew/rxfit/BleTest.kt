package com.patloew.rxfit

import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.BleDevice
import com.google.android.gms.fitness.data.DataType

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

import io.reactivex.Observable
import io.reactivex.Single

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.mockito.Mockito.atLeast
import org.mockito.Mockito.times
import org.powermock.api.mockito.PowerMockito.doReturn

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(Observable::class, Single::class, ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, DataType::class)
class BleTest : BaseTest() {

    @Mock
    internal lateinit var bleDevice: BleDevice
    @Mock
    internal lateinit var dataType: DataType
    internal var dataTypes = arrayOf<DataType>()

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.spy(Single::class.java)
        PowerMockito.mockStatic(Observable::class.java)
        doReturn(100).`when`(Observable::class.java, "bufferSize")
        super.setup()
    }


    // Claim Device

    @Test
    @Throws(Exception::class)
    fun Ble_ClaimDevice_BleDevice() {
        val captor = ArgumentCaptor.forClass(BleClaimDeviceSingle::class.java)

        rxFit.ble().claimDevice(bleDevice)
        rxFit.ble().claimDevice(bleDevice, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(bleDevice, single.bleDevice)
        assertNull(single.deviceAddress)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(bleDevice, single.bleDevice)
        assertNull(single.deviceAddress)
        BaseTest.assertTimeoutSet(single)
    }

    @Test
    @Throws(Exception::class)
    fun Ble_ClaimDevice_DeviceAddress() {
        val captor = ArgumentCaptor.forClass(BleClaimDeviceSingle::class.java)
        val deviceAddress = "deviceAddress"

        rxFit.ble().claimDevice(deviceAddress)
        rxFit.ble().claimDevice(deviceAddress, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(deviceAddress, single.deviceAddress)
        BaseTest.assertNoTimeoutSet(single)
        assertNull(single.bleDevice)

        single = captor.allValues[1]
        assertEquals(deviceAddress, single.deviceAddress)
        BaseTest.assertTimeoutSet(single)
        assertNull(single.bleDevice)
    }

    // Get Claimed Devices

    @Test
    @Throws(Exception::class)
    fun Ble_ListClaimedDevices() {
        val captor = ArgumentCaptor.forClass(BleListClaimedDevicesSingle::class.java)

        rxFit.ble().claimedDevices
        rxFit.ble().getClaimedDevices(BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

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
    fun Ble_ListClaimedDevices_DataType() {
        val captor = ArgumentCaptor.forClass(BleListClaimedDevicesSingle::class.java)

        rxFit.ble().getClaimedDevices(dataType)
        rxFit.ble().getClaimedDevices(dataType, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, atLeast(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(dataType, single.dataType)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(dataType, single.dataType)
        BaseTest.assertTimeoutSet(single)
    }

    // Scan

    @Test
    @Throws(Exception::class)
    fun Ble_Scan() {
        val captor = ArgumentCaptor.forClass(BleScanObservable::class.java)

        rxFit.ble().scan()
        rxFit.ble().scan(BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, times(2))
        Observable.create(captor.capture())

        var observable = captor.allValues[0]
        assertNull(observable.dataTypes)
        assertNull(observable.stopTimeSecs)
        BaseTest.assertNoTimeoutSet(observable)

        observable = captor.allValues[1]
        assertNull(observable.dataTypes)
        assertNull(observable.stopTimeSecs)
        BaseTest.assertTimeoutSet(observable)
    }

    @Test
    @Throws(Exception::class)
    fun Ble_Scan_StopTime() {
        val captor = ArgumentCaptor.forClass(BleScanObservable::class.java)

        val stopTimeSecs = 2
        rxFit.ble().scan(stopTimeSecs)
        rxFit.ble().scan(stopTimeSecs, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, times(2))
        Observable.create(captor.capture())

        var observable = captor.allValues[0]
        assertNull(observable.dataTypes)
        assertEquals(stopTimeSecs, observable.stopTimeSecs as Int)
        BaseTest.assertNoTimeoutSet(observable)

        observable = captor.allValues[1]
        assertNull(observable.dataTypes)
        assertEquals(stopTimeSecs, observable.stopTimeSecs as Int)
        BaseTest.assertTimeoutSet(observable)
    }

    @Test
    @Throws(Exception::class)
    fun Ble_Scan_DateTypes() {
        val captor = ArgumentCaptor.forClass(BleScanObservable::class.java)

        rxFit.ble().scan(dataTypes)
        rxFit.ble().scan(dataTypes, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, times(2))
        Observable.create(captor.capture())

        var observable = captor.allValues[0]
        assertEquals(dataTypes, observable.dataTypes)
        assertNull(observable.stopTimeSecs)
        BaseTest.assertNoTimeoutSet(observable)

        observable = captor.allValues[1]
        assertEquals(dataTypes, observable.dataTypes)
        assertNull(observable.stopTimeSecs)
        BaseTest.assertTimeoutSet(observable)
    }

    @Test
    @Throws(Exception::class)
    fun Ble_Scan_DateTypes_StopTime() {
        val captor = ArgumentCaptor.forClass(BleScanObservable::class.java)

        val stopTimeSecs = 2
        rxFit.ble().scan(dataTypes, stopTimeSecs)
        rxFit.ble().scan(dataTypes, stopTimeSecs, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, times(2))
        Observable.create(captor.capture())

        var observable = captor.allValues[0]
        assertEquals(dataTypes, observable.dataTypes)
        assertEquals(stopTimeSecs, observable.stopTimeSecs as Int)
        BaseTest.assertNoTimeoutSet(observable)

        observable = captor.allValues[1]
        assertEquals(dataTypes, observable.dataTypes)
        assertEquals(stopTimeSecs, observable.stopTimeSecs as Int)
        BaseTest.assertTimeoutSet(observable)
    }

    // Unclaim Device

    @Test
    @Throws(Exception::class)
    fun Ble_UnclaimDevice_BleDevice() {
        val captor = ArgumentCaptor.forClass(BleUnclaimDeviceSingle::class.java)

        rxFit.ble().unclaimDevice(bleDevice)
        rxFit.ble().unclaimDevice(bleDevice, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertEquals(bleDevice, single.bleDevice)
        assertNull(single.deviceAddress)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertEquals(bleDevice, single.bleDevice)
        assertNull(single.deviceAddress)
        BaseTest.assertTimeoutSet(single)
    }

    @Test
    @Throws(Exception::class)
    fun Ble_UnclaimDevice_DeviceAddress() {
        val captor = ArgumentCaptor.forClass(BleUnclaimDeviceSingle::class.java)

        val deviceAddress = "deviceAddress"
        rxFit.ble().unclaimDevice(deviceAddress)
        rxFit.ble().unclaimDevice(deviceAddress, BaseTest.TIMEOUT_TIME, BaseTest.TIMEOUT_TIMEUNIT)

        PowerMockito.verifyStatic(Observable::class.java, times(2))
        Single.create(captor.capture())

        var single = captor.allValues[0]
        assertNull(single.bleDevice)
        assertEquals(deviceAddress, single.deviceAddress)
        BaseTest.assertNoTimeoutSet(single)

        single = captor.allValues[1]
        assertNull(single.bleDevice)
        assertEquals(deviceAddress, single.deviceAddress)
        BaseTest.assertTimeoutSet(single)
    }

}
