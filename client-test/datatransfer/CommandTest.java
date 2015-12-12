package datatransfer;

import static org.junit.Assert.assertEquals;
import main.Const;

import org.junit.Test;

public class CommandTest {
	
	@Test
	public void testParseCommand() {
		
		String cmd;
		Command parsedCmd;
		
		cmd = String.format(Const.COMMAND_IDEN_FORMAT, "somebody");
		parsedCmd = new Command(cmd);
		assertEquals(CommandType.IDEN, parsedCmd.getCommandType());
		assertEquals("somebody", parsedCmd.getCommandContent());
		assertEquals(cmd, parsedCmd.getCommandFullContent());
		
		cmd = Const.COMMAND_STAT;
		parsedCmd = new Command(cmd);
		assertEquals(CommandType.STAT, parsedCmd.getCommandType());
		assertEquals("", parsedCmd.getCommandContent());
		assertEquals(cmd, parsedCmd.getCommandFullContent());
		
		cmd = Const.COMMAND_LIST;
		parsedCmd = new Command(cmd);
		assertEquals(CommandType.LIST, parsedCmd.getCommandType());
		assertEquals("", parsedCmd.getCommandContent());
		assertEquals(cmd, parsedCmd.getCommandFullContent());
		
		cmd = String.format(Const.COMMAND_HAIL_FORMAT, "a message");
		parsedCmd = new Command(cmd);
		assertEquals(CommandType.HAIL, parsedCmd.getCommandType());
		assertEquals("a message", parsedCmd.getCommandContent());
		assertEquals(cmd, parsedCmd.getCommandFullContent());
		
		cmd = String.format(Const.COMMAND_MESG_FORMAT, "somebody", "a message");
		parsedCmd = new Command(cmd);
		assertEquals(CommandType.MESG, parsedCmd.getCommandType());
		assertEquals("somebody a message", parsedCmd.getCommandContent());
		assertEquals(cmd, parsedCmd.getCommandFullContent());
		
		cmd = Const.COMMAND_QUIT;
		parsedCmd = new Command(cmd);
		assertEquals(CommandType.QUIT, parsedCmd.getCommandType());
		assertEquals("", parsedCmd.getCommandContent());
		assertEquals(cmd, parsedCmd.getCommandFullContent());
		
		cmd = "UNKNOWN command";
		parsedCmd = new Command(cmd);
		assertEquals(CommandType.UNKNOWN, parsedCmd.getCommandType());
		assertEquals("command", parsedCmd.getCommandContent());
		assertEquals(cmd, parsedCmd.getCommandFullContent());
		
	}

}
