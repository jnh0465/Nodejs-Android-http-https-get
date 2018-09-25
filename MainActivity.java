package com.example.nodejs_androidhttphttps;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {
    private TextView tv;

    private SharedPreferences DataPre;                     //list data 저장할 SharedPreferences 선언
    private SharedPreferences.Editor DataPreEdit;

    @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                tv = (TextView)findViewById(R.id.textView2);
                Button btn = (Button)findViewById(R.id.GET);

                DataPre = getSharedPreferences("str2", 0);               //UUID값 저장 위해 getSharedPreferences 사용
                DataPreEdit = DataPre.edit();                                           //0 == SharedPreferences 읽기, 쓰기 가능

                btn.setOnClickListener(new View.OnClickListener() {                     //버튼 클릭시 통신
                    @Override
                    public void onClick(View view) {
                        new JSONTask().execute("http://YOURSERVER:3000/payment_toapp"); //######
                    }
        });
    }

    void doJSONParser(String str){  //JSON 파싱해서 textview에 보여주기
        StringBuffer sb = new StringBuffer();

        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String paymentNum = jObject.getString("paymentNum");
                String totPrice = jObject.getString("totPrice");
                String date = jObject.getString("date");
                String menu_list = jObject.getString("menu_list");
                String obj_length = jObject.getString("obj_length");
                String obj_orderInfo_length = jObject.getString("obj_orderInfo_length");

                sb.append(
                        "paymentNum:" + paymentNum + "\ntotPrice:" + totPrice + "\ndate:" + date +  "\nmenu_list:"+menu_list+ "\nobj_length:"+obj_length+"\nobj_orderInfo_length:"+obj_orderInfo_length
                );
            }
            tv.setText(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class JSONTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... urls) {
            try {
                JSONObject jsonObject = new JSONObject();

                HttpURLConnection con = null;
                BufferedReader reader = null;

                try{
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {  //https과정
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });

                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, new X509TrustManager[]{ new X509TrustManager(){
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }}, new SecureRandom());

                    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
                    URL url = new URL("http://YOURSERVER:3000/payment_toapp"); //https://~~~~~~~/payment_toapp

                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "text/html");
                    con.setDoInput(true);
                    con.connect();

                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    //Log.d("A", "1111111"+buffer);  //node 서버에서 들어오는지 확인

                    String line = "";
                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    doJSONParser(buffer.toString());  //Json으로 파싱

                    /*
                    if(line==null){                                                             //서버가 끊겼을때(즉, 렉스에서 결제넘어가면서 쿼리가 진행되지 않았을 때는)는 저장한 데이터사용
                        doJSONParser(DataPre.getString("str2", "0"));
                        Log.d("A", "2222222"+DataPre.getString("str2", "0"));
                    }else{                                                                      //업데이트 될 경우(쿼리 진행시) 데이터 저장
                        DataPreEdit.putString("str2", buffer.toString());     //str값
                        DataPreEdit.commit();                            //저장
                        doJSONParser(buffer.toString());  //Json으로 파싱
                        Log.d("A", "3333333"+DataPre.getString("str2", "0"));
                    }
                    */

                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if(reader != null){
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
