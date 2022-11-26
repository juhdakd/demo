package com.example.clientGUI.video;

import com.example.clientGUI.ChatController;
import com.example.clientGUI.MsgReceiver;
import com.example.clientGUI.MsgSender;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class VideoChoiceController {
    @FXML
    Label context;
    @FXML
    public void initialize() throws IOException {
         context.setText(MsgReceiver.name+"向你发起语音通话");
    }
    @FXML
    protected void cancel() throws IOException {
        MsgSender.dout.writeUTF("7" + "#" + MsgReceiver.name+"#拒绝");
        Stage stage= (Stage) context.getScene().getWindow();
        stage.close();
    }
    @FXML
    protected void accept() throws IOException {
        MsgSender.dout.writeUTF("8" + '#' + MsgReceiver.name);
        Stage stage= (Stage) context.getScene().getWindow();
        stage.close();
    }
}
