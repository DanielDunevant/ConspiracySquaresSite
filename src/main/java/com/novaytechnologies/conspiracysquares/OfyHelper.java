package com.novaytechnologies.conspiracysquares;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

public class OfyHelper implements ServletContextListener
{
	public void contextInitialized(ServletContextEvent event)
	{
		ObjectifyService.register(GameServer.class);
		ObjectifyService.register(GameServer_Player.class);
	}

	public void contextDestroyed(ServletContextEvent event) {}
}