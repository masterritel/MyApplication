package com.example.a.myapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListSMSRecu extends AppCompatActivity {
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
        l=findViewById(R.id.lst2);
        Bundle x=getIntent().getExtras();
        if(x!=null)
        {
            id=x.getString("pid");
            new getLogin().execute();
        }



    }

    class getLogin extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p=new ProgressDialog(ListSMSRecu.this);
            p.setMessage("loading");
            p.show();

        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String,String> map=new HashMap<String, String>();
            map.put("user",id);

            JSONObject o=json.makeHttpRequest("https://salhapfe.000webhostapp.com/allMessageReçu.php","GET",map);

            try {
                success=o.getInt("success");
                if(success==1)
                {
                    JSONArray array=o.getJSONArray("messagesReçu");
                    for(int i=0;i<array.length();i++) {
                        JSONObject ob = array.getJSONObject(i);
                        map=new HashMap<String, String>();
                        map.put("id",ob.getString("id"));
                        map.put("text",ob.getString("msg"));
                        map.put("date",ob.getString("date"));
                        map.put("contact",ob.getString("tel"));

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
            SimpleAdapter adapter=new SimpleAdapter(ListSMSRecu.this,values,R.layout.item_recu,
                    new String[]{"id","contact","text"},new int[]{R.id.id,R.id.tel,R.id.msg});
            l.setAdapter(adapter);


        }
    }

}
