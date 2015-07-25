package sim.components.basic;


public interface Sink {

	/**
	 * Source pode enviar Jobs para este objeto
	 * @param source
	 */
	public void connectFrom ( Source source );
	
	/**
	 * envia Job para ser utilizado por este objeto
	 * @param job objeto a ser enviado para sink
	 */
	public void send(Job job);

	/**
	 * Se um objeto pode ser enviado para sink no momento
	 * @return true se possível, false se não.
	 */
	public boolean canSend();

}
