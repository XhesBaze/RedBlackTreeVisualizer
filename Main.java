public class Main {
	public static void main(String[] args) {
		
		Integer[] array1 = {35,68,51};
		RedBlackTree<Integer> tree = new RedBlackTree<>(array1);
		tree.printInorder();
		System.out.println();

		System.out.println("1.INSERTION");
		tree.printInorder();
		System.out.println();
		System.out.println("Inserting 99:"); 
		tree.insert(99);
		tree.printInorder();
		System.out.println();
		System.out.println("Inserting 159:"); 
		tree.insert(159);
		tree.printInorder();
		System.out.println();
		System.out.println("Inserting 2:"); 
		tree.insert(2);
		tree.printInorder();
		System.out.println();
		System.out.println("Inserting 11:"); 
		tree.insert(11);
		tree.printInorder();
		System.out.println();
			

		System.out.println("2.DELETION");
		tree.printInorder();
		System.out.println();

		
		System.out.println("Deleting 99:");
		tree.delete(99);
		tree.printInorder();
		System.out.println();
		
		System.out.println("Deleting 2:");
		tree.delete(2);
		tree.printInorder();
		System.out.println();


		System.out.println("Deleting 11:");
		tree.delete(11);
		tree.printInorder();
		System.out.println();

		
		System.out.println("Deleting 158:");
		tree.delete(158);
		tree.printInorder();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
	}
}