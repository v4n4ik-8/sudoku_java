import java.util.*;

public class SudokuLogic {

    // Проверка корректности хода
    public static boolean isValidMove(int[][] board, int row, int col, int num) {
          // Проверка строки
          for (int j = 0; j < 9; j++) {
                if (board[row][j] == num) return false;
          }
          // Проверка столбца
          for (int i = 0; i < 9; i++) {
                if (board[i][col] == num) return false;
          }
          // Проверка блока 3x3
          int startRow = (row / 3) * 3;
          int startCol = (col / 3) * 3;
          for (int i = startRow; i < startRow + 3; i++) {
                for (int j = startCol; j < startCol + 3; j++) {
                          if (board[i][j] == num) return false;
                }
          }
          return true;
    }


    // Создание пустой 9x9 матрицы
    public static int[][] createEmptyBoard() {
        return new int[9][9];
    }

    // Генерация полной решённой сетки (рекурсивный Backtracking)
    public static boolean fillBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    List<Integer> numbers = getShuffledNumbers();
                    for (int num : numbers) {
                        if (isValidMove(board, row, col, num)) {
                            board[row][col] = num;
                            if (fillBoard(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    // Случайный список чисел от 1 до 9
    private static List<Integer> getShuffledNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    // Удаление подсказок
    public static void removeNumbers(int[][] board, int clues) {
        int cellsToRemove = 81 - clues;
        Random rand = new Random();

        while (cellsToRemove > 0) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);

            if (board[row][col] != 0) {
                int backup = board[row][col];
                board[row][col] = 0;

                cellsToRemove--;
            }
        }
    }

    // Генерация судоку с разной сложностью
    public static int[][] generateSudoku(String difficulty) {
        int clues;
        switch (difficulty) {
            case "Easy": clues = 40; break;  // Больше чисел → проще
            case "Medium": clues = 32; break;
            case "Hard": clues = 25; break;
            default: clues = 40;
        }

        int[][] board = createEmptyBoard();
        fillBoard(board); 
        removeNumbers(board, clues);
        return board;
    }
}
