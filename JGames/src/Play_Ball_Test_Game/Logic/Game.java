package Play_Ball_Test_Game.Logic;

import java.util.LinkedList;

import jgame.JGColor;
import jgame.JGFont;
import jgame.JGObject;
import jgame.JGPoint;
import jgame.platform.JGEngine;
import Play_Ball_Test_Game.Logic.Entidades.Ball;
import Play_Ball_Test_Game.Recursos.getRecurso;

/**
 * Clase principal del juego Play Ball (Test Game).
 * 
 * 
 * 
 * @author Kazuya
 * @version 1
 */
@SuppressWarnings("serial")
public class Game extends JGEngine
{
	
	//Variables de Clase
	private final double fps = 35;	//frames por segundo
	private final double mfs = 2;	//max frame skip
	
	private LinkedList<JGObject> listaBalls;

	/**
	 * Inicia el juego como un applet.
	 */
	public Game ()
	{
		initEngineApplet();
	}

	/**
	 * Inicia el juego como una aplicación.
	 * 
	 * @param size JGPoint con el ancho y alto de la ventana.
	 */
	public Game (JGPoint size)
	{		
		initEngine(size.x,size.y);
	}
	
	/* 1° TO CALL */
	
	/**
	 * Inicializa el canvas.
	 * 
	 * Canvas:	mapa de fondo del juego, el cual se divide como una matriz de f filas y c columnas.
	 * 			Cada celda (tile) del canvas tiene un tamaño en pixeles de x ancho e y alto.
	 * 
	 * Tuto	->	No ingresar otro método del engine aquí.
	 */
	public void initCanvas ()
	{
		setCanvasSettings(	40,   // c (cantidad de columnas)
							30,   // f (cantidad de filas)
							16,   // x (ancho de cada celda)
							16,   // y (alto de cada celda)
							null, // foreground colour (null = white)
							JGColor.white, // color de fondo (null = black)
							null  // font (null = default font)
						 );
	}
	
	/* CALL WHEN LEVEL GAME START */

	/**
	 * 1. Setea el frame rate y el max frame skip.
	 * 2. Definir border del campo de juego.
	 * 3. Agrega esferas al juego.
	 * 
	 * Tuto	->	Setear frame rate.
	 * Tuto	->	Setear/Cargar Gráficos.
	 * Tuto	->	Crear Objetos del Juego.
	 */
	@SuppressWarnings("unchecked")
	public void initGame ()
	{
		setFrameRate( fps, // fps
					  mfs   // frames máximos a saltear antes de mostrar un frame denuevo
					);
		
		//Define Imagen Básica de las Esferas.
		defineImage( "Ball-3D-00", // nombre del gráfico, usable en todo el juego
		        				   // En caso de usar la imagen para un tile:
                     "-",                // tile name (null = cualquier nombre)
                     0,                  // tile cid
                     getRecurso.getDir("Graficos/Sprites/Ball-3D-00.png"), // nombre del archivo
                     "-"                 // operaciones gráficas extras
				   );
		
		//Define el borde del campo de juego.
		setTileSettings("", 2, 0);
		
		listaBalls = (LinkedList<JGObject>) new LinkedList ();
		
		//for (int i=0; i<10; i++)
			//listaBalls.addLast(new Ball (this));
		listaBalls.addLast(new Ball (this, pfWidth()/2 - 25 - 200, pfHeight()/2 - 25 - 50, DirectionMove.getRightDirection(), 1));
		//listaBalls.addLast(new Ball (this, 0, 0, DirectionMove.getDownRightDirection(), 4));
		listaBalls.addLast(new Ball (this, pfWidth()/2 - 25 + 200, pfHeight()/2 - 25 - 50, DirectionMove.getLeftDirection(), 1));
		//listaBalls.addLast(new Ball (this, pfWidth() - 180, pfHeight() - 20, DirectionMove.getUpLeftDirection(), 4));
	}
	
	/* CALL IN EACH FRAME */
	
	/**
	 * Lógica del juego en cada frame.
	 * 
	 * Tuto	->	aquí se realiza la lógica del juego.
	 * 			(mover objetos, verificar colisiones, etc.)
	 * Tuto	->	no hacer cambios (prints) gráficos aquí.
	 */
	public void doFrame ()
	{
		if (! getKey(JGEngine.KeyEnter))
		{
			moveObjects( null, // prefijo del nombre de los objetos a mover (null = todos)
					 	 0     // cid de los objetos a mover (0 = todos)
			           );
			
			// check obj-obj colisión
			checkCollision( 1, // cids de los objetos que colisionan con los objetos de
							1  // cids de los cuales se llama el método hit ().
					  	  );
			// check obj-tile colisión
			checkBGCollision( 2, // cid del marco (fin del campo de juego)
						  	  1  // cids de los objetos con colisionan con el marco
						    );
			
			// check out of view
			for (JGObject b: listaBalls)
				if (! b.isOnPF(-10, -10))
					listaBalls.remove(b);
		}
	}
	
	/* CALL IN EACH FRAME */
	
	/**
	 * Tuto	->	Cualquier dibujo de gráfico que no sean objetos (entidades) o mapa (canvas), debe hacerse aquí.
	 * 			Usualmente HUD, información, estado, etc.
	 */
	public void paintFrame ()
	{
		drawString("Actually Balls", viewWidth()/2, viewHeight()/2 -20, 0, new JGFont("arial",0,20), JGColor.red);
		drawString("" + listaBalls.size(), viewWidth()/2, viewHeight()/2 +20, 0, new JGFont("arial",0,20), JGColor.red);
	}

}