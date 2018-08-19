package com.novaytechnologies.conspiracysquares;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.lang.Integer;
import java.lang.Long;
import java.lang.String;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;
import java.util.Calendar;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class Cron_RemoveEmptyServers extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		List<GameServer> ServerList = ObjectifyService.ofy().load().type(GameServer.class).list();
		for (GameServer SVR: ServerList)
		{
			Calendar GMT = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
			long lNow = GMT.getTimeInMillis();
			boolean bServerChanged = false;
			
			List<GameServer_Player> PlayerList = ObjectifyService.ofy().load().type(GameServer_Player.class).ancestor(SVR).list();
			for (GameServer_Player Player: PlayerList)
			{
				if (lNow - Player.lLastUpdate > 8000)
				{
					bServerChanged = true;
					SVR.nPlayers--;
					Player.Reset(false);
					ObjectifyService.ofy().save().entity(Player).now();
				}
			}

			if (SVR.nPlayers == 0) ObjectifyService.ofy().delete().entities(SVR, PlayerList).now();
			else if (bServerChanged) ObjectifyService.ofy().save().entity(SVR).now();
		}
	}
}
