package kerim.android.bade

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsFragment : Fragment(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private val locationPermissionCode = 1
    //private var places = mutableListOf<LatLng>()
    private val viewModel: MainActivityViewModel by activityViewModels()
    //private val tseryData = ArrayList<Tsery>()

    companion object {
        fun newInstance() = MapsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        if(isLocationPermissionGranted()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)
        }
        else {
            requestLocationPermission()
        }
        return view
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val zoomLevel = 4.7f //zoom in, zoom out
        val overlaySize = 100f  //
        val bronnoysund = LatLng(65.479317, 11.982560)
        val bjorvika = LatLng(59.90, 10.75)



        viewModel.loadTseryData()
        viewModel.getBadeDataObserver().observe(this){
            for (i in it) {
                mMap.addMarker(MarkerOptions().position(
                    LatLng(i.header.extra.pos.lat.toDouble(),i.header.extra.pos.lon.toDouble())))
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bronnoysund, zoomLevel))

        val googleOverlay = GroundOverlayOptions()
            .image(BitmapDescriptorFactory.fromResource(R.drawable.android))
            .position(bjorvika, overlaySize)


        mMap.addGroundOverlay(googleOverlay)
        if (isLocationPermissionGranted()) {
            mMap.isMyLocationEnabled = true
        }

        with(mMap.uiSettings) {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = true
        }
        mMap.setPadding(0,0,0,140)
        mMap.setOnMarkerClickListener(this)

    }


    private fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
    }
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            locationPermissionCode)
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (parentFragmentManager.fragments.last() !is DetailsFragment){
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in2,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out2
                )
                .replace(R.id.container, DetailsFragment.newInstance(marker.position))
                .addToBackStack(null)
                .commit()
        }
        return false
    }
}