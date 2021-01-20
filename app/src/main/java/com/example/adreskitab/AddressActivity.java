package com.example.adreskitab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import java.io.ByteArrayOutputStream;
import de.hdodenhof.circleimageview.CircleImageView;

public class AddressActivity extends AppCompatActivity {

    private String adresDetay[];
    private LatLng gelenKonum;
    private CircleImageView adresImage;
    private TextView adresText;
    private EditText adresBaslikText;
    private Button saveBtn;
    private Button cancelBtn;
    private PopupMenu popupMenu;
    private byte[] imageArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        adresText = (TextView) findViewById(R.id.adresDetayText);
        adresBaslikText = (EditText) findViewById(R.id.adresCardBaslikText);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        adresImage = (CircleImageView) findViewById(R.id.addressImageView);
        popupMenu = new PopupMenu(AddressActivity.this,adresImage);

        popupMenu.getMenuInflater().inflate(R.menu.image_options_menu,popupMenu.getMenu());



        imageArray = bitmapToByte(BitmapFactory.decodeResource(getResources(),R.drawable.standart_location));

        final Intent intent = getIntent();
        gelenKonum = intent.getExtras().getParcelable("konum");
        adresDetay = intent.getStringArrayExtra("adres");

        setAdresText();



        adresImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {



                        if(item.getItemId() == R.id.action_camera){

                            if(ContextCompat.checkSelfPermission(AddressActivity.this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                                //Kamera Açılacak
                                openCamera();

                            }else{

                                requestPermissions(new String[] {Manifest.permission.CAMERA},1);

                            }

                        }
                        if(item.getItemId() == R.id.action_gallery){

                            if(ContextCompat.checkSelfPermission(AddressActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                                //Galeriye Gidilecek
                                openGallery();

                            }else{

                                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},2);

                            }

                        }


                        return false;
                    }
                });
                popupMenu.show();
            }
        });






    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //izin verildi ise
                openCamera();
            }else{
                //izin verilmedi
            }
        }

        if(requestCode == 2){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //İzin Verildi
                openGallery();
            }else{
                //izin verilmedi
            }
        }


    }

    private void setAdresText(){

   for(int i = 0;i < adresDetay.length;i++){

        if(i == 0){
            adresText.append(adresDetay[i]);
        }else{
            adresText.append("," + adresDetay[i]);
        }

    }


}



private void openCamera(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,0);
        }


}

private void openGallery(){

    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(intent,1);

}

private byte[] bitmapToByte(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0,byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        return bytes;



}


private Bitmap compressImage(Bitmap image){

        int x = image.getHeight();
        int y = image.getWidth();
        System.out.println(x);
        System.out.println(y);
        if(x > 256 || y > 256) image = Bitmap.createScaledBitmap(image,256,256,false);
        System.out.println(image.getByteCount());
        return image;
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK){
            Bundle image = data.getExtras();
            Bitmap bitmapImage = (Bitmap) image.get("data");
            System.out.println(bitmapImage.getByteCount());
            //imaj küçültme
            bitmapImage = compressImage(bitmapImage);
            imageArray = bitmapToByte(bitmapImage);
            adresImage.setImageBitmap(BitmapFactory.decodeByteArray(imageArray,0,imageArray.length));
        }
        if(requestCode == 1 && resultCode == RESULT_OK){

            Uri imageUri = data.getData();
            try {

                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(),imageUri);
                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                    System.out.println(bitmap.getByteCount());
                    bitmap = compressImage(bitmap);
                    imageArray = bitmapToByte(bitmap);
                    adresImage.setImageBitmap(BitmapFactory.decodeByteArray(imageArray,0,imageArray.length));
                }else{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                    bitmap = compressImage(bitmap);
                    imageArray = bitmapToByte(bitmap);
                    adresImage.setImageBitmap(BitmapFactory.decodeByteArray(imageArray,0,imageArray.length));
                }

            }catch (Exception e){
                e.printStackTrace();
                e.getLocalizedMessage();
            }



        }

    }


    public void saveAdress(View view){

        DatabaseHelper databaseHelper = new DatabaseHelper(AddressActivity.this);

        String baslik = String.valueOf(adresBaslikText.getText());
        String enlem = String.valueOf(gelenKonum.latitude);
        String boylam = String.valueOf(gelenKonum.longitude);
        String kordinat = enlem + "," + boylam;
        String tamAdres = "";
        for(int i = 0;i<adresDetay.length;i++){
            tamAdres += adresDetay[i];
        }



       Adres adres = new Adres(baslik,tamAdres,kordinat,imageArray);

        if(baslik.equals("")){
            //Toast Mesajı
            Toast.makeText(AddressActivity.this,"Lütfen Başlık Giriniz",Toast.LENGTH_LONG).show();
            return;
        }else{

            try {



                databaseHelper.dataPut(adres);

            }catch (Exception e){
                e.printStackTrace();
                e.getLocalizedMessage();
            }


        }

        finish();



    }

    public void cancelSaveAdress(View view){

        finish();

    }


}
