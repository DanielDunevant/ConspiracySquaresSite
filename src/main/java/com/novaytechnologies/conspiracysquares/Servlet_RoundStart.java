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
import java.lang.Math;
import java.lang.String;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.googlecode.objectify.ObjectifyService;
import java.util.Random;

public class Servlet_RoundStart extends HttpServlet
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
		
		GameServer GetServer = ObjectifyService.ofy().load().type(GameServer.class).id(ServerName).now();
		if (GetServer != null && GetServer.ServerPassword.equals(ServerPass))
		{
			if (!GetServer.bRoundStarted)
			{
				int nPlayers = GetServer.nPlayers;
				
				ArrayList<Integer> playerSet = new ArrayList<Integer>();
				for(int i = 0; i < nPlayers; i++)
				{
					playerSet.add(0);
				}

				Random rand = new Random();
				if (nPlayers < 10)
				{
					int traitorId = rand.nextInt(nPlayers);
					playerSet.set(traitorId, 1);

					int enforcerId = rand.nextInt(nPlayers);
					while(enforcerId == traitorId)
					{
						enforcerId = rand.nextInt(nPlayers);
					}
					playerSet.set(enforcerId, 2);
				}
				else
				{
					float traitorRatio = 0.2f;
					float enforcerRatio = 0.2f;
					
					int numTraitors = (int)Math.floor(nPlayers * traitorRatio);
					int numEnforcers = (int)Math.floor(nPlayers * enforcerRatio);

					//choose random traitors
					for(int i = 0; i < numTraitors; i++)
					{
						int traitorId = rand.nextInt(nPlayers);
						while(playerSet.get(traitorId) != 0)
						{
							traitorId = rand.nextInt(nPlayers);
						}
						playerSet.set(traitorId, 1);
					}
					
					//choose random enforcers 
					for(int i = 0; i < numEnforcers; i++)
					{
						int enforcerId = rand.nextInt(nPlayers);
						while(playerSet.get(enforcerId) != 0)
						{
							enforcerId = rand.nextInt(nPlayers);
						}
						playerSet.set(enforcerId, 2);
					}
				}

				int nPlayerArrayI = 0;
				int nPlayerArrayGet = 0;
				List<GameServer_Player> PlayerList = ObjectifyService.ofy().load().type(GameServer_Player.class).ancestor(GetServer).list();
				for (GameServer_Player Player: PlayerList)
				{
					nPlayerArrayGet = playerSet.get(nPlayerArrayI);
					switch(nPlayerArrayGet)
					{
						default:
						case 0:
							Player.nFlags = 0b001;
							break;
						case 1:
							Player.nFlags = 0b011;
							break;
						case 2:
							Player.nFlags = 0b101;
							break;
					}
					nPlayerArrayI++;
				}
				GetServer.bRoundStarted = true;
				ObjectifyService.ofy().save().entities(PlayerList, GetServer).now();
			}
			
			PrintWriter write = resp.getWriter();
			GameServer_Player Self = ObjectifyService.ofy().load().type(GameServer_Player.class).parent(GetServer).id(lID).now();
			write.print(Self.nFlags);
		}
	}
}
