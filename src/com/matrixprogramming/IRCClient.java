package com.matrixprogramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Creates a connection to an IRC channel
 */
public class IRCClient
{
	/** Output Stream */
	private volatile PrintWriter output;

	/** Input Stream */
	private BufferedReader input;
	/** The client's username */
	private String username;

	/**
	 * Creates a IRC Client
	 * 
	 * @param ip
	 *            The ip to connect to
	 * @param port
	 *            The port number
	 * @param username
	 *            The client's username
	 * @param password
	 *            The OAuth password
	 */
	public IRCClient(String ip, int port, String username, String password)
	{
		// Create connection and setup streams
		this.username = username;
		Socket tcp;
		try
		{
			tcp = new Socket(ip, port);
			output = new PrintWriter(tcp.getOutputStream());
			input = new BufferedReader(new InputStreamReader(tcp.getInputStream()));
			output.write("PASS " + password + "\n");
			output.write("NICK " + username + "\n");
			output.write("USER " + username + "\n");
			output.flush();
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Sends a message to the user through the output stream
	 * 
	 * @param user
	 *            The user to send message to
	 * @param message
	 *            The message
	 */
	public void sendMessage(String user, String message)
	{
		// ":" + username + "!" + username + "@" + username + ".tmi.twitch.tv
		output.write("PRIVMSG #" + user + " :" + message + "\n");
		output.flush();
	}

	/**
	 * Joins an IRC channel
	 * 
	 * @param channel
	 *            The channel name to join (Case Sensitive)
	 */
	public void joinChannel(String channel)
	{
		output.write("JOIN #" + channel + "\n");
		output.flush();
	}

	/**
	 * Flushes the output stream
	 */
	public void flushOutput()
	{
		output.flush();
	}

	/**
	 * Returns the input stream as a Buffered Reader
	 * 
	 * @return The Buffered Reader
	 */
	public BufferedReader getInputStream()
	{
		return input;
	}

	/**
	 * Returns the output stream as a PrintWriter
	 * 
	 * @return The PrintWriter
	 */
	public PrintWriter getOutputStream()
	{
		return output;
	}

	/**
	 * Returns the username used to connect to the IRC
	 * 
	 * @return The username
	 */
	public String getUsername()
	{
		return username;
	}

}
