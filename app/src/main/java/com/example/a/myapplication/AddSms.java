package com.example.a.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;

public class AddSms extends AppCompatActivity {
Button envoyer;
EditText  message;
String id,tel,idContact,numero;
ArrayList<String> all= new ArrayList<String>();
    ArrayList<String> allContact= new ArrayList<String>();
ArrayList<HashMap<String,String>>values=new ArrayList<HashMap<String,String>>();
    HashMap<String,String> map;
    MultiAutoCompleteTextView phone;
    JSONParser json =new JSONParser();
    ProgressDialog p,p2 ;
    int success;
    Button b;int getSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sms);
        phone=findViewById(R.id.phone);
        message=findViewById(R.id.msg);
        b=findViewById(R.id.send);
        Bundle x=getIntent().getExtras();
        if(x!=null)
        {
            id=x.getString("key");
            Toast.makeText(AddSms.this,id,Toast.LENGTH_LONG).show();
           new getLogin().execute();
        }
        phone.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String,String>map=(HashMap<String, String>)phone.getAdapter().getItem(position);
idContact=map.get("id");
tel=map.get("num");
all.add(tel);
allContact.add(idContact);
String nums=all.toString().replace("[", "");
String fin=nums.replace("]","")+",";

                phone.setText(fin);
                phone.setSelection(fin.length());
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new add().execute();
                if(ContextCompat.checkSelfPermission(AddSms.this, Manifest.permission.SEND_SMS)!=  PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(AddSms.this,new String[]{Manifest.permission.SEND_SMS},102);
                }
                else
                {
                SmsManager smsManager = SmsManager.getDefault();
                for (int a1 = 0; a1 < all.size(); a1++)
                {

             //smsManager.sendTextMessage(all.get(a1).toString(), null, message.getText().toString(),null, null);
                }}
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    class getLogin extends AsyncTask<String,String,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p=new ProgressDialog(AddSms.this);
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
            SimpleAdapter adapter=new SimpleAdapter(AddSms.this,values,R.layout.item_contact,
                    new String[]{"id","nom","num"},new int[]{R.id.id,R.id.nom,R.id.num});
            phone.setAdapter(adapter);
            phone.setThreshold(3);
            phone.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            }
        }


        class add extends AsyncTask<String,String,String>
        {
            @Override
            protected void onPreExecute() {
                p2=new ProgressDialog(AddSms.this);
                p2.setTitle("loading");
                p2.show();

                super.onPreExecute();
            }



            @Override
            protected String doInBackground(String... strings) {
                for(int i=0;i<all.size();i++) {
                    HashMap<String, String> mp = new HashMap<String, String>();
                    mp.put("user", id);
                    mp.put("numero",all.get(i).toString());
                    mp.put("contact",allContact.get(i).toString());
                    mp.put("text",message.getText().toString());
                    JSONObject ot = json.makeHttpRequest("https://salhapfe.000webhostapp.com/addSms.php","GET",mp);
                    try {
                         getSuccess= ot.getInt("success");




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return null;

            }
            @Override
            protected void onPostExecute(String s) {
                p2.dismiss();
                super.onPostExecute(s);
                if(getSuccess==1)
                {
                    AlertDialog.Builder alertDialog=new AlertDialog.Builder(AddSms.this);
                    alertDialog.setTitle("Alerte");
                    alertDialog.setMessage("sms ajouter avec succès");
                    alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(AddSms.this,ListSMSEnvoye.class);
                            intent.putExtra("id",id);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();



                }

            }
        }
    }

