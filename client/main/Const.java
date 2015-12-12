package main;

public class Const {
	
	public static final String HOST_NAME = "localhost";
	public static final int PORT_NUMBER = 9000;
	
	public static final String BAD = "BAD ";
	public static final String BAD_USERNAME_EMPTY = BAD + "username cannot be empty";
	public static final String BAD_USERNAME_INVALID_CHAR = BAD + "username cannot contain , or : or whitespace";
	public static final String BAD_INVALID_COMMAND = BAD + "invalid command";
	
	public static final String OK = "OK ";
	
	public static final String COMMAND_IDEN = "IDEN";
	public static final String COMMAND_IDEN_FORMAT = COMMAND_IDEN + " %s";
	public static final String COMMAND_STAT = "STAT";
	public static final String COMMAND_LIST = "LIST";
	public static final String COMMAND_MESG = "MESG";
	public static final String COMMAND_MESG_FORMAT = COMMAND_MESG + " %s %s";
	public static final String COMMAND_HAIL = "HAIL";
	public static final String COMMAND_HAIL_FORMAT = COMMAND_HAIL + " %s";
	public static final String COMMAND_QUIT = "QUIT";
	
}
