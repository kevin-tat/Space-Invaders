/*
 * import statements
 */

import objectdraw.ActiveObject;
import objectdraw.DrawingCanvas;
import objectdraw.FilledRect;

import java.awt.Color;
import java.awt.Image;

/*
 * class declaration
 */

/**
 * This is the Missile class. It will contain all the information relevant
 * for the missile including the actions the missile will perform.
 *
 * @author Kevin Tat
 * @version Lab 07
 */

public class Missile extends ActiveObject
{
 	/*
	 * constants
	 */

	private static final double DISTANCE = 5.0;
	private static final int DELAY = 33;

 	/*
	 * instance fields
	 */

	private FilledRect theMissile;			// the missile being shot
	private SpaceShip theShip;
	private Invaders armada;

	/**
	 * Missile constructor.
	 *
	 * @param x the X coordinate where to create the missile
	 * @param y the Y coordinate where to create the missile
	 * @param invaders the invaders associated with the game
	 * @param spaceShip the space ship associated with the game
	 * @param canvas the canvas in which the image is created
	 */

	public Missile (double x, double y, Invaders invaders, SpaceShip spaceShip,
					DrawingCanvas canvas)
	{
		/*
		 * remember the values of relevant variables
		 */

		theShip = spaceShip;
		armada = invaders;

		/*
		 * construct the actual missile
		 */

		theMissile = new FilledRect (x, y, 2, 10, canvas);
		theMissile.setColor (Color.RED);


		start ();
	}

	/**
	 * Move the filled rect the appropriate x,y pixels
	 *
	 * @param dx number of pixels to move on the X axis
	 * @param dy number of pixels to move on the Y axis
	 */

	public void move (double dx, double dy)
	{
		theMissile.moveTo (theMissile.getX () + dx, theMissile.getY () + dy);
	}

	/**
	 * Move the appropriate number of pixels up.
	 */

	public void moveUp ()
	{
		this.move (0.0, -DISTANCE);
	}

	/**
	 * Move the missile - execute it's behavior
	 */

	public void run ()
	{
		/*
		 * missile will go up until it gets to the top of the screen or hits one of
		 * the invaders or the space ship
		 */

		while (((theMissile.getY () + theMissile.getHeight ()) >
				SpaceInvaders.TOP_BORDER) && (!theShip.isHit(theMissile)) && (!armada.isHit (theMissile)))
		{
			this.moveUp ();
			pause (DELAY);
		}
		theMissile.removeFromCanvas ();
	}
}