package com.example.clientGUI.video;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import static javafx.geometry.Pos.CENTER;

public class ProgressFrom {

        //private static final System.Logger logger = System.Logger.getLogger(ProgressFrom.class);

        private Stage dialogStage;
        private ProgressIndicator progressIndicator;

        public ProgressFrom(Stage primaryStage) {

            dialogStage = new Stage();
            progressIndicator = new ProgressIndicator();

            // 窗口父子关系
            dialogStage.initOwner(primaryStage);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            dialogStage.initStyle(StageStyle.TRANSPARENT);
            dialogStage.initModality(Modality.APPLICATION_MODAL);

            // progress bar
            Label label = new Label("拨打语音电话中");
            label.setTextFill(Color.BLUE);
            //label.getStyleClass().add("progress-bar-root");
            progressIndicator.setProgress(-1F);
            //progressIndicator.getStyleClass().add("progress-bar-root");
            //progressIndicator.progressProperty().bind(task.progressProperty());
            Button b=new Button();
            b.setText("cancel");
            b.setOnAction(e->{
                this.cancelProgressBar();
            });
            VBox vBox = new VBox();
            vBox.setSpacing(10);
            vBox.setBackground(Background.EMPTY);
            vBox.setAlignment(CENTER);
            vBox.getChildren().addAll(progressIndicator,label,b);

            Scene scene = new Scene(vBox);
            scene.setFill(null);
            dialogStage.setScene(scene);


        }

        public void activateProgressBar() {
            dialogStage.show();
        }

        public Stage getDialogStage(){
            return dialogStage;
        }

        public void cancelProgressBar() {
            dialogStage.close();
        }
    }


