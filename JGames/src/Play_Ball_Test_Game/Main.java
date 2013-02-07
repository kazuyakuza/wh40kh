package Play_Ball_Test_Game;

import jgame.JGPoint;
import Play_Ball_Test_Game.Logic.Game;

/**
 * Main iniciador del juego Play Ball (Test Game)
 * 
 * @author Kazuya
 */
public class Main
{
	//Variables de Clase
	private static int ancho = 640;
	private static int largo = 480;
	
	/**
	 * Inicia el juego en una ventana de tamaño 640x480.
	 * 
	 * @param args
	 */
	public static void main(String [] args)
	{
		new Game (new JGPoint (ancho, largo));
	}

}