package io.softwareshenanigans.catalyst.model

import android.app.Service
import android.content.Intent
import android.content.IntentSender
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task

class LocationService : Service() {
    private val TAG = "LocationService"

    override fun onStart(intent: Intent?, startId: Int) {
        //TODO: foreground service notification stuffs
        Log.i(TAG, "location service started")
        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
//                    exception.startResolutionForResult(applicationContext,
//                        REQUEST_CHECK_SETTINGS)
                    Log.e("ERROR", "Cannot get location due to resolvable exception", exception)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(p0: LocationAvailability?) {
                super.onLocationAvailability(p0)
                Log.i(TAG, "location availability")
            }

            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                Log.i(TAG, "Location is $locationResult")
                if (locationResult?.lastLocation != null) {

//
//                    // Normally, you want to save a new location to a database. We are simplifying
//                    // things a bit and just saving it as a local variable, as we only need it again
//                    // if a Notification is created (when user navigates away from app).
//                    currentLocation = locationResult.lastLocation
//
//                    // Notify our Activity that a new location was added. Again, if this was a
//                    // production app, the Activity would be listening for changes to a database
//                    // with new locations, but we are simplifying things a bit to focus on just
//                    // learning the location side of things.
//                    val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
//                    intent.putExtra(EXTRA_LOCATION, currentLocation)
//                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
//
//                    // Updates notification content if this service is running as a foreground
//                    // service.
//                    if (serviceRunningInForeground) {
//                        notificationManager.notify(
//                            NOTIFICATION_ID,
//                            generateNotification(currentLocation))
//                    }
                } else {
                    Log.d(TAG, "Location information isn't available.")
                }
            }
        }

        task.addOnSuccessListener { _ ->
            Log.i(TAG, "onSuccess for settings task")
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
        }
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

}
