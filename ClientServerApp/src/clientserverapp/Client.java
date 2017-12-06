package clientserverapp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author hrishimane
 */
public class Client {

    String strIN = "", strOUT = "";
    Thread t1, t2;
    boolean flag1 = true, flag2 = true;

    public void inThread(String name, Socket s) {            //method for accepting message streams
        t1 = new Thread(name) {                              //thread for receiving message
            @Override
            public void run() {
                try {
                    DataInputStream din = new DataInputStream(s.getInputStream());   // receiving message stream
                    while (!strIN.equalsIgnoreCase("quit")) {                        // quit logic
                        strIN = din.readUTF();
                        System.out.println("client says: " + strIN);
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    flag1 = false;
                }
            }
        };
        t1.start();
    }

    public void OutThread(String name, Socket s) {       //method for sending message stream
        t2 = new Thread(name) {                          //thread for sending message
            @Override
            public void run() {
                try {
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());              //sending message stream
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));       //output buffer to convert message into stream
                    while (!strIN.equalsIgnoreCase("quit")) {
                        strOUT = br.readLine();
                        dout.writeUTF(strOUT);
                        dout.flush();
                        Thread.sleep(2000);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    flag2 = false;
                }
            }
        };
        t2.start();
    }

    public static void main(String[] args) {
        Client obj = new Client();                               //client object
        System.out.println("I am a CLIENT!");
        String ipString;
        int port;

        Scanner sc = new Scanner(System.in);                     //for accepting intended ip and port numbers
        System.out.print("Enter the IP address of the server: ");
        ipString = sc.next();
        System.out.print("Enter the Port Number of the server: ");
        port = sc.nextInt();
        try {
            InetAddress ip = InetAddress.getByName(ipString);              //block for creating socket objects in order to connect to 
            Socket s = new Socket(ip, port);                                  // intended server
            if (s.isConnected()) {
                System.out.println("Connected!!");
                obj.inThread("RECEIVE", s);
                obj.OutThread("SEND", s);
                //System.out.println(obj.t1.isAlive() + " " + obj.t2.isAlive());
                if ((obj.flag1 == false) || (obj.flag2 == true)) {           //closing the threads
                    obj.t1.join();
                    obj.t2.join();
                }
            }

        } catch (Exception e) {
            System.out.println("Thank you!");
        }

        System.out.println("Connection Terminated!");

    }

}
