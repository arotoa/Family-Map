package cs240.familymapclient.mainactivity.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cs240.familymapclient.mainactivity.DataCache;
import cs240.familymapclient.mainactivity.Tasks.GetDataTask;
import cs240.familymapclient.mainactivity.Tasks.LoginTask;
import cs240.familymapclient.mainactivity.R;
import cs240.familymapclient.mainactivity.Tasks.RegisterTask;
import model.Person;

public class LoginFragment extends Fragment {

    private Listener listener;
    private EditText serverPortField;
    private EditText serverHostField;
    private EditText usernameField;
    private EditText passwordField;
    private EditText firstNameField;
    private EditText lastNameField;
    private EditText emailField;
    private Button loginButton;
    private Button registerButton;
    private View view;

    public interface Listener {
        void notifyDone();
    }

    public void registerListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);

        //initialize views for fields
        serverPortField = view.findViewById(R.id.serverPortField);
        serverHostField = view.findViewById(R.id.serverHostField);
        usernameField = view.findViewById(R.id.usernameField);
        passwordField = view.findViewById(R.id.passwordField);
        firstNameField = view.findViewById(R.id.firstNameField);
        lastNameField = view.findViewById(R.id.lastNameField);
        emailField = view.findViewById(R.id.emailField);

        //automatically fill in port and host field
        serverHostField.setText("192.168.0.177");
        serverPortField.setText("8080");

        //create listeners for all fields and buttons
        serverPortField.addTextChangedListener(mTextWatcher);
        serverHostField.addTextChangedListener(mTextWatcher);
        usernameField.addTextChangedListener(mTextWatcher);
        passwordField.addTextChangedListener(mTextWatcher);
        firstNameField.addTextChangedListener(mTextWatcher);
        lastNameField.addTextChangedListener(mTextWatcher);
        emailField.addTextChangedListener(mTextWatcher);
        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);

        checkForEmptyFields();

        //login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    Handler handler  = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            if (bundle.getBoolean("isSuccess")) {
                                getData(bundle.getString("authtoken"), listener);
                            }
                            else {
                                Toast.makeText(getContext(), (CharSequence)bundle.get("message"),
                                        Toast.LENGTH_LONG).show();
                                System.out.println("Invalid");
                            }
                        }
                    };
                    //log user in
                    LoginTask task = new LoginTask(handler, view);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);
                }
            }
        });

        //register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    Handler handler = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            if (bundle.getBoolean("isSuccess")) {
                                getData(bundle.getString("authtoken"), listener);
                            }
                            else {
                                Toast.makeText(getContext(), (CharSequence)bundle.get("message"),
                                        Toast.LENGTH_LONG).show();
                                System.out.println("Invalid");
                            }
                        }
                    };
                    //register user
                    RegisterTask task = new RegisterTask(handler, view);
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(task);
                }
            }
        });

        return view;
    }


    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkForEmptyFields();
        }
    };

    private void checkForEmptyFields() {
        //check if login fields are empty
        if (serverPortField.getText().toString().equals("")
                || serverHostField.getText().toString().equals("")
                || usernameField.getText().toString().equals("")
                || passwordField.getText().toString().equals("")) {

            registerButton.setEnabled(false);
            loginButton.setEnabled(false);
        }
        else {
            loginButton.setEnabled(true);
            //check if extra register fields are empty
            if (firstNameField.getText().toString().equals("")
                    || lastNameField.getText().toString().equals("")
                    || emailField.getText().toString().equals("")) {

                registerButton.setEnabled(false);
            }
            else {
                registerButton.setEnabled(true);
            }
        }
    }

    private void getData(String authToken, Listener listener) {
        Handler handler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message message) {
                Bundle bundle = message.getData();
                DataCache dataCache = DataCache.getInstance();
                Person user = dataCache.getPerson(dataCache.getPersonID());
                String welcomeText = "Welcome " + user.getFirstName() + " " + user.getLastName();
                Toast.makeText(getContext(), welcomeText, Toast.LENGTH_LONG).show();
                listener.notifyDone();
            }
        };
        //get data for app
        GetDataTask task = new GetDataTask(handler, view, authToken);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(task);
    }


}