package cs240.familymapclient.mainactivity;

import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import request.LoginRequest;
import request.RegisterRequest;
import result.AllEventsResult;
import result.AllPersonsResult;
import result.LoginResult;
import result.RegisterResult;

public class ServerProxy {

    private String serverHost;
    private String serverPort;

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public LoginResult login(LoginRequest request) {
        try {
            //start http connection
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            //send data
            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            //get data
            InputStream respBody;
            String respData;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User successfully logged in");
                respBody = http.getInputStream();
                respData = readString(respBody);
                return gson.fromJson(respData, LoginResult.class);

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                respBody = http.getErrorStream();
                respData = readString(respBody);
                return gson.fromJson(respData, LoginResult.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RegisterResult register(RegisterRequest request) {
        try {
            //start http connection
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.connect();

            //send data
            Gson gson = new Gson();
            String reqData = gson.toJson(request);
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            //get data
            InputStream respBody;
            String respData;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("User successfully registered");
                respBody = http.getInputStream();
                respData = readString(respBody);
                return gson.fromJson(respData, RegisterResult.class);

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                respBody = http.getErrorStream();
                respData = readString(respBody);
                return gson.fromJson(respData, RegisterResult.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AllPersonsResult getPersons(String authToken) {
        try {
            //start http connection
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.connect();

            //get Data
            Gson gson = new Gson();
            InputStream respBody;
            String respData;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("All people successfully retrieved");
                respBody = http.getInputStream();
                respData = readString(respBody);
                return gson.fromJson(respData, AllPersonsResult.class);

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                respBody = http.getErrorStream();
                respData = readString(respBody);
                return gson.fromJson(respData, AllPersonsResult.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AllEventsResult getEvents(String authToken) {
        try {
            //start http connection
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.connect();

            //get data
            Gson gson = new Gson();
            InputStream respBody;
            String respData;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("All events successfully retrieved");
                respBody = http.getInputStream();
                respData = readString(respBody);
                return gson.fromJson(respData, AllEventsResult.class);

            }
            else {
                System.out.println("ERROR: " + http.getResponseMessage());

                respBody = http.getErrorStream();
                respData = readString(respBody);
                return gson.fromJson(respData, AllEventsResult.class);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //For testing purposes only
    public void clear() {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/clear");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(false);
            http.connect();

                if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    System.out.println("Database cleared");

                }
                else {
                    System.out.println("ERROR: " + http.getResponseMessage());
                }
            }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

}
