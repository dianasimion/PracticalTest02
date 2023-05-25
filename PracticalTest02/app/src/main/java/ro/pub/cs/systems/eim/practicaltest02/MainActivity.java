package ro.pub.cs.systems.eim.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText hourEditText = null;
    private EditText minuteEditText = null;

    private EditText ipEditText = null;


    private TextView information = null;

    private ServerThread serverThread = null;

    private final ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            // Retrieves the server port. Checks if it is empty or not
            // Creates a new server thread with the port and starts it
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }
    }

    private final SetTimerButtonClickListener setTimerButtonClickListener = new SetTimerButtonClickListener();

    private class SetTimerButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            // Retrieves the client address and port. Checks if they are empty or not
            //  Checks if the server thread is alive. Then creates a new client thread with the data
            //  and starts it
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress.isEmpty() || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String hour = hourEditText.getText().toString();
            String minute = minuteEditText.getText().toString();
            String ip = ipEditText.getText().toString();

            if (hour.isEmpty() || minute.isEmpty() || ip.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (hour / minute type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            information.setText(Constants.EMPTY_STRING);

            String operationType = "1";

            ClientThread clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), hour, minute, ip, operationType, information);

            clientThread.start();
        }
    }

    private final ResetTimerButtonClickListener resetTimerButtonClickListener = new ResetTimerButtonClickListener();

    private class ResetTimerButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            // Retrieves the client address and port. Checks if they are empty or not
            //  Checks if the server thread is alive. Then creates a new client thread with the data
            //  and starts it
            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress.isEmpty() || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            String hour = hourEditText.getText().toString();
            String minute = minuteEditText.getText().toString();
            String ip = ipEditText.getText().toString();

            if (hour.isEmpty() || minute.isEmpty() || ip.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (hour / minute type) should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            information.setText(Constants.EMPTY_STRING);

            String operationType = "2";

            ClientThread clientThread = new ClientThread(clientAddress, Integer.parseInt(clientPort), hour, minute, ip, operationType, information);

            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_main);

        serverPortEditText = findViewById(R.id.server_port_edit_text);
        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = findViewById(R.id.client_address_edit_text);
        clientPortEditText = findViewById(R.id.client_port_edit_text);

        hourEditText = findViewById(R.id.hour_edit_text);
        minuteEditText = findViewById(R.id.minute_edit_text);
        ipEditText = findViewById(R.id.ip_edit_text);

        // todo
        Button setTimerButton = findViewById(R.id.set_timer_button);
        setTimerButton.setOnClickListener(setTimerButtonClickListener);
        Button resetTimerButton = findViewById(R.id.reset_timer_button);
        resetTimerButton.setOnClickListener(resetTimerButtonClickListener);
        Button pollButton = findViewById(R.id.poll_button);
//        pollButton.setOnClickListener(pollButtonClickListener);

        information = findViewById(R.id.information_text_view);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}