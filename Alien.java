/*
 * import statements
 */

import objectdraw.ActiveObject;
import objectdraw.DrawingCanvas;
import objectdraw.VisibleImage;
import objectdraw.FilledRect;

import java.applet.AudioClip;
import java.awt.Image;
import javax.swing.JApplet;

/*
 * class declaration
 */

/**
 * This is the Alien class. It will contain all the information relevant for
 * the alien including the actions the alien will perform and the images
 * associated with the alien.
 *
 * @author Kevin Tat
 * @version Lab 07
 */

public class Alien extends ActiveObject
{
	/*
	 * constants
	 */

	private static final double DISTANCE = 10.0;
	private static final int DELAY = 33;
	private static int alive = 0;

	/*
	 * instance fields
	 */

	private VisibleImage crntImage;		// current image visible on canvas
	private Image upImage, downImage;	// the two different alien images
	private Image deadAlien;			// the image to represent dead alien
	private DrawingCanvas canvas;		// the drawing canvas for game
	private static AudioClip hitClip = null;	// the sound alien makes when it is hit

	/**
	 * Alien constructor with the two different images that will simulate
	 * movement and another to simulate the alien being hit.
	 *
	 * @param firstImage the first image that will appear on the canvas
	 * @param secondImage the second image that will appear on the canvas
	 *		  when the alien moves
	 * @param deadImage the image to represent a dead alien
	 * @param x the X coordinate where the alien appears
	 * @param y the Y coordinate where the alien appears
	 * @param points the point value of the alien
	 * @param canvas the canvas in which the image is created
	 */

	public Alien (Image firstImage, Image secondImage, Image deadImage,
				  double x, double y, int points, DrawingCanvas canvas)
	{
		/*
		 * assign parameters to instance fields and create visible image
		 */

		downImage = firstImage;
		upImage = secondImage;
		deadAlien = deadImage;
		this.canvas = canvas;

		crntImage = new VisibleImage (downImage, x, y, canvas);

		/*
		 * set up the sound file for a hit alien
		 */

		 Class metaObject = getClass ();

		 if (hitClip == null)
		 {
			 hitClip = JApplet.newAudioClip (metaObject.getResource("shipHit.wav"));
		 }

		alive++;

	}

	/**
	 * Determine if the visible image is at the left border.
	 *
	 * @return true or false depending whether or not the visible image is at the left
	 *         border
	 */

	public boolean atLeftBorder ()
	{
		return ((crntImage.getX () - DISTANCE) < SpaceInvaders.LEFT_BORDER);
	}

	/**
	 * Determine if the visible image is at the right border.
	 *
	 * @return true or false depending whether or not the visible image is at the right
	 *         border
	 */

	public boolean atRightBorder ()
	{
		return ((crntImage.getX () + crntImage.getWidth () + DISTANCE) >
				SpaceInvaders.RIGHT_BORDER);
	}

	/**
	 * Drop the bomb from the location of the alien
	 *
	 * @param base the laser base for the target
	 */

	public void dropBomb (LaserBase base)
	{
		new Bomb (crntImage.getX () + crntImage.getWidth () / 2,
				  crntImage.getY () + crntImage.getHeight () - 4,
				  base, canvas);
	}

	/**
	 * Determine if the visible image is at the bottom of the canvas.
	 *
	 * @return true or false depending whether or not the visible image is at the bottom
	 *         border
	 */

	public boolean hasLanded ()
	{
		return ((crntImage.getY () + crntImage.getHeight () * 2) >=
				SpaceInvaders.BOTTOM_BORDER);
	}

	/**
	 * Determine if the visible image has been hit by the missile
	 *
	 * @param missile the filled rect representing the missile fired from the laser base
	 * @return true or false depending whether or not the missile hit the alien
	 */


	public synchronized boolean isHit (FilledRect missile)
	{
	 	if (!crntImage.isHidden() && crntImage.overlaps (missile))
	 	{
	 	 	/*
	 	 	 * the space ship is hit, hide it from view and create the new image
	 	 	 * showing the dead space ship
	 	 	 */

	 	 	missile.hide ();
	 	 	crntImage.hide ();
	 	 	VisibleImage dead = new VisibleImage (deadAlien, crntImage.getX (),
	 	 										  crntImage.getY (), this.canvas);

	 	 	hitClip.play ();
	 	 	pause (DELAY);
	 	 	dead.removeFromCanvas ();
	 	 	alive--;

	 	 	return true;
	 	 }
	 	 return false;
	}

	/**
	 * Move the visible image the appropriate x,y pixels
	 *
	 * @param dx number of pixels to move on the X axis
	 * @param dy number of pixels to move on the Y axis
	 */

	public void move (double dx, double dy)
	{
		if (crntImage.getImage () == upImage)
		{
			crntImage.setImage (downImage);
		}
		else
		{
			crntImage.setImage (upImage);
		}

		crntImage.moveTo (crntImage.getX () + dx, crntImage.getY () + dy);
	}

	/**
	 * Move the appropriate number of pixels down.
	 */

	public void moveDown ()
	{
		this.move (0.0, DISTANCE);
	}

	/**
	 * Move the appropriate number of pixels left.
	 */

	public void moveLeft ()
	{
		this.move (-DISTANCE, 0.0);
	}

	/**
	 * Move the appropriate number of pixels right.
	 */

	public void moveRight ()
	{
		this.move (DISTANCE, 0.0);
	}

	/**
	 * Determine how many aliens are currently on the screen.
	 *
	 * @return the number of aliens still alive
	 */

	 public static int currentAliens()
	 {
		 return alive;
	 }
}