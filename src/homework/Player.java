package homework;

import java.io.Serializable;

public class Player implements Serializable{
	private int points;
	private boolean myTurn;
	private String name;
	
	Player(){ this.points = 0; this.myTurn = false; this.name = ""; }
	Player(String s){ this.points = 0; this.myTurn = false; this.name = s; }
	Player(int p, boolean t, String n){ this.points = p; this.myTurn = t; this.name = n; } 
	public void setName(String n) { this.name = n; }
	public String getName() { return this.name; }
	public void setTurn() {
		if(this.myTurn == true)
				this.myTurn = false;
		else this.myTurn = true;
	
	}
	public void setTurn(boolean b) { this.myTurn = b; }
	public int getPoints() { return this.points; }
	public void addPoints(int p) {this.points += p; }
	public boolean getTurn() { return myTurn; }
}
