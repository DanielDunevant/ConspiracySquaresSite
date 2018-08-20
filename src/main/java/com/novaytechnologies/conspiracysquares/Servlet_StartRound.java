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
import java.util.Random;
public class Servlet_RoundStart extends HttpServlet 
 @Override
         public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
        {
		resp.addHeader("Access-Control-Allow-Origin", "*");
		String ReqPass = req.getParameter("ReqPass");
        	//TODO: Add asymmetric encryption
        	String ServerName = req.getParameter("ServerName");
		String ServerPass = req.getParameter("ServerPassword");
		traitorRatio=.16666;
		enforcerRatio = .142857;
		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		int players[];
		int i=0;
		int nPlayers =GetServer.nPlayers;
		ArrayList<GameServer_Player> PlayerList = new ArrayList<GameServer_Player>();
		for(i=0;i<nPlayers;i++)
		{
			players[i] = 1;
		}
		if(nPlayers<=10)
		{
			Random rand = new Random();
			traitorId = rand.nextInt(nPlayers+1);
			GameServer_Player traitorPlayer = new GameServer_Player(ServerName, traitorId,playerClass);
			traitorPlayer.nFlags=3; 
			players[traitorId] = 0;
			ObjectifyService.ofy().save().entities(traitorPlayer).now();
			enforcerId = rand.nextInt(nPlayers+1);
			while(enforcerId == traitorId)
			{
				enforcerId = rand.nextInt(nPlayers+1);
			}
			players[enforcerId] = 0;
			GameServer_Player EnforcerPlayer = new GameServer_Player(ServerName, enforcerId,playerClass);
			enforcerPlayer.nFlags=2; 
			ObjectifyService.ofy().save().entities(traitorPlayer).now();
			int i = 0;
			for(i=0;i<nPlayers;i++)
			{
				if(players[i]!==0)
				{
					GameServer_Player innocentPlayer = new GameServer_Player(ServerName, i+1,playerClass);
					innocentPlayer.nFlags=1; 
					ObjectifyService.ofy().save().entities(innocentPlayer).now();

				}

			}
		}
		else
		{
			numTraitors = nPlayers*traitorRatio;
			numEnforcers = nPlayers*enforcerRatio;
			//choose random traitors
			for(int i = 0;i<numTraitors;i++)
			{
				traitorId = rand.nextInt(nPlayers+1);
				while(players[traitorId]==0)
				{
					traitorId = rand.nextInt(nPlayers+1);
				}
				GameServer_Player traitorPlayer = new GameServer_Player(ServerName, traitorId,playerClass);
				traitorPlayer.nFlags=3; 
				players[traitorId] = 0;
				ObjectifyService.ofy().save().entities(traitorPlayer).now();
			}
			//choose random enforcers 
			for(int i = 0;i<numEnforcers;i++)
			{
				enforcerId = rand.nextInt(nPlayers+1);
				while(players[enforcerId]==0)
				{
					enforcerId = rand.nextInt(nPlayers+1);
				}
				GameServer_Player enforcerPlayer = new GameServer_Player(ServerName, enforcerId,playerClass);
				enforcerPlayer.nFlags=2; 
				players[enforcerId] = 0;
				ObjectifyService.ofy().save().entities(enforcerPlayer).now();
			}
			//Set the innocents
			for(int i = 0;i<nPlayers;i++)
			{
				if(players[i]!=0)
				{
					GameServer_Player innocentPlayer = new GameServer_Player(ServerName, i+1,playerClass);
					innocentPlayer.nFlags=1; 
					players[innocentId] = 0;
					ObjectifyService.ofy().save().entities(innocentPlayer).now();
				}
			}
		}
	}
}
