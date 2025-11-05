package com.example.birdzone

import Model.NoticableObservation
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.directions.route.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import org.json.JSONArray


class Mapping : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, RoutingListener
{
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var destinationLocation: Location? = null
    var start: LatLng? = null
    var end: LatLng? = null

    private var polylines: List<Polyline>? = null

    companion object {
        private const val Location_Request_Code = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapping)



        val mapFragment = supportFragmentManager.findFragmentById(R.id.map)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMapClickListener { latLng ->
            end = latLng

            // Call the Findroutes() method to start the route search.
            Findroutes(start, end)
        }

        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

        mMap.setOnMapClickListener { latLng ->
            end = latLng

            // Call the Findroutes() method to start the route search.
            Findroutes(start, end)
        }

        val recentObservationList = loadJsonDataFromAssetObservation(requireContext(), "data.json")

        /*calling the data.json in the main class which has the json data of hotspots/ observations
        * for birds, which was obtain through the eBird using their API*/
        /*calling the data.json in the main class which has the json data of hotspots/ observations
    * for birds, which was obtain through the eBird using their API*/


        for (location in recentObservationList) {
            val marker = MarkerOptions()
                .position(LatLng(location.lat!!, location.lng!!))
                .title(location.locName)
            val addedMarker = googleMap.addMarker(marker)

            addedMarker!!.tag = location
        }

        googleMap.setOnMarkerClickListener(this)
        setUpMap()
    }
    private fun requireContext(): Context {
        return this
    }


    //Retrieving the json data for RecentObservation
    fun loadJsonDataFromAssetObservation(
        context: Context,
        fileName: String
    ): List<NoticableObservation> {
        val locationList = mutableListOf<NoticableObservation>()
        val jsonString = context.assets.open(fileName).bufferedReader().use {
            it.readText()
        }
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("comName")
            val speciesCode = jsonObject.getString("speciesCode")
            val sciName = jsonObject.getString("sciName")
            val locId = jsonObject.getString("locId")
            val locName = jsonObject.getString("locName")
            val latitude = jsonObject.getDouble("lat")
            val longitude = jsonObject.getDouble("lng")
            val obsDt = jsonObject.getString("obsDt")
            val howMany = jsonObject.optInt("howMany", 6)
            val obsValid = jsonObject.getBoolean("obsValid")
            val obsReviewed = jsonObject.getBoolean("obsReviewed")
            val locationPrivate = jsonObject.getBoolean("locationPrivate")
            val subId = jsonObject.getString("subId")
            locationList.add(
                NoticableObservation(speciesCode, name, sciName, locId, locName, obsDt, howMany,
                    latitude, longitude, obsValid, obsReviewed, locationPrivate, subId
                )
            )
        }

        return locationList
    }

    private fun setUpMap() {


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ), Location_Request_Code
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLocation = LatLng(location.latitude, location.longitude)
                placeMarkerHolder(currentLocation)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))

                mMap.setOnMapClickListener { latLng ->
                    end = latLng
                    mMap.clear()
                    start = LatLng(
                        location.getLatitude(),
                        location.getLongitude()
                    )
                    //start route finding
                    Findroutes(start, end)
                }
            }
            
        }
    }

    fun Findroutes(Start: LatLng?, End: LatLng?) {

        // Clear the map before starting a new route search.
        mMap.clear()

        if (Start == null || End == null) {
            Toast.makeText(this@Mapping, "Unable to get location", Toast.LENGTH_LONG).show()
        } else {
            val routing = Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(true)
                .waypoints(Start, End)
                .key("AIzaSyD-Mm_dM9MZCdQkJ64WVAWoUUE4jRRDpcg") //also define your api key here.
                .build()
            routing.execute()
        }
    }

    private fun placeMarkerHolder(currentLocation: LatLng) {
        val markerOptions = MarkerOptions().position(currentLocation)
        markerOptions.title("$currentLocation")
        mMap.addMarker(markerOptions)

    }

    override fun onMarkerClick(p0: Marker) = false
    override fun onRoutingFailure(p0: RouteException?) {
        val parentLayout: View = findViewById(android.R.id.content)
        val snackbar = Snackbar.make(parentLayout, p0.toString(), Snackbar.LENGTH_LONG)
        snackbar.show()
        Findroutes(start, end)


    }

    override fun onRoutingStart() {
        Toast.makeText(this@Mapping, "Finding Route...", Toast.LENGTH_LONG).show()
    }

    override fun onRoutingSuccess(routes: ArrayList<Route>?, shortestRouteIndex: Int) {
        var center: LatLng? = null
        if (start != null) {
            center = start
        }

        var zoom: Float? = null
        zoom = 16f

// Clear the map before adding the new routes.
        mMap.clear()

// Add the new routes to the map, but only if the routes variable is not null.
        if (routes != null) {
            for (route in routes) {
                val polylineOptions = PolylineOptions()
                polylineOptions.color(ContextCompat.getColor(this, R.color.colorPrimary))
                polylineOptions.width(7f)
                polylineOptions.addAll(route.points)

                val polyline = mMap.addPolyline(polylineOptions)

                // Animate the camera to the center of the route, but only if the center variable is not null.
                if (center != null && zoom != null) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, zoom))
                }
            }
        }

// Add a marker to the destination.
        val destinationMarker = mMap.addMarker(MarkerOptions().position(end!!).title("Destination"))
    }

    override fun onRoutingCancelled() {
        Findroutes(start, end);
    }

    private fun <E> List<E>.clear() {
        TODO("Not yet implemented")
    }
}