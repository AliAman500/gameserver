package server;

public class Main {
    public static void main(String[] args) {
        int tcpPort = 1234;
        int udpPort = 4321;

        new TCPServer(tcpPort).start();
        new UDPServer(udpPort).start();
    }
}