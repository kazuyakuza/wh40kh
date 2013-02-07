package Play_Ball_Test_Game.Logic.Entidades;

import jgame.JGColor;
import jgame.JGObject;
import Play_Ball_Test_Game.Logic.Game;

/**
 * Representa una pelota del juego.
 * 
 * @author Kazuya
 */
public class Ball extends JGObject
{
	//Veriables de Instancia
	private JGColor color; //Color de la ball.
	
	/* CONSTRUCTORES */
	
	/**
	 * Inicializa una Ball con un determinado color.
	 * 
	 * @param game Objeto Game que crea el nuevo objeto Ball.
	 */
	public Ball (Game game)
	{
		super ( "ball", // nombre del objeto
				true,   // true = agrega un numero único luego del nombre del objeto
			     		// Tuto	->	Si esto no se realiza, el objeto será reemplazado por otro del mismo nombre
				game.random (0, game.pfWidth()-20),  // pos random X
				game.random (0, game.pfHeight()-20), // pos random Y
				1, 		// cid (Collision ID)
				"Ball-3D-00" // Nomnbre del Sprite o Animación (null = ninguno)
			  );
		xspeed = eng.random(-4,4);
		yspeed = eng.random(-4,4);
		
		color = new JGColor (eng.random(0, 255,20), eng.random(0, 255,20), eng.random(0, 255,20), 100);
	}
	
	/* COMANDOS */
	
	/* CALL FROM moveObjects */
	
	/**
	 * 
	 * Tuto	->	actualización del objeto en cada frame.
	 */
	public void move ()
	{
		//Parche para colision con esquinas.
		if ((x >= 0) && (x <= 1) || (x >= eng.pfWidth()-21) && (x <= eng.pfWidth()-20)
		 && (y >= 0) && (y <= 1) || (y >= eng.pfHeight()-21) && (y <= eng.pfHeight()-20))
		{
			hit_bg (2);
		}
	}
	
	/* CALL IN HEACH FRAME */
	
	/**
	 * Dibujo de la Ball.
	 */
	public void paint ()
	{
		eng.drawOval( x+10,	// Posición x del campo de juego
					  y+10,	// Posición y del campo de juego
					  18,	// Ancho
					  18,	// Alto
					  true,	// Con Relleno
					  true,// Centrado en la posición
					  1,
					  color);
	}
	
	/* CALL WHEN CHECK COLLISION */

	/**
	 * Tuto	->	llamado cuando otro objeto colisiona con el actual.
	 */
	public void hit (JGObject obj)
	{
		// As a reaction to an object collision, we bounce in the
		// direction we came from.  We only do this when the area in that
		// direction seems clear of other objects, otherwise we might
		// start oscillating back and forth.
		// This collision problem is much more difficult than the tile
		// collision problem, because there may be multiple simultaneous
		// collisions, and the other objects are also moving at different
		// speeds.
		// We look ahead several steps in the opposite direction to see
		// if any other object is there.
		
		if (checkCollision (1, -3*xspeed, -3*yspeed) == 0)
		{
			// reverse direction
			xspeed = -xspeed;
			yspeed = -yspeed;
		}
	}
	
	/* CALL WHEN CHECK BG COLLISION */
	
	/** Handle collision with background. Called by checkBGCollision.
	* Tilecid is the combined (ORed) CID of all tiles that this
	* object collides with.  Note: there are two other variants
	* of hit_bg available, namely one passing tilecid plus tile
	* coordinates for each tile that the object collides with, and one
	* passing the tile range that the object overlaps with at the moment
	* of collision.  */
	public void hit_bg (int tilecid) {
		// Look around to see which direction is free.  If we find a free
		// direction, move that way.
		
		if (!and(checkBGCollision(-xspeed,yspeed),3)) {
			xspeed = -xspeed;
		} else if (!and(checkBGCollision(xspeed,-yspeed),3)) {
			yspeed = -yspeed;
		} else if (!and(checkBGCollision(xspeed,-yspeed),3)
		&&         !and(checkBGCollision(-xspeed,-yspeed),3) ) {
			xspeed = -xspeed;
			yspeed = -yspeed;
		}
		// else do nothing. You might think this case never occurs
		// (otherwise, why would the object have collided?), but it
		// does occur because object-object collision might already
		// have reversed the direction of this object.  This is the kind
		// of stuff that makes object interaction difficult.
	}

}