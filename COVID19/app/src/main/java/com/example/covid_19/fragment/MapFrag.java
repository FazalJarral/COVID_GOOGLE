package com.example.covid_19.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.covid_19.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapFrag extends Fragment implements OnMapReadyCallback, LocationListener {
    GoogleMap mMap;
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_map, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (getActivity() != null) {
            Log.e("getActivity", "Not Null");
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
            if (mapFragment != null) {
                Log.e("mapFrag", "Not Null");

                mapFragment.getMapAsync(this);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.clear(); //clear old markers

        CameraPosition googlePlex = CameraPosition.builder()
                .target(new LatLng(33.5614357, 72.8811214))
                .zoom(10)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(10, 10, 10, 100);

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

        // Labs
      addLabs();
      //Center
        addQuarantineCenter();
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("Location ", location.getLatitude() + " " + location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                .title("My Location"));
        // .icon(bitmapDescriptorFromVector(getActivity() , R.drawable.hospital))
        //  .snippet("(051) 9261170"));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    private void addLabs() {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.6887833, 73.1373039))
                .title("National Institute of Health")
                .snippet("(051) 9255881")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(30.192891, 66.9501811))
                .title("Fatima Jinnah Hospital Quetta")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.9589698, 71.4419387))
                .title("Khyber Medical University\n")
                .snippet("+92 91 9217703")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.5469491, 74.3212877))
                .title("Punjab AIDS Control Program\n")
                .snippet("+92 42 37802425")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(31.4487693, 74.2695271))
                .title("Shaukat Khanum Memorial Cancer Hospital and Research Centre")
                .snippet("+92 42 35905000")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(30.2034382, 71.4448101))
                .title("Nishtar Hospital Emergency Department")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.5798447, 73.0431559))
                .title("Armed Forces Institute of Pathology")
                .snippet("+92 51 5176419")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));


        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(24.893656, 67.0709483))
                .title("Aga Khan University Hospital")
                .snippet("+92 21 111 911 911")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(24.9432441, 67.1380157))
                .title("Radiology Department Ojha Campus DUHS")
                .snippet("+92 21 99232660")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( 24.8590144, 67.0079305))
                .title("Civil Hospital Karachi")
                .snippet("+92 21 99215740")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( 24.8432793, 67.0716635))
                .title("The Indus Hospital")
                .snippet("+92 21 35112709")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( 24.8139737, 67.0200971))
                .title("South City Hospital")
                .snippet("+92 21 35374072")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( 34.3312105, 73.4664506))
                .title("AIMS Hospital")
                .snippet("+92 58224 39306")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng( 35.91814, 74.3029741))
                .title("D.H.Q Hospital Gilgit")
                .snippet("+92 58119 20253")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.laboratory)));
    }

    private void addQuarantineCenter(){
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.6176196, 72.970398))
                .title("Haji Camp Islamabad")
                .snippet("Bed Capacity : 300")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.hospital)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(33.6972334, 73.0830579))
                .title("Pakistan-China Friendship Center")
                .snippet("Bed Capacity : 50")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.hospital)));

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(28.5429266, 68.2067755))
                .title("Community Midwifery School")
                .snippet("Bed Capacity : 50")
                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.hospital)));
    }
}
