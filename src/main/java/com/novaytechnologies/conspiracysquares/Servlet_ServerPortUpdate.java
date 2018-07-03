package com.novaytechnologies.conspiracysquares;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class Servlet_ServerPortUpdate extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		String ReqPass = req.getParameter("ReqPass");
		//TODO: Add asymmetric encryption

		String ServerName = req.getParameter("ServerName");
		String ServerPass = req.getParameter("ServerPassword");
		String PlayerIP = req.getParameter("IP");
		String PlayerPort = req.getParameter("PORT");
		
		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		
		if (GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			PrintWriter write = resp.getWriter();

			GetServer.Player_Ports.put(PlayerIP, PlayerPort);

			for (Map.Entry<String, String> entry : GetServer.Player_Update.entrySet())
			{
				GetServer.Player_Update.put(entry.getKey(), "true");
			}

			ObjectifyService.ofy().save().entity(GetServer).now();
		}
	}
}
