package com.patloew.rxfit

import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataSource
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Subscription
import com.google.android.gms.fitness.result.ListSubscriptionsResult

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

import java.util.ArrayList

import io.reactivex.Single

import org.mockito.Mockito.`when`

@RunWith(PowerMockRunner::class)
@SuppressStaticInitializationFor("com.google.android.gms.fitness.Fitness")
@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, DataType::class, BaseRx::class)
class RecordingOnSubscribeTest : BaseOnSubscribeTest() {

    @Mock
    internal lateinit var dataType: DataType
    @Mock
    internal lateinit var dataSource: DataSource
    @Mock
    internal lateinit var subscription: Subscription

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }


    // RecordingListSubscriptionsObservable

    @Test
    fun RecordingListSubscriptionsObservable_Success() {
        val single = PowerMockito.spy(RecordingListSubscriptionsSingle(rxFit, null, null, null))

        val listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult::class.java)

        val subscriptionList = ArrayList<Subscription>()
        subscriptionList.add(Mockito.mock(Subscription::class.java))

        `when`(listSubscriptionsResult.subscriptions).thenReturn(subscriptionList)

        setPendingResultValue(listSubscriptionsResult)
        `when`(listSubscriptionsResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(recordingApi.listSubscriptions(apiClient)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), subscriptionList)
    }

    @Test
    fun RecordingListSubscriptionsObservable_StatusException() {
        val single = PowerMockito.spy(RecordingListSubscriptionsSingle(rxFit, null, null, null))

        val listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult::class.java)

        val subscriptionList = ArrayList<Subscription>()
        subscriptionList.add(Mockito.mock(Subscription::class.java))

        `when`(listSubscriptionsResult.subscriptions).thenReturn(subscriptionList)

        setPendingResultValue(listSubscriptionsResult)
        `when`(listSubscriptionsResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(recordingApi.listSubscriptions(apiClient)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun RecordingListSubscriptionsObservable_WithDataType_Success() {
        val single = PowerMockito.spy(RecordingListSubscriptionsSingle(rxFit, dataType, null, null))

        val listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult::class.java)

        val subscriptionList = ArrayList<Subscription>()
        subscriptionList.add(Mockito.mock(Subscription::class.java))

        `when`(listSubscriptionsResult.subscriptions).thenReturn(subscriptionList)

        setPendingResultValue(listSubscriptionsResult)
        `when`(listSubscriptionsResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(recordingApi.listSubscriptions(apiClient, dataType)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), subscriptionList)
    }

    @Test
    fun RecordingListSubscriptionsObservable_WithDataType_StatusException() {
        val single = PowerMockito.spy(RecordingListSubscriptionsSingle(rxFit, dataType, null, null))

        val listSubscriptionsResult = Mockito.mock(ListSubscriptionsResult::class.java)

        val subscriptionList = ArrayList<Subscription>()
        subscriptionList.add(subscription)

        `when`(listSubscriptionsResult.subscriptions).thenReturn(subscriptionList)

        setPendingResultValue(listSubscriptionsResult)
        `when`(listSubscriptionsResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(recordingApi.listSubscriptions(apiClient, dataType)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // RecordingSubscribeObservable

    @Test
    fun RecordingSubscribeObservable_DataType_Success() {
        val single = PowerMockito.spy(RecordingSubscribeSingle(rxFit, null, dataType, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(recordingApi.subscribe(apiClient, dataType)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }


    @Test
    fun RecordingSubscribeObservable_DataType_StatusException() {
        val single = PowerMockito.spy(RecordingSubscribeSingle(rxFit, null, dataType, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(recordingApi.subscribe(apiClient, dataType)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun RecordingSubscribeObservable_DataSource_Success() {
        val single = PowerMockito.spy(RecordingSubscribeSingle(rxFit, dataSource, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(recordingApi.subscribe(apiClient, dataSource)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }


    @Test
    fun RecordingSubscribeObservable_DataSource_StatusException() {
        val single = PowerMockito.spy(RecordingSubscribeSingle(rxFit, dataSource, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(recordingApi.subscribe(apiClient, dataSource)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    // RecordingSubscribeObservable

    @Test
    fun RecordingUnsubscribeObservable_DataType_Success() {
        val single = PowerMockito.spy(RecordingUnsubscribeSingle(rxFit, null, dataType, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(recordingApi.unsubscribe(apiClient, dataType)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }


    @Test
    fun RecordingUnsubscribeObservable_DataType_StatusException() {
        val single = PowerMockito.spy(RecordingUnsubscribeSingle(rxFit, null, dataType, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(recordingApi.unsubscribe(apiClient, dataType)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun RecordingUnsubscribeObservable_DataSource_Success() {
        val single = PowerMockito.spy(RecordingUnsubscribeSingle(rxFit, dataSource, null, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(recordingApi.unsubscribe(apiClient, dataSource)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun RecordingUnsubscribeObservable_DataSource_StatusException() {
        val single = PowerMockito.spy(RecordingUnsubscribeSingle(rxFit, dataSource, null, null, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(recordingApi.unsubscribe(apiClient, dataSource)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

    @Test
    fun RecordingUnsubscribeObservable_Subscription_Success() {
        val single = PowerMockito.spy(RecordingUnsubscribeSingle(rxFit, null, null, subscription, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(recordingApi.unsubscribe(apiClient, subscription)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), status)
    }

    @Test
    fun RecordingUnsubscribeObservable_Subscription_StatusException() {
        val single = PowerMockito.spy(RecordingUnsubscribeSingle(rxFit, null, null, subscription, null, null))

        setPendingResultValue(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(recordingApi.unsubscribe(apiClient, subscription)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }

}
