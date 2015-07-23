package sim.components.basic;


public interface Sink {

	/**
	 * Se conecta a um source para poder puxar, quando 
	 * @param source
	 */
	public void connectFrom ( Source source );
	
	/**
	 * recebe job
	 * @param job objeto a ser enviado para sink
	 */
	public void send(Job job);

	/**
	 * Se um objeto pode ser enviado para sink no momento
	 * @return true se possível, false se não.
	 */
	public boolean canSend();

}
