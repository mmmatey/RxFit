package com.patloew.rxfit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

abstract class BaseRxObservable<T, ER, E : Emitter<ER>> : BaseRx<T> {
    protected constructor(ctx: Context, services: Array<Api<out Api.ApiOptions.NotRequiredOptions>>, scopes: Array<Scope>?) : super(ctx, services, scopes)
    protected constructor(rxFit: RxFit, timeout: Long?, timeUnit: TimeUnit?) : super(rxFit, timeout, timeUnit)

    var subscriptionInfoMap = ConcurrentHashMap<GoogleApiClient, E>()

    abstract fun onGoogleApiClientReady(apiClient: GoogleApiClient?, subscriber: ER)

    protected fun subscribeEmitter(subscriber: E) {
        val apiClientConnectionCallbacks = ApiClientConnectionCallbacks(subscriber)
        val apiClient = createApiClient(apiClientConnectionCallbacks)
        subscriptionInfoMap[apiClient] = subscriber

        try {
            tryConnect(apiClient)
        } catch (ex: Throwable) {
            subscriber.onError(ex)
        }

        subscriber.setCancelable {
            cancel(apiClient)
        }
    }

    private fun tryConnect(apiClient: GoogleApiClient) {
        Log.d("FIT", "GOOGLE FIT: calling connect")
        scopes.apply {
            if (this == null) {
                apiClient.connect()
            } else {
                if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(ctx), *this)) {
                    BaseRx.observableSet.add(this@BaseRxObservable)
                    runResolutionActivity(scopes = scopes)
                } else {
                    apiClient.connect()
                }
            }
        }
    }

    private fun cancel(apiClient: GoogleApiClient) {
        if (apiClient.isConnected || apiClient.isConnecting) {
            onUnsubscribed(apiClient)
            apiClient.disconnect()
        }
        subscriptionInfoMap.remove(apiClient)
    }

    override fun handleResolutionResult(resultCode: Int, connectionResult: ConnectionResult?) {
        subscriptionInfoMap.forEach { (key, value) ->
            if (!value.isDisposed()) {
                when (resultCode) {
                    Activity.RESULT_OK -> try {
                        key.connect()
                    } catch (ex: Throwable) {
                        value.onError(ex)
                    }
                    else -> value.onError(GoogleAPIConnectionException("Error connecting to GoogleApiClient, resolution was not successful.", connectionResult))
                }
            }
        }
    }

    private fun runResolutionActivity(connectionResult: ConnectionResult? = null, scopes: Array<Scope>? = null) {
        if (!ResolutionActivity.isResolutionShown) {
            val intent = Intent(ctx, ResolutionActivity::class.java)
            connectionResult?.let {
                intent.putExtra(ResolutionActivity.ARG_CONNECTION_RESULT, connectionResult)
            }
            scopes?.let {
                intent.putExtra(ResolutionActivity.ARG_SCOPES, scopes)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(intent)
        }
    }

    inner class ApiClientConnectionCallbacks internal constructor(val emitter: E) : BaseRx.ApiClientConnectionCallbacks() {

        override var apiClient: GoogleApiClient? = null

        override fun onConnected(bundle: Bundle?) {
            try {
                onGoogleApiClientReady(apiClient, emitter.subscriber)
            } catch (ex: Throwable) {
                emitter.onError(ex)
            }
        }

        override fun onConnectionSuspended(cause: Int) {
            emitter.onError(GoogleAPIConnectionSuspendedException(cause))
        }

        override fun onConnectionFailed(connectionResult: ConnectionResult) {
            if (handleResolution && connectionResult.hasResolution()) {
                BaseRx.observableSet.add(this@BaseRxObservable)
                runResolutionActivity(connectionResult)
            } else {
                emitter.onError(GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult))
            }
        }

        override fun setClient(client: GoogleApiClient) {
            this.apiClient = client
        }
    }

}