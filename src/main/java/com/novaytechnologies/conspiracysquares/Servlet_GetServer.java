package com.novaytechnologies.conspiracysquares;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Integer;
import java.lang.Float;
import java.lang.String;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class Servlet_GetServer extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		String ReqPass = req.getParameter("ReqPass");
		//TODO: Add asymmetric encryption

		String ServerName = req.getParameter("ServerName");
		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).filter("ServerName", ServerName).first().now();
		
		if (GetServer != null)
		{
			PrintWriter write = resp.getWriter();
			
			String ServerJoined = req.getParameter("ServerJoined");
			
			if (ServerJoined.equals("LEFT"))
			{
				GetServer.PlayerCount--;
				if (GetServer.PlayerCount <= 0) ObjectifyService.ofy().delete().entity(GetServer).now();
			}
			
			int nID = 0;
			if (ServerJoined.equals("TRUE"))
			{
				nID = GetServer.Player_Flags.size();
				
				write.print("ID=");
				write.print(Long.toString(nID));
				write.print("+");
				
				GetServer.PlayerCount++;
				
				GetServer.Player_X.add(0.0f);
				GetServer.Player_Y.add(0.0f);
				GetServer.Player_Flags.add(0);
			}
			else nID = Integer.parseInt(req.getParameter("ID"));
			
			float fXPos = Float.parseFloat(req.getParameter("X"));
			float fYPos = Float.parseFloat(req.getParameter("Y"));
			int nFlags = Integer.parseInt(req.getParameter("FLAGS"));
			
			GetServer.Player_X.set(nID, fXPos);
			GetServer.Player_Y.set(nID, fYPos);
			GetServer.Player_Flags.set(nID, nFlags);

			write.print("+");
			for (Float XPos : GetServer.Player_X)
			{
				write.print("&");
				write.print(Float.toString(XPos));
			}
			
			write.print("+");
			for (Float YPos : GetServer.Player_Y)
			{
				write.print("&");
				write.print(Float.toString(YPos));
			}
			
			write.print("+");
			for (Integer nFlag : GetServer.Player_Flags)
			{
				write.print("&");
				write.print(Integer.toString(nFlag));
			}
			
			write.print(";");
			
			ObjectifyService.ofy().save().entity(GetServer).now();
		}
	}
}
