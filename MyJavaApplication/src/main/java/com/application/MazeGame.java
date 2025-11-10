package com.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MazeGame extends JFrame {
    private static final int ROWS = 15, COLS = 15;
    private static final int CELL_SIZE = 32;
    private int[][] maze = new int[ROWS][COLS]; // 0-通路 1-墙
    private int playerX = 0, playerY = 0;
    private int endX = ROWS - 1, endY = COLS - 1;
    private java.util.List<Point> path = null; // 自动寻路路径

    public MazeGame() {
        setTitle("迷宫游戏 - 空格自动寻路");
        setSize(COLS * CELL_SIZE + 16, ROWS * CELL_SIZE + 39);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        generateMaze();
        JPanel panel = getJPanel();
        panel.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int dx = 0, dy = 0;
                if (e.getKeyCode() == KeyEvent.VK_UP) dx = -1;
                if (e.getKeyCode() == KeyEvent.VK_DOWN) dx = 1;
                if (e.getKeyCode() == KeyEvent.VK_LEFT) dy = -1;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) dy = 1;
                if (e.getKeyCode() == KeyEvent.VK_SPACE) { // 空格自动寻路并显示
                    path = findPath();
                    panel.repaint();
                    return;
                }
                int nx = playerX + dx, ny = playerY + dy;
                if (nx >= 0 && nx < ROWS && ny >= 0 && ny < COLS && maze[nx][ny] == 0) {
                    playerX = nx;
                    playerY = ny;
                    path = null; // 人手动移动时清除路径显示
                    if (playerX == endX && playerY == endY) {
                        JOptionPane.showMessageDialog(MazeGame.this, "恭喜通关！");
                        generateMaze();
                        playerX = playerY = 0;
                        path = null;
                    }
                    panel.repaint();
                }
            }
        });
        add(panel);
        setVisible(true);
        panel.requestFocusInWindow();
    }

    private JPanel getJPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLS; j++) {
                        if (maze[i][j] == 1) {
                            g.setColor(Color.BLACK);
                            g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        } else {
                            g.setColor(Color.WHITE);
                            g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                            g.setColor(Color.LIGHT_GRAY);
                            g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        }
                    }
                }
                // 自动路径高亮
                if (path != null) {
                    g.setColor(Color.BLUE);
                    for (Point p : path) {
                        g.fillRect(p.y * CELL_SIZE + CELL_SIZE/4, p.x * CELL_SIZE + CELL_SIZE/4, CELL_SIZE/2, CELL_SIZE/2);
                    }
                }
                // 终点
                g.setColor(Color.GREEN);
                g.fillRect(endY * CELL_SIZE, endX * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                // 玩家
                g.setColor(Color.RED);
                g.fillOval(playerY * CELL_SIZE + 4, playerX * CELL_SIZE + 4, CELL_SIZE - 8, CELL_SIZE - 8);
            }
        };
        panel.setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        panel.setFocusable(true);
        return panel;
    }

    // 递归回溯法生成迷宫
    private void generateMaze() {
        for (int i = 0; i < ROWS; i++)
            Arrays.fill(maze[i], 1);
        Random rand = new Random();
        dfs(0, 0, rand);
        maze[0][0] = 0;
        maze[ROWS-1][COLS-1] = 0;
    }

    private void dfs(int x, int y, Random rand) {
        maze[x][y] = 0;
        int[] d = {0, 1, 2, 3};
        for (int i = 0; i < d.length; i++) {
            int r = rand.nextInt(d.length);
            int tmp = d[i]; d[i] = d[r]; d[r] = tmp;
        }
        for (int dir : d) {
            int nx = x, ny = y;
            switch (dir) {
                case 0: nx = x - 2; break; // up
                case 1: nx = x + 2; break; // down
                case 2: ny = y - 2; break; // left
                case 3: ny = y + 2; break; // right
            }
            if (nx >= 0 && nx < ROWS && ny >= 0 && ny < COLS && maze[nx][ny] == 1) {
                maze[(x + nx) / 2][(y + ny) / 2] = 0; // 打通墙
                dfs(nx, ny, rand);
            }
        }
    }

    // BFS自动寻路，返回路径Point列表（包含起点和终点）
    private java.util.List<Point> findPath() {
        boolean[][] visited = new boolean[ROWS][COLS];
        Point[][] prev = new Point[ROWS][COLS];
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(playerX, playerY));
        visited[playerX][playerY] = true;

        int[] dx = {-1, 1, 0, 0}, dy = {0, 0, -1, 1};
        while (!queue.isEmpty()) {
            Point curr = queue.poll();
            if (curr.x == endX && curr.y == endY) break;
            for (int d = 0; d < 4; d++) {
                int nx = curr.x + dx[d], ny = curr.y + dy[d];
                if (nx >= 0 && nx < ROWS && ny >= 0 && ny < COLS &&
                        !visited[nx][ny] && maze[nx][ny] == 0) {
                    visited[nx][ny] = true;
                    prev[nx][ny] = curr;
                    queue.add(new Point(nx, ny));
                }
            }
        }
        // 回溯路径
        if (!visited[endX][endY]) return null; // 无解
        LinkedList<Point> path = new LinkedList<>();
        for (Point at = new Point(endX, endY); at != null; at = prev[at.x][at.y]) {
            path.addFirst(at);
        }
        return path;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MazeGame::new);
    }
}