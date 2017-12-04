/*
 * import statements
 */

import objectdraw.ActiveObject;
import objectdraw.DrawingCanvas;
import objectdraw.FilledRect;

import java.awt.Image;
import java.applet.AudioClip;
import java.util.Random;
import javax.swing.JApplet;

/*
 * class declaration
 */

/**
 * This is the Invaders class.  It's a container class that will perform
 * actions relevant to all of the aliens in the game.
 *
 * @author Kevin Tat
 * @version Lab 07
 */

public class Invaders extends ActiveObject
{
	/*
	 * constants
	 */

	private static final int DELAY = 50;
	private static final int COLUMNS = SpaceInvaders.RIGHT_BORDER / 50;
	private static final int LEFT = -1;
	private static final int RIGHT = 1;
	private static final int ROWS = 3;
	private static final double CHANCE_OF_BOMBING = 0.02;

	/*
	 * instance fields
	 */

	private Alien[][] aliens;		// the matrix containing the aliens
	private AudioClip moveClip;		// the sound aliens make when they move
	private LaserBase theBase;		// the laser base in the program

	/*
	 * class fields
	 */
	 private static boolean land = false;

	/**
	 * Invaders constructor with images for the different alien views as
	 * well as one being hit.
	 *
	 * @param topDown image of the top row alien with hands down
	 * @param topUp image of the top row alien with hands up
	 * @param midDown image of the top row alien with hands down
	 * @param midUp image of the top row alien with hands up
	 * @param botDown image of the top row alien with hands down
	 * @param botUp image of the top row alien with hands up
	 * @param hit the image to represent a dead alien
	 * @param canvas the canvas in which the image is created
	 */

	public Invaders (Image topDown, Image topUp, Image midDown, Image midUp,
					 Image botDown, Image botUp, Image hit, DrawingCanvas canvas)
	{
		/*
		 * create and initialize the data arrays
		 */

		int[] points = {30, 20, 10};

		Image[][] image = {{topDown, topUp},
						   {midDown, midUp},
						   {botDown, botUp}};

		/*
		 * create the arrays that will contain aliens
		 */

		aliens = new Alien[ROWS][COLUMNS];

		for (int row = 0; row < aliens.length; row++)
		{
			for (int col = 0; col < aliens[0].length; col++)
			{
				aliens[row][col] = new Alien (image[row][0], image[row][1], hit,
											  col * 30.0, (row + 2) * 30.0,
											  points[row], canvas);
			}
		}

		/*
		 * initialize laser base
		 */

		theBase = null;

		/*
		 * set up the sound file for alien movement
		 */

		Class metaObject = getClass ();
		moveClip = JApplet.newAudioClip (metaObject.getResource ("move.wav"));

		start ();						// set the object in motion
	}

	/**
	 * Determine if the aliens are at the left border.
	 *
	 * @return true or false depending whether or not any one of the aliens
	 *         is at the left border
	 */

	public boolean atLeftBorder ()
	{
		for (int col = 0; col < aliens[0].length; col++)
		{
			for (int row = 0; row < aliens.length; row++)
			{
				if (aliens[row][col] != null)
				{
					if (aliens[row][col].atLeftBorder ())
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Determine if the aliens are at the right border.
	 *
	 * @return true or false depending whether or not any one of the aliens
	 *         is at the right border
	 */

	public boolean atRightBorder ()
	{
		for (int col = aliens[0].length - 1; col > -1; col--)
		{
			for (int row = 0; row < aliens.length; row++)
			{
				if (aliens[row][col] != null)
				{
					if (aliens[row][col].atRightBorder ())
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Determine if the game is over based on the status of the aliens
	 *
	 * @return true or false depending whether or not any aliens are still alive or
	 *         have landed
	 */

	 public static boolean gameOver()
	 {
		 if (land || Alien.currentAliens() == 0)
		 {
			 return true;
		 }
		 else
		 {
			 return false;
		 }
	 }

	/**
	 * Determine if the aliens have landed.
	 *
	 * @return true or false depending whether or not any one of the aliens
	 *		   has landed
	 */

	public boolean haveLanded ()
	{
		for (int row = aliens.length - 1; row > -1; row--)
		{
			for (int col = 0; col < aliens[0].length; col++)
			{
				if (aliens[row][col] != null)
				{
					if (aliens[row][col].hasLanded ())
					{
						land = true;
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Determine if the alien has other aliens below it
	 *
	 * @param row the row of the current alien
	 * @param col the column of the current alien
	 * @return true or false depending whether or not the alien is blocked
	 */

	 public boolean isBlocked (int row, int col)
	 {
		 if ((row != aliens.length -1) && (aliens[row+1][col] != null))
		 {
			 return true;
		 }
		 else
		 {
			 return false;
		 }
	 }

	/**
	 * Determine if the any of the aliens have been hit by the missile
	 *
	 * @param missile the filled rect representing the missile fired from the laser base
	 * @return true or false depending whether or not the missile hit any of the aliens
	 */
	 public synchronized boolean isHit (FilledRect missile)
	 {
	 	 for (int c = 0; c < aliens[0].length; c++)
	 	 {
			 for (int r = 0; r < aliens.length; r++)
			 {
	 	 		if (aliens[r][c] != null && aliens[r][c].isHit (missile))
	 	 		{
	 	 			 aliens[r][c] = null;

	 	 			 return true;
	 	 		}
			}
		}
	 	return false;
	}
	/**
	 * Move all of the visible aliens down
	 */

	public void moveDown()
	{
		for (int row = aliens.length - 1; row > -1; row--)
		{
			for (int col = 0; col < aliens[0].length; col++)
			{
				if (aliens[row][col] != null)
				{
					aliens[row][col].moveDown();
				}
			}
		}
	}

	/**
	 * Move all of the visible aliens left
	 */

	public void moveLeft()
	{
		for (int col = 0; col < aliens[0].length; col++)
		{
			for (int row = 0; row < aliens.length; row++)
			{
				/*
				 * 2% change the alien will drop a bomb, but only if the
				 * laser base exists
				 */
				if (aliens[row][col] != null)
				{
					if ((Math.random() < CHANCE_OF_BOMBING) && (theBase != null) && (!this.isBlocked(row, col)))
					{
						System.out.println ("alien[" + row + "][" + col +
											"] is bombing");
						aliens[row][col].dropBomb (theBase);
					}
					aliens[row][col].moveLeft();
				}
			}
		}
	}

	/**
	 * Move all of the visible aliens right
	 */

	public void moveRight()
	{
		for (int col = aliens[0].length - 1; col > -1; col--)
		{
			for (int row = 0; row < aliens.length; row++)
			{
				/*
				 * 2% change the alien will drop a bomb, but only if the
				 * laser base exists
				 */
				if (aliens[row][col] != null)
				{
					if ((Math.random() < CHANCE_OF_BOMBING) && (theBase != null) && (!this.isBlocked(row, col)))
					{
						System.out.println ("alien[" + row + "][" + col +
											"] is bombing");
						aliens[row][col].dropBomb (theBase);
					}
					aliens[row][col].moveRight();
				}
			}
		}
	}

	/**
	 * Run the aliens until the game is over
	 */

	public void run ()
	{
		int direction = RIGHT;

		while (!this.haveLanded () && !theBase.gameOver())
		{
			if ((direction == RIGHT) && this.atRightBorder())
			{
				this.moveDown ();
				direction = LEFT;
			}
			else if	((direction == LEFT) && this.atLeftBorder ())
			{
				this.moveDown ();
				direction = RIGHT;
			}
			else if (direction == RIGHT)
			{
				this.moveRight ();
			}
			else
			{
				this.moveLeft ();
			}

			moveClip.play ();
			pause (DELAY);
		}
		System.out.println ("GAME OVER!");
	}

	/**
	 * Set the object upon which the invaders will fire
	 *
	 * @param obj the object being fired upon
	 */

 	public void setTarget (Object obj)
	{
		if (obj instanceof LaserBase)
		{
			theBase = (LaserBase)obj;
		}
	}
}