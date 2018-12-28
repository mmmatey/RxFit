package com.patloew.rxfit

import android.app.PendingIntent
import androidx.annotation.CallSuper

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Result
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.BleApi
import com.google.android.gms.fitness.ConfigApi
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.GoalsApi
import com.google.android.gms.fitness.HistoryApi
import com.google.android.gms.fitness.RecordingApi
import com.google.android.gms.fitness.SensorsApi
import com.google.android.gms.fitness.SessionsApi

import org.mockito.Matchers
import org.mockito.Mock
import org.powermock.api.mockito.PowerMockito
import org.powermock.reflect.Whitebox

import io.reactivex.ObservableEmitter
import io.reactivex.SingleEmitter
import io.reactivex.observers.TestObserver
import org.mockito.Mockito

import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.doReturn

@Suppress("UNCHECKED_CAST")
abstract class BaseOnSubscribeTest : BaseTest() {

    internal lateinit var apiClient: GoogleApiClient
    @Mock
    internal lateinit var status: Status
    @Mock
    internal lateinit var connectionResult: ConnectionResult
    @Mock
    internal lateinit var pendingResult: PendingResult<Result>
    @Mock
    internal lateinit var pendingIntent: PendingIntent

    @Mock
    internal lateinit var bleApi: BleApi
    @Mock
    internal lateinit var configApi: ConfigApi
    @Mock
    internal lateinit var goalsApi: GoalsApi
    @Mock
    internal lateinit var historyApi: HistoryApi
    @Mock
    internal lateinit var recordingApi: RecordingApi
    @Mock
    internal lateinit var sensorsApi: SensorsApi
    @Mock
    internal lateinit var sessionsApi: SessionsApi

    @CallSuper
    @Throws(Exception::class)
    override fun setup() {
        PowerMockito.mockStatic(Fitness::class.java)
        apiClient = Mockito.mock(GoogleApiClient::class.java, Mockito.CALLS_REAL_METHODS)
        Whitebox.setInternalState(Fitness::class.java, bleApi)
        Whitebox.setInternalState(Fitness::class.java, configApi)
        Whitebox.setInternalState(Fitness::class.java, goalsApi)
        Whitebox.setInternalState(Fitness::class.java, historyApi)
        Whitebox.setInternalState(Fitness::class.java, recordingApi)
        Whitebox.setInternalState(Fitness::class.java, sensorsApi)
        Whitebox.setInternalState(Fitness::class.java, sessionsApi)

        doReturn(status).`when`<Status>(status).status

        super.setup()
    }

    // Mock GoogleApiClient connection success behaviour
    protected fun <T> setupBaseObservableSuccess(baseObservable: BaseObservable<T>) {
        setupBaseObservableSuccess(baseObservable, apiClient)
    }

    // Mock GoogleApiClient connection success behaviour
    protected fun <T> setupBaseObservableSuccess(baseObservable: BaseObservable<T>, apiClient: GoogleApiClient?) {
        doAnswer { invocation ->
            val subscriber = (invocation.arguments[0] as BaseRxObservable<T, ObservableEmitter<T>, Emitter<ObservableEmitter<T>>>.ApiClientConnectionCallbacks).emitter.subscriber

            doAnswer {
                baseObservable.onGoogleApiClientReady(apiClient, subscriber)
                null
            }.`when`<GoogleApiClient>(apiClient).connect()

            apiClient
        }.`when`(baseObservable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks::class.java))
    }

    // Mock GoogleApiClient resolution behaviour
    protected fun <T> setupBaseObservableResolution(baseObservable: BaseObservable<T>, apiClient: GoogleApiClient) {
        doAnswer {
            doAnswer {
                try {
                    val observableSetField = BaseRx::class.java.getDeclaredField("observableSet")
                    observableSetField.isAccessible = true
                    (observableSetField.get(baseObservable) as MutableSet<BaseRx<*>>).add(baseObservable)
                } catch (e: Exception) {
                }

                null
            }.`when`(apiClient).connect()

            apiClient
        }.`when`(baseObservable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks::class.java))
    }

    // Mock GoogleApiClient connection success behaviour
    protected fun <T> setupBaseSingleSuccess(baseSingle: BaseSingle<T>) {
        setupBaseSingleSuccess(baseSingle, apiClient)
    }

    // Mock GoogleApiClient connection success behaviour
    protected fun <T> setupBaseSingleSuccess(baseSingle: BaseSingle<T>, apiClient: GoogleApiClient?) {
        doAnswer { invocation ->
            val subscriber = (invocation.arguments[0] as BaseRxObservable<T, SingleEmitter<T>, Emitter<SingleEmitter<T>>>.ApiClientConnectionCallbacks).emitter

            doAnswer {
                baseSingle.onGoogleApiClientReady(apiClient, subscriber.subscriber)
                null
            }.`when`<GoogleApiClient>(apiClient).connect()

            apiClient
        }.`when`(baseSingle).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks::class.java))
    }

    // Mock GoogleApiClient connection error behaviour
    protected fun <T> setupBaseObservableError(baseObservable: BaseObservable<T>) {
        doAnswer { invocation ->
            val subscriber = (invocation.arguments[0] as BaseRxObservable<*, *, *>.ApiClientConnectionCallbacks).emitter

            doAnswer {
                subscriber.onError(GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult))
                null
            }.`when`<GoogleApiClient>(apiClient).connect()

            apiClient
        }.`when`(baseObservable).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks::class.java))
    }

    // Mock GoogleApiClient connection error behaviour
    protected fun <T> setupBaseSingleError(baseSingle: BaseSingle<T>) {
        doAnswer { invocation ->
            val subscriber = (invocation.arguments[0] as BaseRxObservable<*, *, *>.ApiClientConnectionCallbacks).emitter

            doAnswer {
                subscriber.onError(GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult))
                null
            }.`when`<GoogleApiClient>(apiClient).connect()

            apiClient
        }.`when`(baseSingle).createApiClient(Matchers.any(BaseRx.ApiClientConnectionCallbacks::class.java))
    }

    protected fun setPendingResultValue(result: Result) {
        doAnswer {
            (it.arguments[0] as ResultCallback<Result>).onResult(result)
            null
        }.`when`(pendingResult)?.setResultCallback(Matchers.any<ResultCallback<Result>>())
    }

    companion object {

        fun <T> getSubscriber(baseObservable: BaseObservable<T>, apiClient: GoogleApiClient): ObservableEmitter<T>? {
            return try {
                val subscriberField = BaseObservable::class.java.getDeclaredField("subscriptionInfoMap")
                subscriberField.isAccessible = true
                (subscriberField.get(baseObservable) as Map<GoogleApiClient, ObservableEmitter<T>>)[apiClient]
            } catch (e: Exception) {
                null
            }
        }

        fun assertError(sub: TestObserver<*>, errorClass: Class<out Throwable>) {
            sub.assertError(errorClass)
            sub.assertNoValues()
        }

        fun <T> assertSingleValue(sub: TestObserver<T>, value: T) {
            sub.assertComplete()
            sub.assertValue(value)
        }
    }
}
