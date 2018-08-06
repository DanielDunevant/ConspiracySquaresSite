package com.novaytechnologies.conspiracysquares;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.Key;

import java.lang.Integer;
import java.lang.Boolean;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;

import com.googlecode.objectify.ObjectifyService;

@Entity
public class GameServer {
	static final long lPlayerMAX = 32;
	
	@Id public String ServerName;
	public String ServerPassword;
	
	@Index public Integer nPlayers = -1;

	public Long StartTime = 0L;
	public Long RoundStartTime = 0L;
	public Boolean bRoundStarted = false;

	public void Restart()
	{
		StartTime = 0L;
		RoundStartTime = 0L;
		bRoundStarted = false;

		List<GameServer_Player> PlayerList = ObjectifyService.ofy().load().type(GameServer_Player.class).ancestor(this).list();
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
		
		ArrayList<GameServer_Player> PlayerList = new ArrayList<GameServer_Player>();
		for (long lPlayerNum = 0; lPlayerNum < lPlayerMAX; lPlayerNum++)
		{
			GameServer_Player newPlayer = new GameServer_Player(ServerName, lPlayerNum);
			newPlayer.Reset(true);
			PlayerList.add(newPlayer);
		}
		ObjectifyService.ofy().save().entities(PlayerList).now();
	}
}
