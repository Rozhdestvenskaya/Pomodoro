package com.example.stopwatch

import android.content.res.Resources
import android.graphics.drawable.AnimationDrawable
import android.view.View
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatch.databinding.StopwatchItemBinding

class StopwatchViewHolder(
    private val binding: StopwatchItemBinding,
    private val listener: StopwatchListener,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(stopwatch: Stopwatch) {
        binding.stopwatchTimer.text = stopwatch.remainingSec.displayTime()

        val drawable =
            if (stopwatch.isActive) {
                binding.blinkingIndicator.isInvisible = false
                (binding.blinkingIndicator.background as? AnimationDrawable)?.start()
                resources.getDrawable(R.drawable.ic_baseline_pause_24)
            } else {
                binding.blinkingIndicator.isInvisible = true
                (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
                resources.getDrawable(R.drawable.ic_baseline_play_arrow_24)
            }

        binding.startPauseButton.setImageDrawable(drawable)

        binding.stopwatchTimer.text = stopwatch.remainingSec.displayTime()

        binding.customProgress.progress = stopwatch.calculateProgress()

        if (stopwatch.remainingSec > 0) {
            binding.root.setCardBackgroundColor(resources.getColor(android.R.color.white))
        } else {
            binding.root.setCardBackgroundColor(resources.getColor(android.R.color.darker_gray))
            binding.startPauseButton.visibility = View.GONE
            binding.blinkingIndicator.isInvisible = true
            (binding.blinkingIndicator.background as? AnimationDrawable)?.stop()
        }

        initButtonsListeners(stopwatch)
    }

    private fun initButtonsListeners(stopwatch: Stopwatch) {
        binding.startPauseButton.setOnClickListener {
            if (stopwatch.isActive) {
                listener.stop(stopwatch.id)
            } else {
                listener.start(stopwatch.id)
            }
        }

        binding.deleteButton.setOnClickListener { listener.delete(stopwatch.id) }
    }
}