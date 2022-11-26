package com.example.clientGUI;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.clientGUI.ChatController.*;


public class MsgReceiver
        extends Thread
{
    static public  String name;
    Socket socket = null;
    DataInputStream din = null;
    FileWriter FW = null;
    BufferedWriter BW = null;
    private FileOutputStream fos;

    public MsgReceiver()
            throws IOException
    {
        this.socket = HelloApplication.s;
        din = new DataInputStream(socket.getInputStream());
    }

    public void run()
    {
        try
        {
            String str = null;
            while ((str = din.readUTF()) != null)
            {
                ExcuteMsg(str);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void ExcuteMsg(String s) throws IOException {
        System.out.println(s);

        String str[] = s.split("#");
        String op = str[0], msg = str[1];

        if (op.equals("ADD"))
        {

            Button b=new Button(op);
            b.setPrefWidth(160);
            //ChatController.vbox.getChildren().add(b);
        }

        if (op.equals("SHOW"))
        {
            //announce(msg);
        }

        if (op.equals("TALK"))
        {
            Date date = new Date();
            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
            //聊天格式：TALK#MSG#ID

            String id = str[2];

            //model1.addElement(msg);
            ChatController.messagelist.appendText(id + "：" + msg +"("+dateFormat.format(date)+")"+ "\n");
        }

        if (op.equals("FILE"))
        {   System.out.println(msg);
            Label l=new Label(msg);
            l.setPrefWidth(80);
            Platform.runLater(()->{
                ChatController.items.add(l);
            });
        }

        if (op.equals("SAVE"))
        {
            String fileName = msg;

            if (str[2].equals("NEW")) //文件不存在，新建
            {
                File file = new File(downPath+"\\"+ fileName);
//                System.out.println(fileName+"is saving");
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
                System.out.println("======== 文件下载成功 ========");
            }
        }
        if(op.equals("Call")){
            name=str[1];
            Platform.runLater(()-> {
                Stage anotherStage = new Stage();
                Parent anotherRoot = null;
                try {
                    anotherRoot = FXMLLoader.load(getClass().getResource("video-choice.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Scene anotherScene = new Scene(anotherRoot,200,100);

                anotherStage.setTitle("Request");
                anotherStage.setScene(anotherScene);
                anotherStage.show();
            });

        }
        if(op.equals("no")){
            Platform.runLater(()-> {
            progressFrom.cancelProgressBar();
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setContentText("对方已拒绝");
            alert.show();
            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> alert.close());
        });
        }
        if(op.equals("Call-yes")){

        }
    }
}
