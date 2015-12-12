package test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import src.Server;

public class Tester {

	private static final String SERVER_ADDRESS = "localhost";
	private static final int SERVER_PORT = 7000;
	private final ExecutorService executor = Executors.newCachedThreadPool();
	
	@BeforeClass // uncomment this annotation for coverage testing
	public static void setUp() throws InterruptedException {
		new Thread(new Runnable() {
			@Override
			public void run() {
				new Server(SERVER_PORT);
			}
		}).start();
		Thread.sleep(1000); // let the server initialize first
	}
	
	@Test
	public void testInvalidCommands() throws InterruptedException {
		Command[] user1Commands = { new Message("ABC"),
									new Message("RANDOM"), 
									new Message("MESG wrongformat"),
									new Message("IDEN user1"),
									new Message("ABC"),
									new Message("RANDOM"), 
									new Message("MESG wrongformat"),
									new Message("QUIT") };
		StubClient user1 = new StubClient(SERVER_PORT, SERVER_ADDRESS, user1Commands, "user1");
		executor.execute(user1);
		executor.awaitTermination(1, TimeUnit.SECONDS);
		List<String> expectedOutputMsgs = Arrays.asList("BAD invalid command to server",
				"BAD command not recognised",
				"BAD You have not logged in yet",
				"OK Welcome to the chat server user1",
				"BAD invalid command to server",
				"BAD command not recognised",
				"BAD Your message is badly formatted",
				"OK thank you for sending 0 message(s) with the chat service, goodbye.");
		List<String> expectedChatMsgs = Arrays.asList();
		assertListEquals(expectedOutputMsgs, user1.getOutputMsgs());
		assertListEquals(expectedChatMsgs, user1.getChatMsgs());
	}
	
	@Test
	public void testSingleUserUnregistered() throws InterruptedException {
		Command[] strangerCommands = { new Message("LIST"),
									   new Message("STAT"), 
									   new Message("MESG nobody Hello nobody"),
									   new Message("HAIL Hello from nobody"),
									   new Message("QUIT") };
		StubClient stranger = new StubClient(SERVER_PORT, SERVER_ADDRESS, strangerCommands, "stranger");
		executor.execute(stranger);
		executor.awaitTermination(1, TimeUnit.SECONDS);
		List<String> expectedOutputMsgs = Arrays.asList("BAD You have not logged in yet",
				"OK There are currently 1 user(s) on the server You have not logged in yet",
				"BAD You have not logged in yet",
				"BAD You have not logged in yet",
				"OK goodbye");
		List<String> expectedChatMsgs = Arrays.asList();
		assertListEquals(expectedOutputMsgs, stranger.getOutputMsgs());
		assertListEquals(expectedChatMsgs, stranger.getChatMsgs());
	}

	@Test
	public void testSingleUserRegistered() throws InterruptedException {
		Command[] user1Commands = { new Message("IDEN user1"),
									new Message("IDEN impostor"),
									new Message("LIST"),
									new Message("STAT"), 
									new Message("HAIL Hello from user1"),
									new Message("MESG nobody Hello nobody"),
									new Message("MESG user1 Hello me"),
									new Message("QUIT") };
		StubClient user1 = new StubClient(SERVER_PORT, SERVER_ADDRESS, user1Commands, "user1");
		executor.execute(user1);
		executor.awaitTermination(1, TimeUnit.SECONDS);
		List<String> expectedOutputMsgs = Arrays.asList("OK Welcome to the chat server user1",
				"BAD you are already registerd with username user1",
				"OK user1,",
				"OK There are currently 1 user(s) on the server You are logged im and have sent 0 message(s)",
				"BAD the user does not exist",
				"OK your message has been sent",
				"OK thank you for sending 1 message(s) with the chat service, goodbye.");
		List<String> expectedChatMsgs = Arrays.asList("Broadcast from user1: Hello from user1",
				"PM from user1:Hello me");
		assertListEquals(expectedOutputMsgs, user1.getOutputMsgs());
		assertListEquals(expectedChatMsgs, user1.getChatMsgs());
	}
	
	@Test
	public void testDuplicateUsername() throws InterruptedException {
		Command[] user1Commands = { new Message("IDEN user1"),
									new Wait(300),
									new Message("QUIT") };
		Command[] user2Commands = { new Wait(200),
									new Message("IDEN user1"),
									new Wait(300),
									new Message("IDEN user1"),
									new Message("QUIT") };
		StubClient user1 = new StubClient(SERVER_PORT, SERVER_ADDRESS, user1Commands, "user1");
		StubClient user2 = new StubClient(SERVER_PORT, SERVER_ADDRESS, user2Commands, "user2");
		executor.execute(user1);
		executor.execute(user2);
		executor.awaitTermination(1, TimeUnit.SECONDS);
		List<String> expectedUser1OutputMsgs = Arrays.asList("OK Welcome to the chat server user1",
				"OK thank you for sending 0 message(s) with the chat service, goodbye.");
		List<String> expectedUser1ChatMsgs = Arrays.asList();
		assertListEquals(expectedUser1OutputMsgs, user1.getOutputMsgs());
		assertListEquals(expectedUser1ChatMsgs, user1.getChatMsgs());
		List<String> expectedUser2OutputMsgs = Arrays.asList("BAD username is already taken",
				"OK Welcome to the chat server user1",
				"OK thank you for sending 0 message(s) with the chat service, goodbye.");
		List<String> expectedUser2ChatMsgs = Arrays.asList();
		assertListEquals(expectedUser2OutputMsgs, user2.getOutputMsgs());
		assertListEquals(expectedUser2ChatMsgs, user2.getChatMsgs());
	}

	@Test
	public void testMultipleUsers() throws InterruptedException {
		Command[] user1Commands = { new Message("IDEN user1"),
									new Wait(100),
									new Message("LIST"),
									new Message("HAIL Hello from user1"),
									new Message("MESG user2 hello"),
									new Wait(300),
									new Message("QUIT") };
		Command[] user2Commands = { new Message("IDEN user2"),
									new Wait(100),
									new Message("MESG user1 hello"),
									new Wait(100),
									new Message("MESG user3 hello"),
									new Wait(300),
									new Message("QUIT") };
		Command[] user3Commands = { new Message("IDEN user3"),
									new Message("MESG nobody hello"),
				                    new Message("STAT"),
									new Wait(200),
				                    new Message("HAIL Hello from user3"),
				                    new Message("STAT"),
									new Wait(100),
				                    new Message("QUIT") };
		Command[] user4Commands = { new Wait(400),
									new Message("QUIT") };
		StubClient user1 = new StubClient(SERVER_PORT, SERVER_ADDRESS, user1Commands, "user1");
		StubClient user2 = new StubClient(SERVER_PORT, SERVER_ADDRESS, user2Commands, "user2");
		StubClient user3 = new StubClient(SERVER_PORT, SERVER_ADDRESS, user3Commands, "user3");
		StubClient user4 = new StubClient(SERVER_PORT, SERVER_ADDRESS, user4Commands, "user4");
		executor.execute(user1);
		executor.execute(user2);
		executor.execute(user3);
		executor.execute(user4);
		executor.awaitTermination(1, TimeUnit.SECONDS);
		List<String> expectedUser1OutputMsgs = Arrays.asList("OK Welcome to the chat server user1",
				"OK user1, user2, user3,",
				"OK your message has been sent",
				"OK thank you for sending 1 message(s) with the chat service, goodbye.");
		List<String> expectedUser1ChatMsgs = Arrays.asList("PM from user2:hello",
				"Broadcast from user1: Hello from user1",
				"Broadcast from user3: Hello from user3");
		assertListEquals(expectedUser1OutputMsgs, user1.getOutputMsgs());
		assertListEquals(expectedUser1ChatMsgs, user1.getChatMsgs());
		List<String> expectedUser2OutputMsgs = Arrays.asList("OK Welcome to the chat server user2",
				"OK your message has been sent",
				"OK your message has been sent",
				"OK thank you for sending 0 message(s) with the chat service, goodbye.");
		List<String> expectedUser2ChatMsgs = Arrays.asList("Broadcast from user1: Hello from user1",
				"PM from user1:hello",
				"Broadcast from user3: Hello from user3");
		assertListEquals(expectedUser2OutputMsgs, user2.getOutputMsgs());
		assertListEquals(expectedUser2ChatMsgs, user2.getChatMsgs());
		List<String> expectedUser3OutputMsgs = Arrays.asList("OK Welcome to the chat server user3",
				"BAD the user does not exist",
				"OK There are currently 4 user(s) on the server You are logged im and have sent 0 message(s)",
				"OK There are currently 3 user(s) on the server You are logged im and have sent 1 message(s)",
				"OK thank you for sending 1 message(s) with the chat service, goodbye.");
		List<String> expectedUser3ChatMsgs = Arrays.asList(
				"Broadcast from user1: Hello from user1",
				"PM from user2:hello",
				"Broadcast from user3: Hello from user3");
		assertListEquals(expectedUser3OutputMsgs, user3.getOutputMsgs());
		assertListEquals(expectedUser3ChatMsgs, user3.getChatMsgs());
		List<String> expectedUser4OutputMsgs = Arrays.asList("OK goodbye");
		List<String> expectedUser4ChatMsgs = Arrays.asList("Broadcast from user1: Hello from user1");
		assertListEquals(expectedUser4OutputMsgs, user4.getOutputMsgs());
		assertListEquals(expectedUser4ChatMsgs, user4.getChatMsgs());
	}
	
	private void assertListEquals(List<String> expected, List<String> actual) {

		// Check equality of size first
		String errorMsg = "Different list size: %s vs %s";		
		int expectedSize = expected.size();
		int actualSize = actual.size();
		assertEquals(String.format(errorMsg, String.valueOf(expectedSize), String.valueOf(actualSize)),
					 expected.size(), actual.size());
		
		// Then check equality of contents; ordering matters
		errorMsg = "Fail at element #%s: %s vs %s";
		for (int i = 0; i < expectedSize; i++) {
			String expectedContent = expected.get(i).trim();
			String actualContent = actual.get(i).trim();
			assertEquals(String.format(errorMsg, String.valueOf(i + 1), expectedContent, actualContent),
					     expectedContent, actualContent);
		}
		
	}
	
}
