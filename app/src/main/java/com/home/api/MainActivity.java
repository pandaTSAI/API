package com.home.api;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
//    private ArrayList<Api> apis;
    private String startTime1;
    private String endTime1;
    private String parameterName;
    private String parameterUnit;
//    private JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OKHttpdata();
    }

    private void OKHttpdata() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=CWB-35227179-8C6E-40CC-9E0A-ADC7DE41DF64&locationName=%E8%87%BA%E5%8C%97%E5%B8%82&elementName=MinT")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Log.d(TAG, "onResponse: " + json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseJson(json);
                    }
                });
            }
        });
    }

    private void parseJson(String json) {
        ArrayList<Api> apis = new ArrayList<>();
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray jsonArray = jsonObject.getJSONObject("records").getJSONArray("location");
        Log.d(TAG, "getData: data: " + jsonArray);

        Iterator<JSONArray> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject jsonlocation = JSONObject.fromObject(iterator.next());
            jsonlocation.getString("locationName");
            String locationName = jsonlocation.getString("locationName") + "\n";
            Log.d(TAG, "getData: location: " + jsonlocation);
            Log.d(TAG, "getData: location: " + locationName);


            JSONArray jsonArray1 = jsonlocation.getJSONArray("weatherElement");
            Log.d(TAG, "getData: jsonArray1: " + jsonArray1);
            Iterator<JSONArray> iterator1 = jsonArray1.iterator();
            while (iterator1.hasNext()) {
                JSONObject jsonElement = JSONObject.fromObject(iterator1.next());
                jsonElement.getString("elementName");
//                Log.d(TAG, "getData: weatherElement: " + jsonElement);

                JSONArray jsonArray2 = jsonElement.getJSONArray("time");
//                Log.d(TAG, "getData: jsonArray2: " + jsonArray2);
                Iterator<JSONArray> iterator2 = jsonArray2.iterator();
                while (iterator2.hasNext()) {
                    JSONObject jsontime = JSONObject.fromObject(iterator2.next());
//                    Log.d(TAG, "getData: time: " + jsontime);
                    for (int i = 0; i < jsontime.size(); i++) {
//                        Log.d(TAG, "parseJson: time: " + jsontime);

                        startTime1 = jsontime.getString("startTime");
//                        Log.d(TAG, "startTime1 + endTime1 : " +startTime1);
                        endTime1 = jsontime.getString("endTime");
//                        Log.d(TAG, "startTime1 + endTime1 : " +endTime1);

                    }

                    JSONObject jsonparameter = jsontime.getJSONObject("parameter");
//                    Log.d(TAG, "parseJson: parameter: " + jsonparameter);
                    for (int i = 0; i < jsonparameter.size(); i++) {
                        parameterName = jsonparameter.getString("parameterName");
//                        Log.d(TAG, "parseJson: parameterName " + parameterName);
                        parameterUnit = jsonparameter.getString("parameterUnit");
//                        Log.d(TAG, "parseJson: parameterUnit " + parameterUnit);
                    }
                    Api api = new Api(startTime1,endTime1,parameterName,parameterUnit);
                    apis.add(api);
                }
            }
        }
        ApiAdapter adapter = new ApiAdapter(apis);
        recyclerView.setAdapter(adapter);

    }

    public class ApiAdapter extends RecyclerView.Adapter<ApiAdapter.ApiHolder> {
        ArrayList<Api> apis;
        public ApiAdapter(ArrayList<Api> apis){
            this.apis = apis;
        }
        @NonNull
        @Override
        public ApiHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            View view = getLayoutInflater().inflate(R.layout.item, parent, false);
            return new ApiHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ApiHolder apiHolder, int position) {
            Api api = apis.get(position);
            apiHolder.startTime.setText(api.getStartTime());
            apiHolder.endTime.setText(api.getEndTime());
            apiHolder.parameter.setText(api.getParameterName() + api.getParameterUnit());
        }

        @Override
        public int getItemCount() {
            Log.d(TAG, "getItemCount: " +apis.size());
            return apis.size();
        }

        class ApiHolder extends RecyclerView.ViewHolder {
            TextView startTime;
            TextView endTime;
            TextView parameter;

            public ApiHolder(@NonNull View itemView) {
                super(itemView);
                startTime = itemView.findViewById(R.id.item_startTime);
                endTime = itemView.findViewById(R.id.item_endTime);
                parameter = itemView.findViewById(R.id.item_parameter);
            }
        }
    }

    public class TransTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL(strings[0]);
                InputStream is = url.openStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                //readLine 讀到跳行字元
                String line = in.readLine();
                while (line != null) {
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: " + s);
        }
    }

}

