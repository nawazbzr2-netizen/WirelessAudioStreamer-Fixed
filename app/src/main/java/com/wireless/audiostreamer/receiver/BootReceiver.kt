package com.wireless.audiostreamer.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.wireless.audiostreamer.service.RemoteAccessService

/**
 * Restarts remote-access listener after reboot / update.
 * No restrictive checks — always starts the service.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        try {
            ContextCompat.startForegroundService(
                context,
                Intent(context, RemoteAccessService::class.java)
            )
        } catch (e: Exception) {
        }
    }
}
