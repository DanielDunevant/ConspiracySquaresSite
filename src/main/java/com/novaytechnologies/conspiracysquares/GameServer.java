package com.novaytechnologies.conspiracysquares;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.lang.Integer;
import java.lang.Float;
import java.lang.String;
import java.util.ArrayList;

import com.googlecode.objectify.ObjectifyService;

@Entity
public class GameServer {
	@Id public String ServerName;
	
	public Integer PlayerCount = 0;
	
	public ArrayList<Float> Player_X = new ArrayList<>();
	public ArrayList<Float> Player_Y = new ArrayList<>();
	public ArrayList<Integer> Player_Flags = new ArrayList<>();

	public GameServer() {}
	public GameServer(String strName) {ServerName = strName;}
}
