package com.patloew.rxfit

import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.BleApi
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.BleDevice
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.request.BleScanCallback
import com.google.android.gms.fitness.request.StartBleScanRequest
import com.google.android.gms.fitness.result.BleDevicesResult
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, DataType::class, BaseRx::class)
class BleOnSubscribeTest : BaseOnSubscribeTest() {

    @Mock
    internal lateinit var bleDevice: BleDevice
    @Mock
    internal lateinit var dataType: DataType

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }

    // BleClaimDeviceObservable

    @Test
    fun bleClaimDeviceObservable_BleDevice_Success() {
        val single = PowerMockito.spy(BleClaimDeviceSingle(rxFit, bleDevice, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(bleApi.claimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun bleClaimDeviceObservable_DeviceAddress_Success() {
        val deviceAddress = "deviceAddress"
        val single = PowerMockito.spy(BleClaimDeviceSingle(rxFit, null, deviceAddress, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(bleApi.claimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun bleClaimDeviceObservable_BleDevice_StatusException() {
        val single = PowerMockito.spy(BleClaimDeviceSingle(rxFit, bleDevice, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(bleApi.claimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun bleClaimDeviceObservable_DeviceAddress_StatusException() {
        val deviceAddress = "deviceAddress"
        val single = PowerMockito.spy(BleClaimDeviceSingle(rxFit, null, deviceAddress, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(bleApi.claimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }


    // BleUnclaimDeviceObservable

    @Test
    fun bleUnclaimDeviceObservable_BleDevice_Success() {
        val single = PowerMockito.spy(BleUnclaimDeviceSingle(rxFit, bleDevice, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(bleApi.unclaimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun bleUnclaimDeviceObservable_DeviceAddress_Success() {
        val deviceAddress = "deviceAddress"
        val single = PowerMockito.spy(BleUnclaimDeviceSingle(rxFit, null, deviceAddress, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(bleApi.unclaimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun bleUnclaimDeviceObservable_BleDevice_StatusException() {
        val single = PowerMockito.spy(BleUnclaimDeviceSingle(rxFit, bleDevice, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(bleApi.unclaimBleDevice(apiClient, bleDevice)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun bleUnclaimDeviceObservable_DeviceAddress_StatusException() {
        val deviceAddress = "deviceAddress"
        val single = PowerMockito.spy(BleUnclaimDeviceSingle(rxFit, null, deviceAddress, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(bleApi.unclaimBleDevice(apiClient, deviceAddress)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // BleListClaimedDevicesObservable

    @Test
    fun bleListClaimedDevicesObservableWithDataType_Success() {
        val single = PowerMockito.spy(BleListClaimedDevicesSingle(rxFit, dataType, null, null))

        val bleDevicesResult = Mockito.mock(BleDevicesResult::class.java)

        val bleDeviceList = ArrayList<BleDevice>()
        bleDeviceList.add(bleDevice)

        `when`(bleDevicesResult.getClaimedBleDevices(dataType)).thenReturn(bleDeviceList)

        setPendingResultValue(bleDevicesResult)
        `when`(bleDevicesResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), bleDeviceList)
    }

    @Test
    fun bleListClaimedDevicesObservableWithDataType_StatusException() {
        val single = PowerMockito.spy(BleListClaimedDevicesSingle(rxFit, dataType, null, null))

        val bleDevicesResult = Mockito.mock(BleDevicesResult::class.java)

        val bleDeviceList = ArrayList<BleDevice>()
        bleDeviceList.add(bleDevice)

        `when`(bleDevicesResult.getClaimedBleDevices(dataType)).thenReturn(bleDeviceList)

        setPendingResultValue(bleDevicesResult)
        `when`(bleDevicesResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun bleListClaimedDevicesObservableSuccess() {
        val single = PowerMockito.spy(BleListClaimedDevicesSingle(rxFit, null, null, null))

        val bleDevicesResult = Mockito.mock(BleDevicesResult::class.java)

        val bleDeviceList = ArrayList<BleDevice>()
        bleDeviceList.add(bleDevice)

        `when`(bleDevicesResult.claimedBleDevices).thenReturn(bleDeviceList)

        setPendingResultValue(bleDevicesResult)
        `when`(bleDevicesResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), bleDeviceList)
    }

    @Test
    fun bleListClaimedDevicesObservableStatusException() {
        val single = PowerMockito.spy(BleListClaimedDevicesSingle(rxFit, null, null, null))

        val bleDevicesResult = Mockito.mock(BleDevicesResult::class.java)

        val bleDeviceList = ArrayList<BleDevice>()
        bleDeviceList.add(bleDevice)

        `when`(bleDevicesResult.claimedBleDevices).thenReturn(bleDeviceList)

        setPendingResultValue(bleDevicesResult)
        `when`(bleDevicesResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(bleApi.listClaimedBleDevices(apiClient)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // BleScanObservable

    @Suppress("DEPRECATION")
    @Test
    fun bleScanObservable_Success() {
        val observable = PowerMockito.spy(BleScanObservable(rxFit, null, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(bleApi.startBleScan(Matchers.any(GoogleApiClient::class.java), Matchers.any(StartBleScanRequest::class.java))).thenReturn(pendingResult)
        `when`(apiClient.isConnected).thenReturn(true)

        setupBaseObservableSuccess(observable)
        val sub = Observable.create(observable).test()
        BaseOnSubscribeTest.getSubscriber(observable, apiClient)?.onNext(bleDevice)

        verify<BleApi>(bleApi, never()).stopBleScan(Matchers.any(GoogleApiClient::class.java), Matchers.any(BleScanCallback::class.java))
        sub.dispose()
        verify<BleApi>(bleApi).stopBleScan(Matchers.any(GoogleApiClient::class.java), Matchers.any(BleScanCallback::class.java))

        sub.assertNotTerminated()
        sub.assertValue(bleDevice)
    }

    @Suppress("DEPRECATION")
    @Test
    fun bleScanObservable_StatusException() {
        val observable = PowerMockito.spy(BleScanObservable(rxFit, null, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(bleApi.startBleScan(Matchers.any(GoogleApiClient::class.java), Matchers.any(StartBleScanRequest::class.java))).thenReturn(pendingResult)
        `when`(apiClient.isConnected).thenReturn(true)

        setupBaseObservableSuccess(observable)

        BaseOnSubscribeTest.assertError(Observable.create(observable).test(), StatusException::class.java)
    }

    @Suppress("DEPRECATION")
    @Test
    @Throws(Exception::class)
    fun bleScanObservable_SecurityException() {

        PowerMockito.doThrow(SecurityException("Missing Bluetooth Admin permission")).`when`<BleApi>(bleApi).startBleScan(Matchers.any(GoogleApiClient::class.java), Matchers.any(StartBleScanRequest::class.java))

        val observable = PowerMockito.spy(BleScanObservable(rxFit, null, null, null, null))

        setupBaseObservableSuccess(observable)

        BaseOnSubscribeTest.assertError(Observable.create(observable).test(), SecurityException::class.java)
    }
}
