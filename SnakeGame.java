import javax.swing.*;

public class SnakeGame {
    public static void main(String[] args) {
        String[] levels = {"Easy ", "Medium ", "Hard "};
        int choice = JOptionPane.showOptionDialog(null, "Pick your Death Speed:", "Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, levels, levels[1]);

        int speed = switch (choice) {
            case 0 -> 200;
            case 2 -> 80;
            default -> 120;
        };

        JFrame window = new JFrame(" Snake Game");
        JLabel scoreBoard = new JLabel("Score: 0    Highest Score: 0");
        JButton resetBtn = new JButton("Reset your LIFE!");

        JPanel topBar = new JPanel();
        topBar.add(scoreBoard);
        topBar.add(resetBtn);

        GamePanel gameCanvas = new GamePanel(scoreBoard, speed);

        resetBtn.addActionListener(e -> {
            gameCanvas.restartGame();
            gameCanvas.requestFocusInWindow(); 
        });

        window.setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.add(topBar);
        window.add(gameCanvas);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        gameCanvas.requestFocusInWindow();
    }
}
