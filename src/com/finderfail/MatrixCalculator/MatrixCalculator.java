package com.finderfail.MatrixCalculator;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MatrixCalculator extends JFrame {
    private JTextField[][] matrixAFields;
    private JTextArea resultArea;
    private JComboBox<Integer> sizeBox;
    public int size = 2;

    public MatrixCalculator() {
        super("Калькулятор матриц");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Размер матрицы:"));
        sizeBox = new JComboBox<>(new Integer[]{2,3,4});
        sizeBox.setSelectedItem(2);
        sizeBox.addActionListener(e -> rebuildMatrixFields());
        topPanel.add(sizeBox);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1,2,10,10));
        matrixAFields = createMatrixPanel(centerPanel, "Матрица A");
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultArea);
        centerPanel.add(scroll);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        addButton(bottomPanel, "Определитель det(A)", e -> resultArea.setText("det(A) = " + determinant(getMatrix(matrixAFields))));
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JTextField[][] createMatrixPanel(JPanel parent, String title) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(title));
        panel.setLayout(new GridLayout(size, size));
        JTextField[][] fields = new JTextField[size][size];
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                fields[i][j] = new JTextField("",1);
                fields[i][j].setColumns(1);
                fields[i][j].setHorizontalAlignment(JTextField.CENTER);
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

    private void rebuildMatrixFields(){
        size = (Integer) sizeBox.getSelectedItem();
        getContentPane().removeAll();
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Размер матрицы:"));
        topPanel.add(sizeBox);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(1,2,10,10));
        matrixAFields = createMatrixPanel(centerPanel, "Матрица A");
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultArea);
        centerPanel.add(scroll);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        addButton(bottomPanel, "Определитель det(A)", e -> resultArea.setText("det(A) = " + determinant(getMatrix(matrixAFields))));
        add(bottomPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private void addButton(JPanel panel, String label, ActionListener action){
        JButton btn = new JButton(label);
        btn.addActionListener(action);
        panel.add(btn);
    }

    private double[][] getMatrix(JTextField[][] fields){
        int n = fields.length;
        double[][] m = new double[n][n];
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                try {
                    m[i][j] = Double.parseDouble(fields[i][j].getText());
                } catch(Exception e){
                    m[i][j] = 0;
                }
            }
        }
        return m;
    }

    private double determinant(double[][] a){
        int n = a.length;
        if(n==1) return a[0][0];
        if(n==2) return a[0][0]*a[1][1]-a[0][1]*a[1][0];
        double det=0;
        for(int col=0;col<n;col++){
            det += (col%2==0?1:-1)*a[0][col]*determinant(minor(a,0,col));
        }
        return det;
    }

    private double[][] minor(double[][] a,int row,int col){
        int n=a.length;
        double[][] m=new double[n-1][n-1];
        int r=0;
        for(int i=0;i<n;i++){
            if(i==row) continue;
            int c=0;
            for(int j=0;j<n;j++){
                if(j==col) continue;
                m[r][c]=a[i][j];
                c++;
            }
            r++;
        }
        return m;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(MatrixCalculator::new);
    }
}
