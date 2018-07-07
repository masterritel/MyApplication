package com.example.a.myapplication;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UpdateContact extends AppCompatActivity {
TextView t;
EditText e;
Button update;
JSONParser json;
ProgressDialog p2;
String id,nom,c;
int success;
DatabaseHandler dbHandler;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_contact);
        t=findViewById(R.id.textView);
        e=findViewById(R.id.editText);
        update=findViewById(R.id.button);
        Bundle x=getIntent().getExtras();
        if(x!=null)
        {
            id=x.getString("pid");
            nom=x.getString("nom");
            c=x.getString("num");
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Upd().execute();
                if(success==1)
                {
                    Intent ii = new Intent(Intent.ACTION_INSERT_OR_EDIT);
                    ii.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);

                    ii.putExtra("pid", id);
                    ii.putExtra(ContactsContract.Intents.Insert.PHONE,"1233");
                  /*  if(ContextCompat.checkSelfPermission(UpdateContact.this, Manifest.permission.CALL_PHONE)!=  PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(UpdateContact.this,new String[]{Manifest.permission.CALL_PHONE},102);
                    }
                    else {*/
                        startActivity(ii);
                  /* }*/
                }

            }
        });


    }
    class Upd extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            p2=new ProgressDialog(UpdateContact.this);
            p2.setMessage("loading");
            p2.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String>mp=new HashMap<>();
            mp.put("user",id);
            mp.put("num",e.getText().toString());
            JSONObject o=json.makeHttpRequest("https://salhapfe.000webhostapp.com/updateContact.php","GET",mp);
            try {
                 success=o.getInt("success");

            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            p2.dismiss();
            if(success==1)
            {
                Toast.makeText(UpdateContact.this,"Update success",Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(s);
        }
    }
}
