package imageRecongnition;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class TopCodesController{


	Scanner scanner;


	public TopCodesController(){
		scanner  = new Scanner();
	}

	public String codeNumbers(){

		List<TopCode> codes = null;

		String imageCodes = null;

		BufferedImage img = null;

		String ls = "fswebcam -r 640x480 -d /dev/video0 Commands.jpg";

		try{

			Process pGrep = Runtime.getRuntime().exec(ls); // Capture an image from webcam

			pGrep.waitFor();

			img = ImageIO.read(new File("Commands.jpg"));
			System.out.println("Scanning Image: Commands.jpg");
			codes = scanner.scan(img);
			System.out.println("Scan Complete!");
			
		}catch (IOException e){
			return null;
		}
		catch (Exception e){
			return null;
		} 



		if (codes != null) {

			int size = codes.size();

			System.out.println(size);

			int[] codesarray = new int[size];

			int counter = 0;

			for (TopCode top : codes) {


				if(counter == 0){

					codesarray[0] = top.getCode();
					counter = counter+1;
				}else{

					codesarray[counter] = top.getCode();
					counter = counter+1;

				}

			}


			Arrays.sort(codesarray);
			System.out.println(Arrays.toString(codesarray));

			imageCodes = Arrays.toString(codesarray);

			imageCodes = imageCodes.substring(1, imageCodes.length()-1);

		}





		return imageCodes;
	}	





}

