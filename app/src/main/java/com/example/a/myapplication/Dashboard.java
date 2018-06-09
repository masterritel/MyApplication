package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Dashboard extends AppCompatActivity {
String id;
String nom,num;
ListView l;
    JSONParser json =new JSONParser();
    ProgressDialog p;
    int success;
    ArrayList<HashMap<String,String>> values=new ArrayList<HashMap<String,String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        l=findViewById(R.id.lst3);
        Bundle x=getIntent().getExtras();
        if(x!=null)
        {
            id=x.getString("key");
            Toast.makeText(Dashboard.this,id,Toast.LENGTH_LONG).show();
            new getLogin().execute();

        }
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent= new Intent(Dashboard.this,DetailContact.class);
                intent.putExtra("key",id);
                intent.putExtra("key2",nom);
                intent.putExtra("key3",num);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,1,"ajouter sms");

        menu.add(0,2,5,"deconnexion");
        menu.add(0,3,2,"Messages Envoyés");
        menu.add(0,4,3,"Messages Reçus");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 1:Intent i=new Intent(Dashboard.this,AddSms.class);
            i.putExtra("key",id);
                startActivity(i);break;
            case 2:finish();

            case 3:Intent ii=new Intent(Dashboard.this,ListSMSEnvoye.class);
                ii.putExtra("id",id);
                startActivity(ii);
                break;
            case 4:Intent iii=new Intent(Dashboard.this,ListSMSRecu.class);
                iii.putExtra("pid",id);
                startActivity(iii);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    class getLogin extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p=new ProgressDialog(Dashboard.this);
            p.setMessage("loading");
            p.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("user",id);

            JSONObject o=json.makeHttpRequest("https://salhapfe.000webhostapp.com/allContact.php","GET",map);

            try {
                success=o.getInt("success");
                if(success==1)
                {
                    JSONArray array=o.getJSONArray("contacts");
                    for(int i=0;i<array.length();i++) {
                        JSONObject ob = array.getJSONObject(i);
                        map=new HashMap<String, String>();
                        map.put("id",ob.getString("id"));
                        map.put("nom",ob.getString("nom"));
                        map.put("num",ob.getString("numero"));
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
            SimpleAdapter adapter=new SimpleAdapter(Dashboard.this,values,R.layout.item_contact,
                    new String[]{"id","nom","num"},new int[]{R.id.id,R.id.nom,R.id.num});
            l.setAdapter(adapter);
        }
    }


}
