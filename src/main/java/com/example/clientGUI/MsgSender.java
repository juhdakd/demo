package com.example.clientGUI;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class MsgSender
        extends Thread
{

    Socket socket = null;
    static public DataOutputStream dout;

    private static FileInputStream fis;

    static {
        try {
            dout = new DataOutputStream(HelloApplication.s.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    Scanner in = new Scanner(System.in);


    public static void sendMsg(String msg)
    {
        try
        {
            dout.writeUTF(msg);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    public static void sendVoice(){

    }
    public static void sendFile(String filePath, String fileName)
            throws FileNotFoundException, IOException
    {
//        dout.writeUTF("3#"+fileName+"#"+"NEW");
//
//        InputStream FI = new FileInputStream(filePath);
//        InputStreamReader IS = new InputStreamReader(FI, "UTF-8");
//        BufferedReader BR = new BufferedReader(IS);
//
//        String buf = null;
//        while ((buf = BR.readLine()) != null)
//        {
//            dout.writeUTF("3#"+fileName+"#"+buf);
//        }
//        dout.writeUTF("3#"+fileName+"#OVER");
//        BR.close();
//        IS.close();
//        FI.close();
        try {
            File file = new File(filePath);
            if(file.exists()) {
                fis = new FileInputStream(file);
                dout.writeUTF("3#"+fileName+"#"+"NEW");

                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dout.write(bytes, 0, length);
                    dout.flush();
                    progress += length;
                    System.out.print("| " + (100*progress/file.length()) + "% |");
                }
                System.out.println();
                System.out.println("======== 文件传输成功 ========");
                fis.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        while(true)
        {

        }
    }
}
