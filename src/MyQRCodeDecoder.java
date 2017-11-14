
/*
 * 调用QR码的API进行解码
 * */
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;



import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

import jp.sourceforge.qrcode.QRCodeDecoder;
import jp.sourceforge.qrcode.data.QRCodeImage;
import jp.sourceforge.qrcode.exception.DecodingFailedException;


public class MyQRCodeDecoder {
	String decodedData;
	BufferedImage image;
	public MyQRCodeDecoder(WritableImage img) throws IOException{
		QRCodeDecoder decoder = new QRCodeDecoder();
		image = new BufferedImage((int)img.getWidth(),(int) img.getHeight(),BufferedImage.TYPE_INT_RGB);  
		PixelReader pixelReader = img.getPixelReader();
		int i,j;
		//System.out.print(pixelReader.getColor(3, 3).getRed());
		for(i=0;i<image.getWidth();i++){
			for(j=0;j<image.getHeight();j++){
				Color color = pixelReader.getColor(i, j);
				int r = (int) (color.getRed()*255);
				int g = (int) (color.getGreen()*255);
				int b = (int) (color.getBlue()*255);
				int rgb = (r<<16) + (g<<8) + b;
				
				image.setRGB(i, j, rgb);
			}
		}
		
		
		try {
			decodedData = new String(decoder.decode(new J2SEImage(image)), "GBK");
			
		} catch (DecodingFailedException dfe) {
			//System.out.println("Error: " + dfe.getMessage());
			decodedData = new String("Error: " + dfe.getMessage());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public String getMessage(){
		return decodedData;
	}
}
class J2SEImage implements QRCodeImage {
	BufferedImage image;

	public J2SEImage(BufferedImage image) {
		this.image = image;
	}

	public int getWidth() {
		return image.getWidth();
	}

	public int getHeight() {
		return image.getHeight();
	}

	public int getPixel(int x, int y) {
		return image.getRGB(x, y);
	}
}

