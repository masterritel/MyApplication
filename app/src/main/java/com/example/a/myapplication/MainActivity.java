package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
EditText login,pass;
Button ok;
TextView t;
JSONParser json =new JSONParser();
ProgressDialog p ;
int success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login=findViewById(R.id.login);
        pass=findViewById(R.id.pass);
        ok=findViewById(R.id.ok);
        t=findViewById(R.id.ins);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Inscription.class);
                startActivity(i);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
new getLogin().execute();
            }
        });

    }

class getLogin extends AsyncTask<String,String,String>
{
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        p=new ProgressDialog(MainActivity.this);
        p.setMessage("loading");
        p.show();

    }

    @Override
    protected String doInBackground(String... strings) {
        HashMap<String,String> map=new HashMap<String, String>();
        map.put("login",login.getText().toString());
        map.put("pass",pass.getText().toString());


        JSONObject o=json.makeHttpRequest("https://salhapfe.000webhostapp.com/login.php","GET",map);

        try {
            success=o.getInt("success");
            if(success==1)
            {
                JSONArray array=o.getJSONArray("user");
                    JSONObject ob=array.getJSONObject(0);
                    String ch=ob.getString("id");

                Intent i=new Intent(MainActivity.this,Dashboard.class);
                i.putExtra("key",ch);
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
            Toast.makeText(MainActivity.this,"login ou pass incorrecte",Toast.LENGTH_LONG).show();

        }
    }
}
}
