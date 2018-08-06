package com.novaytechnologies.conspiracysquares;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.Key;

import java.lang.Integer;
import java.lang.Boolean;
import java.lang.String;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

import com.googlecode.objectify.ObjectifyService;

@Entity
public class GameServer_Player {
	@Parent Key<GameServer> Server;
	@Id public Long PlayerID;

	public Boolean bActive = false;
	public Long LastUpdate = 0L;
	
	public Long lMoveN = 0L;
	public Float fPosX = 0f;
	public Float fPosY = 0f;
	public Float fSpeedX = 0f;
	public Float fSpeedY = 0f;
	
	public String strName = "";
	public Integer nColor = 0;
	public Integer nFlags = -1;
	
	public void Reset(boolean bRoundRestart)
	{
		if (!bRoundRestart)
		{
			bActive = false;
			strName = "";
		}

		Random randColor = new Random(System.currentTimeMillis());
		nColor = (255 & 0xff) << 24 | (randColor.nextInt(255) & 0xff) << 16 | (randColor.nextInt(255) & 0xff) << 8 | (randColor.nextInt(255) & 0xff);

		lMoveN = 0L;
		fPosX = 0f;
		fPosY = 0f;
		fSpeedX = 0f;
		fSpeedY = 0f;

		nFlags = (bActive) ? 1 : -1;
	}
	
	public GameServer_Player() {}
	public GameServer_Player(String strServer, Long lID)
	{
		Server = Key.create(GameServer.class, strServer);
		PlayerID = lID;
	}
}
