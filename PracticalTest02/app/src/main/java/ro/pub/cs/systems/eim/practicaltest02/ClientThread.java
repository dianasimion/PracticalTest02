package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientThread extends Thread {

    private final String address;
    private final int port;
    private final String hour;
    private final String minute;

    private final String ip;

    private final String operationType;

    private final TextView information;

    private Socket socket;

    public ClientThread(String address, int port, String hour, String minute, String ip, String operationType, TextView information) {
        this.address = address;
        this.port = port;
        this.hour = hour;
        this.minute = minute;
        this.ip = ip;
        this.operationType = operationType;
        this.information = information;
    }

    @Override
    public void run() {
        try {
            // tries to establish a socket connection to the server
            socket = new Socket(address, port);

            // gets the reader and writer for the socket
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            // sends the info to the server
            printWriter.println(hour);
            printWriter.flush();
            printWriter.println(minute);
            printWriter.flush();
            printWriter.println(ip);
            printWriter.flush();
            printWriter.println(operationType);
            printWriter.flush();
            String info;

            // reads the weather information from the server
            while ((info = bufferedReader.readLine()) != null) {
                final String finalizedInfo = info;

                // updates the UI with the weather information. This is done using postt() method to ensure it is executed on UI thread
                information.post(() -> information.setText(finalizedInfo));
            }
        } // if an exception occurs, it is logged
        catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    // closes the socket regardless of errors or not
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}
