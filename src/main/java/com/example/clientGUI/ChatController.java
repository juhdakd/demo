package com.example.clientGUI;

import com.example.clientGUI.video.ProgressFrom;
import com.example.edition.GuiCamera;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

//import static sun.tools.jconsole.inspector.XTree.treeView;

public class ChatController {
    //public static DataModel model,
            //model1;
    @FXML
    private ScrollPane sp;
    @FXML
    private FlowPane fp;
    public static  TextArea messagelist=new TextArea();
    public static Label wjhc=new Label("文件缓存");
    @FXML
    private TextArea message;
    @FXML
    private  ScrollPane scrollPane;
    @FXML
    private Button uploader;
    @FXML
    private TextField addfriend;
    @FXML
    private Button gglts;
    @FXML
    public VBox vbox;
    @FXML
    private VBox vbox2;
    public  ListView<Label> files= new ListView<Label>();
    public static ObservableList<Label> items =FXCollections.observableArrayList (
            wjhc);
    public static String toID = null;
    public Integer type = 0;
    public static ProgressFrom progressFrom;
    public static String filePath = null;
    public static String fileName = null;
    public static String getFileID = null;
    public static String downPath = null;
    public static boolean video=false;

    static boolean last=true;
    @FXML
    private void initialize() throws IOException {
        /*sp=new ScrollPane();
        sp.setLayoutY(20);
        sp.setPrefWidth(160);
        sp.setPrefHeight(400);
        sp.hbarPolicyProperty();*/
        files.setItems(items);
        vbox2.getChildren().add(files);
        new MsgReceiver().start();
        new MsgSender().start();
        messagelist.setPrefHeight(300);
        //model  = new DataModel();
       // model1 = new DataModel();
        files.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Label>) e->{
            getFileID=files.getSelectionModel().getSelectedItem().getText();
        });

        scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                if (last) {
                    scrollPane.setVvalue(1.0);
                    last = false;
                }
            }
        });
        fp.getChildren().add(messagelist);
        HelloApplication.stage.setOnCloseRequest(e->{
            System.exit(0);
        });
    }

    @FXML
    public void actionPerformedSend() {

        String Msg = this.message.getText();
        message.setText("");

        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");
        messagelist.appendText("发送消息给"+toID + "：" + Msg +"("+dateFormat.format(date)+")"+'\n');
        //this.messagelist.getChildren().clear();
        try
        {
            MsgSender.dout.writeUTF(type.toString()+"#"+toID+'#'+Msg);
        }
        catch(IOException e)
        {

        }
    }

    @FXML
    public void actionPerformedAddFriend() {

        String NewUserName = this.addfriend.getText();
        if(NewUserName.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setContentText("找不到该用户");
            alert.show();
            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> alert.close());
            return;
        }
        try
        {
            MsgSender.dout.writeUTF("4" + '#' + NewUserName + "#s");
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        Button b=new Button(NewUserName);
        b.setOnAction(e->{
            this.toID=b.getText();
        });
        b.setPrefWidth(160);

        vbox.getChildren().add(b);
    }
    @FXML
    protected void setGglts(){
        toID="公共聊天室";
        type=2;
    }

    /*protected static int getMessage(Message message){
        Image image=new Image("D:\\1.jpg");
        ImageView imageView=new ImageView();
        imageView.setImage(image);
        imageView.setX(40);
        imageView.setY(40);


        Label messageLabel=new Label(message.getMessageContext());
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(220);
        messageLabel.setStyle("-fx-background-color: rgb(179,231,244); -fx-background-radius: 8px;");
        messageLabel.setPadding(new javafx.geometry.Insets(6));
        messageLabel.setFont(new Font(14));
        HBox.setMargin(messageLabel, new Insets(8, 0, 0, 0));

        boolean isMine = message.getSenderID().equals(HelloController.user.getAccount());
        double[] points;
        if (!isMine) {
            points = new double[]{
                    0.0, 5.0,
                    10.0, 0.0,
                    10.0, 10.0
            };
        } else {
            points = new double[]{
                    0.0, 0.0,
                    0.0, 10.0,
                    10.0, 5.0
            };
        }
        javafx.scene.shape.Polygon triangle = new Polygon(points);
        triangle.setFill(Color.rgb(179,231,244));
        HBox messageBox = new HBox();
        messageBox.setPrefWidth(366);
        messageBox.setPadding(new Insets(10, 5, 10, 5));
        if (isMine) {
            HBox.setMargin(triangle, new Insets(15, 10, 0, 0));
            messageBox.getChildren().addAll(messageLabel, triangle, imageView);
            messageBox.setAlignment(Pos.TOP_RIGHT);
        } else {
            HBox.setMargin(triangle, new Insets(15, 0, 0, 10));
            messageBox.getChildren().addAll(imageView, triangle, messageLabel);
        }
        last = scrollPane.getVvalue() == 1.0;
        messagelist.getChildren().add(messageBox);
        return 1;
    }*/
    @FXML
    protected void announce() throws IOException {
        progressFrom=new ProgressFrom((Stage) this.uploader.getScene().getWindow());
        progressFrom.activateProgressBar();
        MsgSender.dout.writeUTF("6" + '#' + this.toID + "#"+HelloApplication.MyName);

    }
    @FXML
    protected void screenShot(){
        HelloController.getStage(this.uploader).hide();
        GuiCamera cam=new GuiCamera("D:\\test","png");
        cam.snapshot();
        HelloController.getStage(this.uploader).show();
    }
    @FXML
    protected void upLoadSelect(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(HelloController.getStage(uploader));
        if(file.isDirectory())
        {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setContentText("不能选择目录");
            alert.show();
            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> alert.close());
        }
        else if(file.isFile())
        {  this.filePath = file.getAbsolutePath();

        }
        wjhc.setText(file.getName());
        fileName=file.getName();
    }
    @FXML
    protected void upLoading(){
        if (filePath == null)
        {
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setContentText("未选中文件");
            alert.show();
            Window window = alert.getDialogPane().getScene().getWindow();
            window.setOnCloseRequest(e -> alert.close());

        }
        else
        {
            try
            {
                MsgSender.sendFile(filePath, fileName);
            }
            catch (IOException e2)
            {
                e2.printStackTrace();
            }

        }
    }
    @FXML
    protected void download(){
        DirectoryChooser fileChooser = new DirectoryChooser();
        File file = fileChooser.showDialog(HelloController.getStage(uploader));
        //File file=.getSelectedFile();
        if(file.isDirectory())
        {
            downPath = file.getAbsolutePath();
            try
            {
                MsgSender.dout.writeUTF("5#" + getFileID + "#" + HelloApplication.MyName);
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
        else if(file.isFile())
        {

        }


    }



}
