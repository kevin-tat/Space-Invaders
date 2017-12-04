/*
 * import statements
 */

import objectdraw.FilledRect;
import objectdraw.Line;
import objectdraw.WindowController;

import java.awt.Color;
import java.awt.Image;

/*
 * class declaration
 */

/**
 * This program is designed to simulate the game of space invaders. It will
 * create all of the necessary images and objects relevant to the game.
 *
 * @author Kevin Tat
 * @version Lab 07
 */

public class SpaceInvaders extends WindowController
{
	/*
	 * constants
	 */

	public static final int LEFT_BORDER = 0;
	public static final int RIGHT_BORDER = 400;

	public static final int TOP_BORDER = 0;
	public static final int BOTTOM_BORDER = 300;

	public static final int BASEMENT = 50;

	/*
	 * Create the images and any other objects necessary to run the game.
	 */

	public void begin ()
	{
		/*
		 * create the canvas for the game
		 */

		new FilledRect (LEFT_BORDER, TOP_BORDER, RIGHT_BORDER,
						BOTTOM_BORDER + BASEMENT, canvas);
		this.resize (RIGHT_BORDER, BOTTOM_BORDER + BASEMENT);

		/*
		 * create the line seperator for active laser base and remaining lives
		 */

		Line line = new Line (SpaceInvaders.LEFT_BORDER, SpaceInvaders.BOTTOM_BORDER,
							  SpaceInvaders.RIGHT_BORDER, SpaceInvaders.BOTTOM_BORDER,
							  canvas);
		line.setColor (Color.CYAN);

		/*
		 * create the image of the laser base
		 */

		Image siBaseImage = getImage ("base.gif");
		Image siBaseBlast = getImage ("baseBlast.gif");

		LaserBase base = new LaserBase (siBaseImage, siBaseBlast, this,	canvas);

		/*
		 * create the images of the aliens and the hit alien
		 */

		Image siTopDown = getImage ("top0.gif");
		Image siTopUp = getImage ("top1.gif");

		Image siMiddleDown = getImage ("middle0.gif");
		Image siMiddleUp = getImage ("middle1.gif");

		Image siBottomDown = getImage ("bottom0.gif");
		Image siBottomUp = getImage ("bottom1.gif");

		Image siHitAlien = getImage ("invaderBlast.gif");

		/*
		 * Create the invaders using the alien images
		 */

		Invaders invaders = new Invaders (siTopDown, siTopUp, siMiddleDown, siMiddleUp,
										  siBottomDown, siBottomUp, siHitAlien, canvas);

		/*
		 * create the image of the space ship
		 */

		Image siSpaceShip = getImage ("spaceShip.gif");

		SpaceShip ship = new SpaceShip (siSpaceShip, siHitAlien, canvas);

		/*
		 * Set targets
		 */

		invaders.setTarget (base);
		base.setTarget (ship);
		base.setTarget (invaders);
	}
}