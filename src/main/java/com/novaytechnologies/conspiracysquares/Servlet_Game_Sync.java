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
		
		String ReqPass = req.getParameter("P");
		//TODO: Add asymmetric encryption

		String ServerName = req.getParameter("SN");
		String ServerPass = req.getParameter("SP");
		
		Long lID = Long.parseLong(req.getParameter("ID"));
		Long lMN = 0L;
		Long lCN = 0L;
		boolean bMN = false;
		boolean bCN = false;

		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		if (GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			PrintWriter write = resp.getWriter();
			
			List<GameServer_Player> PlayerList = ObjectifyService.ofy().load().type(GameServer_Player.class).ancestor(GetServer).list();
			for (GameServer_Player Player: PlayerList)
			{
				if (Player.bActive && lID != Player.PlayerID)
				{
					bMN = Long.parseLong(req.getParameter("M" + Long.toString(Player.PlayerID))) < Player.lMoveN;
					bCN = Long.parseLong(req.getParameter("C" + Long.toString(Player.PlayerID))) < Player.lChangeN;
					
					if (bMN || bCN)
					{
						write.print(":");
						write.print(Long.toString(Player.PlayerID));
						write.print("&");
						if (bMN)
						{
							write.print("^");
							write.print(Float.toString(Player.fPosX));
							write.print("&");
							write.print(Float.toString(Player.fPosY));
							write.print("&");
							write.print(Float.toString(Player.fSpeedX));
							write.print("&");
							write.print(Float.toString(Player.fSpeedY));
							write.print("&");
							write.print(Long.toString(Player.lMoveN));
							write.print("&");
						}
						if (bCN)
						{
							write.print("#");
							write.print(Integer.toString(Player.nFlags));
							write.print("&");
							write.print(Integer.toString(Player.nColor));
							write.print("&");
							write.print(Player.strName);
							write.print("&");
							write.print(Long.toString(Player.lChangeN));
							write.print("&");
						}
						write.print(";");
					}
				}
			}
		}
	}
}
