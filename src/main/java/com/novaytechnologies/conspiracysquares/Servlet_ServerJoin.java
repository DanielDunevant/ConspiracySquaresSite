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

public class Servlet_ServerJoin extends HttpServlet
{
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		String ReqPass = req.getParameter("ReqPass");
		//TODO: Add asymmetric encryption

		String ServerName = req.getParameter("ServerName");
		String ServerPass = req.getParameter("ServerPassword");
		
		String PlayerIP = "";
        if (req != null) {
            PlayerIP = req.getHeader("X-FORWARDED-FOR");
            if (PlayerIP == null || "".equals(PlayerIP) || PlayerIP.contains(",") ) {
				PlayerIP = req.getHeader("CF-CONNECTING-IP");
				if (PlayerIP == null || "".equals(PlayerIP) || PlayerIP.contains(",")) {
					PlayerIP = req.getRemoteAddr();
				}
            }
        }
		
		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		
		if (PlayerIP.equals("") && GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			PrintWriter write = resp.getWriter();

			GetServer.Player_IPs.add(PlayerIP);
			GetServer.Player_Ports.put(PlayerIP, "-1");
			GetServer.Player_Update.put(PlayerIP, "true");
			
			Integer nID = GetServer.Player_IDs.get(PlayerIP);
			if (nID == null)
			{
				nID = GetServer.Player_IDs.size();
				GetServer.Player_IDs.put(PlayerIP, nID);
			}

			for (Map.Entry<String, String> entry : GetServer.Player_Update.entrySet())
			{
				GetServer.Player_Update.put(entry.getKey(), "true");
			}

			write.print("ID=");
			write.print(Integer.toString(nID));
			
			write.print("+ROUND=");
			if (GetServer.bRoundStarted) write.print("true");
			else write.print("false");
			
			write.print("+IP=");
			write.print(PlayerIP);

			ObjectifyService.ofy().save().entity(GetServer).now();
		}
	}
}
