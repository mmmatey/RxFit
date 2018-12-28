package com.patloew.rxfit

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignIn

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.Scope

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
class ResolutionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            handleIntent()
        }
    }

    override fun onNewIntent(intent: Intent) {
        setIntent(intent)
        handleIntent()
    }

    private fun handleIntent() {
        try {
            val connectionResult = intent.getParcelableExtra<ConnectionResult>(ARG_CONNECTION_RESULT)
            if (connectionResult != null) {
                connectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLUTION)
            } else {
                val parcelableArrayExtra = intent.getParcelableArrayExtra(ARG_SCOPES)
                val scopes = Scope.CREATOR.newArray(parcelableArrayExtra.size)
                for ((index, it) in parcelableArrayExtra.withIndex()) {
                    scopes[index] = it as Scope
                }
                GoogleSignIn.requestPermissions(
                        this,
                        REQUEST_CODE_GOOGLE_SIGN_IN,
                        GoogleSignIn.getLastSignedInAccount(this),
                        *scopes)
            }
            isResolutionShown = true
        } catch (e: IntentSender.SendIntentException) {
            setResolutionResultAndFinish(Activity.RESULT_CANCELED)
        } catch (e: NullPointerException) {
            setResolutionResultAndFinish(Activity.RESULT_CANCELED)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) =
            when (requestCode) {
                REQUEST_CODE_RESOLUTION -> setResolutionResultAndFinish(resultCode)
                REQUEST_CODE_GOOGLE_SIGN_IN -> setResolutionResultAndFinish(resultCode)
                else -> setResolutionResultAndFinish(Activity.RESULT_CANCELED)
            }

    private fun setResolutionResultAndFinish(resultCode: Int) {
        isResolutionShown = false
        BaseRx.onResolutionResult(resultCode, intent.getParcelableExtra(ARG_CONNECTION_RESULT))
        finish()
    }

    companion object {

        internal const val ARG_CONNECTION_RESULT = "connectionResult"
        internal const val ARG_SCOPES = "scopes"

        private const val REQUEST_CODE_RESOLUTION = 123
        private const val REQUEST_CODE_GOOGLE_SIGN_IN = 124

        internal var isResolutionShown = false
            private set
    }
}
