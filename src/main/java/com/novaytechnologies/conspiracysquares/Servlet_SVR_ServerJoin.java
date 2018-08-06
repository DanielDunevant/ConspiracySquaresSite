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

public class Servlet_SVR_ServerJoin extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		String ReqPass = req.getParameter("ReqPass");
		//TODO: Add asymmetric encryption

		String ServerName = req.getParameter("ServerName");
		String ServerPass = req.getParameter("ServerPassword");
		String sName = req.getParameter("Self_Name");

		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		if (GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			PrintWriter write = resp.getWriter();

			GameServer_Player PlayerJoin = ObjectifyService.ofy().load().type(GameServer_Player.class).ancestor(GetServer).filter("bActive", false).first().now();
			if (PlayerJoin != null)
			{
				PlayerJoin.bActive = true;
				PlayerJoin.strName = sName;
				PlayerJoin.nFlags = (GetServer.bRoundStarted) ? -1 : 1;

				write.print("ID=");
				write.print(Long.toString(PlayerJoin.PlayerID));
				
				write.print("+ROUND=");
				if (GetServer.bRoundStarted) write.print("true");
				else write.print("false");
				
				write.print("+COLOR=");
				write.print(Integer.toString(PlayerJoin.nColor));

				ObjectifyService.ofy().save().entity(PlayerJoin).now();
			}
		}
	}
}
