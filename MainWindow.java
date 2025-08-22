import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

public class MainWindow {
    private JFrame window;
    private final JPanel grid = new JPanel(new GridLayout(9, 9));
    private final JTextField[][] cells = new JTextField[9][9];
    private final JPanel buttonPanel = new JPanel();
    private final JButton restartButton = new JButton("Рестарт");
    private final JComboBox<String> difficultyBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});

    private int[][] nums = new int[9][9];
    private String currentDifficulty = "Easy";

    private boolean[][] fixed = new boolean[9][9];

    public MainWindow() {
        window = new JFrame("Судоку");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 550);
        window.setLayout(new BorderLayout());
        window.setLocationRelativeTo(null);

        // Создаём поле 9x9
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(new Font("Arial", Font.BOLD, 18));

                // Границы 3x3
                Border border = BorderFactory.createMatteBorder(
                        (i % 3 == 0 ? 2 : 1),
                        (j % 3 == 0 ? 2 : 1),
                        (i % 3 == 2 ? 2 : 1),
                        (j % 3 == 2 ? 2 : 1),
                        Color.BLACK
                );
                cell.setBorder(border);

                final int row = i;
                final int col = j;

                // Универсальный метод проверки и записи
                Runnable commit = () -> {
                    if (fixed[row][col]) {
                        return; // Нельзя менять фиксированную клетку
                    }

                    String text = cell.getText().trim();
                    if (text.isEmpty()) {
                        nums[row][col] = 0;
                        return;
                    }
                    if (!text.matches("[1-9]")) {
                        JOptionPane.showMessageDialog(window, "Введите число от 1 до 9!");
                        cell.setText("");
                        nums[row][col] = 0;
                        return;
                    }
                    int value = Integer.parseInt(text);

                    // Игнорируем свою же клетку при проверке
                    int old = nums[row][col];
                    nums[row][col] = 0;
                    boolean ok = SudokuLogic.isValidMove(nums, row, col, value);
                    nums[row][col] = old;

                    if (ok) {
                        nums[row][col] = value;
                        cell.setForeground(Color.BLACK);
                    } else {
                        JOptionPane.showMessageDialog(window, "Нельзя ставить это число сюда!");
                        cell.setText("");
                    }
                };

                // Обработчик Enter
                cell.addActionListener(e -> commit.run());

                // Проверка при потере фокуса
                cell.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusLost(FocusEvent e) {
                        commit.run();
                    }

                    @Override
                    public void focusGained(FocusEvent e) {
                        highlightCell(row, col);
                    }
                });

                cells[i][j] = cell;
                grid.add(cell);

                if (fixed[i][j]) {
                    cells[i][j].setFont(new Font("Arial", Font.BOLD, 18));
                    cells[i][j].setForeground(Color.BLACK);
                } else {
                    cells[i][j].setFont(new Font("Arial", Font.PLAIN, 18));
                    cells[i][j].setForeground(Color.BLUE);
                }

            }
        }

        // Кнопки
        buttonPanel.add(restartButton);
        buttonPanel.add(difficultyBox);

        restartButton.addActionListener(e -> restartGame());
        difficultyBox.addActionListener(e -> {
            currentDifficulty = (String) difficultyBox.getSelectedItem();
            restartGame();
            startNewGame(currentDifficulty);
        });

        // Добавляем на окно
        window.add(grid, BorderLayout.CENTER);
        window.add(buttonPanel, BorderLayout.SOUTH);

        startNewGame(currentDifficulty);
    }

    public void show() {
        window.setVisible(true);
    }

    public void restartGame() {
        nums = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setText("");
                cells[i][j].setEditable(true);
                cells[i][j].setBackground(Color.WHITE);
            }
        }
    }

    public void startNewGame(String difficulty) {
          nums = SudokuLogic.generateSudoku(difficulty);
          for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                          if (nums[i][j] != 0) {
                                          cells[i][j].setText(String.valueOf(nums[i][j]));
                                          cells[i][j].setEditable(false);
                                          fixed[i][j] = true;  // ← фиксируем клетку
                          } else {
                                          cells[i][j].setText("");
                                          cells[i][j].setEditable(true);
                                          fixed[i][j] = false;
                          }
                }
          }
    }


    public void highlightCell(int row, int col) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setBackground(Color.WHITE);
            }
        }
        for (int j = 0; j < 9; j++) {
            cells[row][j].setBackground(new Color(220, 240, 255));
        }
        for (int i = 0; i < 9; i++) {
            cells[i][col].setBackground(new Color(220, 240, 255));
        }
        int blockX = (row / 3) * 3;
        int blockY = (col / 3) * 3;
        for (int i = blockX; i < blockX + 3; i++) {
            for (int j = blockY; j < blockY + 3; j++) {
                cells[i][j].setBackground(new Color(200, 230, 250));
            }
        }
        cells[row][col].setBackground(Color.YELLOW);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
