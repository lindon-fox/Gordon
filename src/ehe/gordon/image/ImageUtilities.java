package ehe.gordon.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageUtilities {


	public static BufferedImage fitAndScaleImage(BufferedImage originalImage,
			int scaledWidth, int scaledHeight) {
		// We need to scale the image properly so that it fits on one page.
		double xScale = (double) scaledWidth
		/ (double) originalImage.getWidth();
		double yScale = (double) scaledHeight
		/ (double) originalImage.getHeight();
		// Maintain the aspect ratio by taking the min of those 2 factors and
		// using it to scale both dimensions.
		double aspectScale = Math.min(xScale, yScale);
		int amendedWidth = (int)(originalImage.getWidth() * aspectScale);
		int amendedHeight = (int)(originalImage.getHeight() * aspectScale);
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = scaledBI.createGraphics();
		g2d.translate(0, 0);
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0,0, scaledBI.getWidth(), scaledBI.getHeight());
		
		// Set us to the upper left corner of the picture
		int xStartPosition = (int)((scaledWidth - amendedWidth) / 2.0);
		int yStartPosition = (int)((scaledHeight - amendedHeight) / 2.0);
		g2d.translate(xStartPosition, yStartPosition);
		AffineTransform at = new AffineTransform();
		at.translate(0, 0);

		at.scale(aspectScale, aspectScale);
		g2d.drawRenderedImage(originalImage, at);
//		g2d.fillRect(scaledBI.getWidth() - xStartPosition,scaledBI.getHeight() - yStartPosition, scaledBI.getWidth(), scaledBI.getHeight());
		return scaledBI;
	}

}
