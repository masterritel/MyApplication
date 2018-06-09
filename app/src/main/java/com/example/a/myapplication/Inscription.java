package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Inscription extends AppCompatActivity {
EditText nom,mail,pass,tel;
Button add;
    JSONParser json =new JSONParser();
    ProgressDialog p ;
    int success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        nom=findViewById(R.id.nom);
        mail=findViewById(R.id.mail);
        pass=findViewById(R.id.pass);
        tel=findViewById(R.id.tel);
        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
new addUser().execute();
            }
        });
    }

    class addUser extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p=new ProgressDialog(Inscription.this);
            p.setMessage("loading");
            p.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("nom",nom.getText().toString());
            map.put("mail",mail.getText().toString());
            map.put("pass",pass.getText().toString());
            map.put("phone",tel.getText().toString());

            JSONObject o=json.makeHttpRequest("https://salhapfe.000webhostapp.com/inscription.php","GET",map);
            try {
                success=o.getInt("success");
                if(success==1)
                {
                    Intent i=new Intent(Inscription.this,MainActivity.class);
                    startActivity(i);

                }





            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            if(success==0)
            {
                Toast.makeText(Inscription.this,"Echec",Toast.LENGTH_LONG).show();

            }
        }
    }
}
