package datatransfer;

import main.Const;

public class Command {
	
	private final CommandType type;
	private final String content;
	private final String fullContent;
	
	public Command(String cmd) {
		String[] splitCmd = cmd.split(" ", 2);
		CommandType type;
		switch (splitCmd[0]) {
			case Const.COMMAND_IDEN:
				type = CommandType.IDEN;
				break;
			case Const.COMMAND_STAT:
				type = CommandType.STAT;
				break;
			case Const.COMMAND_LIST:
				type = CommandType.LIST;
				break;
			case Const.COMMAND_HAIL:
				type = CommandType.HAIL;
				break;
			case Const.COMMAND_MESG:
				type = CommandType.MESG;
				break;
			case Const.COMMAND_QUIT:
				type = CommandType.QUIT;
				break;
			default:
				type = CommandType.UNKNOWN;
				break;
		}
		this.type = type;
		this.content = splitCmd.length > 1 ? splitCmd[1] : "";
		this.fullContent = cmd;
	}
	
	public CommandType getCommandType() {
		return type;
	}
	
	public String getCommandContent() {
		return content;
	}
	
	public String getCommandFullContent() {
		return fullContent;
	}

}
