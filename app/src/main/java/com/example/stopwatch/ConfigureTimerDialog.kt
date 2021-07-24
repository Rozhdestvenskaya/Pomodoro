package com.example.stopwatch

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.stopwatch.databinding.DialogConfigureTimerBinding

class ConfigureTimerDialog : DialogFragment() {
    private var positiveButton: Button? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogConfigureTimerBinding.inflate(layoutInflater, null, false)
        binding.numberPickerSeconds.maxValue = MAX
        binding.numberPickerMinutes.maxValue = MAX
        binding.numberPickerHours.maxValue = HOURS_MAX

        val listener = NumberPicker.OnValueChangeListener { _, _, _ ->
            positiveButton?.isEnabled = binding.numberPickerSeconds.value != 0
                    || binding.numberPickerMinutes.value != 0
                    || binding.numberPickerHours.value != 0
        }

        binding.numberPickerSeconds.setOnValueChangedListener(listener)
        binding.numberPickerMinutes.setOnValueChangedListener(listener)
        binding.numberPickerHours.setOnValueChangedListener(listener)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("Установите время")
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                if (activity is OnTimerConfiguredListener)
                    (activity as OnTimerConfiguredListener).onTimerConfigured(
                        "${binding.numberPickerHours.value}:${binding.numberPickerMinutes.value}:${binding.numberPickerSeconds.value}"
                            .toTimeInSeconds()
                    )
            }
            .setNegativeButton(getString(android.R.string.cancel), null)
            .create().apply {
            setOnShowListener {
                positiveButton = getButton(DialogInterface.BUTTON_POSITIVE)
                positiveButton?.isEnabled = binding.numberPickerSeconds.value != 0
                        || binding.numberPickerMinutes.value != 0
                        || binding.numberPickerHours.value != 0
            }
        }
    }

    companion object {
        const val TAG = "ConfigureTimerDialog"
        private const val MAX = 59
        private const val HOURS_MAX = 5
    }
}