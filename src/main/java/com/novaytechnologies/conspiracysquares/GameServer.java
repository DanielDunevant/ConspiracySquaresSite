package com.novaytechnologies.conspiracysquares;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.lang.Integer;
import java.lang.Boolean;
import java.lang.String;
import java.util.HashSet;
import java.util.HashMap;

import com.googlecode.objectify.ObjectifyService;

@Entity
public class GameServer {
	@Id public String ServerName;
	public String ServerPassword;

	public HashSet<String> Player_IPs = new HashSet<>();
	public HashMap<String, String> Player_Ports = new HashMap<>();
	public HashMap<String, String> Player_Update = new HashMap<>();
	
	public Integer NextID = 0;

	public GameServer() {}
	public GameServer(String strName, String strPass)
	{
		ServerName = strName;
		ServerPassword = strPass;
	}
}
