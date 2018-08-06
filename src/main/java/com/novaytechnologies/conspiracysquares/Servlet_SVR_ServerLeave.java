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
import java.util.List;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class Servlet_SVR_ServerLeave extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		String ReqPass = req.getParameter("ReqPass");
		//TODO: Add asymmetric encryption

		String ServerName = req.getParameter("ServerName");
		String ServerPass = req.getParameter("ServerPassword");
		Long lID = Long.parseLong(req.getParameter("Self_ID"));

		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		if (GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			GetServer.nPlayers--;

			if (GetServer.nPlayers <= 0)
			{
				List<GameServer_Player> PlayerList = ObjectifyService.ofy().load().type(GameServer_Player.class).ancestor(GetServer).list();
				ObjectifyService.ofy().delete().entities(PlayerList).now();
				ObjectifyService.ofy().delete().entity(GetServer).now();
			}
			else
			{
				GameServer_Player PlayerLeft = ObjectifyService.ofy().load().type(GameServer_Player.class).parent(GetServer).id(lID).now();
				Random randColor = new Random(System.currentTimeMillis());
				int nColor = (255 << 24) | (randColor.nextInt(255) << 16) | (randColor.nextInt(255) << 8) | randColor.nextInt(255);
				PlayerLeft.Reset(false, nColor);
				ObjectifyService.ofy().save().entity(PlayerLeft).now();
				ObjectifyService.ofy().save().entity(GetServer).now();
			}
		}
	}
}
