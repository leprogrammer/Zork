/**
 * The StoryTreeNode represents a segment of the story with 3 children.
 * @author Tejas
 */
public class StoryTreeNode {
	public static final String WIN_MESSAGE = "YOU WIN";
	public static final String LOSE_MESSAGE = "YOU LOSE";
	
	private String position;
	private String option;
	private String message;
	
	private StoryTreeNode leftChild;
	private StoryTreeNode middleChild;
	private StoryTreeNode rightChild;
	
	/**
	 * Default Constructor
	 */
	public StoryTreeNode(){
		leftChild = middleChild = rightChild = null;
	}
	
	/**
	 * Constructor with specific parameters for member variables
	 * @param position -
	 * Position of node
	 * @param option -
	 * Option of node
	 * @param message -
	 * Message of node
	 */
	public StoryTreeNode(String position, String option, String message){
		this.position = position;
		this.option = option;
		this.message = message;
	}
	
	/**
	 * Check if node has no children and is a leaf.
	 * @return
	 * True if it is a leaf
	 */
	public boolean isLeaf(){
		if(leftChild == null && middleChild == null && rightChild == null)
			return true;
		return false;
	}
	
	/**
	 * Check if node has 3 children
	 * @return
	 * True if all 3 children are not null
	 */
	public boolean fullTree(){
		if(leftChild != null && middleChild != null && rightChild != null)
			return true;
		return false;
	}
	
	/**
	 * Check if node is leaf and winning node
	 * @return
	 * True if node is leaf and winning node
	 */
	public boolean isWinningNode(){
		if(isLeaf() && message.contains(WIN_MESSAGE.subSequence(0, 
										WIN_MESSAGE.length())))
			return true;
		return false;
	}
	
	/**
	 * Check if node is leaf and losing node
	 * @return
	 * True if node is leaf and losing node
	 */
	public boolean isLosingNode(){
		if(isLeaf() && message.contains(LOSE_MESSAGE.subSequence(0, 
										LOSE_MESSAGE.length())))
			return true;
		return false;
	}
	
	/**
	 * Gets left child of node
	 * @return
	 * Left child of node
	 */
	public StoryTreeNode getLeftChild(){
		return leftChild;
	}
	
	/**
	 * Gets right child of node
	 * @return
	 * Right child of node
	 */
	public StoryTreeNode getRightChild(){
		return rightChild;
	}
	
	/**
	 * Gets middle child of node
	 * @return
	 * Middle child of node
	 */
	public StoryTreeNode getMiddleChild(){
		return middleChild;
	}
	
	/**
	 * Sets left child of node
	 * @param left -
	 * Node to set as left child
	 */
	public void setLeft(StoryTreeNode left){
		leftChild = left;
	}
	
	/**
	 * Sets middle child of node
	 * @param middle -
	 * Node to set as middle child
	 */
	public void setMiddle(StoryTreeNode middle){
		middleChild = middle;
	}
	
	/**
	 * Sets right child of node
	 * @param right -
	 * Node to set as right child
	 */
	public void setRight(StoryTreeNode right){
		rightChild = right;
	}
	
	/**
	 * Get position of node
	 * @return
	 * Position of node
	 */
	public String getPosition(){
		return position;
	}
	
	/**
	 * Get message of node
	 * @return
	 * Message of node
	 */
	public String getMessage(){
		return message;
	}
	
	/**
	 * Gets option of node
	 * @return
	 * Option of node
	 */
	public String getOption(){
		return option;
	}
	
	/**
	 * Sets message of node
	 * @param message -
	 * Message to set node
	 * @throws IllegalArgumentException
	 * message is null or empty string
	 */
	public void setMessage(String message)throws IllegalArgumentException{
		if(message == null || message == "")
			throw new IllegalArgumentException();
		this.message = message;
	}
	
	/**
	 * Sets option of node
	 * @param option -
	 * Option to set node
	 * @throws IllegalArgumentException
	 * option is null or empty string
	 */
	public void setOption(String option)throws IllegalArgumentException{
		if(option == null || option == "")
			throw new IllegalArgumentException();
		this.option = option;
	}
	
	/**
	 * Sets position of node
	 * @param position -
	 * Position to set node
	 * @throws IllegalArgumentException
	 * position is null or empty string
	 */
	public void setPosition(String position)throws IllegalArgumentException{
		if(option == null || option == "")
			throw new IllegalArgumentException();
		this.position = position;
	}
}
