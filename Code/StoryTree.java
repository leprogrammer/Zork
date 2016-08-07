import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.zip.DataFormatException;
/**
 * The StoryTree class represents the data structure for the Zork game.
 * @author Tejas
 */
public class StoryTree {
	private StoryTreeNode root;
	private StoryTreeNode cursor;
	private GameState state;
	
	/**
	 * Default Constructor
	 */
	public StoryTree(){
		root = new StoryTreeNode("root", "root", "Hello, welcome to Zork!");
		cursor = root;
		state = GameState.GAME_NOT_OVER;
	}
	
	/**
	 * Reads in a text file describing a StoryTree
	 * @param filename -
	 * Name of file to read from
	 * @return
	 * new StoryTree with data from file
	 * @throws IllegalArgumentException
	 * filename is empty or null
	 * @throws FileNotFoundException
	 * File not found
	 * @throws NodeNotPresentException
	 * Node at cursor is null
	 * @throws TreeFullException
	 * Subtree is full
	 * @throws DataFormatException 
	 * Data inconsistent with expected format
	 */
	public static StoryTree readTree(String filename) 
			throws IllegalArgumentException, 
			FileNotFoundException, NodeNotPresentException, 
			TreeFullException, DataFormatException{
		if(filename == null || filename == "")
			throw new IllegalArgumentException();

		StoryTree temp = new StoryTree();
		Scanner file = new Scanner(new File(filename));
		String position, option, message;
		while(file.hasNextLine()){
			String[] details = file.nextLine().split("\\|");
			if(details.length != 3)
				throw new DataFormatException("Inconsistent data format");
			position = details[0].trim();
			option = details[1].trim();
			message = details[2].trim();
			
			if(position.length() == 1){
				temp.getCursor().setPosition("1");
				temp.setCursorOption(option);
				temp.setCursorMessage(message);
				continue;
			}
			
			temp.selectChildStartRoot(position.substring(0, 
									  position.length() - 2));
			temp.addChild(position.substring(position.length() - 1), 
											 option, message);
		}
		
		file.close();
		return temp;
	}
	
	/**
	 * Saves the StoryTree to the indicated format
	 * @param filename -
	 * Name of file to save to
	 * @param tree -
	 * Tree to save in the file
	 * @throws IllegalArgumentException
	 * filename is empty or null or tree is null
	 * @throws FileNotFoundException
	 * File with specified name not found
	 */
	public static void saveTree(String filename, StoryTree tree)
			throws IllegalArgumentException, FileNotFoundException{
		if(filename == null || filename == "" || tree == null)
			throw new IllegalArgumentException();
		PrintWriter fileOut = new PrintWriter(filename);
		tree.resetCursor();
		StoryTreeNode temp = tree.getCursor();
		String details = tree.getDetails(temp);

		fileOut.write(details);

		fileOut.close();
	}
	
	/**
	 * Gets state of game
	 * @return
	 * Current state of the game
	 */
	public GameState getGameState(){
		return state;
	}
	
	/**
	 * Gets cursor
	 * @return
	 * StoryTreeNode at cursor
	 */
	public StoryTreeNode getCursor(){
		return cursor;
	}
	
	/**
	 * Gets position of cursor
	 * @return
	 * Position of cursor
	 */
	public String getCursorPosition(){
		return cursor.getPosition();
	}
	
	/**
	 * Gets message of cursor
	 * @return
	 * Message of cursor
	 */
	public String getCursorMessage(){
		return cursor.getMessage();
	}
	
	/**
	 * Gets option of cursor
	 * @return
	 * Option of Cursor
	 */
	public String getCursorOption(){
		return cursor.getOption();
	}
	
	/**
	 * Gets array of options for immediate child's of cursor
	 * @return
	 * Array of options for children of cursor
	 */
	public String[][] getOptions(){
		if(cursor.isLeaf())
			return new String[0][0];
		String[][] options = new String[3][2];
		if(cursor.getLeftChild() != null){
			options[0][0] = cursor.getLeftChild().getPosition();
			options[0][1] = cursor.getLeftChild().getOption();
		}
		if(cursor.getMiddleChild() != null){
			options[1][0] = cursor.getMiddleChild().getPosition();
			options[1][1] = cursor.getMiddleChild().getOption();
		}
		if(cursor.getRightChild() != null){
			options[2][0] = cursor.getRightChild().getPosition();
			options[2][1] = cursor.getRightChild().getOption();
		}
		
		return options;
	}
	
	/**
	 * Sets message for cursor
	 * @param message -
	 * Message to set cursor to
	 */
	public void setCursorMessage(String message){
		cursor.setMessage(message);
	}
	
	/**
	 * Sets option for cursor
	 * @param option -
	 * Option to set cursor to
	 */
	public void setCursorOption(String option){
		cursor.setOption(option);
	}
	
	/**
	 * Resets cursor to root
	 */
	public void resetCursor(){
		cursor = root;
	}
	
	/**
	 * Recursively selects node starting from root based on position
	 * @param position -
	 * Position of node to look for
	 * @throws NodeNotPresentException
	 * Node does not exist
	 * @throws IllegalArgumentException
	 * position is null or empty
	 */
	public void selectChildStartRoot(String position) 
			throws NodeNotPresentException, IllegalArgumentException{
		if(position == null || position == "")
			throw new IllegalArgumentException();
		
		StoryTreeNode temp = root;
		if(position.charAt(0) != '1')
			throw new NodeNotPresentException();
		if(position.length() == 1){
			cursor = root;
			return;
		}
		
		int i = 2;
		do{
			if(temp == null)
				throw new NodeNotPresentException();
			
			if(position.charAt(i) == '1')
				temp = temp.getLeftChild();
			else if(position.charAt(i) == '2')
				temp = temp.getMiddleChild();
			else if(position.charAt(i) == '3')
				temp = temp.getRightChild();
			else
				throw new NodeNotPresentException();
			i += 2;
		}while(i < position.length());
		
		if(temp.isWinningNode())
			state = GameState.GAME_OVER_WIN;
		if(temp.isLosingNode())
			state = GameState.GAME_OVER_LOSE;
		cursor = temp;
	}
	
	/**
	 * Selects child with indicated position
	 * @param position -
	 * position of node to set cursor to
	 * @throws NodeNotPresentException
	 * Node does not exist
	 * @throws IllegalArgumentException
	 * position is null or empty
	 */
	public void selectChild(String position) 
			throws NodeNotPresentException, IllegalArgumentException{
		if(position == null || position == "")
			throw new IllegalArgumentException();
		
		StoryTreeNode temp = cursor;
		
		switch(position.charAt(0)){
		case '1':
			if(temp.getLeftChild() == null)
				throw new NodeNotPresentException();
			temp = temp.getLeftChild();
			break;
		case '2':
			if(temp.getMiddleChild() == null)
				throw new NodeNotPresentException();
			temp = temp.getMiddleChild();
			break;
		case '3':
			if(temp.getRightChild() == null)
				throw new NodeNotPresentException();
			temp = temp.getRightChild();
			break;
		default:
			throw new IllegalArgumentException("Did not select node 1, 2, 3");
		}
		cursor = temp;
	}
	
	/**
	 * Adds a child under current cursor with specified option and message
	 * @param option -
	 * Option to set new node to
	 * @param message -
	 * Message to set new node to
	 * @throws TreeFullException
	 * All 3 child spots are full
	 * @throws IllegalArgumentException
	 * option or message are null or empty
	 */
	public void addChild(String option, String message) 
			throws TreeFullException, IllegalArgumentException{
		if(cursor.fullTree())
			throw new TreeFullException();
		if(option == null || message == null || option == "" || message == "")
			throw new IllegalArgumentException();
		
		if(cursor.getLeftChild() == null)
			cursor.setLeft(new StoryTreeNode(cursor.getPosition() + "-1", 
											 option, message));
		else if(cursor.getMiddleChild() == null)
			cursor.setMiddle(new StoryTreeNode(cursor.getPosition() + "-2", 
											   option, message));
		else
			cursor.setRight(new StoryTreeNode(cursor.getPosition() + "-3", 
											  option, message));
	}
	
	/**
	 * Adds new child under the current cursor
	 * @param position -
	 * Position of node to add
	 * @param option -
	 * Option of new node
	 * @param message -
	 * Message of new node
	 * @throws TreeFullException
	 * All 3 child spots are full
	 * @throws IllegalArgumentException
	 * Parameters are null or empty
	 */
	public void addChild(String position, String option, String message)
			throws TreeFullException, IllegalArgumentException{
		if(cursor.fullTree())
			throw new TreeFullException();
		if(option == null || message == null || option == "" || message == "")
			throw new IllegalArgumentException();
		
		if(position.charAt(0) == '1')
			cursor.setLeft(new StoryTreeNode(cursor.getPosition() + "-1", 
											 option, message));
		else if(position.charAt(0) == '2')
			cursor.setMiddle(new StoryTreeNode(cursor.getPosition() + "-2", 
											   option, message));
		else if(position.charAt(0) == '3')
			cursor.setRight(new StoryTreeNode(cursor.getPosition() + "-3", 
											  option, message));
	}
	
	/**
	 * Removes a child under the cursor
	 * @param position -
	 * Position of child to remove
	 * @return
	 * Child which was removed
	 * @throws NodeNotPresentException
	 * Node with indicated position not found
	 * @throws IllegalArgumentException
	 * position is null or empty
	 */
	public StoryTreeNode removeChild(String position) 
			throws NodeNotPresentException, IllegalArgumentException{
		if(position == null || position == "")
			throw new IllegalArgumentException();

		StoryTreeNode temp = cursor;
		switch(position.charAt(0)){
		case '1':
			if(temp.getLeftChild() == null)
				throw new NodeNotPresentException();
			temp = temp.getLeftChild();
			cursor.setLeft(null);
			break;
		case '2':
			if(temp.getMiddleChild() == null)
				throw new NodeNotPresentException();
			temp = temp.getMiddleChild();
			cursor.setMiddle(null);
			break;
		case '3':
			if(temp.getRightChild() == null)
				throw new NodeNotPresentException();
			temp = temp.getRightChild();
			cursor.setRight(null);
			break;
			
		default:
			throw new IllegalArgumentException("Did not select node 1, 2, 3");
		}
		return temp;
	}
	
	/**
	 * Returns cursor to parent node
	 */
	public void returnToParent(){
		StoryTreeNode temp = root;
		if(cursor == temp.getLeftChild() || cursor == temp.getMiddleChild()
				|| cursor == temp.getRightChild()){
			cursor = temp;
		}
		else{
			returnToParent(temp);
		}
	}
	
	private void returnToParent(StoryTreeNode startNode){
		if(startNode == null)
			return;
		if(cursor == startNode.getLeftChild() 
				|| cursor == startNode.getMiddleChild() 
				|| cursor == startNode.getRightChild()){
			cursor = startNode;
		}
		else{
			returnToParent(startNode.getLeftChild());
			returnToParent(startNode.getMiddleChild());
			returnToParent(startNode.getRightChild());
		}
	}
	
	private String getDetails(StoryTreeNode node){
		String details = node.getPosition() + " | " + node.getOption() 
											+ " | " + node.getMessage();
		
		if(node.getLeftChild() != null)
			details += "\n" + getDetails(node.getLeftChild());
		if(node.getMiddleChild() != null)
			details += "\n" + getDetails(node.getMiddleChild());
		if(node.getRightChild() != null)
			details += "\n" + getDetails(node.getRightChild());
		
		return details;
	}
	
	public int getNumberChildren(){
		if(cursor.isLeaf())
			return 0;
		else if(cursor.getLeftChild() != null 
				&& cursor.getMiddleChild() == null 
				&& cursor.getRightChild() == null)
			return 1;
		else if(cursor.getLeftChild() != null 
				&& cursor.getMiddleChild() != null 
				&& cursor.getRightChild() == null)
			return 2;
		else
			return 3;
	}
	
	private int getNumberOfNodes(StoryTreeNode node){
		if(node.isLeaf())
			return 1;
		
		int sum = 1;
		if(node.getLeftChild() != null)
			sum += getNumberOfNodes(node.getLeftChild());
		if(node.getMiddleChild() != null)
			sum += getNumberOfNodes(node.getMiddleChild());
		if(node.getRightChild() != null)
			sum += getNumberOfNodes(node.getRightChild());
		
		return sum;
	}
	
	private int getNumberWinningNodes(StoryTreeNode node){
		if(node.isWinningNode())
			return 1;
		int sum = 0;
		
		if(node.getLeftChild() != null)
			sum += getNumberWinningNodes(node.getLeftChild());
		if(node.getMiddleChild() != null)
			sum += getNumberWinningNodes(node.getMiddleChild());
		if(node.getRightChild() != null)
			sum += getNumberWinningNodes(node.getRightChild());
		
		return sum;
	}
	
	/**
	 * Gets probability of a win from current node
	 * @return
	 */
	public double winProbability(){
		int totalNumberOfNodes = getNumberOfNodes(cursor);
		int totalWinningNodes = getNumberWinningNodes(cursor);
		double probability = (double)totalWinningNodes 
							/ (double)totalNumberOfNodes;
		
		return probability * 100;
	}
}
