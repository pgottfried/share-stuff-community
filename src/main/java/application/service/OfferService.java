package application.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;

import application.entity.Image;

@Component
public class OfferService {
	
	public Image readImgfromFile(File file){
		String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		if(!fileExtension.toLowerCase().equals("jpg") ){
//			|| !fileExtension.toLowerCase().equals("png")
//			|| !fileExtension.toLowerCase().equals("gif")
			return null;
		}
		Image image = new Image();
		try {	 //save all pics to image table
				
				image.setImage(Files.readAllBytes(file.toPath()));
				String picName = file.getName();
				if(picName.length() > 32){
					String suffix = picName.substring(picName.lastIndexOf("."));
					String picTitle = picName.substring(0, 26);
					picName = picTitle.concat(suffix);
				}
				image.setName(picName);
				
		}catch (Exception e) {
			// TODO Auto-generated catch block
			// if this fails remove entry from database
			// return some failure page
			e.printStackTrace();	
		}		 
		return image;
		
	}
	
	public byte[] createThumbnailFromFile(File file){
		String thumbExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
		BufferedImage offerThumbnail;
		byte[] imageInByte = null;
		try {
			offerThumbnail = Scalr.resize(ImageIO.read(file), 250);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(offerThumbnail, thumbExtension, baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		}
		catch(Exception e){
			imageInByte = null;
		}
		return imageInByte;
	}
	
	
	
}
