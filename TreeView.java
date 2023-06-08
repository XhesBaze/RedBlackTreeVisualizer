package application;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class TreeView<E extends Comparable<E>> extends BorderPane {
    private RedBlackTree<E> tree = new RedBlackTree<>();
    private double sideLength = 35; 
    private double vGap = 70; 

    public TreeView(RedBlackTree<E> tree) {
        this.tree = tree;
    }
	
    // displaying the tree in UI
    public void displayTree() {
        this.getChildren().clear();
        if (tree.root != null) {
            displayTree(tree.root, getWidth() / 2, vGap, getWidth() / 8);
        }
    }
	
    // displaying the nodes of the tree and their connections
    public void displayTree(TreeNode<E> root, double x, double y, double hGap) {
        // displaying left child and its connection
        if (root.getLeft() != null) {
            getChildren().add(new Line(x - hGap, y + vGap, x, y ));
            displayTree(root.getLeft(), x - hGap, y + vGap, hGap / 2);
        }
        // displaying right child and its connection
        if (root.getRight() != null) {
            getChildren().add(new Line(x + hGap, y + vGap, x, y ));
            displayTree(root.getRight(), x + hGap, y + vGap, hGap / 2);
        }
        
        // displaying the current node with its value and colour
        Rectangle rectangle = new Rectangle(x - sideLength / 2, y - sideLength / 2, sideLength, sideLength);
        rectangle.setArcWidth(10); // Set the horizontal arc size
        rectangle.setArcHeight(10); // Set the vertical arc size

        Text text = new Text();
        text.setText(root.getData() + "");
        text.setBoundsType(TextBoundsType.VISUAL);
        double textWidth = text.getLayoutBounds().getWidth();
        double textHeight = text.getLayoutBounds().getHeight();

        // center the text in the middle of the rectangle
        text.setX(x - textWidth / 2);
        text.setY(y + textHeight / 2);

        text.setStroke(Color.WHITE);
        if (root.getColor().equals("BLACK")) {
            rectangle.setFill(Color.BLACK);
        } else if (root.getColor().equals("RED")) {
            rectangle.setFill(Color.RED);
        }
        getChildren().addAll(rectangle, text);
    }
}
