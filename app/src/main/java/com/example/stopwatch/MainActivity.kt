package com.example.stopwatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stopwatch.ForegroundService.Companion.COMMAND_ID
import com.example.stopwatch.ForegroundService.Companion.COMMAND_START
import com.example.stopwatch.ForegroundService.Companion.COMMAND_STOP
import com.example.stopwatch.ForegroundService.Companion.REMAINING_TIME_SEC
import com.example.stopwatch.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), StopwatchListener, LifecycleObserver, OnTimerConfiguredListener {

    private lateinit var binding: ActivityMainBinding

    private val stopwatchAdapter = StopwatchAdapter(this)
    private val stopwatches = mutableListOf<Stopwatch>()
    private var nextId = 0
    private var timerJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = stopwatchAdapter
        }

        binding.addNewStopwatchButton.setOnClickListener {
            ConfigureTimerDialog().show(supportFragmentManager, ConfigureTimerDialog.TAG)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        stopwatches.find { it.isActive }?.let {
            val startIntent = Intent(this, ForegroundService::class.java)
            startIntent.putExtra(COMMAND_ID, COMMAND_START)
            startIntent.putExtra(REMAINING_TIME_SEC, it.remainingSec)
            startService(startIntent)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        val stopIntent = Intent(this, ForegroundService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

    override fun onTimerConfigured(seconds: Int) {
        stopwatches.add(Stopwatch(nextId++, totalSec = seconds, isActive = false))
        stopwatchAdapter.submitList(stopwatches.toList())
    }

    override fun start(id: Int) {
        val stopwatch = stopwatches.find { it.id == id }
        timerJob?.cancel()
        stopwatch?.let {
            changeStopwatch(it.id, it.remainingSec, true)

            timerJob = lifecycleScope.launch(Dispatchers.Main + Job()) {
                while (true) {
                    delay(INTERVAL)
                    updateActiveStopwatchTimer(id)
                }
            }
        }
    }

    override fun stop(id: Int) {
        stopwatches.find { it.id == id }?.let {
            changeStopwatch(it.id, it.remainingSec, false)
        }
        timerJob?.cancel()
    }

    override fun delete(id: Int) {
        stopwatches.remove(stopwatches.find { it.id == id })
        stopwatchAdapter.submitList(stopwatches.toList())
    }

    private fun updateActiveStopwatchTimer(id: Int) {
        stopwatches.find { it.id == id }?.let {
            changeStopwatch(it.id, it.remainingSec - 1, it.isActive)
        }
    }


    private fun changeStopwatch(id: Int, remainingSec: Int, isActive: Boolean) {
        stopwatches.forEachIndexed { index, item ->
            if (item.id == id) {
                stopwatches[index] = stopwatches[index].copy(remainingSec = remainingSec, isActive = isActive)
            } else if (isActive) {
                stopwatches[index] = stopwatches[index].copy(isActive = false)
            }
        }
        stopwatchAdapter.submitList(stopwatches.toList())
    }

    private companion object {

        private const val INTERVAL = 1000L
    }
}