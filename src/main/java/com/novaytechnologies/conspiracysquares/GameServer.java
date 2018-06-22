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
	public String ServerPassword;
	public Integer PlayerCount = 0;
	
	public ArrayList<Boolean> Player_Updated = new ArrayList<>();
	
	public Integer PlayerIDs = 0;
	public Float CircleRadius = 32768f;
	
	public ArrayList<Float> Player_X = new ArrayList<>();
	public ArrayList<Float> Player_Y = new ArrayList<>();
	public ArrayList<Integer> Player_Flags = new ArrayList<>();
	public ArrayList<Integer> Player_Colors = new ArrayList<>();
	public ArrayList<String> Player_Names = new ArrayList<>();

	public GameServer() {}
	public GameServer(String strName, String strPass)
	{
		ServerName = strName;
		ServerPassword = strPass;
	}
}
