package com.novaytechnologies.conspiracysquares;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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
			String ServerJoined = req.getParameter("ServerJoined");
			
			if (ServerJoined.equals("LEFT"))
			{
				GetServer.PlayerCount--;
				if (GetServer.PlayerCount <= 0) ObjectifyService.ofy().delete().entity(GetServer).now();
			}
			
			long lID = 0;
			if (ServerJoined.equals("TRUE"))
			{
				lID = GetServer.Player_Flags.size();
				
				resp.getWriter().print("ID=");
				resp.getWriter().print(Long.toString(lID));
				resp.getWriter().print("+");
				
				GetServer.PlayerCount++;
				
				GetServer.Player_X.add(0);
				GetServer.Player_Y.add(0);
				GetServer.Player_Flags.add(0);
			}
			else lID = Long.parseLong(req.getParameter("ID"));
			
			float fXPos = Float.parseFloat(req.getParameter("X"));
			float fYPos = Float.parseFloat(req.getParameter("Y"));
			int nFlags = Integer.parseInt(req.getParameter("FLAGS"));
			
			GetServer.Player_X.set(lID, fXPos);
			GetServer.Player_Y.set(lID, fYPos);
			GetServer.Player_Flags.set(lID, nFlags);

			resp.getWriter().print(Integer.toString("+");
			for (Float XPos : GetServer.Player_X)
			{
				resp.getWriter().print("&");
				resp.getWriter().print(Float.toString(XPos));
			}
			
			resp.getWriter().print(Integer.toString("+");
			for (Float YPos : GetServer.Player_Y)
			{
				resp.getWriter().print("&");
				resp.getWriter().print(Float.toString(YPos));
			}
			
			resp.getWriter().print(Integer.toString("+");
			for (Integer nFlags : GetServer.Player_Flags)
			{
				resp.getWriter().print("&");
				resp.getWriter().print(Integer.toString(nFlags));
			}
			
			resp.getWriter().print(Integer.toString(";");
			
			ObjectifyService.ofy().save().entity(GetServer).now();
		}
	}
}
