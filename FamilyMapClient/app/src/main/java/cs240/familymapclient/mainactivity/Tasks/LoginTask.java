package cs240.familymapclient.mainactivity.Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;


import cs240.familymapclient.mainactivity.DataCache;
import cs240.familymapclient.mainactivity.R;
import cs240.familymapclient.mainactivity.ServerProxy;
import request.LoginRequest;
import result.LoginResult;

public class LoginTask implements Runnable {

    private final Handler messageHandler;
    private final View view;
    private LoginResult result;

    public LoginTask(Handler messageHandler, View view) {
        this.messageHandler = messageHandler;
        this.view = view;
    }

    @Override
    public void run() {
        //create server proxy with host and port fields
        EditText editText = view.findViewById(R.id.serverHostField);
        String serverHost = editText.getText().toString();
        editText = view.findViewById(R.id.serverPortField);
        String serverPort = editText.getText().toString();
        ServerProxy proxy = new ServerProxy(serverHost, serverPort);

        //get rest of data
        editText = view.findViewById(R.id.usernameField);
        String username = editText.getText().toString();
        editText = view.findViewById(R.id.passwordField);
        String password = editText.getText().toString();
        //create login request object and execute it
        LoginRequest request = new LoginRequest(username, password);
        result = proxy.login(request);
        if (result == null) {
            sendMessage(true);
        }
        //set variables that are needed
        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(result.getAuthtoken());
        dataCache.setPersonID(result.getPersonID());
        dataCache.setUserName(result.getUsername());
        sendMessage();
    }



    private void sendMessage() {
        if (result.isSuccess()) {
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean("isSuccess", true);
            messageBundle.putString("authtoken", result.getAuthtoken());
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
        else {
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean("isSuccess", false);
            messageBundle.putString("message", result.getMessage());
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
        }
    }

    //overridden function if server is not connected
    private void sendMessage(Boolean isServerProblem) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean("isSuccess", false);
        messageBundle.putString("message", "Error: Invalid server port or host");
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }


}
