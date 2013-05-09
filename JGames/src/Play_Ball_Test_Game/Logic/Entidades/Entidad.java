package Play_Ball_Test_Game.Logic.Entidades;

/**
 * Representa una Entidad del Juego.
 * 
 * @author Kazuya
 */
public interface Entidad
{
	
	/*COMANDOS*/
	
	/**
	 * Modifica el vector de dirección de la Entidad.
	 * 
	 * @param newDV Nuevo vector de dirección de la Entidad.
	 * @return Vector de dirección viejo.
	 */
	public double[] setDV (double[] newDV);
	
	/**
	 * Modifica la velocidad de la Entidad.
	 * 
	 * @param newV Nueva velocidad de la Entidad.
	 * @return Velocidad vieja.
	 */
	public int setV (int newV);
	
	/*CONSULTAS*/
	
	/**
	 * Devuelve el vector de dirección de la Entidad.
	 * 
	 * @return Vector de dirección.
	 */
	public double[] getDV ();
	
	/**
	 * Devuelve velocidad de la Entidad.
	 * 
	 * @return Velocidad de la Entidad.
	 */
	public int getV ();
	
}