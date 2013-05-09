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
		if ((x >= 0.0) && !(x < 0))
			return 1;
		else
			return -1;
	}
	
	/**
	 * @param x int number
	 * @return 1: si x >= 0
	 * 		  -1: si x <  0
	 */
	public static int signo (int x)
	{
		return signo((double) x);
	}

}