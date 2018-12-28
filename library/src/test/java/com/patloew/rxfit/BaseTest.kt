package com.patloew.rxfit

import android.content.Context
import androidx.annotation.CallSuper

import com.google.android.gms.fitness.Fitness
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull

import org.mockito.Mock

import java.util.concurrent.TimeUnit

import org.mockito.Mockito.`when`

abstract class BaseTest {

    @Mock
    internal lateinit var ctx: Context

    internal lateinit var rxFit: RxFit

    @CallSuper
    @Throws(Exception::class)
    open fun setup() {
        `when`(ctx.applicationContext).thenReturn(ctx)
        rxFit = RxFit(ctx, arrayOf(Fitness.BLE_API), arrayOf())
    }

    companion object {

        const val TIMEOUT_TIME = 1L
        val TIMEOUT_TIMEUNIT = TimeUnit.SECONDS

        fun assertNoTimeoutSet(baseRx: BaseRx<*>) {
            assertNull(baseRx.timeoutTime)
            assertNull(baseRx.timeoutUnit)
        }

        fun assertTimeoutSet(baseRx: BaseRx<*>) {
            assertEquals(TIMEOUT_TIME, baseRx.timeoutTime as Long)
            assertEquals(TIMEOUT_TIMEUNIT, baseRx.timeoutUnit)
        }
    }
}
