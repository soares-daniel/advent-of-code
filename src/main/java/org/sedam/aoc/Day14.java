package org.sedam.aoc;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class Day14 extends Day {
    static final int GRID_WIDTH = 101;
    static final int GRID_HEIGHT = 103;

    static final int TILE_SIZE = 15;

    static final int CONSECUTIVE_THRESHOLD = 7;

    static int currentFrameIndex = 0;
    private boolean isPlaying = true;
    private JLabel timeLabel;
    private RobotPanel panel;

    private final List<List<Robot>> robotStates = new ArrayList<>();

    class Robot {
        int px, py, vx, vy;

        public Robot(int px, int py, int vx, int vy) {
            this.px = px;
            this.py = py;
            this.vx = vx;
            this.vy = vy;
        }

        public void updatePosition() {
            px += vx;
            py += vy;

            px = ((px % GRID_WIDTH) + GRID_WIDTH) % GRID_WIDTH;
            py = ((py % GRID_HEIGHT) + GRID_HEIGHT) % GRID_HEIGHT;
        }
    }

    public String part1(List<String> input) {
        int[][] grid = new int[GRID_WIDTH][GRID_HEIGHT];

        for (String line : input) {
            String[] parts = line.split(" ");
            int px = Integer.parseInt(parts[0].substring(2, parts[0].indexOf(',')));
            int py = Integer.parseInt(parts[0].substring(parts[0].indexOf(',') + 1));
            int vx = Integer.parseInt(parts[1].substring(2, parts[1].indexOf(',')));
            int vy = Integer.parseInt(parts[1].substring(parts[1].indexOf(',') + 1));

            int finalX = (px + 100 * vx) % GRID_WIDTH;
            int finalY = (py + 100 * vy) % GRID_HEIGHT;

            if (finalX < 0) finalX += GRID_WIDTH;
            if (finalY < 0) finalY += GRID_HEIGHT;

            grid[finalX][finalY]++;
        }

        int q1 = 0, q2 = 0, q3 = 0, q4 = 0;
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                if (x == GRID_WIDTH / 2 || y == GRID_HEIGHT / 2) continue;
                if (x < GRID_WIDTH / 2 && y < GRID_HEIGHT / 2) q1 += grid[x][y];
                else if (x >= GRID_WIDTH / 2 && y < GRID_HEIGHT / 2) q2 += grid[x][y];
                else if (x < GRID_WIDTH / 2 && y >= GRID_HEIGHT / 2) q3 += grid[x][y];
                else if (x >= GRID_WIDTH / 2 && y >= GRID_HEIGHT / 2) q4 += grid[x][y];
            }
        }

        int safetyFactor = q1 * q2 * q3 * q4;
        return safetyFactor + "";
    }

    public String part2(List<String> input) {
        List<Robot> robots = new ArrayList<>();

        for (String line : input) {
            String[] parts = line.split(" ");
            int px = Integer.parseInt(parts[0].substring(2, parts[0].indexOf(',')));
            int py = Integer.parseInt(parts[0].substring(parts[0].indexOf(',') + 1));
            int vx = Integer.parseInt(parts[1].substring(2, parts[1].indexOf(',')));
            int vy = Integer.parseInt(parts[1].substring(parts[1].indexOf(',') + 1));

            robots.add(new Robot(px, py, vx, vy));
        }

        Map<Integer, List<Robot>> framesWithPatterns = precomputeFramesWithPatterns(robots);

        SwingUtilities.invokeLater(() -> createAndShowGUI(framesWithPatterns));

        // Keep main thread alive
        int seconds = 0;
        while(seconds < 100000000) {
            try {
                Thread.sleep(1000);
                seconds++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    private Map<Integer, List<Robot>> precomputeFramesWithPatterns(List<Robot> robots) {
        Map<Integer, List<Robot>> relevantFrames = new LinkedHashMap<>();
        List<Robot> currentFrame = deepCopyOfRobots(robots);

        for (int second = 0; second < 10000; second++) {
            if (detectPatterns(currentFrame)) {
                relevantFrames.put(second, deepCopyOfRobots(currentFrame));
            }

            for (Robot robot : currentFrame) {
                robot.updatePosition();
            }
        }

        System.out.println("Relevant frames found: " + relevantFrames.size());
        return relevantFrames;
    }

    private boolean detectPatterns(List<Robot> robots) {
        Set<Point> occupiedPositions = new HashSet<>();
        for (Robot robot : robots) {
            occupiedPositions.add(new Point(robot.px, robot.py));
        }

        boolean[][] grid = new boolean[GRID_WIDTH][GRID_HEIGHT];
        for (Point p : occupiedPositions) {
            grid[p.x][p.y] = true;
        }

        // horizontal patterns
        for (int y = 0; y < GRID_HEIGHT; y++) {
            int consecutive = 0;
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[x][y]) {
                    consecutive++;
                    if (consecutive >= CONSECUTIVE_THRESHOLD) {
                        return true;
                    }
                } else {
                    consecutive = 0;
                }
            }
        }

        // vertical patterns
        for (int x = 0; x < GRID_WIDTH; x++) {
            int consecutive = 0;
            for (int y = 0; y < GRID_HEIGHT; y++) {
                if (grid[x][y]) {
                    consecutive++;
                    if (consecutive >= CONSECUTIVE_THRESHOLD) {
                        return true;
                    }
                } else {
                    consecutive = 0;
                }
            }
        }

        return false;
    }

    private void createAndShowGUI(Map<Integer, List<Robot>> relevantFrames) {
        JFrame frame = new JFrame("Robot Simulation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timeLabel = new JLabel("Time: 0s");

        JButton playPauseButton = new JButton("Pause");
        playPauseButton.addActionListener(e -> {
            isPlaying = !isPlaying;
            if (isPlaying) {
                playPauseButton.setText("Pause");
            } else {
                playPauseButton.setText("Play");
            }
        });

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> {
            currentFrameIndex = 0;
            timeLabel.setText("Time: 0s");
            panel.resetRobots();
            robotStates.clear();
            robotStates.add(deepCopyOfRobots(panel.getRobots()));
            panel.repaint();
        });

        topPanel.add(timeLabel);
        topPanel.add(playPauseButton);
        topPanel.add(resetButton);

        frame.add(topPanel, BorderLayout.NORTH);

        int firstKey = relevantFrames.keySet().stream().sorted().toList().getFirst();
        panel = new RobotPanel(relevantFrames.get(firstKey));
        frame.add(panel, BorderLayout.CENTER);

        frame.setSize(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Timer timer = new Timer(500, e -> {
            if (isPlaying) {
                if (currentFrameIndex < relevantFrames.size() - 1) {
                    int key = relevantFrames.keySet().stream().sorted().toList().get(currentFrameIndex);
                    panel.setRobotsState(relevantFrames.get(key));
                    timeLabel.setText("Time: " + key + "s");
                    panel.repaint();
                    currentFrameIndex++;
                }
            }
        });
        timer.start();

        panel.repaint();
    }

    private List<Robot> deepCopyOfRobots(List<Robot> original) {
        List<Robot> copy = new ArrayList<>();
        for (Robot r : original) {
            copy.add(new Robot(r.px, r.py, r.vx, r.vy));
        }
        return copy;
    }

    class RobotPanel extends JPanel {
        private final List<Robot> robots;
        private final List<Robot> originalRobots;

        public RobotPanel(List<Robot> robots) {
            super();
            this.robots = new ArrayList<>(robots);
            this.originalRobots = new ArrayList<>();
            for (Robot r : robots) {
                originalRobots.add(new Robot(r.px, r.py, r.vx, r.vy));
            }
        }

        public List<Robot> getRobots() {
            return robots;
        }

        public void resetRobots() {
            robots.clear();
            for (Robot r : originalRobots) {
                robots.add(new Robot(r.px, r.py, r.vx, r.vy));
            }
        }

        public void setRobotsState(List<Robot> newState) {
            robots.clear();
            for (Robot r : newState) {
                robots.add(new Robot(r.px, r.py, r.vx, r.vy));
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);

            // robot
            g.setColor(Color.RED);
            for (Robot robot : robots) {
                g.fillRect(robot.px * TILE_SIZE, robot.py * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            // grid
            g.setColor(Color.LIGHT_GRAY);
            for (int x = 0; x < GRID_WIDTH; x++) {
                g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
            }
            for (int y = 0; y < GRID_HEIGHT; y++) {
                g.drawLine(0, y * TILE_SIZE, GRID_WIDTH * TILE_SIZE, y * TILE_SIZE);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        }
    }
}
