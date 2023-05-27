package application;

// creating a stack class to hold the values in the tree
public class LinkedListStack<E> {
	
	// inner linked list class
	public class LinkedList<E> {
		
		//inner node class for the nodes
		private static class Node<E> {
			//needed data fields
			private E element;
			private Node<E> next;
			
			//constructor for setting the fields
			public Node(E e, Node<E> n) {
				this.setElement(e);
				this.setNext(n);
			}
			
			//getters and setters
			public E getElement() {
				return element;
			}

			public Node<E> getNext() {
				return next;
			}
			
			public void setElement(E element)
			{
				this.element = element;
			}
		
			public void setNext(Node<E> n) {
				this.next = n;
			}
			
		}
		
		//data fields for the list
		private Node<E> head ;
		private Node<E> tail ;
		private int size = 0;
		
		//setters
		public void setHead(Node<E> head)
		{
			this.head = head;
		}
		
		public void setTail(Node<E> tail)
		{
			this.tail = tail;
		}
		
		//default constructor
		public LinkedList() {
			
			this.setHead(null);
			this.setTail(null);
		}
		

		//returns the size of the list
		public int size() {
			return this.size;
		}
		
	    //returns if the size is empty
		public boolean isEmpty() {
			return this.size == 0;
		}

		//returns the first element of the list
		public E first() {
			if (isEmpty()) {
				return null;
			}
			return head.getElement();
		}
		
		//returns the last element of the list
		public E last() {
			if (isEmpty()) {
				return null;
			}
			return tail.getElement();
		}
		
		//adds the first element in the list
		public void addFirst(E e) {
			head = new Node<>(e, head);
			if (size == 0) {
				tail = head;
			}
			size++;
		}
		

		//adds the last element in the list
		public void addLast(E e) {
			Node<E> newest = new Node<>(e, null);
			if (isEmpty()) {
				head = newest;
			}
			else {
				tail.setNext(newest);
			}
			tail = newest;
			size++;
		}

		//removes the first element in the list
		public E removeFirst() {
			if (isEmpty()) {
				return null;
			}
			E answer = head.getElement();
			head = head.getNext();
			size--;
			if (size == 0) {
				tail = null;
			}
			return answer;
		}
	}
	
	//implementing stack with the linked list
	private LinkedList<E> stackList = new LinkedList<>();
	
	//default constructor
	public LinkedListStack() {}
	
	//return the size of the stack
	public int size() {
		return stackList.size();
	}
	
	//returns if the stack is empty
	public boolean isEmpty() {
		return stackList.isEmpty();
	}
	
	//adding elements
	public void push(E element) {
		stackList.addFirst(element);
	}
	
	//getting the first element, but not deleting it
	public E peek() {
		return stackList.first();
	}
	
	//deleting the first element
	public E pop() {
		return stackList.removeFirst();
	}
}