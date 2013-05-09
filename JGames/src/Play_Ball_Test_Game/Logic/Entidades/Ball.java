package Play_Ball_Test_Game.Logic.Entidades;

import jgame.JGColor;
import jgame.JGObject;
import Play_Ball_Test_Game.Logic.DirectionMove;
import Play_Ball_Test_Game.Logic.Game;

/**
 * Representa una pelota del juego.
 * 
 * @author Kazuya
 */
public class Ball extends JGObject implements Entidad
{
	//Veriables de Instancia
	private JGColor color; //Color de la ball.
	private double[] dv; //Vector director de movimiento de la ball.
	private int v; //Velocidad de la ball.
	
	/* CONSTRUCTORES */
	
	/**
	 * Inicializa una Ball con un determinado color.
	 * 
	 * @param game Objeto Game que crea el nuevo objeto Ball.
	 */
	public Ball (Game game, double setposx, double setposy, double[] setDirection, int setVelocidad)
	{
		super ( "ball", // nombre del objeto
				true,   // true = agrega un numero único luego del nombre del objeto
			     		// Tuto	->	Si esto no se realiza, el objeto será reemplazado por otro del mismo nombre
				setposx,  // pos random X
				setposy, // pos random Y
				1, 		// cid (Collision ID)
				"Ball-3D-00" // Nomnbre del Sprite o Animación (null = ninguno)
			  );
		
		//Seleccionar velocidad y dirección de movimiento seudo-random.
		v = setVelocidad;
		dv = setDirection;
		xspeed = dv[0] * v;
		yspeed = dv[1] * v;
		
		//Selección seudo-random del color de la Ball.
		color = new JGColor (eng.random(0, 255,40), eng.random(0, 255,40), eng.random(0, 255,40), 100);
	}
	
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
				game.random (0, game.pfWidth()/2 -25),  // pos random X
				game.random (0, game.pfHeight()/2 -25), // pos random Y
				1, 		// cid (Collision ID)
				"Ball-3D-00" // Nomnbre del Sprite o Animación (null = ninguno)
			  );
		
		//Seleccionar velocidad y dirección de movimiento seudo-random.
		v = eng.random(1,10,1);
		dv = DirectionMove.getRandomDirection();
		xspeed = dv[0] * v;
		yspeed = dv[1] * v;
		
		//Selección seudo-random del color de la Ball.
		color = new JGColor (eng.random(0, 255,50), eng.random(0, 255,50), eng.random(0, 255,50), 100);
	}
	
	/* COMANDOS */
	
	/* CALL FROM moveObjects */
	
	/**
	 * 
	 * Tuto	->	actualización del objeto en cada frame.
	 */
	public void move ()
	{
		hited = false;
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
	 * Método llamado cuando this (el objeto actual) es colisionado por otro objeto.
	 *  
	 * @param obj obj colisionado.
	 * 
	 * Tuto	->	llamado cuando otro objeto colisiona con el actual.
	 */
	public void hit (JGObject obj)
	{
		int objv = ((Entidad)obj).getV();
		double[] objDV = ((Entidad)obj).getDV();
		double[] olddv = dv.clone();
		if (v*2 < objv)//La ball obj va a mayor velocidad que a la ball actual, entonces la ball obj rebota y reduce su velocidad, mientras q la actual gana algo de velocidad.
		{
			//Resto de velocidad y modifica la velocidad a obj
			setV(objv/2 - v/2);
			((Entidad)obj).setV(objv/2);
			//Cambiar Dirección (rebote)
			setDV(objDV);
			((Entidad)obj).setDV(olddv);
			hited = true;
		}
		else
			if (!(v > objv*2))//Ninguna de las balls es suficientemente más veloz que la otra como para probocar que la otra tenga mayor velocidad luego del rebote.
				if (v == objv)
				{
					//Acomodar posición final.
					if (Math.abs(dv[0]) + Math.abs(dv[1]) != 1)
					{
						x += 3 * dv[0];
						y += 3 * dv[1];
					}
					if (Math.abs(objDV[0]) + Math.abs(objDV[1]) != 1)
					{
						obj.x += 2 * objDV[0];
						obj.y += 2 * objDV[1];
					}
					//Velocidad en cero.
					setV(0);
					((Entidad)obj).setV(0);
				}
				else
				{
					//Resto de velocidad
					setV(v/2);
					((Entidad)obj).setV(objv/2);
					//Cambiar Dirección (rebote)
					setDV(objDV);
					((Entidad)obj).setDV(dv);
					hited = true;
				}
	}
	
	/* CALL WHEN CHECK BG COLLISION */
	
	/**
	 * Método llamado cuando this (el objeto actual) colisiona con el BG o con un Tile.
	 * 
	 * @param tilecid CID del BG o Tile colisionado.
	 */
	public void hit_bg (int tilecid)
	{
		if ((v == 0) // Si la velocidad es cero, entonces saltear.
		||	(!and(checkBGCollision(xspeed,yspeed),2)) // Si la dirección actual no hace que this choque, saltear.
		/*Otras posibilidades*/)
			return;
		
		if (x + xspeed <= 10)
			if (y + yspeed <= 10)
				dv = DirectionMove.getOppositeDirection(dv);
			else
				if (y + yspeed >= (eng.pfHeight() - 30))
					dv = DirectionMove.getOppositeDirection(dv);
				else
					dv = DirectionMove.getHitSideDirection(dv);
		else
			if (y + yspeed <= 10)
				if (x + xspeed >= (eng.pfWidth() - 30))
					dv = DirectionMove.getOppositeDirection(dv);
				else
					dv = DirectionMove.getHitHorizontalDirection(dv);
			else
			{
				if (x + xspeed >= (eng.pfWidth() - 30))
					if (y + yspeed >= (eng.pfHeight() - 30))
						dv = DirectionMove.getOppositeDirection(dv);
					else
						dv = DirectionMove.getHitSideDirection(dv);
				else
					if (y + yspeed >= (eng.pfHeight() - 30))
						dv = DirectionMove.getHitHorizontalDirection(dv);
			}
		
		//Nueva velocidad con el rebote
		setV(v - 1);
		
		if (v <= 0)
		{//Acomodar posición al frenar
			x += dv[0];
			y += dv[1];
			v = 0;
		}
	}
	
	/**
	 * Modifica el vector de dirección de la ball.
	 * 
	 * @param newDV Nuevo vector de dirección de la ball.
	 * @return Vector de dirección viejo.
	 */
	public double[] setDV (double[] newDV)
	{
		double[] r = dv;
		dv = newDV;
		xspeed = dv[0] * v;
		yspeed = dv[1] * v;
		return r;
	}
	
	/**
	 * Modifica la velocidad de la ball.
	 * 
	 * @param newV Nueva velocidad de la ball.
	 * @return Velocidad vieja.
	 */
	public int setV (int newV)
	{
		int r = v;
		if (newV <= 0)
		{
			v = 0;
			dv[0] = 0;
			dv[1] = 0;
		}
		else
			v = newV;
		xspeed = dv[0] * v;
		yspeed = dv[1] * v;
		return r;
	}
	
	/*CONSULTAS*/
	
	/**
	 * Devuelve el vector de dirección de la ball.
	 * 
	 * @return Vector de dirección.
	 */
	public double[] getDV ()
	{
		return dv;
	}
	
	/**
	 * Devuelve velocidad de la ball.
	 * 
	 * @return Velocidad de la ball.
	 */
	public int getV ()
	{
		return v;
	}

}