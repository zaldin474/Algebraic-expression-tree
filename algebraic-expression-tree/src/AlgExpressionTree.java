//-----------------------------------------------------
// Title: AlgExpressionTree Class
// Author: zeineddin zidan
// ID:99621968516
// Section: 1
// Assignment: 3
// Description: This class builds and manages algebraic expression trees.
//-----------------------------------------------------

import java.util.*;

public class AlgExpressionTree {
    private Node root;
    private final Map<String, Double> variables = new HashMap<>();
    private String originalExpression;
    private String expressionType;

    //--------------------------------------------------------
    // Summary: Constructor - builds the tree from expression and notation
    // Precondition: expression is not null, notation is valid ("prefix", "postfix", "infix")
    // Postcondition: root points to the constructed expression tree
    //--------------------------------------------------------
    public AlgExpressionTree(String expression, String type) {
        this.originalExpression = expression;
        this.expressionType = type;

        switch (type) {
            case "prefix":
                root = buildFromPrefix(expression);
                break;
            case "postfix":
                root = buildFromPostfix(expression);
                break;
            case "infix":
                root = buildFromInfix(expression);
                break;
            default:
                throw new IllegalArgumentException("Unsupported expression type: " + type);
        }
        System.out.println("Expression tree has been created from " + type + ": " + expression);
    }

    //--------------------------------------------------------
    // Summary: Sets the value for a variable
    //--------------------------------------------------------
    public void setVariable(char name, int value) {
        variables.put(String.valueOf(name), (double) value);
    }

    //--------------------------------------------------------
    // Summary: Evaluates the expression represented by the tree
    //--------------------------------------------------------
    public int evaluate() {
        return (int) evaluate(root);  // Cast the result to an integer
    }

    //--------------------------------------------------------
    // Summary: Displays the expression in postfix notation
    //--------------------------------------------------------
    public void displayPostfix() {
        if ("postfix".equals(expressionType)) {
            // Preserve original formatting for postfix
            System.out.println(originalExpression);
        } else {
            displayPostfix(root);
            System.out.println();
        }
    }


    //--------------------------------------------------------
    // Summary: Recursively evaluates the expression tree
    //--------------------------------------------------------
    private double evaluate(Node node) {
        if (node == null) return 0;

        if (!isOperator(node.value)) {
            if (Character.isLetter(node.value.charAt(0))) {
                // If the variable is not initialized, return 0 (or another default value)
                return variables.getOrDefault(node.value, 0.0);
            } else {
                return Double.parseDouble(node.value);
            }
        }

        double left = evaluate(node.left);
        double right = evaluate(node.right);

        switch (node.value) {
            case "+": return left + right;
            case "-": return left - right;
            case "*": return left * right;
            case "/": return right == 0 ? Double.NEGATIVE_INFINITY : left / right;
            default: throw new RuntimeException("Unknown operator: " + node.value);
        }
    }

    private void displayPostfix(Node node) {
        if (node == null) return;
        displayPostfix(node.left);
        displayPostfix(node.right);
        System.out.print(node.value + " ");
    }
  //--------------------------------------------------------
    // Summary: Builds tree from prefix expression
    //--------------------------------------------------------
    private Node buildFromPrefix(String expr) {
        Stack<Node> stack = new Stack<>();
        for (int i = expr.length() - 1; i >= 0; i--) {
            char ch = expr.charAt(i);
            if (Character.isWhitespace(ch)) continue;
            String s = String.valueOf(ch);
            if (isOperator(s)) {
                Node left = stack.pop();
                Node right = stack.pop();
                Node op = new Node(s);
                op.left = left;
                op.right = right;
                stack.push(op);
            } else {
                stack.push(new Node(s));
            }
        }
        return stack.pop();
    }

    //--------------------------------------------------------
    // Summary: Builds tree from postfix expression
    //--------------------------------------------------------
    private Node buildFromPostfix(String expr) {
        Stack<Node> stack = new Stack<>();
        for (char ch : expr.toCharArray()) {
            if (Character.isWhitespace(ch)) continue;
            String s = String.valueOf(ch);
            if (isOperator(s)) {
                Node right = stack.pop();
                Node left = stack.pop();
                Node op = new Node(s);
                op.left = left;
                op.right = right;
                stack.push(op);
            } else {
                stack.push(new Node(s));
            }
        }
        return stack.pop();
    }

    //--------------------------------------------------------
    // Summary: Builds tree from fully parenthesized infix expression
    //--------------------------------------------------------
    private Node buildFromInfix(String expr) {
        Stack<Node> operandStack = new Stack<>();
        Stack<Character> operatorStack = new Stack<>();

        for (int i = 0; i < expr.length(); i++) {
            char ch = expr.charAt(i);
            if (Character.isWhitespace(ch)) continue;

            if (Character.isLetterOrDigit(ch)) {
                operandStack.push(new Node(String.valueOf(ch)));
            } else if (ch == '(') {
                operatorStack.push(ch);
            } else if (ch == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    char op = operatorStack.pop();
                    Node right = operandStack.pop();
                    Node left = operandStack.pop();
                    Node newNode = new Node(String.valueOf(op));
                    newNode.left = left;
                    newNode.right = right;
                    operandStack.push(newNode);
                }
                operatorStack.pop();
            } else if (isOperator(String.valueOf(ch))) {
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(ch)) {
                    char op = operatorStack.pop();
                    Node right = operandStack.pop();
                    Node left = operandStack.pop();
                    Node newNode = new Node(String.valueOf(op));
                    newNode.left = left;
                    newNode.right = right;
                    operandStack.push(newNode);
                }
                operatorStack.push(ch);
            }
        }

        while (!operatorStack.isEmpty()) {
            char op = operatorStack.pop();
            Node right = operandStack.pop();
            Node left = operandStack.pop();
            Node newNode = new Node(String.valueOf(op));
            newNode.left = left;
            newNode.right = right;
            operandStack.push(newNode);
        }

        return operandStack.pop();
    }

    //--------------------------------------------------------
    // Summary: Checks if a character is an operator
    //--------------------------------------------------------
    private boolean isOperator(String s) {
        return "+-*/".contains(s);
    }


    //--------------------------------------------------------
    // Summary: Returns the precedence of an operator
    //--------------------------------------------------------
    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    //--------------------------------------------------------
    // Node class representing a single element in the expression tree
    //--------------------------------------------------------
    private static class Node {
        String value;
        Node left, right;

        Node(String value) {
            this.value = value;
        }
    }
}