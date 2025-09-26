package com.finderfail.MatrixCalculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MatrixCalculator extends JFrame {
    private JTextField[][] matrixAFields;
    private JTextField[][] matrixBFields;
    private JTextArea resultArea;
    private JComboBox<Integer> sizeBox;
    public int size = 2;

    public MatrixCalculator() {
        super("Калькулятор матриц");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new BorderLayout());

        // Верхняя панель (выбор размера)
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Размер матриц:"));
        sizeBox = new JComboBox<>(new Integer[]{2,3,4,5,6,7,8,9,10});
        sizeBox.setSelectedItem(2);
        sizeBox.addActionListener(e -> rebuildMatrixFields());
        topPanel.add(sizeBox);
        add(topPanel, BorderLayout.NORTH);

        // Центральная панель (две матрицы + результат)
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        matrixAFields = createMatrixPanel(centerPanel, "Матрица A");
        matrixBFields = createMatrixPanel(centerPanel, "Матрица B");

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultArea);
        centerPanel.add(scroll);

        add(centerPanel, BorderLayout.CENTER);

        // Нижняя панель (кнопки)
        JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        addButton(bottomPanel, "A + B", e -> showResult(add(getMatrix(matrixAFields), getMatrix(matrixBFields))));
        addButton(bottomPanel, "A - B", e -> showResult(subtract(getMatrix(matrixAFields), getMatrix(matrixBFields))));
        addButton(bottomPanel, "A × B", e -> showResult(multiply(getMatrix(matrixAFields), getMatrix(matrixBFields))));
        addButton(bottomPanel, "Aᵀ", e -> showResult(transpose(getMatrix(matrixAFields))));
        addButton(bottomPanel, "tr(A)", e -> resultArea.setText("tr(A) = " + trace(getMatrix(matrixAFields))));
        addButton(bottomPanel, "det(A)", e -> resultArea.setText("det(A) = " + determinant(getMatrix(matrixAFields))));
        addButton(bottomPanel, "inv(A)", e -> showResult(inverse(getMatrix(matrixAFields))));
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTextField[][] createMatrixPanel(JPanel parent, String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setLayout(new GridLayout(size, size));
        JTextField[][] fields = new JTextField[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                fields[i][j] = new JTextField("", 1);
                fields[i][j].setHorizontalAlignment(JTextField.CENTER);
                // Ограничение ввода одного символа
                fields[i][j].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        if (((JTextField)e.getSource()).getText().length() >= 1) {
                            e.consume();
                        }
                    }
                });
                panel.add(fields[i][j]);
            }
        }
        parent.add(panel);
        return fields;
    }

    private void rebuildMatrixFields() {
        size = (Integer) sizeBox.getSelectedItem();
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Размер матриц:"));
        topPanel.add(sizeBox);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        matrixAFields = createMatrixPanel(centerPanel, "Матрица A");
        matrixBFields = createMatrixPanel(centerPanel, "Матрица B");
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultArea);
        centerPanel.add(scroll);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        addButton(bottomPanel, "A + B", e -> showResult(add(getMatrix(matrixAFields), getMatrix(matrixBFields))));
        addButton(bottomPanel, "A - B", e -> showResult(subtract(getMatrix(matrixAFields), getMatrix(matrixBFields))));
        addButton(bottomPanel, "A × B", e -> showResult(multiply(getMatrix(matrixAFields), getMatrix(matrixBFields))));
        addButton(bottomPanel, "Aᵀ", e -> showResult(transpose(getMatrix(matrixAFields))));
        addButton(bottomPanel, "tr(A)", e -> resultArea.setText("tr(A) = " + trace(getMatrix(matrixAFields))));
        addButton(bottomPanel, "det(A)", e -> resultArea.setText("det(A) = " + determinant(getMatrix(matrixAFields))));
        addButton(bottomPanel, "inv(A)", e -> showResult(inverse(getMatrix(matrixAFields))));
        add(bottomPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void addButton(JPanel panel, String label, ActionListener action) {
        JButton btn = new JButton(label);
        btn.addActionListener(action);
        panel.add(btn);
    }

    private double[][] getMatrix(JTextField[][] fields) {
        int n = fields.length;
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                try {
                    m[i][j] = Double.parseDouble(fields[i][j].getText());
                } catch (Exception e) {
                    m[i][j] = 0;
                }
            }
        }
        return m;
    }

    private void showResult(double[][] m) {
        if (m == null) {
            resultArea.setText("Операция невозможна");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (double[] row : m) {
            for (double val : row) {
                sb.append(String.format("%8.3f", val));
            }
            sb.append("\n");
        }
        resultArea.setText(sb.toString());
    }

    // --- Операции ---

    private double[][] add(double[][] a, double[][] b) {
        int n = a.length;
        double[][] r = new double[n][n];
        for (int i=0; i<n; i++)
            for (int j=0; j<n; j++)
                r[i][j] = a[i][j] + b[i][j];
        return r;
    }

    private double[][] subtract(double[][] a, double[][] b) {
        int n = a.length;
        double[][] r = new double[n][n];
        for (int i=0; i<n; i++)
            for (int j=0; j<n; j++)
                r[i][j] = a[i][j] - b[i][j];
        return r;
    }

    private double[][] multiply(double[][] a, double[][] b) {
        int n = a.length;
        double[][] r = new double[n][n];
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                r[i][j] = 0;
                for (int k=0; k<n; k++) {
                    r[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return r;
    }

    private double[][] transpose(double[][] a) {
        int n = a.length;
        double[][] r = new double[n][n];
        for (int i=0; i<n; i++)
            for (int j=0; j<n; j++)
                r[j][i] = a[i][j];
        return r;
    }

    private double determinant(double[][] a) {
        int n = a.length;
        if (n == 1) return a[0][0];
        if (n == 2) return a[0][0]*a[1][1] - a[0][1]*a[1][0];
        double det = 0;
        for (int col = 0; col < n; col++) {
            det += (col % 2 == 0 ? 1 : -1) * a[0][col] * determinant(minor(a, 0, col));
        }
        return det;
    }

    private double[][] minor(double[][] a, int row, int col) {
        int n = a.length;
        double[][] m = new double[n-1][n-1];
        int r = 0;
        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            int c = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                m[r][c] = a[i][j];
                c++;
            }
            r++;
        }
        return m;
    }

    private int trace(double[][] a) {
        int n = a.length;
        int t = 0;
        for (int i=0; i<n; i++) t += a[i][i];
        return t;
    }

    private double[][] inverse(double[][] a) {
        int n = a.length;
        double det = determinant(a);
        if (det == 0) return null;
        double[][] adj = new double[n][n];
        for (int i=0; i<n; i++) {
            for (int j=0; j<n; j++) {
                adj[j][i] = ((i+j) % 2 == 0 ? 1 : -1) * determinant(minor(a, i, j));
            }
        }
        double[][] inv = new double[n][n];
        for (int i=0; i<n; i++)
            for (int j=0; j<n; j++)
                inv[i][j] = adj[i][j] / det;
        return inv;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MatrixCalculator::new);
    }
}
