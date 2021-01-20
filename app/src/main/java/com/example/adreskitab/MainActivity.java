package com.example.adreskitab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


   private Toolbar topBar;
   public RecyclerView adresList;
   public ListAdapter adresListAdapter;
   public ArrayList<Adres> dataList;
   public DatabaseHelper dh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataList = new ArrayList<Adres>();
        dh = new DatabaseHelper(MainActivity.this);

        dataList = dh.dataList();

        //Setup ToolBar
        topBar = (Toolbar) findViewById(R.id.topbar);

        topBar.setTitle("Adres Kitabım");

        if(Build.VERSION.SDK_INT < 23){
            topBar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorBlack));
        }else{
            topBar.setTitleTextColor(getResources().getColor(R.color.colorBlack,null));
        }

        setSupportActionBar(topBar);

        //Recyler View Setup
        adresList = (RecyclerView) findViewById(R.id.adresRecyle);

        adresList.setHasFixedSize(true);

        adresList.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        adresListAdapter = new ListAdapter(MainActivity.this,dataList);

        adresList.setAdapter(adresListAdapter);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dataListing();
    }


    //Setup Top Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_bar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Menu Actions
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.adress_action){

            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            intent.putExtra("anahtar",0);
            startActivity(intent);


        }
        if(item.getItemId() == R.id.about_action){

            Intent intent = new Intent(MainActivity.this,HakkimdaActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    public void dataListing(){
      dataList = dh.dataList();
      adresListAdapter.updateList(dataList);



    }



}
