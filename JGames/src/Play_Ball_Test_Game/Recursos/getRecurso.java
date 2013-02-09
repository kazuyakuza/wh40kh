package Play_Ball_Test_Game.Recursos;

/**
 * Clae usada para obtener la dirección correcta de los recursos. 
 * 
 * @author Kazuya
 */
public class getRecurso
{
	
	private static boolean needChangeURL = false;
	
	/*Negar cración de objeto*/
	private getRecurso () {}
	
	/**
	 * Devuelve la dirección del recurso solicitado.
	 * 
	 * @param dirRecurso Dirección interna del recurso dentro del paquete de los recursos.
	 * @return dirección del recurso solicitado.
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