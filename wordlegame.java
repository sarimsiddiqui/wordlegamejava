package wordle_project;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.*;

public class wordle_game implements ActionListener{
	
	// this class creates the game board panel for the word to sit 
	class Panel extends JPanel {
		
		JLabel[] words_column = new JLabel [5];
		public Panel() {
			this.setLayout(new GridLayout (1,5));
			Border borderblack = BorderFactory.createLineBorder(Color.BLACK);
			for (int i=0; i<5; ++i) {
				words_column [i] = new JLabel();
				words_column [i].setHorizontalAlignment(JLabel.CENTER);
				words_column[i].setOpaque(true);
				words_column[i].setBorder(borderblack);
				this.add(words_column[i]);
			}
		}
		
		public void clearpanel() {
			for (int i=0; i<5; ++i) {
				words_column [i].setText("");
			}
		}
		
		public void paneltext (String cvalue, int position, Color color) {
			this.words_column[position].setText(cvalue);;
			this.words_column[position].setBackground(color);
		}
		
	}
	
	
	// this class creates the user input and "ok" button layout 
	class userinput extends JPanel {
		
		
		private JTextField UserInput;
		private JButton ok;
		
		
		public userinput () {
			this.setLayout(new GridLayout (1, 2));
			UserInput = new JTextField();
			this.add(UserInput);
			ok = new JButton("Check");
			this.add(ok);
			
		}
		
		public JTextField getUserInput() {
			return UserInput;
		}
		
		public JButton getok() {
			return ok;
		}
		
	}
	
	
	private JFrame mainframe;
	private Panel[] PanelArray = new Panel[6];
	private userinput UserInput;
	private String wordString;
	private int count=0;
	
	
	// this class puts all components into a frame 
	public wordle_game() {
		mainframe = new JFrame("WORDLE");
		mainframe.setSize(800,800);
		mainframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainframe.setLayout(new GridLayout (7,1));
		mainframe.setVisible(true);;
		mainframe.setLocationRelativeTo(null);
		
		for (int i=0; i<6; ++i) {
			PanelArray [i]= new Panel();
			mainframe.add(PanelArray[i]);
		}
		UserInput = new userinput();
		UserInput.getok().addActionListener(this);
		mainframe.add(UserInput);
		mainframe.revalidate();
		
		wordString = getwordString();
		System.out.println("Answer: " + wordString );
	}
	
	
	

	// this class describes what happens when the user clicks "check"
	@Override
	public void actionPerformed(ActionEvent e) {
		String userword = this.UserInput.getUserInput().getText();
		
		if(userword.length() >4) {
			if (check(userword.trim().toUpperCase())) {
				JOptionPane.showMessageDialog(null, "It took you " +count+ " tries!", "You win!", JOptionPane.INFORMATION_MESSAGE );
				mainframe.dispose();
				return;
			}
		}
		if (count >5) {
			JOptionPane.showMessageDialog(null, "You Lost.", "Better luck next time.", JOptionPane.INFORMATION_MESSAGE);
			mainframe.dispose();
			return;
		}
		count++;
		
	}
	
	private void clearpanels () {
		for (int i=0; i<=count; ++i) {
			PanelArray[i].clearpanel();
		}
	}
	
	// this class checks if the word contains letters or is the correct word
	private boolean check(String userword) {
		List<String> wordList = Arrays.asList(wordString.split(""));
		String[] userArray = userword.split("");
		List <Boolean> wordMatchList = new ArrayList<>();
		
		for (int i=0; i<5; ++i) {
			if (wordList.contains(userArray[i])) {
				if(wordList.get(i).equals(userArray[i])) {
					getActivePanel().paneltext(userArray[i], i, Color.GREEN);
					wordMatchList.add(true);
				}
				else {
					getActivePanel().paneltext(userArray[i], i, Color.YELLOW);
					wordMatchList.add(false);
				}
				
			}
			else {
				getActivePanel().paneltext(userArray[i], i, Color.LIGHT_GRAY);
				wordMatchList.add(false);
			}
		}
		return !wordMatchList.contains(false);
	}
	
	public Panel getActivePanel() {
		return this.PanelArray[count];
		
	}
	// chooses a word from txt file 
	public String getwordString() {
		
		Path path = Paths.get("/Users/sarimsiddiqui/Downloads/valid-wordle-words.txt");
		List <String> wordList = new ArrayList<> ();
		try {
			wordList = Files.readAllLines(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Random random = new Random();
		int position = random.nextInt(wordList.size());
		return wordList.get(position).trim().toUpperCase();
	}
	
	
	// main method
	public static void main(String[] args) {
		new wordle_game();
		
	}

}
