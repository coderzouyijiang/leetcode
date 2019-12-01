package cn.zyj.tunnel.javafx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App1 extends Application {

    @Override
    public void init() throws Exception {
        super.init();
        log.info("app1 init");
        log.info("" + getParameters().getRaw());
        log.info("" + getParameters().getNamed());
        log.info("" + getParameters().getUnnamed());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        log.info("app1 stop");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button btn = new Button();
        btn.setText("click me!");
        btn.setOnAction(event -> {
            log.info("app1 click");
        });
        Canvas canvas = new Canvas(500, 500);
        canvas.setStyle("-fx-background-color: #778899;");
        GraphicsContext graphCtx = canvas.getGraphicsContext2D();
        drawShapes(graphCtx);
//        StackPane stackPane = new StackPane();
//        stackPane.getChildren().add(btn);
//        stackPane.getChildren().add(canvas);
        VBox root = new VBox(btn, canvas);
        Scene rootScene = new Scene(root);

        primaryStage.setScene(rootScene);
        primaryStage.setTitle("app1");
        primaryStage.show();
    }

    private void drawShapes(GraphicsContext gc) {
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        gc.strokeOval(60, 60, 30, 30);
        gc.fillRoundRect(110, 60, 30, 30, 10, 10);
        gc.strokeRoundRect(160, 60, 30, 30, 10, 10);
        gc.fillArc(10, 110, 30, 30, 45, 240, ArcType.OPEN);
        gc.fillArc(60, 110, 30, 30, 45, 240, ArcType.CHORD);
        gc.fillArc(110, 110, 30, 30, 45, 240, ArcType.ROUND);
        gc.strokeArc(10, 160, 30, 30, 45, 240, ArcType.OPEN);
        gc.strokeArc(60, 160, 30, 30, 45, 240, ArcType.CHORD);
        gc.strokeArc(110, 160, 30, 30, 45, 240, ArcType.ROUND);
        gc.fillPolygon(new double[]{10, 40, 10, 40},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolygon(new double[]{60, 90, 60, 90},
                new double[]{210, 210, 240, 240}, 4);
        gc.strokePolyline(new double[]{110, 140, 110, 140},
                new double[]{210, 210, 240, 240}, 4);
    }
}
