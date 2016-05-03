package View;

import java.awt.BorderLayout;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsoleView {
	
	private JFrame window;
	private JTextArea console;
	private JScrollPane scroll;
	
	public ConsoleView(String name) {
		window = new JFrame(name);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.getContentPane().setLayout(new BorderLayout());
		window.setSize(640, 480);
		window.setLocationRelativeTo(null);
		
		console = new JTextArea(20, 80);
		scroll = new JScrollPane (console, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		console.setLineWrap(true);
		console.setEditable(false);
		window.add(scroll);
		window.setVisible(true);
	}
	
	public void write(String message) {
		console.append(message);
		seeLastLine();
	}
	
	public void writeln(String message) {
		insertTime();
		console.append(message + '\n');
		seeLastLine();
	}
	
	public void flush() {
		console.setText("");
		seeLastLine();
	}
	
	public void close() {
		window.dispose();
	}
	
	private void seeLastLine() {
		console.setCaretPosition(console.getDocument().getLength());
	}
	
	private void insertTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		console.append('[' + dateFormat.format(cal.getTime()) + "] : ");
	}

}
