import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class QRCodeBeautifier extends Application{
	private ImageView fileImageView;
	private WritableImage fileCodeImage;
	private WritableImage codeImage;
	private Text result;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}
	
	public void start(final Stage primaryStage) throws Exception {
		Text resultText = new Text("解码结果：");
		resultText.setFont(Font.font(20));
		result = new Text();
		result.setFont(Font.font("Arial", 20));
		
		//打开文件按钮
		Button openFileBtn = new Button("打开一个二维码");
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("打开一个二维码");
		//fileChooser.setInitialDirectory(new File("F:\\胖丢科创\\from panger\\二维码实例")); 
		openFileBtn.setOnAction(new EventHandler<ActionEvent>(){

			public void handle(ActionEvent event) {
				File file = fileChooser.showOpenDialog(primaryStage);
				if (file != null) {
					try {
						loadImage(file);
						String path = file.getAbsolutePath();
						String nextPath = path.substring(0, path.lastIndexOf('\\'));
						fileChooser.setInitialDirectory(new File(nextPath)); 
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }	
			}
		});
		
		fileImageView = new ImageView();
		
		
		BorderPane border = new BorderPane();
		HBox hboxTop = new HBox();
		border.setTop(hboxTop);
		hboxTop.setPadding(new Insets(20, 12, 20, 12));
		hboxTop.setStyle("-fx-background-color: #A4D3EE;");
		hboxTop.setSpacing(10);
		hboxTop.getChildren().addAll(resultText, result);
		
		VBox vboxCenter = new VBox();
		border.setCenter(vboxCenter);
		vboxCenter.setPadding(new Insets(15, 12, 5, 12));
		vboxCenter.setStyle("-fx-background-color: #BEBEBE;");
		vboxCenter.setSpacing(10);
		vboxCenter.getChildren().addAll(openFileBtn,fileImageView);
		
		Scene scene = new Scene(border,600,500);
	    primaryStage.setTitle("视觉二维码的美化设计");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}
	protected void loadImage(File file) throws IOException {
		// TODO Auto-generated method stub
		BufferedImage fileImage = null;
		try {
			fileImage = ImageIO.read(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int wid = fileImage.getWidth();
		int hei = fileImage.getHeight();
		fileCodeImage = new WritableImage(wid,hei);
		PixelWriter fileImageWriter = fileCodeImage.getPixelWriter();
		int rgb,r,g,b;
		for(int i=0;i<wid;i++){
			for(int j=0;j<hei;j++){
				fileImageWriter.setColor(i, j, Color.WHITE);
				rgb = fileImage.getRGB(i, j);
				r = (rgb & 0xff0000) >> 16;  
            	g = (rgb & 0xff00) >> 8;  
            	b = (rgb & 0xff);  
            	fileImageWriter.setColor(i, j, Color.rgb(r, g, b));
			}
		}
		codeImage = fileCodeImage;
		fileImageView.setImage(fileCodeImage);
		fileImageView.setFitWidth(250);
		fileImageView.setPreserveRatio(true);
		MyQRCodeDecoder decoder = new MyQRCodeDecoder(fileCodeImage);
		result.setText(decoder.getMessage());
	}
}
