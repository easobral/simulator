package sim.timer;

/**
 * Representa um evento programado. Quando chegar no tempo
 * programado, o runnable passado será executado. 
 * @author eduardo
 *
 */
public class TimedTask implements Runnable,Comparable<TimedTask>{
	private Runnable action;
	public Double time;
	public String debug_msg="";

	public TimedTask (){
	}
	
	/**
	 * 
	 * @param time quando o código será executado
	 * @param action código a ser executado
	 */
	public TimedTask ( Double time, Runnable action){
		this.time=time;
		this.action=action;
	}
	
	/**
	 * Ordenação na fila de eventos
	 */
	@Override
	public int compareTo(TimedTask other) {
		return time<other.time?-1:time==other.time?0:1;
	}

	/**
	 * Executa o objeto passado como parâmetro
	 */
	@Override
	public void run() {
		action.run();
	}
	
	/**
	 * estabelece o código a ser executado
	 * @param action
	 */
	public void setAction(Runnable action){
		this.action=action;
	}
	
}
