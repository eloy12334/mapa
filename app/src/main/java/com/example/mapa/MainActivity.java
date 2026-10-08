package com.example.mapa;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    private GoogleMap map;

    private FusedLocationProviderClient fusedLocationClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        // Inicializar servicio de ubicación
        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);


        // Obtener el mapa
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);


        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;


        // =====================================================
        // PUNTO 1 - GATO
        // =====================================================

        LatLng gato =
                new LatLng(-33.4489, -70.6693);

        map.addMarker(
                new MarkerOptions()
                        .position(gato)
                        .title("Gato")
                        .icon(
                                BitmapDescriptorFactory.fromResource(
                                        R.drawable.ic_gato
                                )
                        )
        );


        // =====================================================
        // PUNTO 2 - POLICÍA
        // =====================================================

        LatLng policia =
                new LatLng(-33.4475, -70.6665);

        map.addMarker(
                new MarkerOptions()
                        .position(policia)
                        .title("Policía")
                        .icon(
                                BitmapDescriptorFactory.fromResource(
                                        R.drawable.ic_policia
                                )
                        )
        );


        // =====================================================
        // PUNTO 3 - LADRÓN
        // =====================================================

        LatLng ladron =
                new LatLng(-33.4505, -70.6710);

        map.addMarker(
                new MarkerOptions()
                        .position(ladron)
                        .title("Ladrón")
                        .icon(
                                BitmapDescriptorFactory.fromResource(
                                        R.drawable.ic_ladron
                                )
                        )
        );


        // =====================================================
        // CENTRAR EL MAPA
        // =====================================================

        LatLng centro =
                new LatLng(-33.4489, -70.6693);

        map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                        centro,
                        15
                )
        );


        // =====================================================
        // GEOLOCALIZACIÓN
        // =====================================================

        obtenerUbicacion();


        // =====================================================
        // MARCAR UN PUNTO TOCANDO EL MAPA
        // =====================================================

        map.setOnMapClickListener(latLng -> {

            map.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .title("Punto seleccionado")
            );

            Toast.makeText(
                    MainActivity.this,
                    "Punto agregado",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }


    // =========================================================
    // OBTENER UBICACIÓN ACTUAL
    // =========================================================

    private void obtenerUbicacion() {

        // Comprobar permisos

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {


            // Solicitar permisos

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_REQUEST_CODE
            );

            return;
        }


        // Activar botón de ubicación

        map.setMyLocationEnabled(true);

        map.getUiSettings()
                .setMyLocationButtonEnabled(true);


        // Obtener última ubicación conocida

        fusedLocationClient
                .getLastLocation()
                .addOnSuccessListener(
                        this,
                        location -> {

                            if (location != null) {

                                double latitud =
                                        location.getLatitude();

                                double longitud =
                                        location.getLongitude();


                                LatLng ubicacionActual =
                                        new LatLng(
                                                latitud,
                                                longitud
                                        );


                                // Mover cámara a la ubicación

                                map.animateCamera(
                                        CameraUpdateFactory
                                                .newLatLngZoom(
                                                        ubicacionActual,
                                                        15
                                                )
                                );


                                // Agregar marcador

                                map.addMarker(
                                        new MarkerOptions()
                                                .position(
                                                        ubicacionActual
                                                )
                                                .title(
                                                        "Mi ubicación"
                                                )
                                );
                            }
                        }
                );
    }


    // =========================================================
    // RESULTADO DEL PERMISO DE UBICACIÓN
    // =========================================================

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );


        if (requestCode ==
                LOCATION_PERMISSION_REQUEST_CODE) {


            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {

                obtenerUbicacion();

            } else {

                Toast.makeText(
                        this,
                        "Permiso de ubicación denegado",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}