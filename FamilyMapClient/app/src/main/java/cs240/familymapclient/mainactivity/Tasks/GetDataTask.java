package cs240.familymapclient.mainactivity.Tasks;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;

import cs240.familymapclient.mainactivity.DataCache;
import cs240.familymapclient.mainactivity.R;
import cs240.familymapclient.mainactivity.ServerProxy;
import model.Event;
import model.Person;
import result.RegisterResult;

public class GetDataTask implements Runnable {

    private final Handler messageHandler;
    private final View view;
    private final String authToken;

    public GetDataTask(Handler messageHandler, View view, String authToken) {
        this.messageHandler = messageHandler;
        this.view = view;
        this.authToken = authToken;
    }

    @Override
    public void run() {
        //create server proxy with host and port fields
        EditText editText = view.findViewById(R.id.serverHostField);
        String serverHost = editText.getText().toString();
        editText = view.findViewById(R.id.serverPortField);
        String serverPort = editText.getText().toString();
        ServerProxy proxy = new ServerProxy(serverHost, serverPort);
        //get data and set in data cache
        Event[] events = proxy.getEvents(authToken).getData();
        Person[] people = proxy.getPersons(authToken).getData();
        DataCache dataCache = DataCache.getInstance();
        dataCache.setEvents(events);
        dataCache.setPeople(people);
        sendMessage();
    }

    private void sendMessage() {
            Message message = Message.obtain();
            Bundle messageBundle = new Bundle();
            messageBundle.putString("message", "Successfully added data from the database!");
            message.setData(messageBundle);
            messageHandler.sendMessage(message);
    }
}