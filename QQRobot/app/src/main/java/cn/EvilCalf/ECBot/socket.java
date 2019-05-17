package cn.EvilCalf.ECBot;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class socket
{
    protected socket()
    {}
    public static void connectServerWithUDPSocket(String str) {

        DatagramSocket socket;
        try {
            socket = new DatagramSocket(1985);
            InetAddress serverAddress = InetAddress.getByName("120.78.197.104");
            byte data[] = str.getBytes();
            DatagramPacket packet = new DatagramPacket(data, data.length ,serverAddress ,10025);
            socket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String ReceiveServerSocketData() {
        DatagramSocket socket;
        String result = null;
        try {
            socket = new DatagramSocket(1985);
            byte data[] = new byte[4 * 1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            socket.receive(packet);
            result = new String(packet.getData(), packet.getOffset(),
                    packet.getLength());
            socket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
