/*
 * import statements
 */

import objectdraw.ActiveObject;
import objectdraw.DrawingCanvas;
import objectdraw.FilledRect;
import objectdraw.VisibleImage;

import java.applet.AudioClip;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JApplet;
import java.util.ArrayList;

/*
 * class declaration
 */

/**
 * This is the LaserBase class. It will contain all the information relevant
 * to the laser base such as movement, shooting, and being hit.
 *
 * @author Kevin Tat
 * @version Lab 07
 */

public class LaserBase extends ActiveObject implements KeyListener
{
 	/*
	 * constants
	 */

	private static final int DELAY = 300;
	private static final double DISTANCE = 5.0;
	private static final int LIVES = 6;

 	/*
	 * instance fields
	 */

	private VisibleImage crntImage;			// the .gif image of the laser base
	private Image deadBase;					// the .gif image of the hit laser base
	private DrawingCanvas theCanvas;		// the canvas for the game
	private AudioClip shootClip;			// the sound when base shoots
	private AudioClip hitClip;				// the sound ship makes when it is hit
	private ArrayList<VisibleImage> laserBases;	// the array of extra laser base images
	private Invaders armada;
	private SpaceShip theShip;

 	/*
	 * class fields
	 */

	 private static int lives = 6;

	/**
	 * Laser base constructor with the image and the aliens that it can shoot.
	 *
	 * @param image the base image that will appear on the canvas
	 * @param hitImage the base image when it is hit
	 * @param keyListener the component to use to listen to key events
	 * @param canvas the canvas in which the image is created
	 */

    public LaserBase (Image image, Image hitImage, Component keyListener,
    				  DrawingCanvas canvas)
    {
		canvas.addKeyListener (this);		// add key listener to the canvas
	  	keyListener.addKeyListener (this);	// add key listener to the active window
	  	keyListener.setFocusable (true);	// inform system that our applet gains focus

		/*
		 * remember the values of relevant variables
		 */

		theCanvas = canvas;
		deadBase = hitImage;

		/*
		 * create the image for the laser base
		 */

		crntImage = new VisibleImage (image, SpaceInvaders.RIGHT_BORDER / 2 - 13,
									  SpaceInvaders.BOTTOM_BORDER - 20, canvas);

		/*
		 * create the extra laser base images
		 */

		laserBases = new ArrayList<VisibleImage> ();

		for (int x = 0; x < LIVES - 1; x++)
		{
			laserBases.add (new VisibleImage (image, x * (crntImage.getWidth() + 10),
											  SpaceInvaders.BOTTOM_BORDER + 20, canvas));
		}

		/*
		 * set up the sound file for a laser base shot
		 */

		Class metaObject = getClass ();
		shootClip = JApplet.newAudioClip (metaObject.getResource ("baseShoot.wav"));

		/*
		 * set up the sound file for when a laser base is hit
		 */

		hitClip = JApplet.newAudioClip (metaObject.getResource("shipHit.wav"));
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
	 * Determine if the game is over based on the number of laser bases left
	 *
	 * @return true or false depending whether or not any laser bases are still alive
	 */

	 public static boolean gameOver ()
	 {
		 if (lives == 0)
		 {
		 	return true;
		 }
		 else
		 {
			 return false;
	 	 }
	 }

	/**
	 * Determine if the laser base has been hit by the bomb
	 *
	 * @param bomb the filled rectangle representing the bomb dropped by an alien
	 * @return true or false depending whether or not the bomb hit the laser base
	 */

	public synchronized boolean isHit (FilledRect bomb)
	{
		if (!crntImage.isHidden() && crntImage.overlaps (bomb))
		{
			/*
			 * the laser base is hit, hide it from view and create the new image
			 * showing the dead laser base
			 */

			bomb.hide ();
			crntImage.hide ();
			VisibleImage dead = new VisibleImage(deadBase, crntImage.getX (),
												 crntImage.getY (), theCanvas);

			hitClip.play ();
			pause (DELAY);
			dead.removeFromCanvas ();

			/*
			 * remove an extra laser base from the bottom of screen if the game is not
			 * over
			 */

			if (laserBases.size() != 0)
			{

				laserBases.remove (laserBases.size() - 1).removeFromCanvas ();
				crntImage.show ();
			}

			lives--;

			return true;
		}
		return false;
	}

	/**
	 * Activates action when a key is pressed.
	 *
	 * @param e the key that was activated
	 */

	public void keyPressed (KeyEvent e)
	{
		if (lives != 0 && !Invaders.gameOver())
		{
			if ((e.getKeyCode () == KeyEvent.VK_LEFT) && !this.atLeftBorder ())
			{
				this.moveLeft ();
			}
			else if ((e.getKeyCode () == KeyEvent.VK_RIGHT) && !this.atRightBorder ())
			{
				this.moveRight ();
			}
			else if (e.getKeyCode () == KeyEvent.VK_SPACE)
			{
				this.shoot ();
			}
		}
	}

	/**
	 * Activates action when a key is released.
	 *
	 * @param e the key that was activated
	 */

	public void keyReleased (KeyEvent e)
	{
	}

	/**
	 * Activates action when a key is pressed and released.
	 *
	 * @param e the key that was activated
	 */

	public void keyTyped (KeyEvent e)
	{
	}

	/**
	 * Move the visible image the appropriate x,y pixels
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
	 * Set the object upon which the laser base will fire
	 *
	 * @param obj the object being fired upon
	 */
	 public void setTarget (Object obj)
	 {
	 	if (obj instanceof Invaders)
	 	{
	 		armada = (Invaders)obj;
	 	}
	 	else if (obj instanceof SpaceShip)
	 	{
			theShip = (SpaceShip)obj;
		}
	 }

  	/**
   	 * Create a new missile and play the shoot sound
   	 */

	public void shoot ()
	{
		new Missile (crntImage.getX () + crntImage.getWidth () / 2 - 1,
					 crntImage.getY () - 10, armada, theShip, theCanvas);
		shootClip.play ();
	}
}
