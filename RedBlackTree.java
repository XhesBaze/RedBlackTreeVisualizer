package application;

public class RedBlackTree<E extends Comparable<E>> {
	
	// needed data fields
	public TreeNode<E> root; // root 
	public TreeNode<E> leaf = new TreeNode<E>(null, "BLACK"); //leaf node, its color is always black
	
	// default constructor
	public RedBlackTree() {
		
	}
	
	public RedBlackTree(E[] array) {
		//constructor that takes an array as argument as uses it to store the values we want to insert into the tree
		if (array.length != 0) {
			for (int i = 0; i < array.length; i++) {
				insert(array[i]);
			}
		}
	}
	
	// method used for insertion
	public void insert(E element) {
		TreeNode<E> child = new TreeNode<>(element, "RED"); //new node, a child node is always red
		child.setLeft(leaf); //the successors of each child node are leafs at the beginning
		child.setRight(leaf); 
		if (isEmpty()) {
			// if the tree is empty, make the child node the root
			root = child;
		} else {
			//data fields we'll use for iterating the tree, we'll need to start from the root, so we copy its content to parent and current nodes
			TreeNode<E> parent = root;
			TreeNode<E> current = root;
			
			// if the node is not a leaf,the tree is not empty
			while (current != leaf) {
				//if the element we want to insert is smaller than the node's element, we need to make it the left child
				if (element.compareTo(current.getData()) < 0) {
					// current becomes the parent of the new element and we continue to its left child
					parent = current;
					current = current.getLeft();
					//if the element we want to insert is bigger than the node's element,we need to make it the right child
				} else if (element.compareTo(current.getData()) > 0) {
					//current becomes the parent of the new element and we continue to its right child
					parent = current;
					current = current.getRight();
				}
			}
			
			//inserting the element as the left/right child as determined 
			if (element.compareTo(parent.getData()) < 0) {
				parent.setLeft(child);
			} else {
				parent.setRight(child);
			}
			
			//setting the node's parent after insertion 
			child.setParent(parent);
		}
		
		
		insertionCases(child);
	}
	
	//helper method to adjust the colours of each node after insertion
	private void insertionCases(TreeNode<E> node) {
		
		//if the root's colour is red, we change it to black,since the root is always black
		if (root.getColor().equals("RED")) {
			
			root.setColor("BLACK");
		} 
		//  if the parent of the new node is black,it is correct, so we don't perform any actions
		else if (node.getParent().getColor().equals("BLACK")) {
			
		} else {
			// if the parent and the uncle of the node are red, we need to make them black, since siblings can't both be red
			TreeNode<E> parent = node.getParent();
			TreeNode<E> uncle = uncle(node);
			TreeNode<E> grandparent = grandparent(node);
			
			if (isRed(parent) && isRed(uncle)) {
				parent.setColor("BLACK");
				uncle.setColor("BLACK");
				//the parent of two black nodes is red
				grandparent.setColor("RED");
			} 
			//if parent is red and uncle is black, it means that the rule we mentioned above is broken, since two siblings need to be black always
			//to fix this we need to perform rotation
			else if (isRed(parent) && isBlack(uncle)) {
				
				//in the case of a LR rotation
				if (isRightChild(node) && isLeftChild(parent)) {
					rotateL(parent);
					TreeNode<E> temp = node;
					node = parent;
					parent = temp;
					
					//in case of a RL rotation
				} else if (isLeftChild(node) && isRightChild(parent)) {
					rotateR(parent);
					TreeNode<E> temp = node;
					node = parent;
					parent = temp;
				}
				
				// in case of a LL rotation
				if (isLeftChild(node) && isLeftChild(parent)) {
					parent.setColor("BLACK");
					grandparent.setColor("RED");
					rotateR(grandparent);
					
				//in case of a RR rotation
				} else if (isRightChild(node) && isRightChild(parent)) {
					parent.setColor("BLACK");
					grandparent.setColor("RED");
					rotateL(grandparent);
				}
			}
		}
	}
	
	//method used for deletion
	public void delete(E element) {
		try {
			//needed data fields
			TreeNode<E> temp = root;
			TreeNode<E> parent = null;
			
			//continue searching until we reach a leaf node
			while (temp != leaf) {
				//find the node which has the element we want to delete
				if (element.equals(temp.getData())) {
					break;
				}
				parent = temp;
				//determining the position of the node
				if (element.compareTo(temp.getData()) < 0) {
					temp = temp.getLeft();
				} else {
					temp = temp.getRight();
				}
			}
			
			//if we traversed the tree but didn't find the element
			if (temp == leaf) {
				throw new NullPointerException("The item cannot be found in the tree.");
			}
			
			// if a leaf node should be deleted
			if (isLeaf(temp)) {
				//if the root is the leaf node, set the root's reference to null
				if (temp.equals(root)) {
					root = null;
				} else {
					// if the node is a left child, set the left child of the parent to the leaf node
					if (isLeftChild(temp)) {
						parent.setLeft(leaf);
					} 
					// if the node is a right child, set the right child of the parent to the leaf node
					else if (isRightChild(temp)) {
						parent.setRight(leaf);
					}
					
					leaf.setParent(parent); // setting the parent after deletion
					
					//if the deleted node is black, set the color of leaf node as DoubleBlack, as an indicator that it needs fixing
					// call fixing colors to fix the color
					if (temp.getColor().equals("BLACK")) {
						leaf.setColor("DoubleBLACK");
						fixingColors(leaf);
					}
				}
			} 
			//if the node to be deleted has a child
			else if (numOfChildren(temp) == 1) {
				//finding the position of the child
				TreeNode<E> child;
				if (temp.getLeft() == leaf) {
					child = temp.getRight();
				} else {
					child = temp.getLeft();
				}
				
				//if the node is the root, set the child as the new root
				if (temp.equals(root)) {
					root = child;
				} 
				//if the node is a left child,set the left child of the parent to the child node
				else if (isLeftChild(temp)) {
					parent.setLeft(child);
				} else {
					// if the node is a right child
					parent.setRight(child);
				}
				child.setParent(parent); // setting the parent node after deletion
				
				if (child.getColor().equals("RED") || temp.getColor().equals("RED")) {
					// if the child or the deleted node is red, the child needs to be black
					child.setColor("BLACK");
				} else if (child.getColor().equals("BLACK") && temp.getColor().equals("BLACK")) {
					// if the child and the deleted node are black, set the child's colour to "DoubleBLACK" as an indicator that
					//the colours need to be fixed
					child.setColor("DoubleBLACK");
					fixingColors(child);
				}
			} 
			//if the node that will be deleted has two children
			else if (numOfChildren(temp) == 2) {
				if (temp.equals(root) && temp.getLeft() == leaf) {
					// if the node is the root and its left child is a leaf node,set the right child as the new root  
					//set its colour to black
					root = temp.getRight();
					root.setColor("BLACK");
				} else {
					// find the maximum value in the left subtree of the node to be deleted
					E max = max(temp);
					delete(max); // recursively delete the maximum value
					temp.setData(max); // replace the value of the node to be deleted with the maximum value
				}
			}
		} catch (NullPointerException e) {
			throw new NullPointerException("The item cannot be found in the tree.");
		}
	}
	
	// method to fix the colours of the nodes after a deletion
	private void fixingColors(TreeNode<E> node) {
		if (node.equals(root)) {
			// if the node is the root,set its colour to black
			node.setColor("BLACK");
		} else {
			TreeNode<E> sibling = sibling(node);
			TreeNode<E> parent = node.getParent();
			
			if (isRed(sibling)) {
				//if the sibling is red, we need to make it black
				if (isRightChild(node)) {
					rotateR(parent); // right rotation
				} else {
					rotateL(parent); // left rotation
				}
				sibling.setColor("BLACK"); // setting the color to black
				sibling = sibling(node); // updating the reference
			}
			
			if (!isBlack(sibling.getLeft()) || !isBlack(sibling.getRight())) {
				// if the sibling and its children are black, we need to fix the colours
				TreeNode<E> RC; // red child
				
				if (isLeftChild(sibling)) {
					// if the sibling is a left child
					if (isRed(sibling.getRight())) {
						// if the right child of the sibling is red, perform RL rotation
						RC = sibling.getRight();
						rotateL(sibling); // left rotation
						rotateR(parent); // right rotation
					} else {
						// if the right child of the sibling is black, perform a RR rotation
						RC = sibling.getLeft();
						rotateR(parent); // right rotation
					}
				} else {
					// if the sibling is a right child
					if (isRed(sibling.getLeft())) {
						// if the left child of the sibling is red, perform LR rotations
						RC = sibling.getLeft();
						rotateR(sibling); // right rotation
						rotateL(parent); // left rotation
					} else {
						// if the left child of the sibling is black, perform a LL rotation
						RC = sibling.getRight();
						rotateL(parent); // left rotation
					}
				}
				
				RC.setColor(parent.getColor()); // set the red child's colour to its parent's colour
				parent.setColor("BLACK"); // the parent becomes black
				sibling.setColor("BLACK"); // the sibling becomes black
			} else {
				//if the sibling and its children are black, the sibling must become red
				sibling.setColor("RED");
				
				if (parent.getColor().equals("BLACK")) {
					// if the parent is black, recursively call the function to fix its colour
					fixingColors(parent);
				} else {
					// if the parent is red, change its colour to black
					parent.setColor("BLACK");
				}
			}
		}
	}
	
	// method to perform left rotation
	private void rotateL(TreeNode<E> node) {
		TreeNode<E> temp = node.getRight();
		node.setRight(temp.getLeft());
		
		if (temp.getLeft() != leaf) {
			temp.getLeft().setParent(node);
		}
		
		temp.setParent(node.getParent());
		
		if (node.getParent() == null) {
			root = temp;
		} else if (node == node.getParent().getLeft()) {
			node.getParent().setLeft(temp);
		} else {
			node.getParent().setRight(temp);
		}
		
		temp.setLeft(node);
		node.setParent(temp);
	}
	
	// method to perform right rotation
	private void rotateR(TreeNode<E> node) {
		TreeNode<E> temp = node.getLeft();
		node.setLeft(temp.getRight());
		
		if (temp.getRight() != leaf) {
			temp.getRight().setParent(node);
		}
		
		temp.setParent(node.getParent());
		
		if (node.getParent() == null) {
			root = temp;
		} else if (node == node.getParent().getRight()) {
			node.getParent().setRight(temp);
		} else {
			node.getParent().setLeft(temp);
		}
		
		temp.setRight(node);
		node.setParent(temp);
	}
	
	// method to get the uncle
	private TreeNode<E> uncle(TreeNode<E> node) {
		TreeNode<E> parent = node.getParent();
		TreeNode<E> grandparent = grandparent(node);
		
		if (grandparent == null) {
			return null;
		}
		
		if (parent == grandparent.getLeft()) {
			return grandparent.getRight();
		} else {
			return grandparent.getLeft();
		}
	}
	
	// method to get the grandparent
	private TreeNode<E> grandparent(TreeNode<E> node) {
		if (node != null && node.getParent() != null) {
			return node.getParent().getParent();
		} else {
			return null;
		}
	}
	
	// method to check if a node is red
	private boolean isRed(TreeNode<E> node) {
		return node != null && node.getColor().equals("RED");
	}
	
	// method to check if a node is black
	private boolean isBlack(TreeNode<E> node) {
		return node != null && node.getColor().equals("BLACK");
	}
	
	// method to check if a node is a leaf node
	private boolean isLeaf(TreeNode<E> node) {
		return node != null && node.equals(leaf);
	}
	
	// method to check if a node is a left child
	private boolean isLeftChild(TreeNode<E> node) {
		if (node == null || node.getParent() == null) {
			return false;
		}
		return node.equals(node.getParent().getLeft());
	}
	
	// method to check if a node is a right child
	private boolean isRightChild(TreeNode<E> node) {
		if (node == null || node.getParent() == null) {
			return false;
		}
		return node.equals(node.getParent().getRight());
	}
	
	// method to find the sibling of a node
	private TreeNode<E> sibling(TreeNode<E> node) {
		if (node == null || node.getParent() == null) {
			return null;
		}
		if (node.equals(node.getParent().getLeft())) {
			return node.getParent().getRight();
		} else {
			return node.getParent().getLeft();
		}
	}
	
	// method to count the number of children of a node
	private int numOfChildren(TreeNode<E> node) {
		int count = 0;
		if (node.getLeft() != leaf) {
			count++;
		}
		if (node.getRight() != leaf) {
			count++;
		}
		return count;
	}
	
	//method to find the maximum value in the tree rooted at the given node
	private E max(TreeNode<E> node) {
		while (node.getRight() != leaf) {
			node = node.getRight();
		}
		return node.getData();
	}
	
	//method to find if a tree is empty
	 public boolean isEmpty() {
	        return root == leaf;
	    }
}
