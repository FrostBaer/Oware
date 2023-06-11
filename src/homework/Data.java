package homework;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Data implements Serializable{
	
	Board board;
	
	public Data() { board = Board.getInstance(); }
	
	public void save() {
		try
        {    
            //Saving of object in a file 
			
            FileOutputStream file = new FileOutputStream("oware.ser"); 
            System.out.println("1");
            ObjectOutputStream out = new ObjectOutputStream(file); 
            System.out.println("2");
              
            // Method for serialization of object 
            out.writeObject(board); 
            System.out.println("3");
            out.close(); 
            file.close(); 
              
            System.out.println("Board has been serialized"); 
  
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("An error occured during serialization.."); 
        } 
	}
	
	public Board load() {
		
		try
        {    
            // Reading the object from a file 
		
            FileInputStream file = new FileInputStream("oware.ser"); 
            ObjectInputStream in = new ObjectInputStream(file); 
              
            // Method for deserialization of object 
            board = (Board)in.readObject(); 
              
            in.close(); 
            file.close(); 
              
            System.out.println("Board has been deserialized.. "); 
            
      
        } 
          
        catch(IOException ex) 
        { 
            System.out.println("An error occured during deserialization.."); 
        } 
          
        catch(ClassNotFoundException ex) 
        { 
            System.out.println("ClassNotFoundException is caught"); 
        } 
  
		return board;
		
    } 
}
