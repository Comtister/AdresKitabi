package com.example.adreskitab;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.security.Permissions;
import java.security.acl.Permission;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    private MaterialAlertDialogBuilder materialAlertDialogBuilderChois;
    private boolean alertDurum = false;
    private Geocoder geocoder;
    private List<Address> adressList;
    private String[] adresDetay;
    private String[] bosAdres;
    private LatLng kullaniciKonum;
    private Button saveBtn;
    private Intent intent;
    private int anahtar;
    private String[] adresEnlem;
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        saveBtn = (Button) findViewById(R.id.mapSaveBtn);
        bosAdres = new String[]{"Tam Adres Alınamadı"};



        sharedPref = getApplicationContext().getSharedPreferences("adresPref",Context.MODE_PRIVATE);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        intent = getIntent();
        anahtar = intent.getIntExtra("anahtar",0);

        if(anahtar == 0){

            if(!(sharedPref.getBoolean("d1",false))){

                materialAlertDialogBuilderChois = new MaterialAlertDialogBuilder(MapsActivity.this)

                        .setTitle("Bilgilendirme")
                        .setMessage("Kaydetmek İstediğiniz Lokasyonu Basılı Tutunuz,Kendi Konumunuzu Kaydetmek İstiyorsanız Sağ Alttaki Butona Tıklayınız.")
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNeutralButton("Bir Daha Gösterme", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putBoolean("d1",true);
                                editor.commit();

                            }
                        });
                materialAlertDialogBuilderChois.show();


            }





            //LongClick Event
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(final LatLng latLng) {



                    mMap.addMarker(new MarkerOptions().position(latLng));
                    //Alert Dialog Kayıt isteniyormu

                    materialAlertDialogBuilder = new MaterialAlertDialogBuilder(MapsActivity.this)
                            .setTitle("Uyarı")
                            .setMessage("Adresi Kaydetmek İstiyormusunuz ?")
                            .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

                                    try {


                                        adressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);


                                        adresDetay = adressList.get(0).getAddressLine(0).split(",");
                                        System.out.println(adresDetay[0]);
                                        System.out.println(adresDetay[1]);
                                        System.out.println(adresDetay[2]);

                                        System.out.println(adressList);
                                        alertDurum = true;
                                        Intent intent = new Intent(MapsActivity.this,AddressActivity.class);
                                        intent.putExtra("konum",latLng);
                                        intent.putExtra("adres",adresDetay);
                                        startActivity(intent);
                                        finish();

                                    }catch (Exception e){
                                        e.printStackTrace();
                                        e.getLocalizedMessage();
                                        alertDurum = true;
                                        Intent intent = new Intent(MapsActivity.this,AddressActivity.class);
                                        intent.putExtra("konum",latLng);
                                        intent.putExtra("adres",bosAdres);
                                        startActivity(intent);
                                        finish();
                                    }



                                }
                            })
                            .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDurum = false;
                                    mMap.clear();
                                }
                            })
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {

                                    if(alertDurum){
                                        return;
                                    }else{
                                        mMap.clear();
                                        //Toast
                                        Toast.makeText(MapsActivity.this,"İşlem İptal Edildi",Toast.LENGTH_LONG).show();
                                    }


                                }
                            });


                    materialAlertDialogBuilder.show();







                }
            });

            //Location Changed Events
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    kullaniciKonum = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kullaniciKonum,15));
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
            };



            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkPermission();



        }

        if(anahtar == 1){

            saveBtn.setVisibility(View.INVISIBLE);
            Intent intent = getIntent();
            adresEnlem = intent.getStringArrayExtra("enlemBoylam");

            final LatLng enlemBoylam = new LatLng(Double.parseDouble(adresEnlem[0]),Double.parseDouble(adresEnlem[1]));

            mMap.addMarker(new MarkerOptions().position(enlemBoylam));

            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    kullaniciKonum = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(enlemBoylam,15));
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
            };



            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            checkPermission();


        }
        //Eski Kodlar

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void checkPermission() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,10,locationListener);
            mMap.setMyLocationEnabled(true);

        } else {

            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //izin verildi
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,100,10,locationListener);
                mMap.setMyLocationEnabled(true);

            }else{
                //izinverilmedi


            }


        }


    }

   public void kaydetBtnAction(View view){

        geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());

        try {
            adressList = geocoder.getFromLocation(kullaniciKonum.latitude,kullaniciKonum.longitude,1);
            adresDetay = adressList.get(0).getAddressLine(0).split(",");
            Intent intent = new Intent(MapsActivity.this,AddressActivity.class);
            intent.putExtra("konum",kullaniciKonum);
            intent.putExtra("adres",adresDetay);
            startActivity(intent);
            finish();
        }catch (Exception e){
            e.getLocalizedMessage();
            e.printStackTrace();
            Intent intent = new Intent(MapsActivity.this,AddressActivity.class);
            intent.putExtra("konum",kullaniciKonum);
            intent.putExtra("adres",bosAdres);
            startActivity(intent);
            finish();
        }








    }




}
