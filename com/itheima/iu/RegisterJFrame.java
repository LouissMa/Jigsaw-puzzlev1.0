package com.itheima.iu;

import domain.User; // Assuming User class is accessible

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
// No need to import CodeUtil unless you plan to add a captcha to registration

public class RegisterJFrame extends JFrame implements MouseListener {

    // UI Components
    JTextField usernameField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JPasswordField confirmPasswordField = new JPasswordField(); // New field for confirming password

    JButton submitButton = new JButton();
    // Optional: Add a button to go back to Login
    // JButton backButton = new JButton();

    // Paths for images - adjust these to your actual image file names/locations
    // It's better to use relative paths e.g., "PuzzleGame/image/register/..."
    // For consistency with LoginJFrame, I'm using absolute paths as placeholders.
    // You should change "D:\\IDEAcode\\javapre\\PTgame\\" to your actual base path
    // or convert to relative paths.

    // Constructor
    public RegisterJFrame() {
        // Initialize the frame
        initJFrame();

        // Add UI elements to the frame
        initView();

        // Make the frame visible
        this.setVisible(true);
    }

    private void initJFrame() {
        this.setSize(488, 500); // Adjusted height for an extra field
        this.setTitle("拼图游戏 注册");
        this.setAlwaysOnTop(true);
        this.setLocationRelativeTo(null);
        // Consider DISPOSE_ON_CLOSE if you want to return to login without exiting app
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // this.setLayout(null); // Explicitly set layout to null if not done by default by getContentPane()
    }

    private void initView() {
        // Set layout to null to use absolute positioning
        this.getContentPane().setLayout(null);

        // 1. Add username label
        JLabel usernameText = new JLabel(new ImageIcon("D:\\IDEAcode\\javapre\\PTgame\\PuzzleGame\\image\\register\\注册用户名.png")); // Reusing login image
        usernameText.setBounds(100, 85, 47, 17); // Adjusted Y position
        this.getContentPane().add(usernameText);

        // 2. Add username input field
        usernameField.setBounds(180, 85, 200, 30); // Adjusted Y position
        this.getContentPane().add(usernameField);

        // 3. Add password label
        JLabel passwordText = new JLabel(new ImageIcon("D:\\IDEAcode\\javapre\\PTgame\\PuzzleGame\\image\\register\\注册密码.png")); // Reusing login image
        passwordText.setBounds(100, 145, 47, 17); // Adjusted Y position
        this.getContentPane().add(passwordText);

        // 4. Add password input field
        passwordField.setBounds(180, 145, 200, 30); // Adjusted Y position
        this.getContentPane().add(passwordField);

        // 5. Add confirm password label
        // You might need a new image "确认密码.png" or use text
        JLabel confirmPasswordText = new JLabel("确认密码:"); // Using text if image not available
        // JLabel confirmPasswordText = new JLabel(new ImageIcon(basePath + "register/确认密码.png"));
        confirmPasswordText.setBounds(100, 205, 70, 17); // Adjusted Y position
        this.getContentPane().add(confirmPasswordText);

        // 6. Add confirm password input field
        confirmPasswordField.setBounds(180, 205, 200, 30); // Adjusted Y position
        this.getContentPane().add(confirmPasswordField);

        // 7. Add submit button
        submitButton.setBounds(150, 280, 128, 47); // Adjusted Y position
        submitButton.setIcon(new ImageIcon("D:\\IDEAcode\\javapre\\PTgame\\PuzzleGame\\image\\register\\注册按钮.png")); // Reusing login's register button image for now
        // Or use a specific one like "register/提交按钮.png"
        submitButton.setBorderPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.addMouseListener(this);
        this.getContentPane().add(submitButton);

        // 8. Add background image (reusing login's background)
        // Note: The login background is 470x390, frame is 488x500.
        // You might want a different background image or adjust its display.
        JLabel background = new JLabel(new ImageIcon("D:\\IDEAcode\\javapre\\PTgame\\PuzzleGame\\image\\register\\background.png"));
        background.setBounds(0, 0, 470, 390); // This will not fill the 488x500 frame
        this.getContentPane().add(background);

        // Refresh the container
        this.getContentPane().repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == submitButton) {
            System.out.println("点击了提交按钮");

            String usernameInput = usernameField.getText();
            String passwordInput = new String(passwordField.getPassword());
            String confirmPasswordInput = new String(confirmPasswordField.getPassword());

            // Basic Validations
            if (usernameInput.isEmpty() || passwordInput.isEmpty() || confirmPasswordInput.isEmpty()) {
                showJDialog("用户名和密码不能为空!");
                return;
            }

            if (!passwordInput.equals(confirmPasswordInput)) {
                showJDialog("两次输入的密码不一致!");
                return;
            }

            // Advanced Validation: Check if username already exists
            // Accessing the static list from LoginJFrame
            if (usernameExists(usernameInput)) {
                showJDialog("用户名已存在，请更换一个!");
                return;
            }

            // If all checks pass, register the user
            LoginJFrame.allUsers.add(new User(usernameInput, passwordInput));
            showJDialog("注册成功!");
            // Optionally, close registration and open login, or just inform user
            this.setVisible(false); // Close registration window
            new LoginJFrame();      // Open login window

        }
        // Handle other source clicks if any (e.g., backButton)
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getSource() == submitButton) {
            // Assuming you have a "注册按下.png" or similar for pressed state
            submitButton.setIcon(new ImageIcon("D:\\IDEAcode\\javapre\\PTgame\\PuzzleGame\\image\\register\\注册按下.png")); // Reusing login's for now
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getSource() == submitButton) {
            submitButton.setIcon(new ImageIcon("D:\\IDEAcode\\javapre\\PTgame\\PuzzleGame\\image\\register\\注册按钮.png")); // Reusing login's for now
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Can be used for hover effects
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Can be used for hover effects
    }

    // Helper method to show dialog messages (copied from LoginJFrame for encapsulation)
    public void showJDialog(String content) {
        JDialog jDialog = new JDialog();
        jDialog.setSize(250, 150); // Adjusted size for potentially longer messages
        jDialog.setAlwaysOnTop(true);
        jDialog.setLocationRelativeTo(null);
        jDialog.setModal(true);

        // Center the text in the dialog
        JLabel warning = new JLabel(content, SwingConstants.CENTER);
        // warning.setBounds(0, 0, 200, 150); // Not needed if using default layout for JDialog's content pane
        jDialog.getContentPane().add(warning);

        jDialog.setVisible(true);
    }

    // Helper method to check if username exists
    private boolean usernameExists(String username) {
        for (User user : LoginJFrame.allUsers) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}