package test;


public class Message extends Command {
	
	private final String cmd;
	
	public Message(String cmd) {
		super();
		this.cmd = cmd;
	}
	
	public String getCommand() {
		return cmd;
	}

}
