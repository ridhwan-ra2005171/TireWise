package com.mrm.tirewise.networkConnectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkConnectivityObserver( private val context : Context) : ConnectivityObserver {

    // Create an instance of ConnectivityManager
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observe() : Flow<ConnectivityObserver.Status> {
        return callbackFlow {
            val callback = object : ConnectivityManager.NetworkCallback() {
                // Override the function of ConnectivityManager.NetworkCallback

                override fun onAvailable(network: android.net.Network) {
                    super.onAvailable(network)
                    trySend(ConnectivityObserver.Status.Available) // send the available status
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(ConnectivityObserver.Status.Unavailable) // send the unavailable status
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(ConnectivityObserver.Status.Unavailable) // send the unavailable status
                }
            }

            // Register the callback, in order to call this function whenever there is a change
            connectivityManager.registerDefaultNetworkCallback(callback)

            // Await close is used to close the flow whenever the coroutine is cancelled (aka whenever the lifecycle ends)
            awaitClose {
                connectivityManager.unregisterNetworkCallback(callback) // Unregister the callback
            }
        }.distinctUntilChanged() // Only send the status when it changes. For example if it goes from Available to ,again, Available, the status will not be sent
    }
}