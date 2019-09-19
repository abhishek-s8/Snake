import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.*;
import java.util.LinkedList;
import javax.swing.*;

public class Snake extends JFrame implements KeyListener, ActionListener {
    Timer t = new Timer(50,this); //the timer that starts the game as well as the speed of the snake
    private SnakePanel s = new SnakePanel(); //represents the board
    LinkedList list = new LinkedList(); //the body of the snake 
    boolean gamePaused = false; //holds whether the game is paused or not
    int x = 0; //the x coordinate of board
    int y = 0; //the y coordinate of board
    public Snake() {
        t.start(); //starts the timer
        s.setFocusable(true); //whether this component is focusable
        s.addKeyListener(this); //adds the specified key listener to receive key events from this component
        add(s); //add snake 
        setVisible(true); //shows this window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Sets the operation that will happen when the user closes this frame
        setSize(410,410); //sets the size of the frame
        setLocationRelativeTo(null); //windows place middle of screen
        setTitle("Snake"); //sets title of window as Snake
    }
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Snake Game\n\nUse Keys:\n                                W > Move Up\nA > Move Left       S > Move Down       D > Move Right\n\nH > Help\nP > Pause (Press again to resume)\nQ > Exit", "Welcome Screen", JOptionPane.DEFAULT_OPTION);
        new Snake();  //links to the snake method that starts the game   
        playIntroSound.playIntroSound(); //plays the intro music for game
    }
    public void keyPressed(KeyEvent e) {} //when a key has been pressed
    public void keyReleased(KeyEvent e) {} //when a key has been released
    public void keyTyped(KeyEvent e) { //the key that was typed
        char ch = e.getKeyChar(); //gets the character that was typed
        if(ch == 'w') { //if w go up
            s.moveUp();
        }
        else if(ch == 's') { //if s go down
            s.moveDown();
        }
        else if(ch == 'a') { //if a go up 
            s.moveLeft();
        }
        else if(ch == 'd') { //if d go right
            s.moveRight();
        }
        else if(ch == 'q') { //if q end the game
            t.stop();
            playGameOverSound.playGameOverSound(); //plays the game over sound
            JOptionPane.showMessageDialog(null, "You quit the Game!\nClosing...", "Closing" , JOptionPane.CLOSED_OPTION);            
            dispose(); //resources will be destroyed
        }
        else if(ch == 'p') { //if p pause the game 
            if(gamePaused) { //if not paused continue
                gamePaused = false;
                t.start();
            }
            else { //stop the game 
                gamePaused = true;
                t.stop();
                s.repaint(); //repaints the message 
            }
        }

        else { //if user enters any other key stop and display message
            t.stop(); 
            s.repaint();
            gamePaused = true;
            JOptionPane.showMessageDialog(null, "SNAKE Game \n\nUse Keys:\n                                W > Move Up\nA > Move Left       S > Move Down       D > Move Right\n\nH > Help\nP > Pause (Press again to resume)\nQ  > Exit", "Help", JOptionPane.WARNING_MESSAGE);
            gamePaused = false;
            t.start(); //start when user presses OK
        }
    }
    public void actionPerformed(ActionEvent e) {
        s.sX += x; 
        s.sY += y;        
        if(checkCollision(s.sX, s.sY))  {    
        	playGameOverSound.playGameOverSound();
            JOptionPane.showMessageDialog(null, "Game Over\nFinal Score : " + (list.size() + 1));
            t.stop();
            dispose();
            new Choice(); //opens the window giving user a choice of restarting
        }
        s.repaint(); //updates
    }
  
    public boolean checkCollision(int x, int y) {
        if(x < 0 || x > 380 || y < 0 || y  > 360) { //if snake hits edges game over
            return true;
        }
        if(!list.isEmpty()) { //if snake hits itself game over
            for(int i=0; i<list.size()-1; i++) {
                Point p = (Point)list.get(i);
                if(x > p.x - 10 && x < p.x + 10 && y > p.y - 10 && y < p.y + 10) {
                    return true;                    
                }                    
            }
        }
        return false; //otherwise snake didn't die yet
    }
    
    public class SnakePanel extends JPanel {
        int sX = 200; //x the snake spawns at initially  
        int sY = 200; //y the snake spawns at initially
        int foodX = 300; //the x the food spawns at initially
        int foodY = 300; //they y the food spawns at initially
        boolean eaten = false; //whether the snake was eaten or not
        public void generateFood() { //generates the food around the grid randomly
            foodX = (int)(Math.random()*300) + 20; 
            foodY = (int)(Math.random()*300) + 20;
            if(checkCollision(foodX, foodY)) //if the snake goes to the point that is occupied by the food generate another one
                generateFood();
        }
        public void moveDown() { //goes 10 up
            y = 10;
            x = 0;
        }
        public void moveUp() { //goes 10 down
            y = -10;
            x = 0;
        }    
        public void moveLeft() { //goes 10 left
            x = -10;
            y = 0;
        }
        public void moveRight() { //goes 10 right
            x = 10;
            y = 0;
        }
        public void foodEaten() {
            if(sX > foodX - 10 && sX < foodX + 10 && sY > foodY - 10 && sY < foodY + 10) { //when snake hits food coordinates
                playSound.playSound(); //plays sound every time food is eaten
                generateFood(); //generates food afterwards
                Point p = new Point();  //the point where snake hits food
                p.x = sX; //where the x of the snake is 
                p.y = sY; //where the y of the snake is
                list.addLast(p); //adds length to the snake on that point
            }
        }
        protected void paintComponent(Graphics g) {
            g.setColor(Color.black); //sets to black
            g.fillRect(0,0,410,410); //dimensions           
            g.setColor(Color.DARK_GRAY); //the grid color
            for(int i=0; i<410; i+=10) {     //draws the grid outline
                for(int j=0; j<410; j+=10) {
                    g.drawRect(i, j, 10, 10);
                }
            }
            g.setColor(Color.orange); //colour of food
            g.fillOval(foodX, foodY, 10, 10); //fills in colour
            g.setColor(Color.white); //colour of outline of food
            g.drawOval(foodX, foodY, 10, 10); //draws the oval of food
            foodEaten(); //food gets eaten
            Point pAdd = new Point(); //
            pAdd.x = sX;
            pAdd.y = sY;
            list.addLast(pAdd); //adds to the snake length
            if(!list.isEmpty()) {
                for(int i=0; i<list.size(); i++) {
                    Point p = (Point)list.get(i);
                    if(i == list.size()-1) 
                        g.setColor(Color.red);
                    else
                        g.setColor(Color.blue);
                    g.fillRect(p.x, p.y, 10, 10);         
                    g.setColor(Color.white);
                    g.drawRect(p.x, p.y, 10, 10);
                    g.drawRect(p.x-1, p.y-1, 12, 12);
                }
            }
            list.removeFirst(); //removes and returns the first element from this list
            g.setColor(Color.yellow); //colour of the score tracker
            g.drawString("Your Score : ", 0, 10);  //string of the score
            g.setColor(Color.white); //colour of the number
            g.drawString(String.valueOf(list.size() + 1), 80, 10);//displays the score which is the size of the snake
            if(gamePaused) { //displays the message when game paused
                g.setColor(Color.red); //in red
                g.drawString("GAME PAUSED", 145, 180);
            }
        }
    }

public class Choice extends JFrame implements ActionListener{ //jframe that pops up showing the choice to play game
    private JButton yes = new JButton("Yes"); //yes button
    private JButton no = new JButton("No"); //no button
    private JPanel question = new JPanel(); //create panel
    private JLabel label = new JLabel("Do you wish to play again?"); //label asking question
    Font font = new Font("Verdana", Font.BOLD, 20); //the font
    public Choice() {
        question.setBackground(Color.white);
        question.setForeground(Color.black);
        question.setEnabled(false);
        question.setSize(400,50);
        add(question, BorderLayout.CENTER); //add and put in center 
        question.setLayout(new FlowLayout(FlowLayout.CENTER));
        question.add(label);
        question.setForeground(Color.black);
        label.setFont(font);        
        JPanel p = new JPanel(); //creates another panel for yes/no button
        p.setLayout(new GridLayout(1,2));
        p.setVisible(true);
        p.add(yes); //add yes button 
        p.add(no); //add no button 
        add(p, BorderLayout.SOUTH); //add and put in south of layout
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        yes.addActionListener(this);
        no.addActionListener(this);
        yes.setBackground(Color.white);
        yes.setForeground(Color.black);
        yes.setSize(200,100);
        no.setBackground(Color.white); 
        no.setForeground(Color.black); 
        no.setSize(200,100); 
        setTitle("#AnotherOne"); 
        setSize(400,150); 
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == yes) { //if user picks yes button
            dispose(); //destroy all memory used
            new Snake(); //start again
            playIntroSound.playIntroSound(); //play the intro sound
        }
        else if(e.getSource() == no) { //if user picks no button
            dispose(); //destroy all memory 
        }
    }
}
}
