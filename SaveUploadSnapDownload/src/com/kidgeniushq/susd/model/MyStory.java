package com.kidgeniushq.susd.model;


public class MyStory {
	private int viewCount;
	private String id,date;
	private byte[] snapData;

	public MyStory() {

	}
	public MyStory(int vc, String i, String d) {
this.viewCount=vc;
this.id=i;
this.date=d;
	}
	public void setImageBytes(byte[] data){
		this.snapData=data;
	}
	@Override
	public String toString(){
		return "Views:"+viewCount+" "+date;
		
	}
	public byte[] getData(){
		return this.snapData;
	}
}
