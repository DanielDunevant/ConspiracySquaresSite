package com.novaytechnologies.conspiracysquares;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.Key;

import java.lang.Integer;
import java.lang.Boolean;
import java.lang.String;
import java.util.HashSet;
import java.util.HashMap;

import com.googlecode.objectify.ObjectifyService;

@Entity
public class GameServer {
	static final long lPlayerMAX = 32;
	
	@Id public String ServerName;
	public String ServerPassword;
	
	@Index public Integer nPlayers = -1;

	public Long StartTime = 0;
	public Long RoundStartTime = 0;
	public Boolean bRoundStarted = false;

	public void Restart()
	{
		StartTime = 0;
		RoundStartTime = 0;
		bRoundStarted = false;

		List<GameServer_Player> PlayerList = ObjectifyService.ofy().load().type(GameServer_Player.class).parent(this).list().now();
		for (GameServer_Player Player: PlayerList)
		{
			Player.Reset(true);
		}
		ObjectifyService.ofy().save().entities(PlayerList).now();
	}

	public GameServer() {}
	public GameServer(String strName, String strPass)
	{
		ServerName = strName;
		ServerPassword = strPass;
		
		List<GameServer_Player> PlayerList;
		for (long lPlayerNum = 0; lPlayerNum < lPlayerMAX; lPlayerNum++)
		{
			GameServer_Player newPlayer = new GameServer_Player(ServerName, lPlayerNum);
			newPlayer.Reset(true);
			PlayerList.add(newPlayer);
		}
		ObjectifyService.ofy().save().entities(PlayerList).now();
	}
}
