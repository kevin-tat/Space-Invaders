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
 * This is the SpaceShip class. It will contain all the information relevant for the
 * space ship including the actions the space ship will perform and the images
 * associated with the space ship.
 *
 * @author Kevin Tat
 * @version Lab 07
 */

public class SpaceShip extends ActiveObject
{
	/*
	 * constants
	 */

	private static final double DISTANCE = 10.0;
	private static final int DELAY = 150;

	/*
	 * instance fields
	 */

	private AudioClip moveClip;			// the sound ship makes when it moves
	private AudioClip hitClip;			// the sound ship makes when it is hit
	private VisibleImage crntImage;		// current image visible on the coanvas
	private int points;					// the point value of a space ship
	private Image deadShip;				// the image of the dead ship
	private DrawingCanvas theCanvas;
	private LaserBase base;

	/**
	 * Space ship constructor with the two different images that will simulate movement
	 * and another to simulate the space ship being hit.
	 *
	 * @param image the image of the space ship that will appear on the canvas
	 * @param hitImage the image to represent hit space ship
	 * @param canvas the canvas in which the image is created
	 */

	public SpaceShip (Image image, Image hitImage, DrawingCanvas canvas)
	{
		/*
		 * save the relevant variables
		 */

		/*
		 * create and hide the space ship
		 */

		theCanvas = canvas;
		crntImage = new VisibleImage (image, 0.0, 30.0, canvas);
		points = 0;
		crntImage.hide ();
		deadShip = hitImage;

		/*
		 * set up the sound file for a moving space ship
		 */

		Class metaObject = getClass ();
		moveClip = JApplet.newAudioClip (metaObject.getResource ("spaceShip.wav"));

		/*
		 * set up the sound file for a hit space ship
		 */

		hitClip = JApplet.newAudioClip (metaObject.getResource ("shipHit.wav"));

		start ();			// set the object in motion
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
	 * Determine if the space ship has been hit by the missile
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
	 			VisibleImage dead = new VisibleImage (deadShip, crntImage.getX (),
	 												  crntImage.getY (), theCanvas);

	 			hitClip.play ();
	 			pause (DELAY);
	 			dead.removeFromCanvas ();

	 			System.out.println("You earned " + (int)(Math.random() * 39 + 2) * 25);

	 			return true;
	 		}
	 		return false;
	}


	/**
	 * Move the current image the appropriate x,y pixels
	 *
	 * @param dx number of pixels to move on the X axis
	 * @param dy number of pixels to move on the Y axis
	 */

	public void move (double dx, double dy)
	{
		crntImage.moveTo (crntImage.getX () + dx, crntImage.getY () + dy);
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
	 * Run the space ship - execute it's behavior
	 */

	public void run ()
	{
		while (!base.gameOver() && !Invaders.gameOver())
		{
			/*
			 * determine how long before the next space ship appears
			 */

			//pause ((int)(Math.random() * 4001) + 1000);
			crntImage.show ();

			/*
			 * create the point value of the current space ship
			 */

			points = (int)(Math.random() * 39 + 2) * 25;

			/*
			 * play the sound clip until the ship is hit or goes off the screen
			 */

			moveClip.loop ();

			/*
			 * determine if the space ship appears on the left side and moves right
			 * or on the right side and moves left
			 */

			if (Math.random() >= 0.50)
			{
				crntImage.moveTo (SpaceInvaders.LEFT_BORDER, 30.0);

				while (!this.atRightBorder ())
				{
					this.moveRight ();
					pause (DELAY);
				}
			}
			else
			{
				crntImage.moveTo (SpaceInvaders.RIGHT_BORDER - crntImage.getWidth (),
								  30.0);
				while (!this.atLeftBorder ())
				{
					this.moveLeft ();
					pause (DELAY);
				}
			}

			/*
			 * hide the ship and stop the music
			 */

			crntImage.hide ();
			moveClip.stop ();
		}
	}
}