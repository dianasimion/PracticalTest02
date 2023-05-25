package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Timer;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.util.EntityUtils;


public class CommunicationThread extends Thread {

    private final ServerThread serverThread;
    private final Socket socket;

    // Constructor of the thread, which takes a ServerThread and a Socket as parameters
    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    // run() method: The run method is the entry point for the thread when it starts executing.
    // It's responsible for reading data from the client, interacting with the server,
    // and sending a response back to the client.
    @Override
    public void run() {
        // It first checks whether the socket is null, and if so, it logs an error and returns.
        if (socket == null) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] Socket is null!");
            return;
        }
        try {
            // Create BufferedReader and PrintWriter instances for reading from and writing to the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (minute hour ip operation!");

            // Read the values sent by the client
            String hour = bufferedReader.readLine();
            String minute = bufferedReader.readLine();
            String ip = bufferedReader.readLine();
            String operationType = bufferedReader.readLine();

            if (hour == null || hour.isEmpty() || minute == null || minute.isEmpty() || ip == null || ip.isEmpty() || operationType == null || operationType.isEmpty()) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] Error receiving parameters from client (hour / minute / ip / operation type!");
                return;
            }

            // It checks whether the serverThread has already received the weather forecast information for the given ip.
            HashMap<String, TimerInformation> data = serverThread.getData();
            TimerInformation info;
            TimerInformation newInfo;
            // exista deja in map ip -ul
            if (data.containsKey(ip)) {
                newInfo = new TimerInformation(hour, minute, ip);

                if (operationType.equals("1")) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] The info will be overriden!");
//                info = data.get(ip);

//                    newInfo = new TimerInformation(hour, minute, ip);
                    data.put(ip, newInfo);
                    info = newInfo;

                    serverThread.setData(ip, newInfo);
                }

                info = newInfo;


                if (operationType.equals("2")) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] The info will be removed!");

                    serverThread.removeData(ip);
                }

            } else {
                Log.i(Constants.TAG, "[COMMUNICATION THREAD] The will be added in the database ");
                 // her was the http request

//                 Create a timer object
                info = new TimerInformation(hour, minute, ip);

                // Cache the information for the given ip
                serverThread.setData(ip, info);
            }

            if (info == null) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] info timer is null!");
                return;
            }

            // Send the information back to the client
            String result = null;
            switch (operationType) {
                case "1":
                    result = info.toString();
                    break;
                case "2":
                    result ="removed this entry";
                    break;
                case "3":
//                    result = info.toString();
                    break;
                default:
                    result = "[COMMUNICATION THREAD] Wrong information type!";
            }

            // to do
            // Send the result back to the client
            printWriter.println(result);
            printWriter.flush();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }

}
