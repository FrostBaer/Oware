package homework;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ControllerTest {

	Board board;
	
	@Before
	public void setUp() {
		board = Board.getInstance();
	}
	
	@After
	public void setDefault() {
		board.setDefault();
	}
	
	@Test
	public void testPlayerTurn() {
		Player p = new Player();
		p.setTurn();
		Assert.assertTrue(p.getTurn());
	}
	
	@Test
	public void testBoardInstance() {
		Board b = Board.getInstance(); 
		Assert.assertSame(board, b);
	}
	
	@Test
	public void testBoardConstructor() {
		Assert.assertEquals(12, board.getHoles().size());
		for(int i = 0; i < board.getHoles().size(); i++) {
			Assert.assertEquals(4, board.getHoles().get(i).getContent());
		}
	}
	
	@Test
	public void testBoardStep1() throws Exception{
		System.out.println("--testBoardStep1");
		board.step(0);

		Assert.assertEquals(0, board.getHoles().get(0).getContent());
		for(int i = 1; i < 5; i++) {
			Assert.assertEquals(5, board.getHoles().get(i).getContent());
		}
		for(int i = 5; i < 12; i++) {
			Assert.assertEquals(4, board.getHoles().get(i).getContent());
		}
	}
	
	@Test
	public void testBoardStep2() throws Exception{
		System.out.println("--testBoardStep2");
		board.setNext();
		board.step(10);
		
		Assert.assertEquals(0, board.getHoles().get(10).getContent());
		Assert.assertEquals(5, board.getHoles().get(11).getContent());
		
		for(int i = 0; i < 3; i++) {
			Assert.assertEquals(5, board.getHoles().get(i).getContent());
		}
		for(int i = 3; i < 10; i++) {
			Assert.assertEquals(4, board.getHoles().get(i).getContent());
		}
	}
	
	@Test
	public void testCollect() throws Exception{	
		System.out.println("--testCollect");
		board.step(0);		
		board.step(9);
		board.step(1);
		board.step(8);
		Assert.assertEquals(0, board.getCollector(1).getContent());
		Assert.assertEquals(2, board.getCollector(2).getContent());
		Assert.assertEquals(0, board.getHoles().get(0).getContent());
	}
	
	@Test
	public void testCollectMore1() throws Exception{	
		System.out.println("--testCollectMore1");
		
		for(int i = 6; i < 12; i++) { board.getHoles().get(i).setContent(2); }
		board.getCollector(1).setContent(12);
		board.getCollector(2).setContent(12);
		board.step(4);		
		
		for(int i = 6; i < 9; i++) { Assert.assertEquals(0, board.getHoles().get(i).getContent()); }
		Assert.assertEquals(21, board.getCollector(1).getContent());
		Assert.assertEquals(5, board.getHoles().get(5).getContent());
	}
	
	@Test
	public void testCollectMore2() throws Exception{	
		System.out.println("--testCollectMore2");
		
		for(int i = 7; i < 12; i++) { board.getHoles().get(i).setContent(1); }
		board.getCollector(1).setContent(7);
		board.getCollector(2).setContent(8);
		board.step(4);		
		
		for(int i = 7; i < 9; i++) { Assert.assertEquals(0, board.getHoles().get(i).getContent()); }
		Assert.assertEquals(11, board.getCollector(1).getContent());
		Assert.assertEquals(5, board.getHoles().get(6).getContent());
	}
	
	@Test (expected = Exception.class)
	public void testWinner() throws Exception{
		System.out.println("--testWinner");
		board.getHoles().get(7).setContent(2);
		board.getCollector(1).setContent(25);
		board.step(3);
	} 
	
	@Test
	public void testEmpty() {
		System.out.println("--testEmpty");
		Assert.assertFalse(board.empty(5, 8));
		for(int i = 0; i < 12; i++) {
			board.getHoles().get(i).setContent(0);
		}
		Assert.assertTrue(board.empty(0, 12));
	}
	
	@Test
	public void testSetNext1() throws Exception{
		System.out.println("--testSetNext1");
		board.setNext();
		Assert.assertFalse(board.getPlayer(1).getTurn());
		Assert.assertTrue(board.getPlayer(2).getTurn());
	}
	
	@Test(expected = Exception.class)
	public void testSetNext2() throws Exception{
		System.out.println("--testSetNext2");
		for(int i = 6; i < 12; i++) {
			board.getHoles().get(i).setContent(0);
		}
		board.step(0);
	}
	
	@Test(expected = Exception.class)
	public void testCheckFew() throws Exception{
		System.out.println("--testCheckFew");
		//setUp
		for(int i = 0; i < board.getHoles().size(); i++) {
			board.getHoles().get(i).setContent(0);
		}
		board.getCollector(1).setContent(22);
		board.getCollector(2).setContent(22);
		board.getHoles().get(0).setContent(1);
		board.getHoles().get(1).setContent(1);
		board.getHoles().get(8).setContent(2);
		
		//steps
		board.step(0);
		board.step(8);
		board.step(1);
		board.step(9);
		board.step(2);
		board.step(10);
		board.step(3);
	}
	
	@Test
	public void testChanged() throws Exception{
		System.out.println("--testChanged");
		for(int i = 6; i < 12; i++) {
			board.getHoles().get(i).setContent(1);
		}
		board.step(5);
		System.out.println(board.getCollector(1).getChanged());
		Assert.assertEquals(0, board.getHoles().get(5).getChanged());
		for(int i = 6; i < 12; i++) {
			Assert.assertEquals(0, board.getHoles().get(9).getChanged());
		}
		Assert.assertEquals(7, board.getCollector(1).getChanged());
	}
}


