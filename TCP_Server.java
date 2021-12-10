import java.net.*;
import java.io.*;

public class TCP_Server {

    private Socket socket = null;
    private DataInputStream in = null;
    private ServerSocket serverSocket = null;
    private DataOutputStream output = null;
    String line = null;

    public TCP_Server(int port) throws Exception {
        System.out.println("* Waiting for a client *");
        //open the port and waiting the client 
        serverSocket = new ServerSocket(port);
        // Port matches with the client and the IP entered correctly
        socket = serverSocket.accept();
        System.out.println("Client is accepted : " + socket.toString());
        // loop read Connection mode from client
        while (true) {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            line = in.readUTF();
            // methode to reading the msg From Socket
            readingFromSocket();
            // if Connection mode is close closing the Connection
            if (line.equalsIgnoreCase("close")) {
                closingConnection();
                break;
            }
        }
    }
    // methode to reading the msg From Socket
    public void readingFromSocket() throws Exception {
        line = line.trim().toLowerCase();
        try {
            // switch based on what client chose
            switch (line) {
                case "open": {
                    System.out.println("Connection State : Open ");
                    // read msg from client
                    line = in.readUTF();
                    System.out.println("Client Massage is : " + line);
                    System.out.println();
                    // method to return the msg
                    writingIntoSocket(line);
                    break;
                }
                case "secure": {
                    System.out.println("Connection State : Secure");
                    // read msg and key from client
                    line = in.readUTF();
                    int key = in.readByte();
                    // loop to decrept Msg shift three 
                    String decreptMsg = "";
                    for (int i = 0; i < line.length(); i++) {
                        decreptMsg += (char) (line.charAt(i) - key);
                    }
                    System.out.println("Client Massage before encoding : " + line);
                    System.out.println("Client Massage after encoding : " + decreptMsg);
                    System.out.println();
                    //loop to shift  of msg 
                    String encreptMsg = "";
                    for (int i = 0; i < line.length(); i++) {
                        encreptMsg += (char) (decreptMsg.charAt(i) + key);
                    }
                    // method to return the msg
                    writingIntoSocket(encreptMsg);
                    break;
                }

                case "close": {
                    System.out.println("Connection State : close ");
                    // method to return the msg
                    writingIntoSocket(line);
                    break;
                }

                default: {
                    System.out.println("* Client Has Not Enter Connection State , RETRY * ");
                }

            }
        } catch (Exception i) {
            System.out.println(i);
        }

    }
    // method to return the msg
    public void writingIntoSocket(String line) throws Exception {
        //create object to write in socket
        output = new DataOutputStream(socket.getOutputStream());
        String msg = line;
        output.writeBytes(msg + '\n');
    }
    // method to close all object that established
    public void closingConnection() throws Exception {
        System.out.println("* Server is closing the connection *");
        in.close();
        output.close();
        socket.close();

    }

    public static void main(String[] args) throws Exception {
        // initialize object server
        TCP_Server server = new TCP_Server(50000);
    }
}
