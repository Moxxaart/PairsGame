import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class Pairs extends JFrame implements ActionListener {

    private List<JButton> buttons;
    private Map<JButton, Integer> buttonIds;
    private List<String> icons;
    private JButton firstButton;
    private boolean hasFirstSelection;
    private boolean isClickable;

    public Pairs() {
        super("Find a Pair Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLayout(new GridLayout(4, 4));

        buttons = new ArrayList<>();
        buttonIds = new HashMap<>();
        icons = new ArrayList<>();
        isClickable = true;

        // Add custom icons to the 'icons' list from the resources folder
        for (int i = 1; i <= 8; i++) {
            String iconPath = "resources/icon" + i + ".png";
            ImageIcon icon = new ImageIcon(iconPath);
            if (icon.getImageLoadStatus() != MediaTracker.ERRORED) {
                icons.add(iconPath);
                icons.add(iconPath); // Repeat each icon twice
            } else {
                // Fallback to using numbers as graphics
                icons.add(String.valueOf(i));
                icons.add(String.valueOf(i)); // Repeat each number twice
            }
        }

        // Shuffle the icons
        Collections.shuffle(icons);

        // Create buttons with default graphics and add them to the frame
        for (int i = 0; i < 16; i++) {
            JButton button = new JButton();
            button.addActionListener(this);
            buttons.add(button);
            add(button);
            buttonIds.put(button, i + 1); // Assign unique ID to each button
        }

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        int clickedId = buttonIds.get(clickedButton);
        String iconPath = icons.get(buttons.indexOf(clickedButton));

        if (!isClickable || clickedButton == firstButton) {
            return; // Ignore clicks if buttons are not clickable or the same button is clicked twice
        }

        if (iconPath.matches("\\d+")) {
            // Use number as graphics
            clickedButton.setText(iconPath);
        } else {
            // Use custom graphics
            ImageIcon icon = new ImageIcon(iconPath);
            clickedButton.setIcon(icon);
        }

        System.out.println("Clicked button ID: " + clickedId); // Print the unique ID of the clicked button

        if (!hasFirstSelection) {
            // First button selected
            hasFirstSelection = true;
            firstButton = clickedButton;
        } else {
            // Second button selected
            hasFirstSelection = false;
            int firstId = buttonIds.get(firstButton);

            if (clickedButton.getText().equals(firstButton.getText()) && clickedId != firstId) {
                // Found a pair
                clickedButton.setEnabled(false);
                firstButton.setEnabled(false);
            } else {
                // Not a pair
                isClickable = false;
                Timer timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        firstButton.setText("");
                        clickedButton.setText("");
                        firstButton.setIcon(null);
                        clickedButton.setIcon(null);
                        firstButton.setEnabled(true);
                        clickedButton.setEnabled(true);
                        isClickable = true;
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Pairs();
            }
        });
    }
}
