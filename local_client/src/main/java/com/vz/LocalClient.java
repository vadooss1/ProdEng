package com.vz;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import static com.vz.ConfigLoader.*;

public class LocalClient {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            String request = "";
            do {
                System.out.print("Enter values of root Path, depth, mask in line separated by comma OR 'stop' to exit: ");

                if(scanner.hasNextLine() && (request = scanner.nextLine()) != null && request.length() > 1) {
                    try(Socket socket = new Socket(host, port);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))
                    )
                    {
                        System.out.println(String.format("Connected to the server %s:%d !", host, port));
                        writer.println(request);
                        writer.flush();
                        System.out.println(String.format("Sent request '%s'", request));
                        String response = null;
                        System.out.println(String.format("Get response from the server %s:%d", host, port));
                        while ((response = reader.readLine()) != null) System.out.println(response);
                    }
                }
            }
            while (!request.equalsIgnoreCase(stopCommand));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
