package com.patloew.rxfit

import androidx.core.content.ContextCompat

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.common.api.Status
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.Goal
import com.google.android.gms.fitness.request.GoalsReadRequest
import com.google.android.gms.fitness.result.GoalsResult

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
@PrepareOnlyThisForTest(ContextCompat::class, Fitness::class, Status::class, ConnectionResult::class, BaseRx::class)
class GoalsOnSubscribeTest : BaseOnSubscribeTest() {

    @Mock
    internal lateinit var goalsReadRequest: GoalsReadRequest
    @Mock
    internal lateinit var goal: Goal

    @Before
    @Throws(Exception::class)
    override fun setup() {
        MockitoAnnotations.initMocks(this)
        super.setup()
    }

    // GoalsReadCurrentSingle

    @Test
    fun GoalsReadCurrentSingle_Success() {
        val goalsResult = Mockito.mock(GoalsResult::class.java)

        val single = PowerMockito.spy(GoalsReadCurrentSingle(rxFit, goalsReadRequest, null, null))

        val goalList = ArrayList<Goal>()
        goalList.add(goal)
        `when`(goalsResult.goals).thenReturn(goalList)

        setPendingResultValue(goalsResult)
        `when`(goalsResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(true)
        `when`<PendingResult<*>>(goalsApi.readCurrentGoals(apiClient, goalsReadRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertSingleValue(Single.create(single).test(), goalList)
    }

    @Test
    fun GoalsReadCurrentSingle_StatusException() {
        val goalsResult = Mockito.mock(GoalsResult::class.java)

        val single = PowerMockito.spy(GoalsReadCurrentSingle(rxFit, goalsReadRequest, null, null))

        val goalList = ArrayList<Goal>()
        goalList.add(goal)
        `when`(goalsResult.goals).thenReturn(goalList)

        setPendingResultValue(goalsResult)
        `when`(goalsResult.status).thenReturn(status)
        `when`(status.isSuccess).thenReturn(false)
        `when`<PendingResult<*>>(goalsApi.readCurrentGoals(apiClient, goalsReadRequest)).thenReturn(pendingResult)

        setupBaseSingleSuccess(single)

        BaseOnSubscribeTest.assertError(Single.create(single).test(), StatusException::class.java)
    }
}
