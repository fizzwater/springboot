package com.qin.springboot.a009mq;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class BRESTAMQPPingCheck {
    private static final String USERNAME="rabbitadmin";
    private static final String PASSWORD="123456";
    //%2F代表"/"(默认的vhost)
    private static final String URL="http://192.168.103.93:15672/api/aliveness-test/%2F";
    public static void main(String[] args) throws UnsupportedEncodingException {
        HttpResponse response = null;
        int statusCode = 0;
        try {
            DefaultHttpClient Client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);
            String up = USERNAME+":"+PASSWORD;
            //设置凭证
            String credentials = new BASE64Encoder().encode(up.getBytes("UTF-8"));
            httpGet.setHeader("Authorization","Basic "+credentials);
            response = Client.execute(httpGet);
            //读取响应内容
            BufferedReader breader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder responseString = new StringBuilder();
            String line = null;
            while ((line = breader.readLine()) !=null) {
                responseString.append(line);
            }
            breader.close();
            String repsonseStr =responseString.toString();
            statusCode = response.getStatusLine().getStatusCode();
            System.out.println("statusCode="+statusCode+" repsonseStr =" + repsonseStr);
        } catch (Exception e) {
            System.out.println("Could not connect to "+URL);

            e.printStackTrace();
        }
        //响应码大于299要么代表错误，要么就是发送给客户端额外的指令
        if(statusCode>299){
            System.out.println("Critical:Broker not alive:"+statusCode);

        }
        System.out.println("OK! Connect to "+URL+" successful ");

    }
}
