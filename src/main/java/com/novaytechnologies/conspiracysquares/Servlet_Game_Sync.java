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

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class Servlet_Game_Sync extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		String ReqPass = req.getParameter("ReqPass");
		//TODO: Add asymmetric encryption

		String ServerName = req.getParameter("ServerName");
		String ServerPass = req.getParameter("ServerPassword");

		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		if (GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			PrintWriter write = resp.getWriter();
			
			List<GameServer_Player> PlayerList = ObjectifyService.ofy().load().type(GameServer_Player.class).ancestor(GetServer).list();
			for (GameServer_Player Player: PlayerList)
			{
				write.print("+");
				write.print(Player.PlayerID);
				write.print("-");
				write.print(Player.fPosX);
				write.print("-");
				write.print(Player.fPosY);
				write.print("-");
				write.print(Player.fSpeedX);
				write.print("-");
				write.print(Player.fSpeedY);
				write.print("-");
				write.print(Player.nFlags);
				write.print("-");
				write.print(Player.nColor);
				write.print("-");
				write.print(Player.strName);
			}
			write.print(";");
		}
	}
}
