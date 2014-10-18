package com.kidgeniushq.susd.model;


public class MyStory {
	private int viewCount;
	private String id,date;
	private byte[] snapData;

	public MyStory() {

	}
	public MyStory(int vc, String i, String d, byte[] sd) {
this.viewCount=vc;
this.id=i;
this.date=d;
this.snapData=sd;
	}
	@Override
	public String toString(){
		return "Views:"+viewCount+" "+date;
		
	}
	public byte[] getData(){
		return this.snapData;
	}
}
