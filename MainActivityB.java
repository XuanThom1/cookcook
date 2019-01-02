package com.example.parsaniahardik.json_parse_listview;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivityB extends AppCompatActivity {

    private static String ten;
    private TextView tvName;
    private String idTenMon="";
    private String jsonURL = "https://cook-cook.herokuapp.com/api/4tfood/onerecipe?id=";
    private final int jsoncode = 1;
    ArrayList<String> duLieu;
    private TextView tenMon, mota, danhmuc, timen, timecb, giatien, thichhop;
    private Button btn_NL, btn_CT;
    private ImageView IMG;
    private String tenmon, danhMuc,thichHop, giaTien, timeCB, timeN, img, moTa ;
    public static final String TITLE = "title";
    public static final String BUNDLE = "bundel";




    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_b);

        tvName = (TextView) findViewById(R.id.tenmon);


        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(MainActivity.BUNDLE);
            idTenMon = (String) bundle.get(MainActivity.TITLE);

        }
        jsonURL = jsonURL+idTenMon;
        fetchJSON();
        button();





    }
    @SuppressLint("StaticFieldLeak")
    private void fetchJSON(){

        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        new AsyncTask<Void, Void, String>(){
            protected String doInBackground(Void[] params) {
                String response="";
                try {
                    URL url = new URL(jsonURL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    InputStream bufferedReader = new BufferedInputStream(httpURLConnection.getInputStream());


                    response = convertStreamToString(bufferedReader);



                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }
            protected void onPostExecute(String result) {
                //do something with response
                Log.d("newwwss",result);
                onTaskCompleted(result,jsoncode);
            }
        }.execute();
    }

    public void onTaskCompleted(String response, int serviceCode) {
        Log.d("responsejson", response.toString());
        switch (serviceCode) {
            case jsoncode:

                if (isSuccess(response)) {
                    removeSimpleProgressDialog();
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        JSONObject dataObj = jsonArray.getJSONObject(0);
                        tenmon = dataObj.getString("name");
                        danhMuc = dataObj.getString("cname");
                        thichHop = dataObj.getString("suitable_for");
                        giaTien = dataObj.getString("cost")+"VND";
                        timeCB = dataObj.getString("prep_time");
                        timeN = dataObj.getString("cook_time");
                        img = dataObj.getString("link_img");
                        moTa = dataObj.getString("description");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }else {
                    Toast.makeText(MainActivityB.this, getErrorCode(response), Toast.LENGTH_SHORT).show();
                }
        }
        textView();
        tenMon.setText(tenmon);
        danhmuc.setText(danhMuc);
        thichhop.setText(thichHop);
        giatien.setText(giaTien);
        timecb.setText(timeCB);
        timen.setText(timeN);
        mota.setText(moTa);
        Picasso.get().load(img).into(IMG);
    }



    public boolean isSuccess(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                return true;
            } else {

                return false;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public String getErrorCode(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "No data";
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private void textView(){
        tenMon = (TextView) findViewById(R.id.tenmon);
        danhmuc = (TextView) findViewById(R.id.category);
        thichhop = (TextView) findViewById(R.id.thichhop);
        giatien = (TextView) findViewById(R.id.vnd);
        timecb = (TextView) findViewById(R.id.preptime);
        timen = (TextView) findViewById(R.id.cooktime);
        mota =(TextView) findViewById(R.id.description);
        IMG = (ImageView) findViewById(R.id.imageView);

    }

    private void button(){
        btn_NL = (Button) findViewById(R.id.button3);
        btn_CT = (Button) findViewById(R.id.button4);

        btn_NL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityB.this, MainActivityNL.class);
                Bundle bundle = new Bundle();
                bundle.putString(TITLE,idTenMon);
                intent.putExtra(BUNDLE, bundle);
                MainActivityB.this.startActivity(intent);
                MainActivityB.this.finish();
            }
        });

        btn_CT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityB.this, MainActivityNL.class);
                Bundle bundle = new Bundle();
                bundle.putString(TITLE,idTenMon);
                intent.putExtra(BUNDLE, bundle);
                MainActivityB.this.startActivity(intent);
                MainActivityB.this.finish();
            }
        });
    }

    public void ChuyenID(){




    }

}
