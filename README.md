# Zork
Emulation of the text-based game Zork in Java

This program uses a Ternary tree model to emulate the choices and paths available to a user in the game "Zork".
It allows the user to either play the game or edit the decision choices available or add new ones through a console-based menu.
The game details are stored in the file named Zork.txt and has a specific format. Each line represents a node or decision choice and these lines are separeted into three parts, the node location, the choice for that node, and the message of the node.

The node location has a format of a number from one to three and a hypen which joins the numbers to show a link.
For example, 1-1-1 means that this node is the left child of the left child of the root node.
1-3 means this node is the right child of the root node.
The file must always start with a location of 1 and the lines following it must be a preorder traversal of the tree.

This program was developed as an assignment for Stony Brook University's CSE214 course.
