package com.example.clientGUI;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class HelloController {
    @FXML
    private TextField port;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField severInput;
    @FXML
    private TextField password;
    @FXML
    private Button enter;
    @FXML
    private Button close;
    @FXML
    private Button changport;
    @FXML
    private Button changesever;
    private DataOutputStream dout = null;
    private DataInputStream din = null;
    private Socket socket = null;
    @FXML
    protected void setChangport() {
        this.port.editableProperty().setValue(!(port.editableProperty().getValue()));

    }
    @FXML
    protected void setChangesever() {
        this.severInput.editableProperty().setValue(!(severInput.editableProperty().getValue()));

    }
    @FXML
    protected void register() throws IOException {
        changePage(this.enter,"register.fxml",500,400,-100,-50);
    }
    @FXML
    protected void exit(){
        Stage stage=getStage(this.close);
        stage.close();
    }
    public static Stage getStage(Button button){
        return (Stage)button.getScene().getWindow();

    }
    public static int changePage(Button button,String s,int x,int y,int moveX,int moveY) throws IOException {
        Parent other = FXMLLoader.load(HelloController.class.getResource(s));
        Scene scene=new Scene(other,x,y);
        Stage stage=HelloController.getStage(button);
        stage.setScene(scene);
        stage.setX(stage.getX()+moveX);
        stage.setY(stage.getY()+moveY);
        return 0;
    }
    void ConnectServer()
    {
        try
        {
            HelloApplication.s = new Socket(severInput.getText(), Integer.valueOf(port.getText()));
            this.socket = HelloApplication.s;
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
        }
        catch(java.rmi.UnknownHostException un)
        {
            un.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
    @FXML
    public  void login() throws IOException {
        if(!(password.getText().equals("ztty"))){
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("密码错误");
            alert.setContentText("服务器令牌错误");
            alert.setX(550);
            alert.setY(250);
            alert.setWidth(400);
            alert.show();
            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> alert.close());
            return;
        }

        ConnectServer();
        try
        {
            dout.writeUTF(nameInput.getText());
            HelloApplication.MyName = nameInput.getText();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        changePage(this.enter,"chat.fxml",580,440,-100,-50);

    }

}