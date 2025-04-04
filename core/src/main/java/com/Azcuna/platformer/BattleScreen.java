package com.Azcuna.platformer;

import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class BattleScreen extends javax.swing.JFrame {
    Random random = new Random();
    private Image backgroundImage;
    private Character player;  // The current player
    private Character[] enemies;  // Array to store 10 enemies
    private int currentEnemyIndex = 0;  // Index to track the current enemy
    int skill = 0;

    // Declare an array of characters (playable characters)
    private Character[] characters = new Character[3];


    /**
     * Creates new form EdgeRunnersBattleScreen
     */
    public BattleScreen() {
        setTitle("Edge Runners");
        initComponents();

        background();
        initializeCharacters();  // Initialize all 10 playable characters
        initializeEnemies();  // Initialize the enemies
        setCharacter(0);
        startBattle();  // Start the battle
    }

    public void background(){
        ImageIcon pic = new ImageIcon("/Users/russjiehopista/IdeaProjects/EdgeRunners/assets/background2.jpg");

        // Get the image from the icon
        Image image = pic.getImage();

        // Get the dimensions of the JLabel (background)
        int labelWidth = lblBackground.getWidth();  // Width of the JLabel
        int labelHeight = lblBackground.getHeight();  // Height of the JLabel

        // Get the original image's width and height
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        // Scale the image to fit the entire JLabel (stretching)
        Image scaledImage = image.getScaledInstance(labelWidth, labelHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Set the icon to the JLabel
        lblBackground.setIcon(scaledIcon);
    }


    public void setPicture(int option, String user){
        ImageIcon pic;
        switch(option){
            case 0:
                pic = new ImageIcon("/Users/russjiehopista/IdeaProjects/EdgeRunners/assets/david.jpg");
                break;
            case 1:
                pic = new ImageIcon("/Users/russjiehopista/IdeaProjects/EdgeRunners/assets/lucy.jpg");
                break;
            case 2:
                pic = new ImageIcon("/Users/russjiehopista/IdeaProjects/EdgeRunners/assets/david.jpg");
                break;
            default:
                pic = new ImageIcon("/Users/russjiehopista/IdeaProjects/EdgeRunners/assets/david.jpg");
                lblPlayer.setIcon(pic);
                lblEnemy.setIcon(pic);
                break;
        }

        Image image = pic.getImage();
        int width = lblPlayer.getWidth();
        int height = lblPlayer.getHeight();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Set the image depending on the user
        if (user.equals("player")) {
            lblPlayer.setIcon(scaledIcon);
        } else {
            lblEnemy.setIcon(scaledIcon);
        }
    }

    public void setPictureEnemy(int option, String user) {
        ImageIcon pic = null;

        // Get the enemy from the randomized list
        Character enemy = enemies[option];

        // Set picture based on the character type (or class)
        if (enemy instanceof David) {
            pic = new ImageIcon("/Users/russjiehopista/IdeaProjects/EdgeRunners/assets/david.jpg");
        } else if (enemy instanceof Lucy) {
            pic = new ImageIcon("/Users/russjiehopista/IdeaProjects/EdgeRunners/assets/lucy.jpg");
        } else {
            pic = new ImageIcon("default.jpg");
        }

        // Resize and update the picture
        Image image = pic.getImage();
        int width = lblPlayer.getWidth();
        int height = lblPlayer.getHeight();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Set the image depending on the user (player or enemy)
        if (user.equals("player")) {
            lblPlayer.setIcon(scaledIcon);
        } else {
            lblEnemy.setIcon(scaledIcon);
        }
    }


    public void initializeCharacters() {
        characters[0] = new David(true);
        characters[1] = new Lucy(true);
        characters[2] = new David(true);
    }

    // Select the character to play as
    public void setCharacter(int characterIndex) {
        player = characters[characterIndex];  // Set the player to the selected character
        lblPlayer.setText(player.getName());
        setPicture(characterIndex, "player");
    }

    // Initialize the 10 enemies
    public void initializeEnemies() {
        // Initialize the enemies with a fixed number (e.g., 10 enemies)
        enemies = new Character[3];
        enemies[0] = new David(false);
        enemies[1] = new Lucy(false);
        enemies[2] = new David(false);

        //making a random enemy generator
        // Convert the array to a list
        ArrayList<Character> enemyList = new ArrayList<>(List.of(enemies));

        // Shuffle the list to randomize the order
        Collections.shuffle(enemyList);

        // Convert the list back to an array
        enemies = enemyList.toArray(new Character[0]);

        setPictureEnemy(currentEnemyIndex, "enemy");
    }


    // Method to start the battle with the first enemy
    public void startBattle() {
        if (enemies == null || enemies.length == 0) {
            System.out.println("Error: No enemies to fight!");
            return;
        }

        updateBattleUI();
        nextTurn();
    }

    // Update the battle UI with the current enemy and player's stats
    public void updateBattleUI() {
        prgPlayer.setValue(player.getHealth());
        prgEnemy.setValue(enemies[currentEnemyIndex].getHealth());
        prgPlayerMana.setValue(player.getMana());
        prgEnemyMana.setValue(enemies[currentEnemyIndex].getMana());

        //pwede sad diri for file handling
    }

    // Handle the player's and enemy's turns
    public void nextTurn() {
        if (!player.isAlive()) {
            lblEnemy.setText("You lost the battle!");
            prompt();
            return;
            //create method for file handling
        }

        if (!enemies[currentEnemyIndex].isAlive()) {
            currentEnemyIndex++;
            if (currentEnemyIndex >= enemies.length) {
                lblEnemy.setText("You Win! All Enemies Defeated!");
                return;
            }
            // Reset player's health and mana after defeating the enemy
            prompt();

            setPictureEnemy(currentEnemyIndex, "enemy");
            updateBattleUI();
        }
    }

    // Method for player attacking the enemy
    public void attackEnemy() {
        if (player.isAlive() && enemies[currentEnemyIndex].isAlive()) {
            player.attack(enemies[currentEnemyIndex]);

            updateBattleUI();  // Update progress bars after attack

            if (!enemies[currentEnemyIndex].isAlive()) {
                System.out.println(enemies[currentEnemyIndex].getName() + " is defeated!");
                updateBattleUI();  // Update UI after defeating the enemy
            }
        }
    }

    public void enemyAtckPlayer(){
        int rand = random.nextInt(3) + 1;
        enemies[currentEnemyIndex].setSkillChoice(rand);
        // If the enemy is still alive, it's the enemy's turn to attack the player
        if (enemies[currentEnemyIndex].isAlive()) {
            enemies[currentEnemyIndex].attack(player);
        }

        // Check if enemy is defeated
        if (!enemies[currentEnemyIndex].isAlive()) {
            currentEnemyIndex++;  // Move to the next enemy
            if (currentEnemyIndex >= enemies.length) {
                lblEnemy.setText("You Win! All Enemies Defeated!");
            } else {
                // Reset player's health and mana after defeating the enemy
                prompt();

                setPictureEnemy(currentEnemyIndex, "enemy");
                updateBattleUI();  // Update UI for the new enemy
            }
        } else {
            // If the enemy is still alive, update the battle UI
            updateBattleUI();
        }
    }


    public void promptMana() {
        JOptionPane.showMessageDialog(null,
            "No more mana", // The message you want to display
            "Mo Mana",      // Title of the dialog
            JOptionPane.INFORMATION_MESSAGE); // The type of message (this determines the icon)
    }

    public void prompt(){
        int response1 = JOptionPane.showConfirmDialog(null,
            " Won! Do you want to play again?",
            "Game Over", JOptionPane.YES_NO_OPTION);
        if (response1 == JOptionPane.NO_OPTION) {
            System.exit(0);
        } else {
            player.setHealth(100);
            player.setMana(100);
        }
    }




























    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        btnSkill1 = new javax.swing.JButton();
        btnSkill2 = new javax.swing.JButton();
        btnSkill3 = new javax.swing.JButton();
        lblEnemy = new javax.swing.JLabel();
        lblPlayer = new javax.swing.JLabel();
        prgPlayer = new javax.swing.JProgressBar();
        prgEnemy = new javax.swing.JProgressBar();
        prgPlayerMana = new javax.swing.JProgressBar();
        prgEnemyMana = new javax.swing.JProgressBar();
        lblBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnSkill1.setText("skill 1");
        btnSkill1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSkill1ActionPerformed(evt);
            }
        });

        btnSkill2.setText("skill 2");
        btnSkill2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSkill2ActionPerformed(evt);
            }
        });

        btnSkill3.setText("special");
        btnSkill3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSkill3ActionPerformed(evt);
            }
        });

        lblEnemy.setText("enemy");
        lblEnemy.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblEnemyAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        lblPlayer.setText("player");
        lblPlayer.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                lblPlayerAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        prgPlayer.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                prgPlayerAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        prgEnemy.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                prgEnemyAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        prgPlayerMana.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                prgPlayerManaAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        prgEnemyMana.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                prgEnemyManaAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });

        lblBackground.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(107, 107, 107)
                    .addComponent(prgPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(396, 396, 396)
                    .addComponent(prgEnemy, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addGap(107, 107, 107)
                    .addComponent(prgPlayerMana, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(649, 649, 649)
                    .addComponent(prgEnemyMana, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addGap(110, 110, 110)
                    .addComponent(lblPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(714, 714, 714)
                    .addComponent(lblEnemy, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createSequentialGroup()
                    .addGap(120, 120, 120)
                    .addComponent(btnSkill1)
                    .addGap(24, 24, 24)
                    .addComponent(btnSkill2)
                    .addGap(24, 24, 24)
                    .addComponent(btnSkill3))
                .addComponent(lblBackground, javax.swing.GroupLayout.PREFERRED_SIZE, 1430, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(60, 60, 60)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(2, 2, 2)
                            .addComponent(prgPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(prgEnemy, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(prgPlayerMana, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(prgEnemyMana, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(5, 5, 5)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblPlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblEnemy, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(73, 73, 73)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnSkill1)
                        .addComponent(btnSkill2)
                        .addComponent(btnSkill3)))
                .addComponent(lblBackground, javax.swing.GroupLayout.PREFERRED_SIZE, 850, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>

    private void lblPlayerAncestorAdded(javax.swing.event.AncestorEvent evt) {
        // TODO add your handling code here:

    }

    private void btnSkill2ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // Ensure the player is alive before proceeding
        if (!player.isAlive()) {
            lblEnemy.setText("You lost the battle!");
            prompt();
            return;
        }

        if(player.getMana() < player.getS2ManaCost()){
            promptMana();
            return;
        }

        // Player's turn to attack the enemy
        player.setSkillChoice(2);
        attackEnemy();
        enemyAtckPlayer();

    }

    private void btnSkill1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // Ensure the player is alive before proceeding
        if (!player.isAlive()) {
            lblEnemy.setText("You lost the battle!");
            prompt();
            return;
        }

        if(player.getMana() < player.getS1ManaCost()){
            promptMana();
            return;
        }

        // Player's turn to attack the enemy
        player.setSkillChoice(1);
        attackEnemy();
        enemyAtckPlayer();

    }

    private void btnSkill3ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // Ensure the player is alive before proceeding
        if (!player.isAlive()) {
            lblEnemy.setText("You lost the battle!");
            prompt();
            return;
        }

        if(player.getMana() < player.getS3ManaCost()){
            promptMana();
            return;
        }

        // Player's turn to attack the enemy
        player.setSkillChoice(3);
        attackEnemy();
        enemyAtckPlayer();

    }

    private void prgPlayerAncestorAdded(javax.swing.event.AncestorEvent evt) {
        // TODO add your handling code here:
        prgPlayer.setValue(100);
        prgPlayer.setStringPainted(true);
    }

    private void prgEnemyAncestorAdded(javax.swing.event.AncestorEvent evt) {
        // TODO add your handling code here:
        prgEnemy.setValue(100);
        prgEnemy.setStringPainted(true);
    }

    private void prgPlayerManaAncestorAdded(javax.swing.event.AncestorEvent evt) {
        // TODO add your handling code here:
        prgPlayerMana.setValue(player.getMana());
        prgPlayerMana.setStringPainted(true);
    }

    private void prgEnemyManaAncestorAdded(javax.swing.event.AncestorEvent evt) {
        // TODO add your handling code here:
        prgEnemyMana.setValue(enemies[currentEnemyIndex].getMana());
        prgEnemyMana.setStringPainted(true);
    }

    private void lblEnemyAncestorAdded(javax.swing.event.AncestorEvent evt) {
        // TODO add your handling code here:
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */

        // Make sure to run the JFrame on the Event Dispatch Thread


        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BattleScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BattleScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BattleScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BattleScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>



        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BattleScreen().setVisible(true);
            }
        });


    }

    // Variables declaration - do not modify
    private javax.swing.JButton btnSkill1;
    private javax.swing.JButton btnSkill2;
    private javax.swing.JButton btnSkill3;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.JLabel lblBackground;
    private javax.swing.JLabel lblEnemy;
    private javax.swing.JLabel lblPlayer;
    private javax.swing.JProgressBar prgEnemy;
    private javax.swing.JProgressBar prgEnemyMana;
    private javax.swing.JProgressBar prgPlayer;
    private javax.swing.JProgressBar prgPlayerMana;
    // End of variables declaration


}
