package sg.edu.nus.iss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class App 
{
    public static void main( String[] args ) throws NumberFormatException, UnknownHostException, IOException
    {
        String serverHost = args[0];
        String serverPort = args[1];

        // Establish connection to server in server file - Slide 8
        // *** server must already be started

        Socket socket = new Socket(serverHost, Integer.parseInt(serverPort));

        // Setup console input from keyboard
        // Variable to save keyboard inputs
        // Variable to save msgReceived
        Console con = System.console();
        String keyboardInput = "";
        String msgReceived = "";
        // Similar to server - Slide 9
        try (InputStream is = socket.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            try (OutputStream os = socket.getOutputStream()) {
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                // While loop
                while (!keyboardInput.equals("close")) {
                    keyboardInput = con.readLine("Enter a command plox: ");

                    // Send message across through the communication tunnel
                    dos.writeUTF(keyboardInput);
                    dos.flush();

                    // Receive message from server (response) and proces it
                    msgReceived = dis.readUTF();
                    System.out.println(msgReceived);
                }

                // Clsoe output stream
                dos.close();
                bos.close();
                os.close();
            }
            catch (EOFException e) {
                e.printStackTrace();
            }

            dis.close();
            bis.close();
            is.close();

        }
        catch (EOFException e) {
            e.printStackTrace();
            socket.close();
        }


    }
}
