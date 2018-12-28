package com.patloew.rxfitsample

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar

import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Api
import com.google.android.gms.common.api.Scope
import com.google.android.gms.fitness.Fitness
import com.patloew.rxfit.RxFit
import java.util.concurrent.TimeUnit

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
class MainActivity : AppCompatActivity(), MainView {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    private lateinit var rxFit: RxFit

    private lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById<View>(R.id.rv_main) as RecyclerView
        progressBar = findViewById<View>(R.id.pb_main) as ProgressBar

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        rxFit = RxFit(this, arrayOf(Fitness.SESSIONS_API, Fitness.RECORDING_API, Fitness.HISTORY_API, Fitness.CONFIG_API), arrayOf(Scope(Scopes.FITNESS_ACTIVITY_READ)))
        rxFit.setDefaultTimeout(15, TimeUnit.SECONDS)
        rxFit.config().disableFit()

        presenter = MainPresenter(rxFit)
        presenter.attachView(this)

        presenter.getFitnessData()
    }

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

    // View Interface

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun showRetrySnackbar() {
        progressBar.visibility = View.GONE
        Snackbar.make(recyclerView, "Error getting Fit data", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry") { v -> presenter.getFitnessData() }
                .show()
    }

    override fun onFitnessSessionDataLoaded(fitnessSessionDataList: List<FitnessSessionData>) {
        recyclerView.adapter = FitnessSessionAdapter(fitnessSessionDataList)
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        if (fitnessSessionDataList.isEmpty()) {
            Snackbar.make(recyclerView, "No sessions found", Snackbar.LENGTH_INDEFINITE).show()
        }
    }
}
