package Play_Ball_Test_Game.Recursos;

/**
 * Clae usada para obtener la direcci�n correcta de los recursos. 
 * 
 * @author Kazuya
 */
public class getRecurso
{
	
	private static boolean needChangeURL = false;
	
	/*Negar craci�n de objeto*/
	private getRecurso () {}
	
	/**
	 * Devuelve la direcci�n del recurso solicitado.
	 * 
	 * @param dirRecurso Direcci�n interna del recurso dentro del paquete de los recursos.
	 * @return direcci�n del recurso solicitado.
	 */
	public static String getDir (String dirRecurso)
	{
		needChangeURL = true;
		return "/Play_Ball_Test_Game/Recursos/" + dirRecurso;
	}
	
	/**
	 * Indica si es necesario cambiar la url de los recursos.
	 * 
	 * @return indica si es necesario cambiar la url de los recursos.
	 */
	public static boolean needChangeURL ()
	{
		return needChangeURL;
	}

}