/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clientserverapp;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author hrishimane
 */
public class Server {

    String strIN = "", strOUT = "";
    Thread t1, t2;
    boolean flag1 = true, flag2 = true;

    public void inThread(String name, Socket s) {                               //method for receiving the message
        t1 = new Thread(name) {                                                 //thread for accepting message
            @Override
            public void run() {
                try {
                    DataInputStream din = new DataInputStream(s.getInputStream());   //input stream for receiving the message
                    while (!strIN.equalsIgnoreCase("quit")) {
                        strIN = din.readUTF();                                       //quit logic
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

    public void OutThread(String name, Socket s) {                               //method for sending the message
        t2 = new Thread(name) {                                                  //thread for sending the message
            @Override
            public void run() {
                try {
                    DataOutputStream dout = new DataOutputStream(s.getOutputStream());         //output data stream 
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));  //output buffer for sending the message
                    while (!strIN.equalsIgnoreCase("quit")) {                      //quit logic
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
        Server obj = new Server();
        System.out.println("I am a SERVER!");
        String ipString;
        int port;

        Scanner sc = new Scanner(System.in);                                     // for accepting ip and port numbers
        System.out.print("Enter the IP address of the server: ");
        ipString = sc.next();
        System.out.print("Enter the Port Number of the server: ");
        port = sc.nextInt();
        try {
            InetAddress ip = InetAddress.getByName(ipString);                   //block for creating server socket and accepting
            ServerSocket ss = new ServerSocket(port, 3, ip);                    //message from 3 clients
            System.out.println("Waiting for connections...");
            Socket s = ss.accept();
            obj.inThread("RECEIVE", s);
            obj.OutThread("SEND", s);
            //System.out.println(obj.t1.isAlive() + " " + obj.t2.isAlive());       //for closing the threads
            if ((obj.flag1 == false) || (obj.flag2 == true)) {
                obj.t1.join();
                obj.t2.join();
            }
        } catch (Exception e) {
            System.out.println("Thank you!");
        }
        
        System.out.println("Connection terminated!");

    }

}
