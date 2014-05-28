package imageRecongnition;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import topcodes.Scanner;
import topcodes.TopCode;

public class ImageReconginition {

	protected Webcam webcam;

	protected Scanner scanner;

	public ImageReconginition(){
		this.webcam   = Webcam.getDefault();
		this.scanner  = new Scanner();

		if (webcam != null) {
			System.out.println("Webcam: " + webcam.getName());
		} else {
			System.out.println("No webcam detected");
		}

		webcam.open();

	}


	public String takeSnapshot(){

		StringBuilder result = new StringBuilder();
		List<TopCode> codes = null;
		ByteBuffer imageDataRaw;
		IntBuffer imageDataSampled;
		int[] imageData = new int[webcam.getViewSize().width * webcam.getViewSize().height];

		webcam.getImage().getRGB(0, 0, webcam.getViewSize().width, webcam.getViewSize().height, imageData, 0, 0);
		//imageDataSampled = imageDataRaw.asIntBuffer();
		
		//imageData = new int[imageDataSampled.limit()];
		//imageDataSampled.get(imageData);
		
		try {
			ImageIO.write(webcam.getImage(), "PNG", new File("hello-world.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		codes = scanner.scan(
				imageData,
				webcam.getViewSize().width,
				webcam.getViewSize().height);

		if(codes.size() != 0){
			for(TopCode code : codes){
				result.append(code.getCode() + ",");
			}
		}


		return result.toString();
	}

	public static void main(String[] args){
		ImageReconginition ir = new ImageReconginition();
		try {

			for(int i = 0; i < 5; i++){
				System.out.println(i);
				Thread.sleep(1000);
			}

			System.out.println(ir.takeSnapshot());

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
