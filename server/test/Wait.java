package test;


public class Wait extends Command {
	
	private final int dur;
	
	public Wait(int dur) {
		super();
		this.dur = dur;
	}
	
	public int getDur() {
		return dur;
	}

}
