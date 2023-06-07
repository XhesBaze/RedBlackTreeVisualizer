package application;

import java.util.ArrayList;

public class RedBlackTree<E extends Comparable<E>> {
	protected TreeNode<E> root;
	protected final TreeNode<E> leaf = new TreeNode<E>(null, "BLACK");
	
	public RedBlackTree() {}
	
	public RedBlackTree(E[] array) {
		if (array.length != 0) {
			for (int i = 0; i < array.length; i++) {
				insert(array[i]);
			}
		}
	}

	public void insert(E key) {
		TreeNode<E> child = new TreeNode<>(key, "RED");
		child.left = leaf; // at the beginning the left and right children are null
		child.right = leaf; 
		
		//if the tree is empty, make the new node the root
		if (isEmpty()) {
			root = child;
		}
		else {
				
			//does the insertion after the position of where the element should be is determined by the position() method
			    TreeNode<E> parent = position(key);
			    //inserts the element
				if (key.compareTo(parent.getData()) < 0) {
					parent.left = child;
				}
				else {
					parent.right = child;
				}
				child.parent = parent;
			
			}
		//does the color fix
		fixingColorsAfterInsertion(child); 
	}
	

	//determines the position of where the new element should be inserted
	private TreeNode<E> position(E key) {
		TreeNode<E> parent = root;
		TreeNode<E> current = root;
		while (current != leaf) { 
			if (key.compareTo(current.getData()) < 0) {
				parent = current;
				current = current.left;
			}
			else if (key.compareTo(current.getData()) > 0) {
				parent = current;
				current = current.right;
			}
		}
		return parent;
	}
	
	//method to fix colours after insertion
	private void fixingColorsAfterInsertion(TreeNode<E> node) {
		
		// first case: root is red, we need to change it to black
		if (root.color.equals("RED")) {
			root.color = "BLACK"; 
		}
		
		// second case: parent is black, it's the default rule, do nothing
		else if (node.parent.color.equals("BLACK")) {} 
		
		else {
			TreeNode<E> parent = node.parent;
			TreeNode<E> uncle = uncle(node);
			TreeNode<E> grandparent = grandparent(node);
			// third case: the parent and uncle are both red, two siblings cannot be red, we need to make one of them red
			if (isRed(parent) && isRed(uncle)) {
				parent.color = "BLACK"; 			// change parent to black
				uncle.color = "BLACK"; 			 	// change uncle to black
				grandparent.color = "RED"; 		// change grandparent to red, a red node can't have red children
				fixingColorsAfterInsertion(grandparent); 	// checking grandparent for any mistakes done
			}
			
			// case four one: inner grandchild (LR rotation)
			else if (parent.color.equals("RED") && uncle.color.equals("BLACK")) {
				
				if (isRightChild(node) && isLeftChild(parent)) {
					leftRotate(parent);
					
					// swap the node and the parent (the first part of the rotation)
					TreeNode<E> temp = node;
					node = parent;
					parent = temp;
				}
				
				// case four two : outer grandchild (RL rotation)
				else if (isLeftChild(node) && isRightChild(parent)) {
					rightRotate(parent);
					
					// swap the node and the parent (the second part of the rotation)
					TreeNode<E> temp = node;
					node = parent;
					parent = temp;
				}
				
				// case five one: LL rotation (parent and node cannot be both red)
				if (isLeftChild(node) && isLeftChild(parent)) {
					parent.color = "BLACK";		// change parent to black (parent becomes the new root, so it must be black)
					grandparent.color = "RED";	// change grandparent to red (it becomes the right child of the new root, so it must be red, because it becomes a sibling)
					rightRotate(grandparent);	// right rotate the grandparent (making the grandparent the right child)
				}
				
				// case five two: RR rotation
				else if (isRightChild(node) && isRightChild(parent)) {
					parent.color = "BLACK";		// change parent to black (parent becomes the new root, so it must be black)
					grandparent.color = "RED";	// change grandparent to red (it becomes the left child of the new root, so it must be red, because it becomes a sibling)
					leftRotate(grandparent);	// right rotate the grandparent (making the grandparent the left child)
				}
			}
		}
	}
	

	public void delete(E key) {
		if (isEmpty()) {
				System.out.println("Tree is empty.");
		}
		try {
			TreeNode<E> node = nodeToBeDeleted(key);
			TreeNode<E> parent = node.parent;
			if (isLeaf(node)) { // case 1: node is the only node in the tree
				if (node.equals(root)) { 
					root = null;
				}
				else {
					if (isLeftChild(node)) {
						parent.left = leaf;
					}
					else if (isRightChild(node)) {
						parent.right = leaf;
					}
					leaf.parent = parent; 
					if (node.color.equals("BLACK")) {
						leaf.color = "TEMPBLACK";
						fixTempBlack(leaf);
					}
				}
			}
			else if (numChildren(node) == 1) { // case 2: node has one child
				TreeNode<E> child;
				if (node.left == leaf) {
					child = node.right;
				}
				else {
					child = node.left;
				}
				if (node.equals(root)) {
					root = child;
				}
				else if (isLeftChild(node)) {
					parent.left = child;
				}
				else {
					parent.right = child;
				}
				child.parent = parent; 
				if (child.color.equals("RED") || node.color.equals("RED")) { // node & child cannot both be red
					child.color = "BLACK";
				}
				else if (child.color.equals("BLACK") && node.color.equals("BLACK")) {
					child.color = "TEMPBLACK";
					fixTempBlack(child);
				}
			}
			else if (numChildren(node) == 2) { // case 3: node has two children
				if (node.equals(root) && node.left == leaf) {
					root = node.right;
					root.color = "BLACK";
				}
				else {
					E max = maxLeftSubtree(node);
					delete(max);
					node.setData(max);
				}
			}
		} catch (NullPointerException e) {
			throw new NullPointerException("The item cannot be found in the tree.");
		}
	}
	

	private TreeNode<E> nodeToBeDeleted(E key) {
		TreeNode<E> current = root;
		while (current != leaf) {
			if (key.equals(current.getData())) {
				return current;
			}
			else if (key.compareTo(current.getData()) < 0) {
				current = current.left;
			}
			else if (key.compareTo(current.getData()) > 0) {
				current = current.right;
			}
		}
		throw new NullPointerException();
	}
	
	private void fixTempBlack(TreeNode<E> node) {
		
		// case 1: root is tempblack
		if (node.equals(root)) {
			node.color = "BLACK"; 
		}
		
		else {
			TreeNode<E> sibling = sibling(node);
			TreeNode<E> parent = node.parent;
			
			// case 2: sibling is red
			if (isRed(sibling)) {
				
				// case two one:  node is a right child
				if (isRightChild(node)) {
					sibling.color = "BLACK"; // change sibling to black
					parent.color = "RED"; 	// change parent to red
					rightRotate(parent); 	// right rotate the parent
				}
				
				// case two two: The node is a left child
				else if (isLeftChild(node)) {
					sibling.color = "BLACK"; // change sibling to black
					parent.color = "RED"; 	// change parent to red
					leftRotate(parent); 	// left rotate the parent
				}
				fixTempBlack(node); 
			}
			
			// case three: sibling has at least one red child
			else if (hasRedChild(sibling)) {
				TreeNode<E> RedChild; 
				
				// case three one: sibling is a left child
				if (isLeftChild(sibling)) {
					
					// right child of the sibling is red
					if (isRed(sibling.right)) {
						RedChild = sibling.right;
						leftRotate(sibling);		// LR the sibling
						rightRotate(parent);		// RR the parent
						RedChild.color = parent.color;// change RedChild to the color of parent
						sibling.color = "BLACK";		// change sibling to black
						parent.color = "BLACK";			// change parent to black
						node.color = "BLACK";			// change node to black
					}
					
					// left child of the sibling is red
					else if (isRed(sibling.left)) {
						RedChild = sibling.left;
						rightRotate(parent);			// RR the parent
						sibling.color = parent.color;	// change sibling to the color of parent
						RedChild.color = "BLACK";		// change RedChild to black
						parent.color = "BLACK";			// change parent to black
						node.color = "BLACK";			// change node to black
					}
				}
				
				// case three two: sibling is a right child
				else if (isRightChild(sibling)) {
					
					// left child of the sibling is red
					if (isRed(sibling.left)) {
						RedChild = sibling.left;
						rightRotate(sibling);		// RR the sibling
						leftRotate(parent);			// LR the parent
						RedChild.color = parent.color;// change RedChild to the color of parent
						sibling.color = "BLACK";	// change sibling to black
						parent.color = "BLACK";		// change parent to black
						node.color = "BLACK";		// change node to black
					}
					
					//  right child of the sibling is red
					else if (isRed(sibling.right)) {
						RedChild = sibling.right;
						leftRotate(parent);				// LL rotate the parent
						sibling.color = parent.color;	// change sibling to the color of parent
						RedChild.color = "BLACK";		// change RedChild to black
						parent.color = "BLACK";			// change parent to black
						node.color = "BLACK";			// change node to black
					}
				}
			}
			
			// Case four: sibling and both of its children are black 
			else if ((isBlack(sibling) && isBlack(sibling.left) && isBlack(sibling.right)) ||
					 (isBlack(sibling) && isDoubleBlack(sibling.left) && isDoubleBlack(sibling.right))) {
					 // second condition is used when the tempblack node is a leaf and the sibling's children are
					 // leafs, since changing leafs to tempblack changes all instances of leafs to tempblack
				
				// case four one: parent of sibling is red
				if (isRed(parent)) { 		// sibling and node share the same parent
					sibling.color = "RED";	// change sibling to red
					parent.color = "BLACK";	// change parent to black
					node.color = "BLACK";	// change node to black
				}
				
				// case four two: parent of sibling is black
				else if (isBlack(sibling.parent)) {
					sibling.color = "RED";	// change sibling to red
					parent.color = "TEMPBLACK";	// change parent to double black
					node.color = "BLACK";	// change node to black
					fixTempBlack(parent);	
				}
			}
		}
	}
	

	private E maxLeftSubtree(TreeNode<E> node) {
		TreeNode<E> current = node.left;
		while (current.right != leaf) {
			current = current.right;
		}
		return current.getData();
	}
	
	
	public boolean find(E key) {
		TreeNode<E> current = root;
		while (current != leaf) {
			if (key.equals(current.getData())) {
				return true;
			}
			else if (key.compareTo(current.getData()) < 0) {
				current = current.left;
			}
			else if (key.compareTo(current.getData()) > 0) {
				current = current.right;
			}
		}
		return false;
	}
	

	public int depth(TreeNode<E> node) {
		if (!find(node.getData())) {
			throw new NullPointerException("Cannot find the element in the tree.");
		}
		if (node.equals(root)) { 
			return 0;
		}
		return 1 + depth(node.parent); 
	}

	public int height(TreeNode<E> node) {
		if (node == null) {
			return -1;
		}
		return 1 + Math.max(height(node.left), height(node.right));
	}
	

	public boolean isEmpty() {
		return root == null;
	}
	
	
	public boolean isLeaf(TreeNode<E> node) {
		if (node == leaf) {
			return false;
		}
		return (node.left == leaf) && (node.right == leaf);
	}
	

	public boolean isLeftChild(TreeNode<E> node) {
		if (node.equals(root) || node.parent.left == null) {
			return false;
		}
		return node.parent.left.equals(node);
	}
	
	
	public boolean isRightChild(TreeNode<E> node) {
		if (node.equals(root) || node.parent.right == null) {
			return false;
		}
		return node.parent.right.equals(node);
	}
	
	
	public TreeNode<E> sibling(TreeNode<E> node) {
		if (node.equals(root)) {
			return null;
		}
		else if (isLeftChild(node)) {
			return node.parent.right;
		}
		return node.parent.left;
	}
	
	
	public TreeNode<E> uncle(TreeNode<E> node) {
		if (node.equals(root) || node.equals(root.left) || node.equals(root.right)) {
			return null;
		}
		return sibling(node.parent);
	}
	

	public TreeNode<E> grandparent(TreeNode<E> node) {
		if (node.equals(root) || node.equals(root.left) || node.equals(root.right)) {
			return null;
		}
		return node.parent.parent;
	}
	
	public ArrayList<TreeNode<E>> inorder() {
		ArrayList<TreeNode<E>> list = new ArrayList<>();
		if (!isEmpty()) {
			LinkedListStack<TreeNode<E>> stack = new LinkedListStack<>();
			TreeNode<E> current = root;
			while (!stack.isEmpty() || current != leaf) {
				if (current != leaf) {
					stack.push(current);
					current = current.left;
				}
				else {
					current = stack.pop();
					list.add(current);
					current = current.right;
				}
			}
		}
		return list;
	}
	

	private int numChildren(TreeNode<E> node) {
		int count = 0;
		if (node.left != leaf) {
			count++;
		}
		if (node.right != leaf) {
			count++;
		}
		return count;
	}
	
	
	private boolean isRed(TreeNode<E> node) {
		return node.color.equals("RED");
	}
	

	private boolean isBlack(TreeNode<E> node) {
		return node.color.equals("BLACK");
	}
	
	
	private boolean isDoubleBlack(TreeNode<E> node) {
		return node.color.equals("DOUBLEB");
	}
	
	
	private boolean hasRedChild(TreeNode<E> node) {
		if (isRed(node.left) || isRed(node.right)) {
			return true;
		}
		return false;
	}
	
	
	private void leftRotate(TreeNode<E> root) {
		TreeNode<E> node = root.right;
		node.parent = root.parent;
		
		// root of the entire tree is used as the root of the rotation
		if (this.root.equals(root)) { 
			this.root = node;
		}
		else if (isLeftChild(root)) {
			node.parent.left = node;
		}
		else if (isRightChild(root)) {
			node.parent.right = node;
		}
		root.right = node.left;
		root.right.parent = root; 
		node.left = root;
		node.left.parent = node;
	}
	
	
	private void rightRotate(TreeNode<E> root) {
		TreeNode<E> node = root.left;
		node.parent = root.parent; 
		
		//  root of the entire tree is used as the root of the rotation
		if (this.root.equals(root)) { 
			this.root = node;
		}
		else if (isLeftChild(root)) {
			node.parent.left = node;
		}
		else if (isRightChild(root)) {
			node.parent.right = node;
		}
		root.left = node.right;
		root.left.parent = root; 
		node.right = root;
		node.right.parent = node; 
	}
}
