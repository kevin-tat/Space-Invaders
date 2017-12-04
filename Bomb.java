/*
 * import statements
 */

import objectdraw.ActiveObject;
import objectdraw.DrawingCanvas;
import objectdraw.FilledRect;

import java.awt.Color;

/*
 * class declaration
 */

/**
 * This is the Bomb class. It will contain all the information relevant for the
 * bomb including the actions the bomb will perform.
 *
 * @author Kevin Tat
 * @version Lab 07
 */

public class Bomb extends ActiveObject
{
 	/*
	 * constants
	 */

	private static final int DISTANCE =  3;
	private static final int DELAY = 33;

 	/*
	 * instance fields
	 */

	private FilledRect theBomb;			// the bomb being dropped
	private LaserBase theBase;			// the laser base for the game

	/**
	 * Bomb constructor.
	 *
	 * @param x the X coordinate where to create the bomb
	 * @param y the Y coordinate where to create the bomb
	 * @param base the laser base associated with the game
	 * @param canvas the canvas in which the image is created
	 */

	public Bomb (double x, double y, LaserBase base, DrawingCanvas canvas)
	{
		/*
		 * construct the actual bomb
		 */

		theBomb = new FilledRect(x, y, 2, 10, canvas);
		theBomb.setColor(Color.BLUE);

		/*
		 * remember the values of relevant variables
		 */

		theBase = base;

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
		theBomb.moveTo (theBomb.getX () + dx, theBomb.getY () + dy);
	}

	/**
	 * Move the appropriate number of pixels down.
	 */

	public void moveDown ()
	{
		this.move (0.0, DISTANCE);
	}

	/**
	 * Move the bomb - execute it's behavior
	 */

	public void run ()
	{
		/*
		 * bomb will go down until it gets to the bottom of the screen or hits the
		 * laser base
		 */

		while (((theBomb.getY () + theBomb.getHeight ()) <
				SpaceInvaders.BOTTOM_BORDER) && !theBase.isHit (theBomb))
		{
			this.moveDown();
			pause (DELAY);
		}
		theBomb.removeFromCanvas ();
	}
}