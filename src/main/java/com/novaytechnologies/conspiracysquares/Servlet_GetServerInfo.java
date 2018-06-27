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

public class Servlet_GetServerInfo extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		String ReqPass = req.getParameter("ReqPass");
		//TODO: Add asymmetric encryption

		String ServerName = req.getParameter("ServerName");
		String ServerPass = req.getParameter("ServerPassword");
		String ServerJoined = req.getParameter("ServerJoined");
		String PlayerIP = req.getParameter("IP");
		
		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		
		if (GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			PrintWriter write = resp.getWriter();

			if (ServerJoined.equals("LEFT"))
			{
				GetServer.Player_IPs.remove(PlayerIP);
				GetServer.Player_Ports.remove(PlayerIP);
				GetServer.Player_Update.remove(PlayerIP);
				
				if (GetServer.Player_IPs.size() == 0) ObjectifyService.ofy().delete().entity(GetServer).now();
				else
				{
					for (Map.Entry<String, String> entry : GetServer.Player_Update.entrySet())
					{
						GetServer.Player_Update.put(entry.getKey(), "true");
					}
					ObjectifyService.ofy().save().entity(GetServer).now();
				}
			}
			else if (ServerJoined.equals("TRUE"))
			{
				GetServer.Player_IPs.add(PlayerIP);
				GetServer.Player_Ports.put(PlayerIP, "-1");
				GetServer.Player_Update.put(PlayerIP, "true");
				
				Integer nID = GetServer.Player_IDs.get(PlayerIP);
				if (nID == null) GetServer.Player_IDs.put(PlayerIP, GetServer.Player_IDs.size());

				for (Map.Entry<String, String> entry : GetServer.Player_Update.entrySet())
				{
					GetServer.Player_Update.put(entry.getKey(), "true");
				}

				write.print("ID=");
				write.print(Integer.toString(nID));
				
				ObjectifyService.ofy().save().entity(GetServer).now();
			}
			else if (ServerJoined.equals("PORT"))
			{
				String PlayerPort = req.getParameter("PORT");
				GetServer.Player_Ports.put(PlayerIP, PlayerPort);

				for (Map.Entry<String, String> entry : GetServer.Player_Update.entrySet())
				{
					GetServer.Player_Update.put(entry.getKey(), "true");
				}

				ObjectifyService.ofy().save().entity(GetServer).now();
			}
			else
			{
				GetServer.Player_Update.put(PlayerIP, "false");
				for(String strIP : GetServer.Player_IPs)
				{
					write.print(strIP);
					write.print("+");
					write.print(GetServer.Player_Ports.get(strIP));
					write.print("+");
				}
				write.print("+");
				ObjectifyService.ofy().save().entity(GetServer).now();
			}
		}
	}
}
