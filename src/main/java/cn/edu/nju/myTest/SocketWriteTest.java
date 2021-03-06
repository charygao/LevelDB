package cn.edu.nju.myTest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by HCW on 2017/10/26.
 */
public class SocketWriteTest {
    public static CloseableHttpClient httpclient = null;

//    public static void main(String[] args) {
//        String[] url = {"http://192.168.1.211:8500/process","http://192.168.1.110:8500/process"};
//
//        for (int i = 0; i < 31245; i++) {
//            Map<String,Object> map = new HashMap<String,Object>();
//            Map<String, String> value = new HashMap<>();
//            value.put("col" + i, "value" + i);
//            map.put("key", i + "");
//            map.put("value", value);
//            sendPost(url[i%2], "{'key':'" + i + "', 'value':{'col" + i + "':'val" + i + "'}}");
//        }
//    }

    public static void main(String[] args) {
        String[] url = {"http://192.168.1.211:8500/batchProcess","http://192.168.1.110:8500/batchProcess"};

        for (int i = 0; i < 1006000; i++) {
            StringBuilder content = new StringBuilder();
            content.append("{");
            for (int j = 0;j < 10000; j++, i++) {
                content.append("'" + i + "':{'col" + i + "':'val" + i + "'}");
                if (j != 9999) {
                    content.append(",");
                }
            }
            content.append("}");
            sendPost(url[0], content.toString());
        }
    }

    //post请求方法
    public static String sendPost(String url, String data) {
        String response = null;
        try {
            httpclient = HttpClients.createDefault();
            CloseableHttpResponse httpresponse = null;
            try {
                HttpPost httppost = new HttpPost(url);
                StringEntity stringentity = new StringEntity(data, ContentType.create("text/json", "UTF-8"));
                httppost.setEntity(stringentity);
                httpresponse = httpclient.execute(httppost);
//                response = EntityUtils.toString(httpresponse.getEntity());
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
