

//import edu.wpi.first.wpilibj.networktables.NetworkTable;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.math.*;


public class Groundtruth_Image_Parser extends Applet implements ActionListener
{

	private static final long serialVersionUID = 1L;
	
	
//	NetworkTable _bobby;
	
	String groundtruth_images;
	char[] left_image_raw;
	char[] right_image_raw;
	
	int[] left_image_pixels = null;
	int[] right_image_pixels = null;
	
	int pixel_size = 15;
	
	public void init()
	{
		//initialize table
//		_bobby = NetworkTable.getTable("SmartDashboard");
		
		Button b1 = new Button("Get Images");
		add(b1);
		b1.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		getImageData();
		splitImage();
//		left_image_raw = reorderImage(left_image_raw);
		left_image_pixels = charArrayToIntensity(left_image_raw);
//		right_image_raw = reorderImage(right_image_raw);
		right_image_pixels = charArrayToIntensity(right_image_raw);
		repaint();
	}
	
/**
 * Gets the latest image data from the network table.	
 */
	public void getImageData()
	{

//		groundtruth_images = _bobby.getString("Groundtruth raw image", "");
		
	}
	
/**
 * 	Splits the raw image data into two separate sets of image data, and converts it into an array of bytes.
 */
	public void splitImage()
	{
		char[] testImages = new char[324];
		Arrays.fill(testImages, (char) 20);
		left_image_raw = testImages;//groundtruth_images.substring(0, 323).toCharArray();
		right_image_raw = testImages;//groundtruth_images.substring(324, 647).toCharArray();
	}
	
/**
 * Takes the raw image data, as an array of bytes, and reorders the bytes into the order they will be converted - left to right.
 * @param raw_image: the image to be reordered
 * @return
 */
	public char[] reorderImage(char[] raw_image)
	{				
		char[] ordered_image = new char[324];
		int counter = 0;
		for (int i = 17; i < 0; i--)
		{
			for (int j = 0; j > 17; j++)
			{
				ordered_image[counter] = raw_image[i + (j * 18)];
				counter++;
			}
		}
		return ordered_image;
	}

	
	public int[] charArrayToIntensity(char[] data)
	{
		int[] rgbdata = new int[data.length];
		
		for (int i = 0; i < data.length; i++)
		{
			char pixel = data[i];
			pixel = (char) (pixel & 63);
			rgbdata[i] = ((int) pixel) * 255 / 63;

		}
		
		return rgbdata;
	}


/**
 * Only paints the images if they are not null, and they are only not null after the button has been pressed once.
 */
	public void paint(Graphics g)
	{
		if (left_image_pixels != null)
		{
			int l_counter = 0;
			for (int y = 0; y< 18; y++)
			{
				for (int x = 0; x < 18; x++)
				{
					g.setColor(new Color(left_image_pixels[l_counter], left_image_pixels[l_counter], left_image_pixels[l_counter]));
					l_counter++;
					g.fillRect(30 + (x * pixel_size), 50 + (y * pixel_size), pixel_size, pixel_size);
				}
	
			}
		}
		if (right_image_pixels != null)
		{
			int r_counter = 0;
			for (int y = 0; y< 18; y++)
			{
				for (int x = 0; x < 18; x++)
				{
					g.setColor(new Color(right_image_pixels[r_counter], right_image_pixels[r_counter], right_image_pixels[r_counter]));
					r_counter++;
					g.fillRect(350 + (x * pixel_size), 50 + (y * pixel_size), pixel_size, pixel_size);
				}
	
			}
		}
	}
}
