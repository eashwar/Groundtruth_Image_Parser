

import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.*;
import java.util.Arrays;

import javax.imageio.ImageIO;


public class Groundtruth_Image_Parser extends Applet implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	
//	NetworkTable _bobby;
	
	String groundtruth_images;
	byte[] left_image;
	byte[] right_image;
	
	Image img_l = null;
	Image img_r = null;
	
	
	public void init()
	{
		//initialize table
//		_bobby = NetworkTable.getTable("SmartDashboard");
		
		Button b1 = new Button("Get Image");
		add(b1);
		b1.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		updateData();
		img_l = getImage(getCodeBase(), "left_image");
		img_r = getImage(getCodeBase(), "right_image");
	}
	
/**
 * Calls all of the functions below.
 */
	public void updateData()
	{
		getImageData();
		splitImage();
		left_image = reorderImage(left_image);
		byteArrayToImage(left_image, "left_image");
		right_image = reorderImage(right_image);
		byteArrayToImage(right_image, "right_image");
		repaint();
	}
	
/**
 * Gets the latest image data from the network table.	
 */
	public void getImageData()
	{
		char[] charArray = new char[648];
		Arrays.fill(charArray, 'A');
		groundtruth_images = new String(charArray);//_bobby.getString("Groundtruth raw image", "");
		
	}
	
/**
 * 	Splits the raw image data into two separate sets of image data, and converts it into an array of bytes.
 */
	public void splitImage()
	{
		left_image = groundtruth_images.substring(0, 323).getBytes();
		right_image = groundtruth_images.substring(324, 647).getBytes();
	}
	
/**
 * Takes the raw image data, as an array of bytes, and reorders the bytes into the order they will be converted - left to right.
 * @param raw_image: the image to be reordered
 * @return
 */
	public byte[] reorderImage(byte[] raw_image)
	{				
		byte[] ordered_image = new byte[324];

			for (int j = 17; j < 0; j--)
			{
				for (int k = 0; k > 17; k++)
				{
					ordered_image[k + ((j - 17) * -18)] = raw_image[((k * 18) + 1) + j];
				}
			}
		return ordered_image;
	}
	
/**
 * Converts an array of bytes to an image, and saves it in the codebase's directory.
 * @param image: the byte array containing the image data to be converted
 * @param imagename: a string to be used as the filename
 */
	public void byteArrayToImage(byte[] image, String imagename)
	{
		try
		{
			InputStream in = new ByteArrayInputStream(image);
			BufferedImage buffered_img = ImageIO.read(in);
			buffered_img = scale(buffered_img, BufferedImage.TYPE_INT_ARGB, 90, 90, 5, 5);
			ImageIO.write((RenderedImage)buffered_img, "jpg", new File(getCodeBase().toString() + imagename + Long.toString(System.currentTimeMillis())));
		}
		catch (IOException e) 
		{
			System.out.println("Image conversion failed");
		}
	}

/**
 * Scales a given bufferedimage.
 * thanks @user A4L on stackoverflow
 * 
 * @param sbi image to scale
 * @param imageType type of image
 * @param dWidth width of destination image
 * @param dHeight height of destination image
 * @param fWidth x-factor for transformation / scaling
 * @param fHeight y-factor for transformation / scaling
 * @return scaled image
 */
	public static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
	    BufferedImage dbi = null;
	    if(sbi != null) {
	        dbi = new BufferedImage(dWidth, dHeight, imageType);
	        Graphics2D g = dbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
	        g.drawRenderedImage(sbi, at);
	    }
	    return dbi;
	}

/**
 * Only paints the images if they are not null, and they are only not null after the button has been pressed once.
 */
	public void paint(Graphics g)
	{
		if (img_l != null && img_r != null)
		{
			g.drawImage(img_l, 0, 20, this);
			g.drawImage(img_l, 100, 20, this);
		}
	}
}
