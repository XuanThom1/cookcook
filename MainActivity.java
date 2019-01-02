package com.example.parsaniahardik.json_parse_listview;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String jsonURL = "https://cook-cook.herokuapp.com/api/4tfood/recipes?filter=%7B%22name%22:%22%22,%22cid%22:%22%22%7D";
    private final int jsoncode = 1;
    private ListView listView;
    ArrayList<TennisModel> tennisModelArrayList;
    private TennisAdapter tennisAdapter;
    private TennisModel playersModel;
    private SearchView searchView;
    private CharSequence charSequence;
    private EditText edSearch;
    private Button btnSearch;
    private String search="";
//    private TextView tvname ;
//    Dialog loginDialog;
    public static final String TITLE = "title";
    public static final String BUNDLE = "bundel";
    private String name[];
    private String id[];

    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.lv);

        fetchJSON();
        chuyenTrang();
        Search();

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
                    removeSimpleProgressDialog();  //will remove progress dialog
                    tennisModelArrayList = getInfo(response);
                    tennisAdapter = new TennisAdapter(this,tennisModelArrayList);
                    listView.setAdapter(tennisAdapter);

                }else {
                    Toast.makeText(MainActivity.this, getErrorCode(response), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public ArrayList<TennisModel> getInfo(String response) {
        ArrayList<TennisModel> tennisModelArrayList = new ArrayList<>();
        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.getString("success").equals("true")) {

                JSONArray dataArray = new JSONArray(response);

                for (int i = 0; i < dataArray.length(); i++) {

                    playersModel = new TennisModel();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    playersModel.setName(dataobj.getString("name"));
                    playersModel.setCountry(dataobj.getString("cname"));
                    playersModel.setCity(dataobj.getString("cost"));
                    playersModel.setImgURL(dataobj.getString("link_img"));
                    playersModel.setId(dataobj.getString("id"));
                    tennisModelArrayList.add(playersModel);

                }
            //}

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tennisModelArrayList;
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

    public void chuyenTrang(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView < ? > adapter, View view,int position, long arg){
                final TextView id = (TextView) view.findViewById(R.id.id_monAn);
                final TextView v = (TextView) view.findViewById(R.id.name);

                        Intent intent = new Intent(MainActivity.this, MainActivityB.class);
                        String idTenMon = id.getText().toString();

                                Bundle bundle = new Bundle();
                                bundle.putString(TITLE,idTenMon);
                                intent.putExtra(BUNDLE, bundle);
                        MainActivity.this.startActivity(intent);
                }
            }
        );
    }

    public void Search() {
       edSearch = (EditText) findViewById(R.id.edName);
       btnSearch = (Button) findViewById(R.id.bt_search);

       btnSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               search = edSearch.getText().toString();
               jsonURL = jsonURL+search;
               tennisModelArrayList.clear();
               Intent intent = new Intent(MainActivity.this,MainActivity.class);

               MainActivity.this.startActivity(intent);
           }

       });
       jsonURL = "https://cook-cook.herokuapp.com/api/4tfood/recipes?filter=%7B%22name%22:%22%22,%22cid%22:%22%22%7D";



    }
}
