package Play_Ball_Test_Game.lib;

/**
 * Métodos útiles para varias clases.
 * 
 * @author Kazuya
 */
public class utils
{
	
	/**
	 * @param x Double number
	 * @return 1: si x >= 0
	 * 		  -1: si x <  0
	 */
	public static int signo (double x)
	{
		return signo ((int) x);
	}
	
	/**
	 * @param x int number
	 * @return 1: si x >= 0
	 * 		  -1: si x <  0
	 */
	public static int signo (int x)
	{
		if (x >= 0)
			return 1;
		else
			return -1;
	}

}