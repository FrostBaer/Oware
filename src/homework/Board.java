package homework;

//the class implementing the gameboard (singleton)

import java.util.ArrayList;
import java.io.Serializable;
import java.lang.Exception;

public class Board implements Serializable{
	private static Board instance = null;
	private Player p1;
	private Player p2;
	private Hole collector1;
	private Hole collector2;
	private ArrayList <Hole> holes;
	private int few; //hany kor ota van <x db ko a palyan
	
	private Board(){
		//2 jatekos, 2 gyujtorekesz, godrok
		few = 0;
		p1 = new Player("player1");
		p2 = new Player("player2");
		p1.setTurn(true);
		p2.setTurn(false);
		collector1 = new Hole(0);
		collector2 = new Hole(0);
		holes = new ArrayList<Hole>();
		for(int i = 0; i < 12; i++) {
			holes.add(new Hole());
	}
}
			
	public static Board getInstance() {
		if(instance == null)
			instance = new Board();
		return instance;
	}
	
	public void fxTest() {
		for(int i = 0; i < 12; i++) {
			this.holes.get(i).setContent(6);
		}
	}
	
	public void setDefault() {
		for(int i = 0; i < 12; i++) {
			this.holes.get(i).setContent(4);
		}
		p1.setTurn(true);
		p2.setTurn(false);
		collector1.setContent(0);
		collector2.setContent(0);
		few = 0;
	}
	
	public Hole getCollector(int i){
		if(i == 1) { return this.collector1; }
		else if(i == 2) { return this.collector2; }
		else return null;
	}
	public Player getPlayer(int i) {
		if(i == 1) { return this.p1; }
		else if(i == 2) { return this.p2; }
		else return null;
	}
	public ArrayList <Hole> getHoles(){ return this.holes; }
	
	private void incrFew() {
		if(collector1.getContent()+collector2.getContent() > 40) { 
			this.few++;
		}
		System.out.println("incrFew = " + this.few);
	}
	
	//megvaltoztatja a soron levo jatekost, ha az eppen kovetkezo tud mivel lepni, a step() hívja meg
	public void setNext() throws Exception{
		System.out.println("setNext");
		if(p2.getTurn()) {
			if(!(empty(0, 6))){
				p1.setTurn();
				p2.setTurn();
			}
			else { System.out.println(p1.getName() + " does not have any stones! It's " + p2.getName() + "'s turn!"); 
				throw(new Exception(p2.getName() + " does not have any stones! It's " + p1.getName() + "'s turn!"));
			}
		}
		else if(p1.getTurn()) {
			if(!(empty(6,12))) {
				p1.setTurn();
				p2.setTurn();
			}
			else { System.out.println(p2.getName() + " does not have any stones! It's " + p1.getName() + "'s turn!"); 
			throw(new Exception(p1.getName() + " does not have any stones! It's " + p2.getName() + "'s turn!"));
		}
		}
	}
	
	//a ket index kozotti godrok uresek-e
	public boolean empty(int a, int b) {
		boolean bool = true;
		for(int i = a; i < b; i++) {
			if(!(this.getHoles().get(i).getContent() == 0)) {
				bool = false;
			}
		}
		System.out.println("Empty? "+bool);
		return bool;
	}
	
	//a jatekos sajat godrot jelolt-e ki
	private boolean validStep(int hole) {
		if(p1.getTurn() && hole < 6 && hole >= 0)
			return true;
		if(p2.getTurn() && hole < 12 && hole >= 6)
			return true;
		return false;
	}

	public void checkFew() throws Exception{
		System.out.println("checkFew");
		if(this.few < 8) { return; } 
		int res = 48-this.collector1.getContent() + this.collector2.getContent();
		if(res%2 != 0) { res--; }
		this.collector1.addContent(res/2);
		this.collector1.addContent(res/2);
		throw new Exception("The winner is " + this.winner().getName() + "!"); 
	}
	
	//A parameterként kapott sorszamu godrot kiuriti es jobbra indulva egyenkent szetosztja a tartalmat a tobbi godorbe  
	public void step(int hole) throws Exception{
		System.out.println("Stepper");
		if(!(this.validStep(hole))){ throw new Exception("Wrong side!"); }
		
		for(int i=0; i<12; i++) {
			holes.get(i).setChanged(-1);
		}
		
		int hLast = -1;
		int cont = holes.get(hole).getContent();
		if(cont == 0) { throw new Exception("This hole is empty!"); }
		holes.get(hole).setContent(0);
		holes.get(hole).setChanged(0);
		//System.out.println("Uritett godor tartalma: " + holes.get(hole).getChanged());
		
		//while we have stone, decrease & add 1-1 to each hole beginned by the one after "hole" 
		if(hole == 11) { hole = -1; }
		for(int j = hole+1; cont > 0; cont--, j++) {
			holes.get(j).addContent();
			holes.get(j).setChanged(holes.get(j).getContent());
			hLast = j;
			if(j == 11)
				j = -1;
		} 
		System.out.println("hLast = "+ hLast);
		this.collect(hLast);
		this.incrFew();
		this.checkFew();
		this.setNext();
	}
	
	private Player winner() {
		if(this.collector1.getContent() >= 25)
			return p1;
		if(this.collector2.getContent() >= 25)
			return p2;
		return null;
	}
	
	//ha lepes vegen vannak begyujtendo kovek, azokat begyujti, parameterkent azt a godrot veszi at, amelyikbe utolsokent kerult ko.
	//TODO: mi van, ha valaki megnyerte
	private void collect(int hLast) throws Exception{
		System.out.println("Collector");
		
		collector1.setChanged(-1);
		collector2.setChanged(-1);
		
		Hole coll;
		if(p1.getTurn()) {
			coll = collector1;
		} else { coll = collector2; }
				
		while(collectOne(hLast, coll)) {
			if(hLast == 0) {
				hLast = 11;
			}
			else { hLast--; }				
		}
		
		if(this.winner() != null) { throw new Exception("The winner is " + this.winner().getName() + "!"); }
	}
	
	private boolean collectOne(int hLast, Hole coll) {
		System.out.println("CollectOne");
		//ha nem 2 vagy 3 db ko van az utolso godorben
		if(!(holes.get(hLast).getContent() == 2 || holes.get(hLast).getContent() == 3)){ return false; }
		// ha nem az ellenfel terfelere kerult az utolso ko
		if(!(hLast >=0 && hLast < 6 && p2.getTurn() || hLast >=6 && hLast < 12 && p1.getTurn())) { return false; }
		
		System.out.println("Before: "+coll.getChanged());
		coll.addContent(holes.get(hLast).getContent());
		coll.setChanged(coll.getChanged()+holes.get(hLast).getContent());
		System.out.println("After: "+coll.getChanged());
		this.holes.get(hLast).setChanged(0);
		this.holes.get(hLast).setContent(0);
		this.few = 0;
		return true;
	}
}
