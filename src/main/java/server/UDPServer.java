package server;

import java.io.IOException;
import java.net.*;

public class UDPServer implements Runnable {

    private int port;
	private Thread thread;

    public UDPServer(int port) {
        this.port = port;
    }

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(port)) {
            Logger.logln("udp", "UDP Server started on port " + port);

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String message = new String(packet.getData(), 0, packet.getLength());
                Logger.logln("udp", "UDP Received: " + message);

                String response = "Echo from UDP Server: " + message;
                DatagramPacket responsePacket = new DatagramPacket(
                        response.getBytes(),
                        response.length(),
                        packet.getAddress(),
                        packet.getPort()
                );

                socket.send(responsePacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}