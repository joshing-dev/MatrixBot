package com.matrixprogramming;

/**
 * Handles it's own IRCClient and InputHandler
 * 
 * @author Joshua Eldridge
 * @version 12/4/2015
 */
public class MatrixBot
{
	private IRCClient client;
	private InputHandler inputHandler;
	private OAuthEncoder encoder;

	public MatrixBot(String channelName)
	{
		encoder = new OAuthEncoder();
		client = new IRCClient("irc.twitch.tv", 443, "codematrixbot", encoder.getOAuth());
		inputHandler = new InputHandler(client, channelName);
		inputHandler.start();
	}
}
