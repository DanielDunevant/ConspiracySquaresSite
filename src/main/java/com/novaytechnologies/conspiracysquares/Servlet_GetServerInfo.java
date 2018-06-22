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

import java.util.concurrent.Semaphore;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.ObjectifyService;

public class Servlet_GetServerInfo extends HttpServlet {
	
	static private Semaphore ServerSemaphore = new Semaphore(1);
	
	static private final int FLAG_ALIVE = 0b1;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		
		String ReqPass = req.getParameter("ReqPass");
		//TODO: Add asymmetric encryption

		String ServerName = req.getParameter("ServerName");
		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		
		String ServerPass = req.getParameter("ServerPassword");
		
		if (GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			PrintWriter write = resp.getWriter();
			
			String ServerJoined = req.getParameter("ServerJoined");
			
			int nID = Integer.parseInt(req.getParameter("ID"));

			boolean bTryAgain = true;
			while (bTryAgain)
			{
				bTryAgain = false;
				try
				{
					ServerSemaphore.acquire();
					try
					{
						if (ServerJoined.equals("LEFT"))
						{
							GetServer.PlayerCount--;
							GetServer.Player_Flags.set(nID, 0b0);
						}
						else if (ServerJoined.equals("TRUE"))
						{
							nID = GetServer.PlayerIDs++;
							
							GetServer.PlayerCount++;
							
							int nColor = Integer.parseInt(req.getParameter("Player_Color"));
							String strName = req.getParameter("Player_Name");
							
							GetServer.Player_Updated.add(false);
							GetServer.Player_X.add(0.0f);
							GetServer.Player_Y.add(0.0f);
							GetServer.Player_Flags.add(FLAG_ALIVE);
							GetServer.Player_Colors.add(nColor);
							GetServer.Player_Names.add(strName);
							
							for (Boolean bUpdate : GetServer.Player_Updated)
							{
								bUpdate = false;
							}
						}
						else
						{
							float fXupdate = Float.parseFloat(req.getParameter("Player_X"));
							float fYupdate = Float.parseFloat(req.getParameter("Player_Y"));
							int nFupdate = Integer.parseInt(req.getParameter("Player_Flags"));
							
							GetServer.Player_X.set(nID, fXupdate);
							GetServer.Player_Y.set(nID, fYupdate);
							GetServer.Player_Flags.set(nID, nFupdate);
						}
						
						if (GetServer.PlayerCount <= 0)
							ObjectifyService.ofy().delete().entity(GetServer).now();
						else
						{
							write.print("ID=");
							write.print(Integer.toString(nID));
							
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
							
							if (!GetServer.Player_Updated.get(nID))
							{
								write.print("+");
								for (Integer nColor : GetServer.Player_Colors)
								{
									write.print("&");
									write.print(Integer.toString(nColor));
								}
								
								write.print("+");
								for (String nName : GetServer.Player_Names)
								{
									write.print("&");
									write.print(nName);
								}
								
								GetServer.Player_Updated.set(nID, true);
							}
							
							write.print("+");
							write.print("&");
							write.print(";");
							
							ObjectifyService.ofy().save().entity(GetServer).now();
						}
					}
					catch (Exception e) {bTryAgain=true;}
					finally
					{
						ServerSemaphore.release();
					}
				}
				catch (InterruptedException e) {bTryAgain=true;}
			}
		}
	}
}
