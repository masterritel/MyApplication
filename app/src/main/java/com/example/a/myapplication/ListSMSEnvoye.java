package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListSMSEnvoye extends AppCompatActivity {
String id;
ListView l;

    ArrayList<HashMap<String,String>>values=new ArrayList<HashMap<String,String>>();
    HashMap<String,String> map;

    JSONParser json =new JSONParser();
    ProgressDialog p ;
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_smsenvoye);
        l=findViewById(R.id.lst);
        Bundle x=getIntent().getExtras();
        if(x!=null)
        {
            id=x.getString("id");
new getLogin().execute();
        }
      /* l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(ListSMSEnvoye.this,DetailContact.class);
                i.putExtra("key",id);
                startActivity(i);
            }
        });*/



    }

    class getLogin extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p=new ProgressDialog(ListSMSEnvoye.this);
            p.setMessage("loading");
            p.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("user",id);

            JSONObject o=json.makeHttpRequest("https://salhapfe.000webhostapp.com/allMessageEnvoyer.php","GET",map);

            try {
                success=o.getInt("success");
                if(success==1)
                {
                    JSONArray array=o.getJSONArray("envoyes");
                    for(int i=0;i<array.length();i++) {
                        JSONObject ob = array.getJSONObject(i);
                        map=new HashMap<String, String>();
                        map.put("id",ob.getString("id"));
                        map.put("text",ob.getString("text"));
                        map.put("date",ob.getString("date"));
                        map.put("contact",ob.getString("contact"));

                        values.add(map);
                    }
                    Log.e("values",values.toString());
             /*       Intent i=new Intent(AddSms.this,Dashboard.class);
                    i.putExtra("key",id);
                    startActivity(i);*/

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
            SimpleAdapter adapter=new SimpleAdapter(ListSMSEnvoye.this,values,R.layout.item_send,
                    new String[]{"id","contact","text"},new int[]{R.id.id,R.id.contact,R.id.message});
            l.setAdapter(adapter);


        }
    }

}
