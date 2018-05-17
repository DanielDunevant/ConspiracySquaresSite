package com.novaytechnologies.conspiracysquares;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class Servlet_EndServer extends HttpServlet {

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
			GetServer.PlayerCount = 0;
			ObjectifyService.ofy().delete().entity(GetServer).now();
		}
	}
}
