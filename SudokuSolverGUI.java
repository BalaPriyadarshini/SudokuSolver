import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class SudokuSolverGUI {

    private static final int SIZE = 9;
    private static JTextField[][] textFields = new JTextField[SIZE][SIZE];

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        JPanel gridPanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                textFields[i][j] = new JTextField();
                textFields[i][j].setHorizontalAlignment(JTextField.CENTER);
                textFields[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                gridPanel.add(textFields[i][j]);
            }
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton solveButton = new JButton("Solve");
        solveButton.setFont(new Font("Arial", Font.BOLD, 18));
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] board = new int[SIZE][SIZE];
                
                // Parse user input into the board array
                try{
                    for (int i = 0; i < SIZE; i++) {
                        for (int j = 0; j < SIZE; j++) {
                            String text = textFields[i][j].getText();
                            board[i][j] = text.isEmpty() ? 0 : Integer.parseInt(text);
                        }
                    }
                }catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Solve the Sudoku
                if(solve(board)){
                    displaySolution(board);
                } else {
                    JOptionPane.showMessageDialog(frame, "This Sudoku puzzle cannot be solved.", "Unsolvable", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 18));
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        textFields[i][j].setText("");
                    }
                }
            }
        });

        buttonPanel.add(solveButton);
        buttonPanel.add(refreshButton);

        frame.add(gridPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static boolean solve(int[][] board) {
        int n = board.length;
        int row = -1;
        int col = -1;
        boolean empty = false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    row = i;
                    col = j;
                    empty = true;
                    break;
                }
            }
            if(empty == true) {
                break;
            }
        }
        if(empty == false){
            return true;
        }
        for (int number = 1; number <= 9; number++) {
            if(isSafe(board, row, col, number)) {
                board[row][col] = number;
                if(solve(board)){
                    return true;
                } 
                else{
                    board[row][col] = 0;
                }
            }
        }
        return false;
    }

    private static boolean isSafe(int[][] board, int row, int col, int num) {
        for (int i = 0; i < board.length; i++) {
            if(board[row][i] == num){
                return false;
            }
        }
        for(int[] nums : board){
            if (nums[col] == num) {
                return false;
            }
        }
        int sqt = (int)Math.sqrt(board.length);
        int rowStart = row - row % sqt;
        int colStart = col - col % sqt;
        for (int r = rowStart; r < rowStart + sqt; r++) {
            for (int c = colStart; c < colStart + sqt; c++) {
                if (board[r][c] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void displaySolution(int[][] board) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                textFields[i][j].setText(String.valueOf(board[i][j]));
            }
        }
    }
}
