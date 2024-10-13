package server;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class TCPServer implements Runnable {

    private int port;
    private Thread thread;
    private ExecutorService threadPool;

    public TCPServer(int port) {
        this.port = port;
        this.threadPool = Executors.newCachedThreadPool();
    }

    public void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Logger.logln("tcp", "Server started on port " + port);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
					Logger.logln("tcp", "A new client connected: " + clientSocket.getInetAddress().getHostAddress()+":"+clientSocket.getPort());
                    threadPool.submit(new ClientHandler(clientSocket));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    private static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String message = in.readLine();
                Logger.logln("tcp", "TCP Received: " + message);
                out.println("Echo from TCP Server: " + message);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}