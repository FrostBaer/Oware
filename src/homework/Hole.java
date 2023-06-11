package homework;

import java.io.Serializable;

public class Hole implements Serializable{
	
	private int content;
	private int changed;
	
	public Hole() { this.content = 4; this.changed = -1; }
	public Hole(int c) { this.content = c; }
	public int getContent() { return this.content; }
	public void setChanged(int c) { this.changed = c; }
	public int getChanged() { return this.changed; }
	public void setContent(int c) { this.content = c; }
	public void addContent(int c) { this.content += c; }
	public void addContent() { this.content += 1; }
}
