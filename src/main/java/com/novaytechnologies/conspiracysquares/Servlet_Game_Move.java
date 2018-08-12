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

public class Servlet_Game_Move extends HttpServlet
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
		Float fX = Float.parseFloat(req.getParameter("Self_X"));
		Float fY = Float.parseFloat(req.getParameter("Self_Y"));
		Float fDX = Float.parseFloat(req.getParameter("Self_dX"));
		Float fDY = Float.parseFloat(req.getParameter("Self_dY"));
		
		Long lMoveNum = Long.parseLong(req.getParameter("Self_MoveNum"));

		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		if (GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			GameServer_Player GetPlayer = ObjectifyService.ofy().load().type(GameServer_Player.class).parent(GetServer).id(lID).now();
			if (GetPlayer != null && lMoveNum >= GetPlayer.lMoveN)
			{
				GetPlayer.fPosX = fX;
				GetPlayer.fPosY = fY;
				GetPlayer.fSpeedX = fDX;
				GetPlayer.fSpeedY = fDY;
				GetPlayer.lMoveN = lMoveNum;
				ObjectifyService.ofy().save().entity(GetPlayer).now();
			}
		}
	}
}
