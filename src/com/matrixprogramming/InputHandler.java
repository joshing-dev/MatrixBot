package com.matrixprogramming;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Thread to handle the incoming messages from IRCClient
 * 
 * @author Joshua Eldridge
 * @version 12/6/2015
 */
public class InputHandler extends Thread
{

	private IRCClient client;
	private String channelName;
	private long previousTime;
	private long currentTime;
	private int messageCount;
	private HashMap<String, String> commandMap;

	public InputHandler(IRCClient client, String channelName)
	{

		this.client = client;
		this.channelName = channelName;
		this.commandMap = new HashMap<String, String>();
		createCommandMap(commandMap);
		client.joinChannel(channelName);
	}

	@Override
	public void run()
	{
		try
		{
			previousTime = System.nanoTime();
			System.out.println(previousTime);
			String str;
			while ((str = client.getInputStream().readLine()) != null)
			{
				currentTime = System.nanoTime();
				if (messageCount < 20 && ((currentTime - previousTime) / 1000000000) >= 30)
				{
					previousTime = System.nanoTime();
					messageCount = 0;
				}
				long timeDifference = currentTime - previousTime;
				if (messageCount >= 20 && ((currentTime - previousTime) / 1000000000) <= 30)
				{
					System.out.println("Throttling output");
					System.out.println(timeDifference / 1000000);
					Thread.sleep(30000 - (timeDifference / 1000000));
					messageCount = 0;
					previousTime = System.nanoTime();
				}
				System.out.println(str);
				handleMessage(str);
				System.out.println("Messages: " + messageCount + " Timer: " + (timeDifference / 1000000000));
			}
			System.out.println(str);

		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public void handleMessage(String str)
	{
		String message = "";
		if (str.contains("PRIVMSG"))
		{

			// Kudos to vavbro for this nice code
			String[] s1 = str.split("PRIVMSG");
			String user = s1[0].split("!")[0].replace(":", "");
			String fullUser = s1[0].trim().replace(":", "");
			String channel = s1[1].split(":")[0].trim();
			message = s1[1].replace(channel + " :", "").trim();
		}
		if (str.equals("PING :tmi.twitch.tv"))
		{
			client.getOutputStream().write("PONG :tmi.twitch.tv\n");
			client.flushOutput();
			messageCount++;
		}
		if (commandMap.containsKey(message))
		{
			String commandValue = this.getCommandValue(message);
			client.sendMessage(channelName, commandValue);
			messageCount++;
		}
	}

	public void createCommandMap(HashMap<String, String> map)
	{
		map.put("!commands", "This is currently being updated");
		map.put("Kappa", "Don't kappa me betch Kappa");
		map.put("!website", "http://matrix159.github.io");
		
	}

	public String getCommandValue(String command)
	{
		return commandMap.get(command);
	}

	public IRCClient getClient()
	{
		return client;
	}

	public String getChannelName()
	{
		return channelName;
	}
}
