# Nodejs-Android-http-https-get
___
##최종목표
-
`원래목표 :     
` 
<br>
- [x] get으로 nodejs 서버에서 데이터 받아오기
- [x] https 연결 구현 **_180922**
 
`애로사항 : `   
~~~
http는 여러 의미서 통신이 간편한데 https로 데이터를 옮기는게 너무 힘들었다ㅠㅠ 
처음보는 SSL에 인증서에.. 결국은 인증서가 필요치 않은 방법을 찾아 적용했지만, 관련자료가 너무 부족해 진행하기가 어려웠다
get으로 서버에서 안드로이드로 보내는 방식은 수월했지만 post로 안드로이드에서 서버로 데이터를 옮기는 과정이 어려웠다...
~~~
___
#180922-23 
-
`nodejs에서 보내는 data`
~~~node.js
app.get('/payment_toapp', (req, res) => {		       	  //// 어플로(어플내에서 저장예정 )
  var data =[
    {'paymentNum':'122f6c3a-574a-4e56-80ca-b4f6ea1b20e05',
    'totPrice':'5800','date':'2018-09-23 15:10',
  'menu_list':'aide', 'obj_length':1, 'obj_orderInfo_length' : 6}
  ];
  console.log(data);
  res.json(data);
});
~~~
`안드로이드에서 json을 파싱`
~~~java
void doJSONParser(String str){
        StringBuffer sb = new StringBuffer();
        try {
            JSONArray jarray = new JSONArray(str);   // JSONArray 생성
            for(int i=0; i < jarray.length(); i++){
                JSONObject jObject = jarray.getJSONObject(i);  // JSONObject 추출
                String paymentNum = jObject.getString("paymentNum");
                String totPrice = jObject.getString("totPrice");
                String date = jObject.getString("date");
                String menu = jObject.getString("menu_list");
                String obj_length = jObject.getString("obj_length");
                String obj_orderInfo_length = jObject.getString("obj_orderInfo_length");

                String[] paymentNum_list = paymentNum.split(",");
                String[] totPrice_list = totPrice.split(",");
                String[] date_list = date.split(",");
                String[] menu_list = menu.split(",");
                String[] obj_length_list = obj_length.split(",");
                String[] obj_orderInfo_length_list = obj_orderInfo_length.split(",");

                sb.append(
                    "paymentNum:" + paymentNum_list[0] + "\ntotPrice:" + totPrice + "\ndate:" + date +  "\nmenu_list:"+menu+ "\nobj_length:"+obj_length+"\nobj_orderInfo_length:"+obj_orderInfo_length
                );
            }
            tv.setText(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
~~~
`https연결을 가능케해주는 코드 중 일부`
~~~java
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
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
                    URL url = new URL("http://yourserver/payment_toapp"); 

                    con = (HttpURLConnection) url.openConnection();
                    con.connect();
~~~
`nodejs서버 -> 안드로이드`      
<img src="https://user-images.githubusercontent.com/38582562/45939510-02920800-c00e-11e8-8949-ff1580d297d4.jpg" width="40%"> 
<img src="https://user-images.githubusercontent.com/38582562/45939511-032a9e80-c00e-11e8-80cf-633a6c0679c6.jpg" width="40%"> 
