package imageRecongnition;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class TopCodesController{


	Scanner scanner;


	public TopCodesController(){
		scanner  = new Scanner();
	}

	public String codeNumbers(List<TopCode> sampleCodes){

		StringBuilder builder = new StringBuilder();
		
		List<TopCode> codes = null;

		BufferedImage img = null;

		String ls = "fswebcam -r 640x480 -d /dev/video0 Commands.jpg";

		try{

			if(sampleCodes == null){
			Process pGrep = Runtime.getRuntime().exec(ls); // Capture an image from webcam

			pGrep.waitFor();

			img = ImageIO.read(new File("Commands.jpg"));
			System.out.println("Scanning Image: Commands.jpg");
			codes = scanner.scan(img);
			System.out.println("Scan Complete!");
			}else{
				codes = sampleCodes;
			}
			
		}catch (IOException e){
			return null;
		}
		catch (Exception e){
			return null;
		} 



		if (codes != null) {

			int size = codes.size();

			System.out.println("Found " + size + " Topcodes");

			Collections.sort(codes, new Comparator<TopCode>() {

				@Override
				public int compare(TopCode o1, TopCode o2) {
					if(o1.getCode() == o2.getCode()){
						return 0;
					}else if(o1.getCode() > o2.getCode()){
						return 1;
					}else if(o1.getCode() < o2.getCode()){
						return -1;
					}
					
					return 0;
				}
			});
			
			
			for (TopCode top : codes) {

				builder.append(top.getCode() + ",");
			
			}

			
			builder.deleteCharAt(builder.length() - 1);
			
			System.out.println(builder.toString());
			
			return builder.toString();

		}else{
			System.out.println("No Topcodes Found!");
		}

		return null;
	}
	
	public static void main(String[] args){
		TopCodesController controller = new TopCodesController();
		
		List<TopCode> sampleCodes = new ArrayList<TopCode>();
		
		sampleCodes.add(new TopCode(97));
		sampleCodes.add(new TopCode(105));
		sampleCodes.add(new TopCode(301));
		sampleCodes.add(new TopCode(303));
		sampleCodes.add(new TopCode(105));
		sampleCodes.add(new TopCode(303));
		
		
		controller.codeNumbers(sampleCodes);
	}
}

