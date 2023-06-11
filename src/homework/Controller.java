package homework;

public class Controller {
	private static Controller instance = null;
	private Board board;
	private Data d;
	
	private Controller(){
		board = Board.getInstance();
		d = new Data();
	}
	
	public Board getBoard() { return board; }
	
	public static Controller getInstance() {
		if(instance == null)
			instance = new Controller();
		return instance;
	}
	
	public void newGame() {
		board.setDefault();
	}
	
	public void stepHole(int hole) throws Exception{
		board.step(hole);
	}
	
	public void save() {
		d.save();
	}
	
	public void load() {
		board = d.load();
	}
}
