package handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;

public class JSONHelper {

    public <T> Object decodeFromJSON(InputStream reqBody, Class<?> requestClass) throws IOException {
        String reqData = readString(reqBody);
        System.out.println(reqData);

        Gson gson = new Gson();
        return (gson.fromJson(reqData, requestClass));
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    public void encodeToJSON (HttpExchange exchange, Object result) throws IOException {
        Gson gson = new Gson();
        Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
        gson.toJson(result, resBody);
        resBody.close();
        exchange.getResponseBody().close();
    }

}
