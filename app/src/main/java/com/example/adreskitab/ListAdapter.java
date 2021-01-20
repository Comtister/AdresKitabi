package com.example.adreskitab;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.AdresCardHolder>{

    Context context;
    ArrayList<Adres> dataList;
    DatabaseHelper databaseHelper;


    public ListAdapter(Context context, ArrayList<Adres> dataList) {
        this.context = context;
        this.dataList = dataList;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public AdresCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_card,parent,false);

        return new AdresCardHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdresCardHolder holder, final int position) {

        final Adres adres = dataList.get(position);

        final String[] enlemBoylam = adres.adresKordinat.split(",");



        holder.adresBaslik.setText(adres.adresBaslik);
        holder.adresDetay.setText(adres.adresDetay);

        Bitmap bitmap = BitmapFactory.decodeByteArray(dataList.get(position).adresImage,0,dataList.get(position).adresImage.length);

        holder.adresImage.setImageBitmap(bitmap);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //Alert D,alog Gelecek
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context)
                        .setTitle("Uyarı")
                        .setMessage("Kayıtlı Adresi Silmek İstediğinizden Eminmisiniz ?")
                        .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Hayır Aksiyonu Boş Geçilecek

                            }
                        })
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseHelper.dataDelete(adres.id);
                                updateList(databaseHelper.dataList());
                            }
                        });
                materialAlertDialogBuilder.show();

                return false;
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,MapsActivity.class);
                intent.putExtra("anahtar",1);
                intent.putExtra("enlemBoylam",enlemBoylam);
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }




    public class AdresCardHolder extends RecyclerView.ViewHolder{

        ImageView adresImage;
        TextView adresBaslik;
        TextView adresDetay;

        public AdresCardHolder(@NonNull View itemView) {
            super(itemView);

            adresImage = (ImageView) itemView.findViewById(R.id.adresCardImage);
            adresBaslik = (TextView) itemView.findViewById(R.id.adresCardBaslikText);
            adresDetay = (TextView) itemView.findViewById(R.id.adresCardDetayText);


        }
    }



    public void updateList(ArrayList<Adres> dataList){
        this.dataList = dataList;
        notifyDataSetChanged();

    }





}
