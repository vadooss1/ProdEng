package com.vz;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import static com.vz.ConfigLoader.*;

public class LocalServer {

    public static volatile boolean stopped = false;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println(String.format("Server started! localhost:%d", port));
            while (!stopped)
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> {
                        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                            String lineRequest = reader.readLine().trim();
                            if (lineRequest != null && lineRequest.equalsIgnoreCase(stopCommand)) stopped = true;

                            String[] request = lineRequest != null && !lineRequest.isEmpty() ? lineRequest.split("[,\\s]+") : new String[]{"0","0","0"};
                            DirTreeKeeper dirTreeKeeper = new DirTreeKeeper(request[0]);

                            System.out.println(String.format("Get request:%s", Arrays.toString(request)));

                            Thread scan = new Thread(() -> dirTreeKeeper.scanningDirTreeAllElements());
                            Thread response = new Thread(() -> dirTreeKeeper.gettingDirElementsBy(
                                    request.length > 1 && request[1] != null ? Integer.parseInt(request[1]) : 0,
                                    request.length > 2 ? request[2] : "",
                                    writer));
                            scan.start();
                            response.start();
                            scan.join();
                            response.join();

                            System.out.println(String.format("Sent response for request:%s", Arrays.toString(request)));
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            System.out.println("Server stopped !");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
