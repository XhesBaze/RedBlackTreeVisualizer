package application;

import java.util.ArrayList;

public class RedBlackTree<E extends Comparable<E>> {
	

	//needed data fields
	public TreeNode<E> root;
	public TreeNode<E> leaf = new TreeNode<E>(null, "BLACK");
	

	// default constructor
	public RedBlackTree() {
		
	}
	
	//constructor that takes an array as argument as uses it to store the values we want to insert into the tree
	public RedBlackTree(E[] array) {
		if (array.length != 0) {
			for (int i = 0; i < array.length; i++) {
				insert(array[i]);
			}
		}
	}
	
	// method used for insertion
	public void insert(E element) {

	    TreeNode<E> child = new TreeNode<>(element, "RED");//new node, a child node is always red
	    child.setLeft(leaf);  //the successors of each child node are leafs at the beginning
	    child.setRight(leaf); 

	    if (isEmpty()) {
	    	// if the tree is empty, make the child node the root
	        root = child;
	    } else {//data fields we'll use for iterating the tree, we'll need to start from the root, so we copy its content to parent and current nodes
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

	    //method to fix colours after insertion
	    fixingColoursAfterInsertion(child); 
	}

	//helper method to adjust the colours of each node after insertion
	private void fixingColoursAfterInsertion(TreeNode<E> node) {
		//if the root's colour is red, we change it to black,since the root is always black
	    if (root.getColor().equals("RED")) {
	        root.setColor("BLACK"); 
	    //  if the parent of the new node is black,it is correct, so we don't perform any actions
	    } else if (node.getParent().getColor().equals("BLACK")) {
	        
	    } else {
	    	// if the parent and the uncle of the node are red, we need to make them black, since siblings can't both be red
	        TreeNode<E> parent = node.getParent();
	        TreeNode<E> uncle = uncle(node);
	        TreeNode<E> grandparent = grandparent(node);

	        if (isRed(parent) && isRed(uncle)) {
	        	
	            parent.setColor("BLACK");
	            uncle.setColor("BLACK");
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

			// if a leaf node gets deleted
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
					leaf.setParent(parent);  // setting the parent after deletion
					
					
					//if the deleted node is black, set the colour of leaf node as DoubleBlack, as an indicator that it needs fixing
					// call fixing colours to fix the colour
					if (temp.getColor().equals("BLACK")) {
						leaf.setColor("DoubleBLACK");
						fixingColorsAfterDeletion(leaf);
					}
				}
			} 
			//if the node to be deleted has a child
			else if (numOfChildren(temp) == 1) { 
				TreeNode<E> child;
				//finding the position of the child
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
				// setting the parent node after deletion
				child.setParent(parent); 
				if (child.getColor().equals("RED") || temp.getColor().equals("RED")) { 
					// if the child or the deleted node is red, the child needs to be black
					child.setColor("BLACK");
				} else if (child.getColor().equals("BLACK") && temp.getColor().equals("BLACK")) {
					// if the child and the deleted node are black, set the child's colour to "DoubleBLACK" as an indicator that
					//the colours need to be fixed
					child.setColor("DoubleBLACK");
					fixingColorsAfterDeletion(child);
				}
			} 
			//if the node that will be deleted has two children
			else if (numOfChildren(temp) == 2) {
				// if the node is the root and its left child is a leaf node,set the right child as the new root  
				//set its colour to black
				if (temp.equals(root) && temp.getLeft() == leaf) {
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
	private void fixingColorsAfterDeletion(TreeNode<E> node) {
	    if (node.equals(root)) {
	    	// if the node is the root,set its colour to black
	        node.setColor("BLACK"); 
	    } else {
	        TreeNode<E> sibling = sibling(node);
	        TreeNode<E> parent = node.getParent();

	        if (isRed(sibling)) {
	        	//if the sibling is red, we need to make it black
	            if (isRightChild(node)) {
	                rotateR(parent);// right rotation 
	            } else {
	                rotateL(parent); // left rotation
	            }
	            sibling.setColor("BLACK"); // setting the colour to black
	            sibling = sibling(node); // updating the reference
	        }

	     // if the sibling and its children are black, we need to fix the colours
	        if (!isBlack(sibling.getLeft()) || !isBlack(sibling.getRight())) {
	            
	            TreeNode<E> RC; //red child

	         // if the sibling is a left child
	            if (isLeftChild(sibling)) {
	                if (isRed(sibling.getRight())) {
	                	// if the right child of the sibling is red, perform RL rotation
	                    RC = sibling.getRight(); 
	                    rotateL(sibling); 
	                    rotateR(parent); 
	                } else {
	                	// if the right child of the sibling is black, perform a RR rotation
	                    RC = sibling.getLeft();
	                    rotateR(parent);
	                }
	            } else {
	            	// if the left child of the sibling is red, perform LR rotation
	                if (isRed(sibling.getLeft())) {
	                    RC = sibling.getLeft();
	                    rotateR(sibling);
	                    rotateL(parent); 
	                } else {
	                	// if the left child of the sibling is black, perform a LL rotation
	                    RC = sibling.getRight();
	                    rotateL(parent); 
	                }
	            }

	            RC.setColor(parent.getColor()); // set the red child's colour to its parent's colour
	            sibling.setColor("BLACK"); //all the other nodes become black
	            parent.setColor("BLACK"); 
	            node.setColor("BLACK"); 
	            
	            // sibling and its children are black
	        } else if (isBlack(sibling) && isBlack(sibling.getLeft()) && isBlack(sibling.getRight())) {
	            if (isRed(parent)) {
	                sibling.setColor("RED"); 
	                parent.setColor("BLACK");
	                node.setColor("BLACK");
	            } else {
	                sibling.setColor("RED");
	                parent.setColor("DoubleBLACK"); 
	                node.setColor("BLACK");
	                fixingColorsAfterDeletion(parent);
	            }
	        }
	    }
	}

	//method to perform left rotation
	private void rotateL(TreeNode<E> root) {
		TreeNode<E> temp = root.getRight();
		temp.setParent(root.getParent()); 
		
		if (this.root.equals(root)) { 
			this.root = temp;
		}
		else if (isLeftChild(root)) {
			temp.getParent().setLeft(temp);
		}
		else if (isRightChild(root)) {
			temp.getParent().setRight(temp);
		}
		root.setRight(temp.getLeft());
		root.getRight().setParent(root); 
		temp.setLeft(root);
		temp.getLeft().setParent(temp); 
	}
	

	//method to perform right rotation
	private void rotateR(TreeNode<E> root) {
		TreeNode<E> temp = root.getLeft();
		temp.setParent(root.getParent()); 
		
		if (this.root.equals(root)) { 
			this.root = temp;
		}
		else if (isLeftChild(root)) {
			temp.getParent().setLeft(temp);
		}
		else if (isRightChild(root)) {
			temp.getParent().setRight(temp);
		}
		root.setLeft(temp.getRight());
		root.getLeft().setParent(root); 
		temp.setRight(root);
		temp.getRight().setParent(temp);
	}

	//method to find the max of the left subtree
	private E max(TreeNode<E> node) {
		TreeNode<E> current = node.getLeft();
		while (current.getRight() != leaf) {
			current = current.getRight();
		}
		return current.getData();
	}

	//method to check if a given element is in the tree
	public boolean find(E key) {
		TreeNode<E> current = root;
		while (current != leaf) {
			if (key.equals(current.getData())) {
				return true;
			}
			else if (key.compareTo(current.getData()) < 0) {
				current = current.getLeft();
			}
			else if (key.compareTo(current.getData()) > 0) {
				current = current.getRight();
			}
		}
		return false;
	}
	
	//method to return the depth of a tree
	public int depth(TreeNode<E> node) {
		if (!find(node.getData())) {
			throw new NullPointerException("The node cannot be found in the tree.");
		}
		if (node.equals(root)) { 
			return 0;
		}
		return 1 + depth(node.getParent());
	}

	//method to return the height of a tree
	public int height(TreeNode<E> node) {
		if (node == null) {
			return -1;
		}
		return 1 + Math.max(height(node.getLeft()), height(node.getRight()));
	}
	

	//method that returns if a tree is empty or not
	public boolean isEmpty() {
		return root == null;
	}

	//method that checks if a node is a leaf or not
	public boolean isLeaf(TreeNode<E> node) {
		if (node == leaf) {
			return false;
		}
		return (node.getLeft() == leaf) && (node.getRight() == leaf);
	}
	

	//method that checks if a node is a left child
	public boolean isLeftChild(TreeNode<E> node) {
		if (node.equals(root) || node.getParent().getLeft() == null) {
			return false;
		}
		return node.getParent().getLeft().equals(node);
	}
	

	//method that checks if a node is a right child
	public boolean isRightChild(TreeNode<E> node) {
		if (node.equals(root) || node.getParent().getRight() == null) {
			return false;
		}
		return node.getParent().getRight().equals(node);
	}
	

	//method that returns the sibling of a node
	public TreeNode<E> sibling(TreeNode<E> node) {
		if (node.equals(root)) {
			return null;
		}
		else if (isLeftChild(node)) {
			return node.getParent().getRight();
		}
		return node.getParent().getLeft();
	}
	

	//method that returns the uncle of a node
	public TreeNode<E> uncle(TreeNode<E> node) {
		if (node.equals(root) || node.equals(root.getLeft()) || node.equals(root.getRight())) {
			return null;
		}
		return sibling(node.getParent());
	}
	

	//method that returns the grandparent of a node
	public TreeNode<E> grandparent(TreeNode<E> node) {
		if (node.equals(root) || node.equals(root.getLeft()) || node.equals(root.getRight())) {
			return null;
		}
		return node.getParent().getParent();
	}
	
	//method to get the elements of the tree in order traversal
	public ArrayList<TreeNode<E>> inorder() {
		ArrayList<TreeNode<E>> list = new ArrayList<>();
		if (!isEmpty()) {
			LinkedListStack<TreeNode<E>> stack = new LinkedListStack<>();
			TreeNode<E> current = root;
			while (!stack.isEmpty() || current != leaf) {
				if (current != leaf) {
					stack.push(current);
					current = current.getLeft();
				}
				else {
					current = stack.pop();
					list.add(current);
					current = current.getRight();
				}
			}
		}
		return list;
	}
	
	//method to print the tree elements inorder
	public void printInorder() {
	    if (!isEmpty()) {
	        LinkedListStack<TreeNode<E>> stack = new LinkedListStack<>();
	        TreeNode<E> current = root;
	        while (!stack.isEmpty() || current != leaf) {
	            if (current != leaf) {
	                stack.push(current);
	                current = current.getLeft();
	            } else {
	                current = stack.pop();
	                System.out.println(current.getData()); 
	                current = current.getRight();
	            }
	        }
	    }
	}


	//method to get the number of children of a node
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
	

	//method that checks if a node is red
	private boolean isRed(TreeNode<E> node) {
		return node.getColor().equals("RED");
	}
	

	//method that checks if a node is black
	private boolean isBlack(TreeNode<E> node) {
		return node.getColor().equals("BLACK");
	}

}
