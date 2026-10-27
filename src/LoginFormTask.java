import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class User {

    private String username;
    private String password;
    private String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {

    WelcomePage(User user) {
        super("Welcome");

        setLayout(new GridLayout(2, 1, 10, 10));

        Label hello = new Label(
                "Hello, " + user.getUsername(),
                Label.CENTER
        );

        Label roleLabel = new Label(
                "Your role is: " + user.getRole(),
                Label.CENTER
        );

        hello.setFont(new Font("SansSerif", Font.BOLD, 20));

        add(hello);
        add(roleLabel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(350, 180);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

public class LoginFormTask extends Frame {

    private ArrayList<User> users = new ArrayList<>();
    private int wrongPasswordAttempts = 0;

    public LoginFormTask() {
        super("Login");

        loadUsers();

        setLayout(new GridBagLayout());

        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));

        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(
                new FlowLayout(FlowLayout.CENTER, 15, 0)
        );

        Button cancel = new Button("Cancel");
        Button login = new Button("Login");

        buttons.add(cancel);
        buttons.add(login);

        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        login.addActionListener(e -> {

            String enteredUsername = username.getText().trim();
            String enteredPassword = password.getText();
            String selectedRole = role.getSelectedItem();

            // Check empty fields
            if (enteredUsername.isEmpty()
                    || enteredPassword.isEmpty()) {

                showMessage("Please fill in all fields.");
                return;
            }

            // Find user
            User user = findUser(enteredUsername);

            if (user == null) {
                showMessage("Username does not exist.");
                return;
            }

            // Check password
            if (!user.getPassword().equals(enteredPassword)) {

                wrongPasswordAttempts++;

                if (wrongPasswordAttempts >= 3) {
                    login.setEnabled(false);
                    login.invalidate();

                    showMessage(
                            "Too many wrong password attempts. Login disabled."
                    );
                } else {
                    showMessage(
                            "Incorrect password. Attempts left: "
                                    + (3 - wrongPasswordAttempts)
                    );
                }

                return;
            }

            // Check role
            if (!user.getRole().equals(selectedRole)) {
                showMessage("Incorrect role.");
                return;
            }

            // Successful login
            wrongPasswordAttempts = 0;

            showMessage("Login successful!");

            new WelcomePage(user);
            dispose();
        });

        cancel.addActionListener(e -> {
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    private void loadUsers() {

        users.add(new User("shahd", "1234", "Student"));
        users.add(new User("ta1", "5678", "TA"));
        users.add(new User("prof1", "abcd", "Professor"));
    }

    private User findUser(String name) {

        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }

        return null;
    }

    private void showMessage(String message) {
        MessageDialog dialog = new MessageDialog(this, message);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}

class MessageDialog extends Dialog {

    MessageDialog(Frame parent, String message) {
        super(parent, "Message", true);

        setLayout(new FlowLayout());

        add(new Label(message));

        Button ok = new Button("OK");
        add(ok);

        ok.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
    }
}
