package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class DetailContact extends AppCompatActivity {
ImageButton delete,edit;
JSONParser json;
ProgressDialog p,p2;
String id,nom,c;
int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
        Bundle x=getIntent().getExtras();
        if(x!=null)
        {
            id=x.getString("key1");
            nom=x.getString("key2");
            c=x.getString("key3");


        }
        delete=findViewById(R.id.delete);
        edit=findViewById(R.id.edit);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new det().execute();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DetailContact.this,UpdateContact.class);
                intent.putExtra("pid",id);
                intent.putExtra("nom",nom);
                intent.putExtra("num",c);
                startActivity(intent);
            }
        });

    }
    class det extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            p=new ProgressDialog(DetailContact.this);
            p.setMessage("loading");
            p.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<>();
            map.put("user",id);
            JSONObject o=json.makeHttpRequest("https://salhapfe.000webhostapp.com/deleteContact.php","GET",map);
            try {
                success=o.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            p.dismiss();
            if(success==1)
            {
                Toast.makeText(DetailContact.this,"votre contact est delete avec succé",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }
    }
   
}
