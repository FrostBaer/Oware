package homework;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

//the class that handles graphics (javafx)

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FxGraphics extends Application {

	private Controller cn;
	private MenuBar menuBar;
	private ArrayList <MyPoint2D> holes;
	private ArrayList <Text> texts;
			
    public static void fxMain(String[] args) {
        launch(args);
    }
    
    private void hInit() {
    	texts = new ArrayList <Text>();
    	holes = new ArrayList <MyPoint2D>();
    	for(int i = 0; i < 12; i++) {
    		this.holes.add(new MyPoint2D());
    	}
    }
    
    private void redraw(Canvas canvas, GraphicsContext gc) {
         this.redrawHoles(gc);
         this.redrawStones(gc);
         this.setLabels();
    }   
    
    public void drawNew(GraphicsContext gc) {
    	drawShapes(gc);
		drawHoles(gc);
        drawStones(gc);
        this.setLabels();
    }
    
    @Override
    public void start(Stage primaryStage) {
    	cn = Controller.getInstance();
    	this.hInit();
        primaryStage.setTitle("Oware game");
        Group root = new Group();
        Canvas canvas = new Canvas(1000, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawNew(gc);
        VBox vbox = createMenu(canvas, gc, primaryStage);
                  
        canvas.setOnMouseClicked(e-> this.select(e, canvas, gc));
        
        //set names
        Label player1 = new Label("Player 1:");
        TextField p1Text = new TextField ();
        Label player2 = new Label("Player 2:");
        TextField p2Text = new TextField ();
        Button set1 = new Button("Set");
        Button set2 = new Button("Set");
   
        set1.setOnAction(new EventHandler<ActionEvent>() {        	
        	    public void handle(ActionEvent e){
        	        if ((p1Text.getText() != null && !p1Text.getText().isEmpty())) {
        	            cn.getBoard().getPlayer(1).setName(p1Text.getText());
        	            System.out.println(cn.getBoard().getPlayer(1).getName());
        	        } else {System.out.println("Player1 ures");} 
        	    }
        	 });
        
        set2.setOnAction(new EventHandler<ActionEvent>(){        	
    	    public void handle(ActionEvent e) {
     	        if ((p2Text.getText() != null && !p2Text.getText().isEmpty())) {
    	            cn.getBoard().getPlayer(2).setName(p2Text.getText());
    	            System.out.println(cn.getBoard().getPlayer(2).getName());
    	        } else {System.out.println("Player2 ures");} 
    	     }
    	 });
        
        HBox hbox1 = new HBox();
        HBox hbox2 = new HBox();
        hbox1.setSpacing(10);
        hbox1.getChildren().addAll(player1, p1Text,  set1);
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(player2, p2Text, set2);
        BorderPane hpane = new BorderPane();
        hpane.setLeft(hbox1);
        hpane.setRight(hbox2);
        
        BorderPane bp = new BorderPane();
        bp.setTop(vbox);
        bp.setCenter(canvas);
        bp.setBottom(hpane);
        
        
        root.getChildren().add(createLabels());
        root.getChildren().add(bp);
        root.getChildren().add(vbox);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
 
       
    public void setLabels() {
	    for(int i = 0; i < texts.size(); i++) {
	    	texts.get(i).setText(Integer.toString(cn.getBoard().getHoles().get(i).getContent()));
	    }
    }
    
	public GridPane createLabels() {
	    GridPane grid = new GridPane();
	    
	    grid.setHgap(103);
	    grid.setVgap(300);
	    grid.setPadding(new Insets(17, 0, 0, 220));		
	    //grid.setGridLinesVisible(true);
	    
	    for(int i = 0; i < 12; i++) {
	    	texts.add(new Text(Integer.toString(cn.getBoard().getHoles().get(0).getContent())));
	    }
	    
	    for(int i = 0; i < texts.size(); i++) {
	    	texts.get(i).setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
	    	texts.get(i).setFill(Color.DARKSALMON);
	    }
	    
	    
    	for(int i = 0; i < 6; i++) {
	    	grid.add(texts.get(i), i, 1); 
	    }
    	
	    for(int i = 5, j = 6; i > -1 && j < texts.size(); i--, j++) {
	    	grid.add(texts.get(j), i, 0); 
	    }
    	
	
	    return grid;
	}
    
    public VBox createMenu(Canvas canvas, GraphicsContext gc, Stage stage) {  	
    	    	
    	Menu menu = new Menu("Menu");
    	MenuItem newGame = new MenuItem("New Game");
    	MenuItem loadGame = new MenuItem("Load Game");
        MenuItem saveGame = new MenuItem("Save Game");
        MenuItem exitGame = new MenuItem("Exit");
        
        EventHandler<ActionEvent> action = menuActions(canvas, gc, stage);

        newGame.setOnAction(action);
        loadGame.setOnAction(action);
        saveGame.setOnAction(action);
        exitGame.setOnAction(action);
        
        menu.getItems().add(newGame);
        menu.getItems().add(loadGame);
        menu.getItems().add(saveGame);
        menu.getItems().add(exitGame);
        
        menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        VBox vb = new VBox(menuBar);
        return vb;
    }
    
    public void drawStones(GraphicsContext gc) {
    	gc.setFill(Color.DARKSALMON);
    	gc.setStroke(Color.BISQUE);
    	gc.setLineWidth(2);
    	//draw stones to holes
    	
    	MyPoint2D p = new MyPoint2D(210.0f, 230.0f); 
    	
    	for(int j = 0; j < 6; j++) {	// 6 hole/row
	    		for(int k = 0; k < cn.getBoard().getHoles().get(j).getContent(); k++) {
	    			MyPoint2D rand = new MyPoint2D(0,0);
	    			rand.set(ThreadLocalRandom.current().nextInt((int)p.getX()-20, (int)p.getX()+20), ThreadLocalRandom.current().nextInt((int)p.getY()-20, (int)p.getY()+20));
					gc.fillOval(rand.getX(), rand.getY(), 20, 20);
					gc.strokeOval(rand.getX(), rand.getY(), 20, 20);
	    		}
				p.add(110.0f, 0.0f);
    		
		}
    	p.setY(110.0f);
    	p.subt(110.0f, 0);
    	for(int j = 6; j < 12; j++) {
    		
	    		for(int k = 0; k < cn.getBoard().getHoles().get(j).getContent(); k++) {
	    			MyPoint2D rand = new MyPoint2D(0,0);
	    			rand.set(ThreadLocalRandom.current().nextInt((int)p.getX()-20, (int)p.getX()+20), ThreadLocalRandom.current().nextInt((int)p.getY()-20, (int)p.getY()+20));
					gc.fillOval(rand.getX(), rand.getY(), 20, 20);
					gc.strokeOval(rand.getX(), rand.getY(), 20, 20);
	    		}
	    		p.subt(110.0f, 0.0f);
    		}
    	
    	//draw stones to collectors
    	gc.setFill(Color.ORANGE);
    	p.set(80.0f, 40.0f);
    	for(int i = 1; i <= 2 ; i++) {
    		//if(cn.getBoard().getCollector(i).getChanged() != -1) {
    			//System.out.println("Itt vagyok! coll= " + cn.getBoard().getCollector(i).getChanged());
	    		for(int k = 0; k < cn.getBoard().getCollector(i).getContent(); k++) {
	    			//System.out.println("Draw collector: " + i +" "+k);
	    			MyPoint2D rand = new MyPoint2D(0,0);
	    			rand.set(ThreadLocalRandom.current().nextInt((int)p.getX(), (int)p.getX()+60), ThreadLocalRandom.current().nextInt((int)p.getY(), (int)p.getY()+200));
					gc.fillOval(rand.getX(), rand.getY(), 20, 20);
					gc.strokeOval(rand.getX(), rand.getY(), 20, 20);
	    		}
    	//	}
			p.add(770.0f, 0.0f);
		}
    }
    
    public void redrawStones(GraphicsContext gc) {
    	
    	gc.setFill(Color.DARKSALMON);
    	gc.setStroke(Color.BISQUE);
    	gc.setLineWidth(2);
    	//draw stones to holes
    	
    	MyPoint2D p = new MyPoint2D(210.0f, 230.0f); 
    	
    	for(int j = 0; j < 6; j++) {	// 6 hole/row
    		if(cn.getBoard().getHoles().get(j).getChanged() != -1) {
    			System.out.println("Draw stones changed: " + cn.getBoard().getHoles().get(j).getChanged());
	    		for(int k = 0; k < cn.getBoard().getHoles().get(j).getChanged(); k++) {
	    			System.out.println("Draw stones: " + j +" "+k); //TODO: vhol itt van a hiba
	    			MyPoint2D rand = new MyPoint2D(0,0);
	    			rand.set(ThreadLocalRandom.current().nextInt((int)p.getX()-20, (int)p.getX()+20), ThreadLocalRandom.current().nextInt((int)p.getY()-20, (int)p.getY()+20));
					gc.fillOval(rand.getX(), rand.getY(), 20, 20);
					gc.strokeOval(rand.getX(), rand.getY(), 20, 20);
	    		}   		
	 		}
    		p.add(110.0f, 0.0f);
		}
    	p.setY(110.0f);
    	p.subt(110.0f, 0);
    	for(int j = 6; j < 12; j++) {
    		if(cn.getBoard().getHoles().get(j).getChanged() != -1) {
    			System.out.println("Draw stones changed: " + cn.getBoard().getHoles().get(j).getChanged());
	    		for(int k = 0; k < cn.getBoard().getHoles().get(j).getChanged(); k++) {
	    			System.out.println("Draw stones: " + j +" "+k); //TODO: vhol itt van a hiba
	    			MyPoint2D rand = new MyPoint2D(0,0);
	    			rand.set(ThreadLocalRandom.current().nextInt((int)p.getX()-20, (int)p.getX()+20), ThreadLocalRandom.current().nextInt((int)p.getY()-20, (int)p.getY()+20));
					gc.fillOval(rand.getX(), rand.getY(), 20, 20);
					gc.strokeOval(rand.getX(), rand.getY(), 20, 20);
	    		}
	    		
    		}
    		p.subt(110.0f, 0.0f);
    	}
    	
    	//draw stones to collectors
    	gc.setFill(Color.ORANGE);
    	p.set(80.0f, 40.0f);
    	for(int i = 1; i <= 2 ; i++) {
    		if(cn.getBoard().getCollector(i).getChanged() != -1) {
    			System.out.println("Itt vagyok! coll= " + cn.getBoard().getCollector(i).getChanged());
	    		for(int k = -1; k < cn.getBoard().getCollector(i).getChanged(); k++) {
	    			System.out.println("Draw collector: " + i +" "+k);
	    			MyPoint2D rand = new MyPoint2D(0,0);
	    			rand.set(ThreadLocalRandom.current().nextInt((int)p.getX(), (int)p.getX()+60), ThreadLocalRandom.current().nextInt((int)p.getY(), (int)p.getY()+200));
					gc.fillOval(rand.getX(), rand.getY(), 20, 20);
					gc.strokeOval(rand.getX(), rand.getY(), 20, 20);
	    		}
    		}
			p.add(770.0f, 0.0f);
		}
    }
    
    private void drawShapes(GraphicsContext gc) {
    	gc.setFill(Color.BURLYWOOD);
    	gc.setStroke(Color.SADDLEBROWN);
    	gc.setLineWidth(5);
    	gc.fillRoundRect(150, 40, 700, 300, 5, 5); //table
    	gc.strokeRoundRect(150, 40, 700, 300, 5, 5);
    	
    	gc.setFill(Color.NAVAJOWHITE); //collectors
    	gc.fillRoundRect(80, 40, 80, 300, 5, 5);
    	gc.strokeRoundRect(80, 40, 80, 300, 5, 5);
    	gc.fillRoundRect(850, 40, 80, 300, 5, 5);
    	gc.strokeRoundRect(850, 40, 80, 300, 5, 5);
    }
    
    public void drawHoles(GraphicsContext gc){
    	gc.setFill(Color.BISQUE); //holes
    	gc.setStroke(Color.SADDLEBROWN);
    	gc.setLineWidth(2);
    	MyPoint2D p = new MyPoint2D(180.0f, 190.0f); 
    	
    	for(int j = 0; j < 6; j++) {
    		this.holes.get(j).set(p.getX(), p.getY());
    		gc.fillOval(p.getX(), p.getY(), 100, 100);
			gc.strokeOval(p.getX(), p.getY(), 100, 100);
			p.add(110.0f, 0.0f);
		}
    	p.setY(80.0f);
    	p.subt(110.0f, 0);
    	for(int j = 6; j < 12; j++) {
    		this.holes.get(j).set(p.getX(), p.getY());
    		gc.fillOval(p.getX(), p.getY(), 100, 100);
			gc.strokeOval(p.getX(), p.getY(), 100, 100);
			p.subt(110.0f, 0.0f);
    	}
    }
    
    public void redrawHoles(GraphicsContext gc){
    	gc.setFill(Color.BISQUE); //holes
    	gc.setStroke(Color.SADDLEBROWN);
    	gc.setLineWidth(2);
    	MyPoint2D p = new MyPoint2D(180.0f, 190.0f); 
    	
    	for(int j = 0; j < 6; j++) {
    		if(cn.getBoard().getHoles().get(j).getChanged() != -1) {
    			System.out.println("Redraw hole " + j);
	    		this.holes.get(j).set(p.getX(), p.getY());
	    		gc.fillOval(p.getX(), p.getY(), 100, 100);
				gc.strokeOval(p.getX(), p.getY(), 100, 100);
    		}
    		p.add(110.0f, 0.0f);
		}
    	p.setY(80.0f);
    	p.subt(110.0f, 0);
    	for(int j = 6; j < 12; j++) {
    		if(cn.getBoard().getHoles().get(j).getChanged() != -1) {
    			System.out.println("Redraw hole " + j);
	    		this.holes.get(j).set(p.getX(), p.getY());
	    		gc.fillOval(p.getX(), p.getY(), 100, 100);
				gc.strokeOval(p.getX(), p.getY(), 100, 100);
    		}
    		p.subt(110.0f, 0.0f);
    	}
    }
    
    //EventHandlers
    
    private EventHandler<ActionEvent> menuActions(Canvas canvas, GraphicsContext gc, Stage stage) {
        return new EventHandler<ActionEvent>() {
        	        	
            public void handle(ActionEvent event) {
            	           
                MenuItem mItem = (MenuItem) event.getSource();
                String name = mItem.getText();
                if ("New Game".equalsIgnoreCase(name)) {
                	System.out.println("Starting new game...");
            		cn.newGame();
            		drawNew(gc);
                } else if ("Save Game".equalsIgnoreCase(name)) {
                	//TODO: mentsuk el a jatekot
                	System.out.println("Saving game...");
                	cn.save();                	
                } else if ("Exit".equalsIgnoreCase(name)) {
                	
                    System.out.println("Exit...");
                    
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Exit game");
                    alert.setHeaderText("All unsaved data will be lost.");
                    alert.setContentText("Do you want to exit anyway?");

                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK){
                        stage.hide();
                    } else {/*do nothing*/}
                    
                } else if ("Load Game".equalsIgnoreCase(name)) {
         
                    System.out.println("Loading game...");
                    cn.load();
                    drawNew(gc);
                }
            }
        };
     }

//  checks whether the mouse location is within the circle or not
    private void select(MouseEvent e, Canvas canvas, GraphicsContext gc) {
        double mouseX = e.getSceneX();
        double mouseY = e.getSceneY();
        boolean selected = false;
        for(int i = 0; i < 12; i++) {
        	selected = (mouseX >= this.holes.get(i).getX() && mouseX <= this.holes.get(i).getX() + 100) &&
                    (mouseY >= this.holes.get(i).getY() && mouseY <= this.holes.get(i).getY() + 100); 
            if(selected) {  
            	try{
            		cn.stepHole(i);           		
            		redraw(canvas, gc);
            		
            	} catch (Exception exc){
            		System.out.println("Exception! " + exc.getMessage());
            		if(exc.getMessage().equalsIgnoreCase("Wrong side!") || exc.getMessage().equalsIgnoreCase("This hole is empty!")){/*do nothing*/}
            		else {
            			redraw(canvas, gc);
            			Alert alert = new Alert(AlertType.INFORMATION);
            			alert.setTitle("Attention:");
            			alert.setHeaderText(null);
            			alert.setContentText(exc.getMessage());
            			alert.showAndWait();
            		}
            	}
            }
        }
    }
}
