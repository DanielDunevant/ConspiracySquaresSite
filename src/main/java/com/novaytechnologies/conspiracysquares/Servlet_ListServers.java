package com.novaytechnologies.conspiracysquares;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.List;
import java.lang.Integer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class Servlet_ListServers extends HttpServlet {

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");

		List<GameServer> ServerList = ofy().load().type(GameServer.class).list();
		for (GameServer SVR: ServerList)
		{
			System.out.println("+");
			System.out.println(SVR.ServerName);
			System.out.println("&");
			System.out.println(Integer.toString(SVR.PlayerCount));
		}
		System.out.println(";");
	}
}
