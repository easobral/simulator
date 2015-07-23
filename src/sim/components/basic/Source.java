package sim.components.basic;


public interface Source {

	/**
	 * Se conecta a um sink
	 * @param sink Objeto para onde enviar os Jobs.
	 */
	public void connectTo ( Sink sink );
	
	/**
	 * 
	 * @return Objeto pendente que ainda não foi enviado.
	 */
	public Job get();
	

	/**
	 * Se o método pull pode no momento enviar um job
	 * @return true se possível, false se não.
	 */
	public boolean canGet();

}
