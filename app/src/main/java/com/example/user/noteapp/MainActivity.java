package com.example.user.noteapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static ArrayList<String> notes=new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    static Set<String> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=findViewById(R.id.listview);
        SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.user.noteapp", Context.MODE_PRIVATE);
        set = sharedPreferences.getStringSet("notes",null);
        notes.clear();
        if(set!=null){
            notes.addAll(set);

        }
        else{
            notes.add("Emaple One");
            set=new HashSet<String>();
            set.addAll(notes);
            sharedPreferences.edit().putStringSet("notes",set).apply();
        }



        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i =new Intent(getApplicationContext(),EditYourNote.class);
                i.putExtra("noteId",position);
                startActivity(i);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you Sure?")
                        .setMessage("Do you want to delete this?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                SharedPreferences sharedPreferences=MainActivity.this.getSharedPreferences("com.example.user.noteapp", Context.MODE_PRIVATE);
                                if(set==null){
                                    set=new HashSet<String>();
                                }else {
                                    set.clear();
                                }
                                set.clear();
                                set.addAll(notes);
                                sharedPreferences.edit().remove("notes").apply();
                                sharedPreferences.edit().putStringSet("notes",set).apply();
                                arrayAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("NO",null)
                        .show();


                return true;
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.add){
            notes.add("");
            SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.user.noteapp", Context.MODE_PRIVATE);
            if(set==null){
                set=new HashSet<String>();
            }else {
                set.clear();
            }
            set.clear();
            set.addAll(notes);
            sharedPreferences.edit().remove("notes").apply();
            sharedPreferences.edit().putStringSet("notes",set).apply();
            arrayAdapter.notifyDataSetChanged();
            Intent i =new Intent(getApplicationContext(),EditYourNote.class);
            i.putExtra("noteId",notes.size()-1);
            startActivity(i);


            return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
