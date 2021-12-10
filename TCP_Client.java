import java.net.*;
import java.io.*;

public class TCP_Client {

    Socket socket;
    DataInputStream input;
    DataOutputStream out;

    public TCP_Client() throws Exception {
        try {
            //initialize ip address and port number for server
            socket = new Socket("192.168.8.156", 50000);
            // loop to redisplay connection state massage to change the mode
            while (true) {
                System.out.println("Connection is established , Type the option you chose of the mode : open , secure , close ");
                //create object to raed from client and put in the stream
                input = new DataInputStream(System.in);
                String option = input.readLine();
                //create object to write in socket
                out = new DataOutputStream(socket.getOutputStream());
                // method to enable client to send msg throgh socket
                writingIntoSocket(option);
                // if client chose close mode will break loop
                if (option.equalsIgnoreCase("close")) {
                    closingConnection();
                    break;
                }
            }
            // it if client run before server print exception and server is down 
        } catch (Exception i) {
            System.err.println("Server is Down " + i);
        }
    }
    // method to enable client to send msg throgh socket
    public void writingIntoSocket(String line) throws Exception {
        // send the mode to the server 
        out.writeUTF(line);
        line = line.trim().toLowerCase();
        // switch the mode that client chose
        switch (line) {
            case "open": {
                System.out.print("Write your message : ");
                //read msg from screen 
                line = input.readLine();
                //send the msg to server
                out.writeUTF(line);
                System.out.println("Your message sent successfully to the server. ");
                // method to read server response
                readingFromSocket("open",0);
                break;
            }
            case "secure": {
                System.out.print("Write your message : ");
                //read msg from screen 
                line = input.readLine();
                // initialize key for encrept msg 
                int key = 3;
                String encreptMsg = "";
                //loop to shift three letters of msg 
                for (int i = 0; i < line.length(); i++) {
                    encreptMsg += (char) (line.charAt(i) + key);
                }
                //send the msg encrepted to server
                out.writeUTF(encreptMsg);
                //send key to server 
                out.writeByte(key);
                System.out.println("Your message sent successfully to the server. ");
                // method to read server response
                readingFromSocket("secure",key);
                break;
            }
            case "close":{
                // method to read server response
                readingFromSocket("close" , 0);
                break;}
            default: {
                System.out.println("* You Have Not Enter Connection State or you enter wrong option , RETRY *");
            }
        }
    }
    // method to read server response
    public void readingFromSocket(String line, int key) throws Exception {
        // read the msg from server 
        input = new DataInputStream(socket.getInputStream());
        String fromServer = input.readLine();
        // switch the mode that client passed from parameter 
        switch (line) {
            case "open": {
                // read from server what recived from client
                System.out.println("You recived from Server : " + fromServer);
                System.out.println();
            }
            break;
            case "secure": {
                // read from server encrepted msg what recived from client
                System.out.println("Client Massage that recived from Server before encoding : " + fromServer);
                String decreptMsg = "";
                // loop to decrept Msg
                for (int i = 0; i < fromServer.length(); i++) {
                    decreptMsg += (char) (fromServer.charAt(i) - key);
                }
                // after encoding the msg from server 
                System.out.println("Client Massage that recived from Server after encoding : " + decreptMsg);
                System.out.println();
            }
            break;
            case "close": {
                System.out.println("* Server is closing the connection *");
                break;
            }
        }
    }
// method to close all object that established
    public void closingConnection() throws Exception {
        System.out.println("* Client is closing the connection *");
        input.close();
        out.close();
        socket.close();
    }
    public static void main(String[] args) throws Exception {
        // initialize object client
        TCP_Client Client = new TCP_Client();
    }
}
