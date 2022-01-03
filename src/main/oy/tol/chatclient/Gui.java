package oy.tol.chatclient;

import java.awt.Color;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;

/**
 * Original client by Antti Juustila: https://github.com/anttijuu/O3-chat-client
 * ChatClient is the console based UI for the ChatServer. It profides the
 * necessary functionality for chatting. The actual comms with the ChatServer
 * happens in the ChatHttpClient class.
 */

// Gui for the modified ChatClient
public class Gui {

    // 1. SET CHAT CLIENT
    private ChatClient chat = new ChatClient();
    private Method registerMethod;
    private Method autofetchMethod;
    private Method cancelAutofetchMethod;
    private Method getNewMessagesMethod;
    private Method postMessageMethod;

    // Set https on/off
    protected void setHttpsClient(String certificateFileWithPath, boolean useHttps) {
        // Create httpClient for ChatClient
        chat.setHttps(useHttps);
        chat.certificateFileWithPath(certificateFileWithPath);
        chat.createHttpClient();
    }

    // Set private methods accessible
    protected void setMethodsAccessible() throws NoSuchMethodException, SecurityException {
        registerMethod = ChatClient.class.getDeclaredMethod("registerUser");
        registerMethod.setAccessible(true);

        autofetchMethod = ChatClient.class.getDeclaredMethod("autoFetch");
        autofetchMethod.setAccessible(true);

        cancelAutofetchMethod = ChatClient.class.getDeclaredMethod("cancelAutoFetch");
        cancelAutofetchMethod.setAccessible(true);

        getNewMessagesMethod = ChatClient.class.getDeclaredMethod("getNewMessages");
        getNewMessagesMethod.setAccessible(true);

        postMessageMethod = ChatClient.class.getDeclaredMethod("postMessage", String.class);
        postMessageMethod.setAccessible(true);
    }

    // 2. SET VISUALS
    // Frames
    private JFrame mainFrame = new JFrame();
    private JFrame dialogWindow = new JFrame();
    private JFrame dialogWindow2 = new JFrame();

    // Panels
    private JPanel downPanel = new JPanel();
    private JPanel upPanel = new JPanel();
    private JPanel rightPanel = new JPanel();
    private JPanel loggedLeftPanel = new JPanel();
    private JPanel loggedOutLeftPanel = new JPanel();
    private JPanel passwordfieldPanel = new JPanel();
    private ArrayList<JPanel> jpanels = new ArrayList<JPanel>();

    // TextAreas & JTextFields
    private JTextArea infoText = new JTextArea();
    private static JTextArea messages = new JTextArea();
    private JTextField userInput = new JTextField();

    // Strings
    private String themeText = "light";
    private String autofetchText = "off";
    private String chatVersionHistory = "This version is based on original client by Antti Juustila:\nhttps://github.com/anttijuu/O3-chat-client ";
    private String[] optionsOkCancel = new String[] { "OK", "Cancel" };
    private String newServer = null;
    private char[] echoedInput;

    // Booleans
    private boolean boldSetting = false;
    private boolean darkThemeSetting = false;
    private boolean loop = false;

    // JButtons
    private JButton refreshButton = new JButton("Refresh");
    private JButton biggerFontButton = new JButton("+");
    private JButton smallerFontButton = new JButton("-");
    private JButton boldFontButton = new JButton("B");
    private JButton clearButton = new JButton("Clear");
    private JButton loginButton = new JButton("Login");
    private JButton registerButton = new JButton("Register");
    private JButton okButton = new JButton("OK");
    private JButton sendButton = new JButton("Send");
    private ArrayList<JButton> jButtons = new ArrayList<JButton>();

    // JRadioButtons
    private JRadioButton lightRadioButton = new JRadioButton("light");
    private JRadioButton darkRadioButton = new JRadioButton("dark");
    private JRadioButton offAutofetchButton = new JRadioButton("Off");
    private JRadioButton onAutofetchButton = new JRadioButton("On");
    private ArrayList<JRadioButton> jRadioButtons = new ArrayList<JRadioButton>();

    // Jmenus
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu, settingsMenu, userMenu, helpMenu;
    private JMenuItem aboutMenuItem, exitMenuItem, serverMenuItem, autofetchMenuItem, themeMenuItem, nickMenuItem,
            getStartedMenuItem, logoutMenuItem;
    private ArrayList<JMenuItem> jMenuItems = new ArrayList<JMenuItem>();
    private ArrayList<JMenu> jMenus = new ArrayList<JMenu>();

    // Integers
    private int userOption;
    private int currentFont = 13;

    // Jlabels
    private JLabel welcomeText = new JLabel();
    private JLabel passwordQuestion = new JLabel();

    // Custom colors
    private Color customOrange = new Color(255, 153, 51);
    private Color customLightBlue = new Color(225, 238, 247);
    private Color customBlue = new Color(59, 89, 182);
    private Color custonBlue2 = new Color(177, 206, 230);
    private Color customGray = new Color(197, 205, 212);

    // Others
    private JScrollPane scroll = new JScrollPane(messages);
    private JPasswordField passwordField = new JPasswordField(null, 15);

    // Set main frame
    private void setMainFrame() {
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        BorderLayout mainLayout = new BorderLayout(0, 0);
        mainFrame.setIconImage(new ImageIcon("icon.png").getImage());
        mainFrame.setTitle("Chat client version " + chat.chatClientVersion);

        // window size fixed
        mainFrame.setMinimumSize(new Dimension(768, 432));
        mainFrame.setMaximumSize(new Dimension(1300, 500));

        mainFrame.setLayout(mainLayout);
        mainFrame.setJMenuBar(menuBar);
    }

    // Set panels
    private void setPanels() {

        // set layouts
        GridBagLayout gridbagLayout = new GridBagLayout();
        GridLayout gridLayout = new GridLayout(1, 0);
        FlowLayout flowLayout = new FlowLayout(0, 1, 0);
        BorderLayout borderLayout = new BorderLayout(2, 2);
        BorderLayout borderLayout2 = new BorderLayout(0, 0);

        upPanel.setLayout(flowLayout);
        loggedOutLeftPanel.setLayout(gridbagLayout);
        loggedLeftPanel.setLayout(borderLayout);
        rightPanel.setLayout(gridLayout);
        downPanel.setLayout(flowLayout);
        passwordfieldPanel.setLayout(borderLayout2);

        // set borders
        EmptyBorder border = new EmptyBorder(55, 75, 55, 35);
        EmptyBorder border2 = new EmptyBorder(25, 75, 10, 5);
        EmptyBorder border3 = new EmptyBorder(110, 15, 75, 5);
        EmptyBorder border4 = new EmptyBorder(0, 75, 30, 5);

        loggedOutLeftPanel.setBorder(border);
        loggedLeftPanel.setBorder(border2);
        rightPanel.setBorder(border3);
        downPanel.setBorder(border4);

        // add layouts to panels
        jpanels.add(downPanel);
        jpanels.add(upPanel);
        jpanels.add(loggedLeftPanel);
        jpanels.add(loggedOutLeftPanel);
        jpanels.add(rightPanel);
    }

    // Set components
    private void setComponents() {

        // set components
        welcomeText.setText("To start a chat please login or register");
        welcomeText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        loginButton.setPreferredSize(new Dimension(200, 50));
        registerButton.setPreferredSize(new Dimension(200, 50));
        sendButton.setPreferredSize(new Dimension(100, 45));
        infoText.setPreferredSize(new Dimension(250, 100));
        infoText.setFont(new Font("Tahoma", Font.PLAIN, 14));
        messages.setLineWrap(true);
        messages.setWrapStyleWord(true);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        passwordQuestion.setText("Please enter your password\n [2-15 chars]");
        passwordQuestion.setOpaque(true);
        setButtons();
        userInput.setPreferredSize(new Dimension(400, 50));
        userInput.setFont(new Font("Tahoma", Font.PLAIN, 15));
    }

    // Set buttons
    private void setButtons() {
        jButtons.add(refreshButton);
        jButtons.add(biggerFontButton);
        jButtons.add(smallerFontButton);
        jButtons.add(boldFontButton);
        jButtons.add(clearButton);
        jButtons.add(loginButton);
        jButtons.add(registerButton);
        jButtons.add(sendButton);
        jButtons.add(okButton);

        jRadioButtons.add(lightRadioButton);
        jRadioButtons.add(darkRadioButton);
        jRadioButtons.add(offAutofetchButton);
        jRadioButtons.add(onAutofetchButton);
    }

    // Add components to panels
    private void addComponentsToPanels() {

        // adding components to panels
        upPanel.add(refreshButton);
        upPanel.add(clearButton);
        upPanel.add(biggerFontButton);
        upPanel.add(smallerFontButton);
        upPanel.add(boldFontButton);
        loggedLeftPanel.add(scroll, BorderLayout.CENTER);
        loggedLeftPanel.add(upPanel, BorderLayout.NORTH);
        rightPanel.add(infoText);
        downPanel.add(userInput);
        downPanel.add(sendButton);

        // while adding layout constraints
        GridBagConstraints layoutConstrait = new GridBagConstraints();
        layoutConstrait.weightx = 2;
        layoutConstrait.weighty = 2;
        layoutConstrait.fill = GridBagConstraints.HORIZONTAL;
        layoutConstrait.gridx = 5;
        layoutConstrait.gridy = 0;
        layoutConstrait.gridwidth = 0;
        loggedOutLeftPanel.add(welcomeText, layoutConstrait);
        layoutConstrait.gridx = 5;
        layoutConstrait.gridy = 2;
        layoutConstrait.gridwidth = 0;
        layoutConstrait.insets = new Insets(2, 0, 0, 0);
        loggedOutLeftPanel.add(loginButton, layoutConstrait);
        layoutConstrait.gridx = 5;
        layoutConstrait.gridy = 8;
        layoutConstrait.gridwidth = 0;
        layoutConstrait.insets = new Insets(2, 0, 0, 0);
        loggedOutLeftPanel.add(registerButton, layoutConstrait);
        passwordfieldPanel.add(passwordQuestion, BorderLayout.NORTH);
        passwordfieldPanel.add(passwordField, BorderLayout.SOUTH);
    }

    // Add panels to frame
    private void addPanelsToFrame() {
        mainFrame.add(loggedOutLeftPanel, BorderLayout.CENTER);
        mainFrame.add(downPanel, BorderLayout.SOUTH);
        mainFrame.add(rightPanel, BorderLayout.EAST);

    }

    // Set menus
    private void setMenus() {

        // menus
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(fileMenu);
        jMenus.add(fileMenu);

        settingsMenu = new JMenu("Settings");
        settingsMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(settingsMenu);
        jMenus.add(settingsMenu);

        userMenu = new JMenu("User");
        userMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(userMenu);
        jMenus.add(userMenu);

        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_A);
        menuBar.add(helpMenu);
        jMenus.add(helpMenu);

        // menu items
        aboutMenuItem = new JMenuItem("About", KeyEvent.VK_T);
        aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        fileMenu.add(aboutMenuItem);
        jMenuItems.add(aboutMenuItem);

        exitMenuItem = new JMenuItem("Exit", KeyEvent.VK_T);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        fileMenu.add(exitMenuItem);
        jMenuItems.add(exitMenuItem);

        serverMenuItem = new JMenuItem("Change server", KeyEvent.VK_T);
        serverMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
        settingsMenu.add(serverMenuItem);
        jMenuItems.add(serverMenuItem);

        autofetchMenuItem = new JMenuItem("Autofetch", KeyEvent.VK_T);
        autofetchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
        settingsMenu.add(autofetchMenuItem);
        jMenuItems.add(autofetchMenuItem);

        themeMenuItem = new JMenuItem("Theme", KeyEvent.VK_T);
        themeMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));
        settingsMenu.add(themeMenuItem);
        jMenuItems.add(themeMenuItem);

        nickMenuItem = new JMenuItem("Change nick", KeyEvent.VK_T);
        nickMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.ALT_MASK));
        userMenu.add(nickMenuItem);
        jMenuItems.add(nickMenuItem);

        logoutMenuItem = new JMenuItem("Logout", KeyEvent.VK_T);
        logoutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_8, ActionEvent.ALT_MASK));
        userMenu.add(logoutMenuItem);
        jMenuItems.add(logoutMenuItem);

        getStartedMenuItem = new JMenuItem("Get started", KeyEvent.VK_T);
        getStartedMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, ActionEvent.ALT_MASK));
        helpMenu.add(getStartedMenuItem);
        jMenuItems.add(getStartedMenuItem);
    }

    // Add mouse/keyboard action listeners
    private void addActionListeners() {
        okButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dialogWindow2.dispose();
            }
        });
        lightRadioButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                darkRadioButton.setSelected(false);
                lightRadioButton.setSelected(true);
            }
        });
        darkRadioButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lightRadioButton.setSelected(false);
                darkRadioButton.setSelected(true);
            }
        });
        offAutofetchButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAutofetchButton.setSelected(false);
                offAutofetchButton.setSelected(true);
            }
        });
        onAutofetchButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                offAutofetchButton.setSelected(false);
                onAutofetchButton.setSelected(true);
            }
        });
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showDialogWindow(7);
            }
        });
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exit();

            }
        });
        serverMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkServer();
            }
        });
        autofetchMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleAutofetch();
            }
        });
        themeMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleTheme();
            }
        });
        nickMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkNick();
            }
        });
        getStartedMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getStarted();
            }
        });
        logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout();
            }
        });
        loginButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkUserCredentials(1); // 1 Login, 2 Register
            }
        });
        registerButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkUserCredentials(2); // 1 Login, 2 Register
            }
        });
        sendButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMessage();
            }
        });
        Action enterAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = userInput.getText();
                if (message.length() > 0) {
                    userInput.setText("");
                    try {
                        postMessageMethod.invoke(chat, message);
                    } catch (IllegalAccessException | InvocationTargetException exception) {
                        JOptionPane.showMessageDialog(dialogWindow, "Error! " + exception.getLocalizedMessage());
                    }
                }
            }
        };
        userInput.addActionListener(enterAction);
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshMessages();
            }
        });
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                messages.setText("");
                chat.clearMessages();

            }
        });
        biggerFontButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeFontBigger();
            }
        });
        smallerFontButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeFontSmaller();
            }
        });
        boldFontButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleBoldFont();
            }
        });
    }

    // Start gui
    protected void startGui() {
        setMainFrame();
        setMenus();
        setPanels();
        setComponents();
        addComponentsToPanels();
        addPanelsToFrame();
        addActionListeners();
        setLightTheme();
        changeViewLoggedOut();
        autofetchMenuItem.setSelected(true);
        lightRadioButton.setSelected(true);
        updateInfoText();
        mainFrame.setVisible(true);
    }

    // Change views (logged)
    private void changeViewLogged() {
        mainFrame.setLocationRelativeTo(null);

        // Change login screen to chat screen
        mainFrame.remove(loggedOutLeftPanel);
        mainFrame.add(loggedLeftPanel, BorderLayout.CENTER);

        mainFrame.setResizable(true);

        sendButton.setVisible(true);
        userInput.setVisible(true);
        userMenu.setEnabled(true);
        logoutMenuItem.setVisible(true);

        mainFrame.revalidate();
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    // Change views (logged out)
    private void changeViewLoggedOut() {
        mainFrame.setLocationRelativeTo(null);
        // No larger window allowed, minimum fixed size: 768 x 432
        mainFrame.setSize(768, 432);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);

        // Change login screen to chat screen
        mainFrame.remove(loggedLeftPanel);
        mainFrame.add(loggedOutLeftPanel, BorderLayout.CENTER);

        offAutofetchButton.setSelected(true);
        sendButton.setVisible(false);
        userInput.setVisible(false);
        userMenu.setEnabled(false);
        logoutMenuItem.setVisible(false);

        mainFrame.revalidate();
        SwingUtilities.updateComponentTreeUI(mainFrame);
    }

    // Toggle themes
    private void toggleTheme() {
        showDialogWindow(5);
        if (darkThemeSetting == false) {
            setLightTheme();
        } else {
            setDarkTheme();
        }
        messages.repaint();
        SwingUtilities.updateComponentTreeUI(mainFrame);
        updateInfoText();
    }

    // Set theme (light)
    private void setLightTheme() {
        uiManagerChangeColor();
        for (JButton button : jButtons) {
            button.setFont(new Font("Tahoma", Font.PLAIN, currentFont));
            button.setFocusPainted(false);
            button.setBackground(customBlue);
            button.setForeground(Color.white);
        }
        okButton.setBackground(customLightBlue);
        okButton.setForeground(Color.BLACK);
        for (JRadioButton jradiobutton : jRadioButtons) {
            jradiobutton.setBackground(Color.WHITE);
            jradiobutton.setForeground(Color.BLACK);
        }
        for (JMenuItem menu : jMenuItems) {
            menu.setBackground(Color.WHITE);
            menu.setForeground(Color.BLACK);

        }
        for (JMenu menu : jMenus) {
            menu.setBackground(custonBlue2);
            menu.setForeground(Color.BLACK);
        }
        menuBar.setBackground(custonBlue2);
        for (JPanel jpanel : jpanels) {
            jpanel.setBackground(customLightBlue);
        }
        dialogWindow.setBackground(customGray);
        welcomeText.setForeground(Color.BLACK);
        mainFrame.setBackground(Color.WHITE);
        userInput.setBackground(Color.WHITE);
        userInput.setForeground(Color.BLACK);
        infoText.setBackground(customLightBlue);
        infoText.setForeground(Color.BLACK);
        passwordQuestion.setBackground(Color.WHITE);
        passwordQuestion.setForeground(Color.BLACK);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        passwordQuestion.setBackground(Color.WHITE);
        changeTextColor();
    }

    // Set theme (dark)
    private void setDarkTheme() {
        uiManagerChangeColor();
        for (JButton button : jButtons) {
            button.setFont(new Font("Tahoma", Font.PLAIN, currentFont));
            button.setFocusPainted(false);
            button.setBackground(customOrange);
            button.setForeground(Color.DARK_GRAY);
        }
        for (JRadioButton jradiobutton : jRadioButtons) {
            jradiobutton.setBackground(Color.DARK_GRAY);
            jradiobutton.setForeground(Color.WHITE);
        }
        for (JMenuItem menu : jMenuItems) {
            menu.setBackground(Color.DARK_GRAY);
            menu.setForeground(customOrange);

        }
        for (JMenu menu : jMenus) {
            menu.setBackground(Color.DARK_GRAY);
            menu.setForeground(customOrange);
        }
        menuBar.setBackground(Color.DARK_GRAY);
        for (JPanel jpanel : jpanels) {
            jpanel.setBackground(Color.BLACK);
        }
        infoText.setBackground(Color.BLACK);
        infoText.setForeground(customOrange);
        welcomeText.setForeground(customOrange);
        userInput.setBackground(Color.DARK_GRAY);
        userInput.setForeground(Color.WHITE);
        passwordField.setBackground(Color.DARK_GRAY);
        passwordField.setForeground(Color.WHITE);
        passwordQuestion.setBackground(Color.DARK_GRAY);
        passwordQuestion.setForeground(Color.WHITE);
        dialogWindow.setBackground(customGray);
        mainFrame.setBackground(Color.DARK_GRAY);
        changeTextColor();
    }

    // Set ui manager colors
    private void uiManagerChangeColor() {
        if (!darkThemeSetting) {
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("OptionPane.messagebackground", Color.WHITE);
            UIManager.put("OptionPane.foreground", new Color(225, 238, 247));
            UIManager.put("OptionPane.messageForeground", Color.BLACK);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("Panel.foreground", Color.BLACK);
            UIManager.put("Button.background", new Color(225, 238, 247));
            UIManager.put("Button.foreGround", Color.BLACK);
            UIManager.put("RadioButton.background", Color.WHITE);
            UIManager.put("RadioButton.foreground", Color.BLACK);
            UIManager.put("PasswordField.background", Color.WHITE);
            UIManager.put("PasswordField.foreground", Color.BLACK);
            UIManager.put("TextField.background", Color.WHITE);
            UIManager.put("TextField.foreground", Color.BLACK);
        } else {
            UIManager.put("OptionPane.background", Color.DARK_GRAY);
            UIManager.put("OptionPane.messagebackground", Color.DARK_GRAY);
            UIManager.put("OptionPane.foreground", customOrange);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Panel.background", Color.DARK_GRAY);
            UIManager.put("Panel.foreground", Color.WHITE);
            UIManager.put("Button.background", customOrange);
            UIManager.put("Button.foreGround", Color.WHITE);
            UIManager.put("RadioButton.background", Color.ORANGE);
            UIManager.put("RadioButton.foreground", Color.DARK_GRAY);
            UIManager.put("PasswordField.background", Color.DARK_GRAY);
            UIManager.put("PasswordField.foreground", Color.WHITE);
            UIManager.put("TextField.background", Color.DARK_GRAY);
            UIManager.put("TextField.foreground", Color.WHITE);
        }
    }

    // Change text color
    private void changeTextColor() {
        if (boldSetting == true) {
            messages.setFont(new Font("Tahoma", Font.BOLD, currentFont));
            if (darkThemeSetting == false) {
                messages.setBackground(Color.white);
                messages.setForeground(Color.BLACK);
            } else {
                messages.setForeground(Color.WHITE);
                messages.setBackground(Color.DARK_GRAY);
            }
        } else {
            messages.setFont(new Font("Tahoma", Font.PLAIN, currentFont));
            if (darkThemeSetting == false) {
                messages.setBackground(Color.white);
                messages.setForeground(Color.BLACK);
            } else {
                messages.setForeground(Color.WHITE);
                messages.setBackground(Color.DARK_GRAY);
            }
        }
    }

    // Make font bogger
    private void changeFontBigger() {
        if (currentFont < 20) {
            currentFont++;
        }
        messages.setCaretPosition(messages.getDocument().getLength());
        if (boldSetting == false) {
            messages.setFont(new Font("Tahoma", Font.PLAIN, currentFont));
        } else {
            messages.setFont(new Font("Tahoma", Font.BOLD, currentFont));
        }
        messages.revalidate();
        messages.repaint();
    }

    // Make font smaller
    private void changeFontSmaller() {
        if (currentFont > 10) {
            currentFont--;
        }
        if (boldSetting == false) {
            messages.setFont(new Font("Tahoma", Font.PLAIN, currentFont));
        } else {
            messages.setFont(new Font("Tahoma", Font.BOLD, currentFont));
        }
        messages.revalidate();
        messages.repaint();
    }

    // Toggle bold font
    private void toggleBoldFont() {
        if (boldSetting == false) {
            messages.setFont(new Font("Tahoma", Font.BOLD, currentFont));
            boldSetting = true;
        } else {
            messages.setFont(new Font("Tahoma", Font.PLAIN, currentFont));
            boldSetting = false;
        }
        messages.repaint();
    }

    // 3. GUI FUNCTIONS
    // Get started
    private void getStarted() {
        if (chat.getUserStatus() == false) {
            showDialogWindow(15);
        } else {
            showDialogWindow(16);
        }
    }

    // Check user credentials
    private void checkUserCredentials(int choice) {
        loop = true;
        while (loop == true) {
            showDialogWindow(2);
            if (chat.getUsername() != null) {
                if (chat.getUsername().isBlank() == false && chat.getUsername().length() >= 2
                        && chat.getUsername().length() <= 15) {
                    checkPassword(choice);
                } else {
                    showDialogWindow(9);
                }
            } else {
                loop = false;
                resetUserInfo();
            }
        }
    }

    // Check user input (password)
    private void checkPassword(int choice) {
        while (loop == true) {
            passwordField.setText("");
            showDialogWindow(3);
            if (userOption == 0) {
                echoedInput = passwordField.getPassword();
                String password = new String(echoedInput);
                chat.setPassword(password);
            } else {
                loop = false;
                resetUserInfo();
                break;
            }
            if (chat.getPassword() != null) {
                if (chat.getPassword().isBlank() == false && chat.getPassword().length() >= 2
                        && chat.getPassword().length() <= 15) {
                    if (choice == 1) {
                        connectToTheServer(choice);
                        loop = false;
                    } else {
                        checkEmail(choice);
                        loop = false;
                    }
                } else {
                    showDialogWindow(9);
                }
            } else {
                loop = false;
                resetUserInfo();
            }
        }
    }

    // Check user input (email)
    private void checkEmail(int choice) {
        while (loop == true) {
            showDialogWindow(4);
            if (chat.getEmail() != null) {
                if (chat.getEmail().isBlank() == false && chat.getEmail().length() >= 5
                        && chat.getEmail().length() <= 64) {
                    connectToTheServer(choice);
                    updateInfoText();
                    loop = false;
                } else {
                    showDialogWindow(9);
                }
            } else {
                loop = false;
                resetUserInfo();
            }
        }
    }

    // Check user input (nick)
    private void checkNick() {
        if (chat.getUserStatus() == true) {
            loop = true;
            while (loop == true) {
                showDialogWindow(10);
                if (chat.getNick() != null) {
                    if (chat.getNick().isBlank() == false && chat.getNick().length() >= 0) {
                        showDialogWindow(12);
                        updateInfoText();
                        loop = false;
                    } else {
                        showDialogWindow(9);
                    }
                } else {
                    loop = false;
                }
            }
        } else {
            showDialogWindow(11);
        }
    }

    // Check user input (server)
    private void checkServer() {
        loop = true;
        while (loop) {
            showDialogWindow(8);
            if (newServer != null) {
                if (newServer.isBlank() == false && newServer.length() > 0) {
                    changeServer(newServer);
                    loop = false;
                } else {
                    showDialogWindow(9);
                }
            } else {
                loop = false;
            }
        }
    }

    // Check user input (message) and send message
    private void sendMessage() {
        String message = userInput.getText();
        if (message != null) {
            message.trim();
            if (message.isBlank() == false && message.length() > 0) {
                userInput.setText("");
                try {
                    postMessageMethod.invoke(chat, message);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    JOptionPane.showMessageDialog(dialogWindow, "Error! " + exception.getLocalizedMessage());
                }
                return;
            } else {
                showDialogWindow(1);
                userInput.setText("");
                return;
            }
        }
        showDialogWindow(1);
    }

    // Get new messages without autofetch
    private void refreshMessages() {
        try {
            Object object = getNewMessagesMethod.invoke(chat);
            int newMessages = (int) object;
            if (newMessages == 0) {
                messages.setCaretPosition(messages.getDocument().getLength());
                showDialogWindow(18);
            } else {
                messages.setText(chat.getText());
                messages.setCaretPosition(messages.getDocument().getLength());
            }
        } catch (IllegalAccessException | InvocationTargetException exception) {
            JOptionPane.showMessageDialog(dialogWindow, "Error! " + exception.getLocalizedMessage());
        }
    }

    // Toggle autofetch
    private void toggleAutofetch() {
        if (chat.getUserStatus() == true) {
            showDialogWindow(17);
            if (onAutofetchButton.isSelected()) {
                autofetchText = "on";
                onAutofetchButton.setSelected(true);
                offAutofetchButton.setSelected(false);
                try {
                    chat.setAutofetchOn();
                    autofetchMethod.invoke(chat);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    JOptionPane.showMessageDialog(dialogWindow, "Error! " + exception.getLocalizedMessage());
                }
            }
            if (offAutofetchButton.isSelected()) {
                autofetchText = "off";
                onAutofetchButton.setSelected(false);
                offAutofetchButton.setSelected(true);
                try {
                    cancelAutofetchMethod.invoke(chat);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    JOptionPane.showMessageDialog(dialogWindow, "Error! " + exception.getLocalizedMessage());
                }
            }
            updateInfoText();
        } else {
            showDialogWindow(11);
        }
    }

    // Update messages window text
    protected static void updateMessagesWindow(String message) {
        messages.setText(message);
        messages.setCaretPosition(messages.getDocument().getLength());
    }

    // Update info text
    private void updateInfoText() {
        infoText.setText("Server: " + chat.getServer() + "\nUser: " + chat.getUsername() + "\nNick: " + chat.getNick()
                + "\nAutofetch: " + autofetchText + " \nTheme: " + themeText);
    }

    // Change server
    private void changeServer(String newServer) {
        int choice = JOptionPane.showConfirmDialog(dialogWindow,
                "Change server from " + chat.getServer() + " to " + newServer + "?", null, JOptionPane.YES_NO_OPTION);
        if (choice == 0) {
            chat.setServer(newServer);
            resetUserInfo();
            try {
                cancelAutofetchMethod.invoke(chat);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                JOptionPane.showMessageDialog(dialogWindow, "Error! " + exception.getLocalizedMessage());
            }
            setDefaultSettings();
            updateInfoText();
            changeViewLoggedOut();
            showDialogWindow(19);
        }
    }

    // Connect to the server
    private void connectToTheServer(int choice) {
        if (choice == 1) {
            try {
                getNewMessagesMethod.invoke(chat);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                JOptionPane.showMessageDialog(dialogWindow, "Error! " + exception.getLocalizedMessage());
            }
            messages.setText(chat.getText());
            messages.setCaretPosition(messages.getDocument().getLength());
            if (chat.getUserStatus() == true) {
                changeViewLogged();
                updateInfoText();
                return;
            }
            resetUserInfo();
        } else {
            try {
                registerMethod.invoke(chat);
                if (chat.getUserStatus() == true ) {
                    refreshMessages();
                }
            } catch (IllegalAccessException | InvocationTargetException exception) {
                JOptionPane.showMessageDialog(dialogWindow, "Error! " + exception.getLocalizedMessage());
            }
            if (chat.getUserStatus() == true) {
                changeViewLogged();
                updateInfoText();
                return;
            }
            resetUserInfo();
        }
    }

    // Dialog window options
    private void showDialogWindow(int userChoice) {
        switch (userChoice) {
        case 1:
            JOptionPane.showMessageDialog(dialogWindow, "Please write something first", "Alert",
                    JOptionPane.INFORMATION_MESSAGE);
            break;
        case 2:
            String username = JOptionPane.showInputDialog(dialogWindow, "Please enter your username [2-15 chars]",
                    "Login/Register", JOptionPane.INFORMATION_MESSAGE);
            chat.setUsername(username);
            break;
        case 3:
            userOption = JOptionPane.showOptionDialog(dialogWindow, passwordfieldPanel, "User password",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, optionsOkCancel, null);
            break;
        case 4:
            String email = JOptionPane.showInputDialog(dialogWindow, "Please enter your email [5-64 chars]",
                    "User email", JOptionPane.INFORMATION_MESSAGE);
            chat.setEmail(email);
            break;
        case 5:
            JOptionPane.showOptionDialog(dialogWindow2, "Choose a theme", "Theme", JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE, null, new Object[] { lightRadioButton, darkRadioButton, okButton },
                    lightRadioButton);
            if (lightRadioButton.isSelected()) {
                themeText = "light";
                darkThemeSetting = false;
            } else {
                themeText = "dark";
                darkThemeSetting = true;
            }
            break;
        case 6:
            JOptionPane.showMessageDialog(dialogWindow, "Logged out successfully!", "Logout confirmation",
                    JOptionPane.INFORMATION_MESSAGE);
            break;
        case 7:
            JOptionPane.showMessageDialog(dialogWindow,
                    "Chat client made for course: Ohjelmointi 4\n@ 2021 Peetu Kaan\n\nClient version: "
                            + chat.chatClientVersion + "\nUsing server version: " + chat.getServerVersion() + "\n\n"
                            + chatVersionHistory,
                    "About", JOptionPane.INFORMATION_MESSAGE);
            break;
        case 8:
            newServer = JOptionPane.showInputDialog(dialogWindow, "Connect to the server", "Server",
                    JOptionPane.INFORMATION_MESSAGE);
            break;
        case 9:
            JOptionPane.showMessageDialog(dialogWindow, "Can't be empty or too short/long");
            break;
        case 10:
            String nick = JOptionPane.showInputDialog(dialogWindow, "Enter your nickname [2-15 chars]", "Nick",
                    JOptionPane.INFORMATION_MESSAGE);
            chat.setNick(nick);
            break;
        case 11:
            JOptionPane.showMessageDialog(dialogWindow, "Please login or register first", "Alert",
                    JOptionPane.INFORMATION_MESSAGE);
            break;
        case 12:
            JOptionPane.showMessageDialog(dialogWindow, "Nick changed!");
            break;
        case 13:
            JOptionPane.showMessageDialog(dialogWindow, "Logged out successfully!", "Logout confirmation",
                    JOptionPane.INFORMATION_MESSAGE);
            break;
        case 14:
            userOption = JOptionPane.showConfirmDialog(dialogWindow, "Log out user " + chat.getUsername() + "?", null,
                    JOptionPane.YES_NO_OPTION);
            break;
        case 15:
            JOptionPane.showMessageDialog(dialogWindow, "Please login or register before use \n", "Get started",
                    JOptionPane.INFORMATION_MESSAGE);
            break;
        case 16:
            JOptionPane.showMessageDialog(dialogWindow,
                    "Sending a message:\nPress enter or click send button\n\nRetrieve messages automatically from the server:\nTurn autofetch on",
                    "Get started", JOptionPane.INFORMATION_MESSAGE);
            break;
        case 17:
            JOptionPane.showOptionDialog(dialogWindow2, "Turn autofect off or on", "Autofect",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    new Object[] { offAutofetchButton, onAutofetchButton, okButton }, offAutofetchButton);
            break;
        case 18:
            JOptionPane.showMessageDialog(dialogWindow, "No new messages from server.");
            break;
        case 19:
            JOptionPane.showMessageDialog(dialogWindow,
                    "Remember to register and/or login to the new server! \n Server in use is: " + chat.getServer());
            break;
        }
    }

    // Return default settings
    private void setDefaultSettings() {
        boldSetting = false;
        loop = false;
        currentFont = 13;
        offAutofetchButton.setSelected(true);
        onAutofetchButton.setSelected(false);
        autofetchText = "off";
    }

    // Reset user info
    private void resetUserInfo() {
        chat.setUsername(null);
        chat.setNick(null);
        chat.setPassword(null);
        chat.setUserStatus(false);
        chat.resetTimer();
        chat.clearMessages();
        passwordField.setText("");
        messages.setText("");
        updateInfoText();
    }

    // Logout
    private void logout() {
        showDialogWindow(14);
        if (userOption == 0) {
            try {
                cancelAutofetchMethod.invoke(chat);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                JOptionPane.showMessageDialog(dialogWindow, "Error! " + exception.getLocalizedMessage());
            }
            resetUserInfo();
            setDefaultSettings();
            updateInfoText();
            changeViewLoggedOut();
            showDialogWindow(13);
        }
    }

    // Exit application
    private void exit() {
        System.exit(0);
    }
}
