package application;

//class for nodes of the red black tree
public class TreeNode<E extends Comparable<E>> {
	//needed data fields
	private E data;
	private String color;
	private TreeNode<E> left;
	private TreeNode<E> right;
	private TreeNode<E> parent;

	//constructor used when a new node is created
	public TreeNode(E data, String color) {
		this.setData(data);
		this.setColor(color);
	}
	
	//getters and setters 
	public E getData() {
		return this.data;
	}

	public void setData(E data) {
		this.data = data;
	}

	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public TreeNode<E> getLeft() {
		return this.left;
	}

	public void setLeft(TreeNode<E> left) {
		this.left = left;
	}

	public TreeNode<E> getRight() {
		return this.right;
	}

	public void setRight(TreeNode<E> right) {
		this.right = right;
	}

	public TreeNode<E> getParent() {
		return this.parent;
	}

	public void setParent(TreeNode<E> parent) {
		this.parent = parent;
	}
}