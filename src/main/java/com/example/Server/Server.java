package com.example.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{
    public static ArrayList<Client> ClientList = new ArrayList<>();
    public static ArrayList<String> FileList = new ArrayList<>();
    //public static

    public static void main(String[] args)
            throws IOException
    {

        ServerSocket ss = new ServerSocket(9999);
        System.out.println("服务器运行中...");

        while(true)
        {
            try
            {
                //监听客户端，分给线程
                Client newClient = new Client(ss.accept());
                ClientList.add(newClient);
                newClient.start();
            }

            catch (IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }
}


class  Client
        extends Thread
{

    private Socket socket;
    public String id = null;
    public DataInputStream din;

    private FileOutputStream fos;
    private static FileInputStream fis;
    public DataOutputStream dout;
    protected String username = null;

    public FileWriter FW = null;
    public BufferedWriter BW = null;


    //服务线程的构造函数
    public Client(Socket socket)
    {
        this.socket = socket;

        try
        {
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            this.id = din.readUTF();

            PublicSend("TALK#"+this.id+"已连接到聊天室。#"+"系统消息", this.id);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void run()
    {
        String newPhrase = null;
        try
        {
            while ((newPhrase = din.readUTF()) != null)
            {
                ExcuteMsg(newPhrase);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            OffLine(this.id);
        }
    }

    public void ExcuteMsg(String phrase)
    {
        System.out.println(phrase);

        String [] arr = phrase.split("#");
        int type = Integer.valueOf(arr[0]);
        String to = arr[1], msg = arr[2];

        //Type#to#msg

        //type 1私聊 2群聊 3传文件 4添加好友 5下载文件
        //to 接收人/群的id
        //msg 消息


        if (type == 1)
        {
            PrivateSend("TALK#"+msg+"#"+id, to);
        }
        else if (type == 2)
        {
            PublicSend("TALK#"+msg+"#"+"[来自公共聊天室]"+this.id, this.id); //群发
        }
        else if (type == 3)
        {
            String fileName = to;

            if (msg.equals("NEW")) //文件不存在，新建
            {
//                File fl = new File("D:\\" + fileName);
//                try
//                {
//                    fl.createNewFile();
//                    FW = new FileWriter("D:\\" + fileName);
//                    BW = new BufferedWriter(FW);
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
                try {
                    din = new DataInputStream(socket.getInputStream());

                    File directory = new File("D:\\FTCache");
                    if (!directory.exists()) {
                        directory.mkdir();
                    }
                    File file = new File(directory.getAbsolutePath() + File.separatorChar + fileName);
                    fos = new FileOutputStream(file);

                    // 开始接收文件
                    byte[] bytes = new byte[1024];
                    int length = 0;
                    while ((length = din.read(bytes, 0, bytes.length)) != -1) {
                        fos.write(bytes, 0, length);
                        fos.flush();
                        if (length < 1024) break;
                    }
                    fos.close();
                    System.out.println("======== 文件传输成功 ========");
                    Server.FileList.add(fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (type == 4)
        {
            if (PrivateSend("ADD#"+id,to))
            {
                returnMsg("ADD#" + to);
                returnMsg("TALK#"+"添加好友"+to+"成功！#"+"系统消息");
                PrivateSend("TALK#"+this.id+"已添加你为好友！#"+"系统消息", to);
            }
            else
            {
                returnMsg("SHOW#"+"未找到用户！");
            }
        }
        else if (type == 5)
        {
            for (String str : Server.FileList)
            {
                if (str.equals(to))
                {
                    SendFile(str);
                    break;
                }
            }
        }
        else if(type==6){
            //to=arr[2];
            PrivateSend("Call#"+msg,to);
        }
        else if (type==7) {
            PrivateSend("no#"+msg,to);

        }
        else if(type==8){
            PrivateSend("Call-yes#"+msg,to);
        }

    }

    public void SendFile(String fileName)
    {

        String filePath = "D:\\FTCache" + File.separatorChar + fileName;
        try
        {
            returnMsg("SAVE#" + fileName + "#" + "NEW");

            File file = new File(filePath);
            if(file.exists()) {
                fis = new FileInputStream(file);

                // 开始传输文件
                System.out.println("======== 开始传输文件 ========");
                byte[] bytes = new byte[1024];
                int length = 0;
                long progress = 0;
                while ((length = fis.read(bytes, 0, bytes.length)) != -1) {
                    dout.write(bytes, 0, length);
                    dout.flush();
                    progress += length;
                    System.out.print("| " + (100 * progress / file.length()) + "% |");
                }
                System.out.println();
                System.out.println("======== 文件传输成功 ========");
                fis.close();
            }
            else{
                System.out.println("未找到对应文件");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    public void OffLine(String name)
    {
        for (Client cl : Server.ClientList)
        {
            try
            {
                cl.dout.writeUTF("TALK#"+this.id+"已下线。#"+"系统消息");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        Server.ClientList.remove(this);
    }

    //群发消息
    public boolean PublicSend(String msg, String id)
    {
        for (Client cl : Server.ClientList)
        {
            if (cl.id.equals(id)) continue;
            try
            {
                cl.dout.writeUTF(msg);
            }
            catch(IOException e)
            {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        return false;
    }

    //私发消息
    public boolean PrivateSend(String msg, String id)
    //    //消息内容、接收人
    {
        for (Client cl : Server.ClientList)
        {
            if (cl.id.equals(id))
            {
                try
                {
                    cl.dout.writeUTF(msg);

                } catch (IOException e)
                {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    //给本客户端回发消息
    public void returnMsg(String s)
    {
        try
        {
            dout.writeUTF(s);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}




