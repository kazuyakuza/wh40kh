package Play_Ball_Test_Game.Logic.Entidades;

import java.util.ArrayList;
import java.util.Hashtable;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import jgame.JGColor;
import jgame.JGFont;
import jgame.JGObject;
import Play_Ball_Test_Game.Logic.DirectionMove;
import Play_Ball_Test_Game.Logic.Game;
import Play_Ball_Test_Game.lib.utils;

/**
 * Representa una pelota del juego.
 * 
 * @author Kazuya
 */
public class Ball extends JGObject implements Entidad
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
				game.random (0, game.pfWidth()-25),  // pos random X
				game.random (0, game.pfHeight()-25), // pos random Y
				1, 		// cid (Collision ID)
				"Ball-3D-00" // Nomnbre del Sprite o Animación (null = ninguno)
			  );
		
		//Seleccionar velocidad y dirección de movimiento seudo-random.
		int randomDir = eng.random(1,DirectionMove.getDirMove().getMyMaxDirections("Ball-3D-00"),1);
		int aux = 1;
		for (double[] xy: DirectionMove.getDirMove().getMyDirections("Ball-3D-00").values())
		{
			if (aux == randomDir)
			{
				xspeed = eng.random(1,4,1) * xy[0];
				yspeed = eng.random(1,4,1) * xy[1];
				break;
			}
			aux++;
		}
		
		//Selección seudo-random del color de la Ball.
		color = new JGColor (eng.random(0, 255,40), eng.random(0, 255,40), eng.random(0, 255,40), 100);
	}
	
	/* COMANDOS */
	
	/* CALL FROM moveObjects */
	
	/**
	 * 
	 * Tuto	->	actualización del objeto en cada frame.
	 */
	public void move ()
	{
	}
	
	/* CALL IN HEACH FRAME */
	
	/**
	 * Color de la Ball.
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
	 * Método llamado cuando this (el objeto actual) colisiona con otro objeto.
	 * 
	 * @param obj obj colisionado.
	 * 
	 * Tuto	->	llamado cuando otro objeto colisiona con el actual.
	 */
	@SuppressWarnings("unchecked")
	public void hit (JGObject obj)
	{
		if (xspeed == 0 && yspeed == 0)
			return;
		
		ArrayList<double[]> posNewDirection = (ArrayList<double[]>) new ArrayList ();
		for (double[] xy: DirectionMove.getDirMove().getMyDirections("Ball-3D-00").values())
			if (checkCollision(obj.colid,(xspeed * xy[0]) + utils.signo(xy[0]),
									     (yspeed * xy[1]) + utils.signo(xy[1])) == 0)
				posNewDirection.add(xy);		
		
		if (! posNewDirection.isEmpty())
		{
			double[] xy = posNewDirection.get(eng.random(0,posNewDirection.size()-1,1));
			xspeed = (xspeed * xy[0]) + utils.signo(xy[0]);
			yspeed = (yspeed * xy[1]) + utils.signo(xy[1]);
		}
	}
	
	/* CALL WHEN CHECK BG COLLISION */
	
	/**
	 * Método llamado cuando this (el objeto actual) colisiona con el BG o con un Tile.
	 * 
	 * @param tilecid CID del BG o Tile colisionado.
	 */
	@SuppressWarnings("unchecked")
	public void hit_bg (int tilecid)
	{
		if (xspeed == 0 && yspeed == 0)
			return;
		
		ArrayList<double[]> posNewDirection = (ArrayList<double[]>) new ArrayList ();
		for (double[] xy: DirectionMove.getDirMove().getMyDirections("Ball-3D-00").values())
			if (!and(checkBGCollision((xspeed * xy[0]) + utils.signo(xy[0]),
									   (yspeed * xy[1]) + utils.signo(xy[1])),2))
				posNewDirection.add(xy);		
		
		if (! posNewDirection.isEmpty())
		{
			double[] xy = posNewDirection.get(eng.random(0,posNewDirection.size()-1,1));
			xspeed = (xspeed * xy[0]) + utils.signo(xy[0]);
			yspeed = (yspeed * xy[1]) + utils.signo(xy[1]);
		}
		else
		{
			while (and(checkBGCollision(0,0),2))
			{
				x -= utils.signo (x);
				y -= utils.signo (y);
			}
			xspeed = yspeed = 0;
		}
	}

}