/**
 * Created By: Tejas Prasad
 */
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

/**
 * The Zork class runs the Zork game with an editor
 * @author Tejas
 */
public class Zork {
	private static Scanner input;
	
	/**
	 * Asks the user for a file and creates the game from that file.
	 * Asks user then to play the game, edit or exit the program and save
	 * the tree to the file.
	 * @param args
	 */
	public static void main(String[] args){
		StoryTree story = null;
		boolean firstRun = true;
		input = new Scanner(System.in);
		String fileName, userInput = null;
		System.out.println("Hello and Welcome to Zork!\n");
		System.out.print("Please enter the file name: ");
		fileName = input.nextLine();
		do{
			try{
				if(firstRun){
					System.out.println("Loading game from file...\n");
					story = StoryTree.readTree(fileName);
					System.out.println("\nFile loaded!\n");
					firstRun = false;
				}

				userInput = mainMenu();

				switch(userInput){
				case "E":
					editTree(story);
					break;
				case "P":
					playTree(story);
					break;
				case "Q":
					System.out.println("\nGame being saved to " 
							+ fileName + "...\n");
					StoryTree.saveTree(fileName, story);
					System.out.println("\nSave successful!\n");
					System.out.println("\nProgram terminating normally.");
					return;
				default:
					System.out.println("That is not a valid option.");
				}
			}
			catch(InputMismatchException e){
				System.out.println("Input error");
			}
			catch(IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
			catch(FileNotFoundException e){
				System.out.println(e.getMessage());
			}
			catch(NodeNotPresentException e){
				System.out.println("\nError with node\n");
			}
			catch(TreeFullException e){
				System.out.println("\nTree is full\n");
			}
			catch(DataFormatException e){
				System.out.println(e.getMessage());
			}
		}while(userInput.charAt(0) != 'Q');
	}
	
	/**
	 * Displays an editor menu for the user to edit the game.
	 * @param tree -
	 * StoryTree to edit
	 */
	public static void editTree(StoryTree tree){
		String userInput = null;

		do{
			try{
				userInput = displayMenu();

				switch(userInput){
				case "V":
					System.out.println("\nPosition: " 
							+ tree.getCursorPosition());
					System.out.println("Option: " 
							+ tree.getCursorOption());
					System.out.println("Message: " 
							+ tree.getCursorMessage() + "\n");
					break;
				case "S":
					selectChild(tree);
					break;
				case "O":
					editCursor(tree, true);
					break;
				case "M":
					editCursor(tree, false);
					break;
				case "A":
					addChild(tree);
					break;
				case "P":
					tree.returnToParent();
					break;
				case "D":
					deleteChild(tree);
					break;
				case "R":
					tree.resetCursor();
					break;
				case "Q":
					System.out.println("\nExiting editor\n");
					break;
				default:
					System.out.println("That is not a valid option.\n");
				}
			}
			catch(InputMismatchException e){
				System.out.println("Input error\n");
			}
			catch(IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
			catch(NodeNotPresentException e){
				System.out.println("Error. Child does not exist.\n");
			} 
			catch(TreeFullException e){
				System.out.println("Tree is full at the cursor.\n");
			}
			catch(Exception e){
				
			}
		}while(userInput != "Q");
	}

	/**
	 * Plays the Zork game stored in the StoryTree
	 * @param tree -
	 * StoryTree which has the Zork game
	 */
	public static void playTree(StoryTree tree){
		String userInput = null;
		String[][] options = new String[3][2];
		System.out.println(tree.getCursorOption() + "\n");

		do{
			try{
				System.out.println(tree.getCursorMessage());

				options = tree.getOptions();
				
				for(int i = 0; i < 3; i++){
					if(options[i][0] == null || options[i][1] == null)
						continue;
					System.out.println((i + 1) + ") " + options[i][1]);
				}

				System.out.print("Please make a choice: ");
				userInput = input.next();
				System.out.println();
				
				if(userInput.toUpperCase().charAt(0) == 'C'){
					System.out.printf("\nProbability of a win at this point:"
							+ " %2.2f", tree.winProbability());
					System.out.println("%\n");
					continue;
				}
				
				tree.selectChild(userInput);
				if(tree.getCursor().isWinningNode())
					break;
				if(tree.getCursor().isLosingNode())
					break;
			}
			catch(IllegalArgumentException e){
				System.out.println(e.getMessage());
			}
			catch(NodeNotPresentException e){
				System.out.println("Error. Child does not exist.\n");
			}
		}while(!tree.getCursor().isWinningNode() 
				|| !tree.getCursor().isLosingNode());

		System.out.println(tree.getCursorMessage() + "\n");
		System.out.println("Thanks for playing.");
	}

	private static String mainMenu(){
		System.out.print("Would you like to edit(E), play(P), or quit(Q)? ");

		String temp = null;
		do{
			temp = input.nextLine();
		}while(temp.isEmpty());
		
		return temp.toUpperCase();
	}
	
	private static String displayMenu() {
		System.out.print("Zork Editor:\n"
				+ "V) View Cursor's position, option, and message.\n"
				+ "S) Select a child of this cursor(options are 1, 2, 3)\n"
				+ "O) Set the option of the cursor\n"
				+ "M) Set the message of the cursor\n"
				+ "P) Return the cursor to its parent node\n"
				+ "A) Add a child StoryNode to the cursor\n"
				+ "D) Delete one of the cursor's children and its subtree\n"
				+ "R) Move the cursor to the root of the tree\n"
				+ "Q) Remove slide at cursor\n\n"
				+ "Select a menu option:");
		
		String temp = null;
		do{
			temp = input.nextLine();
		}while(temp.isEmpty());
		
		return temp.toUpperCase();
	}
	
	private static void selectChild(StoryTree tree)
			throws IllegalArgumentException, NodeNotPresentException{
		int children = tree.getNumberChildren();
		switch(children){
		case 0:
			System.out.println("The node has no children. ");
			return;
		case 1:
			System.out.print("Please select a child[1]: ");
			break;
		case 2:
			System.out.print("Please select a child[1, 2]: ");
			break;
		case 3:
			System.out.print("Please select a child[1, 2, 3]: ");
			break;
		}
		String userInput = input.next();
		input.nextLine();
		tree.selectChild(userInput);
	}
	
	private static void deleteChild(StoryTree tree) 
			throws IllegalArgumentException, NodeNotPresentException{
		int children = tree.getNumberChildren();
		switch(children){
		case 0:
			System.out.print("The node has no children");
			return;
		case 1:
			System.out.print("Please select a child[1]: ");
			break;
		case 2:
			System.out.print("Please select a child[1, 2]: ");
			break;
		case 3:
			System.out.print("Please select a child[1, 2, 3]: ");
			break;
		}
		String userInput = input.next();
		input.nextLine();
		tree.removeChild(userInput);
		
		System.out.println("\nSubtree deleted.\n");
	}
	
	private static void addChild(StoryTree tree) throws TreeFullException{
		if(tree.getCursor().isLeaf())
			throw new TreeFullException();
		else{
			String option, message;
			System.out.print("Enter an option: ");
			option = input.nextLine();
			System.out.print("Enter a message: ");
			message = input.nextLine();
			
			tree.addChild(option, message);
			
			System.out.println("\nChild added.\n");
		}
	}
	
	private static void editCursor(StoryTree tree, boolean editOption){
		if(editOption){
			System.out.print("Please enter a new option: ");
			String userInput = input.nextLine();

			tree.setCursorOption(userInput);
			System.out.println("\nOption set.\n");
		}
		else{
			System.out.print("Please enter a new message: ");
			String userInput = input.nextLine();
			
			tree.setCursorMessage(userInput);
			System.out.println("\nMessage set.\n");
		}
	}
}
