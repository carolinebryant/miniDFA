import javax.swing.*;
import java.awt.*;
/*
Scrollable window that displays all output for the MIPS machine
 */
public class OutputWindow extends JFrame {

    private JPanel centerPanel;
    private JTextArea textArea;

    public OutputWindow(){
        super("MIPS Output");
        this.setSize(600, 800);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        centerPanel = new JPanel();

        textArea = new JTextArea(40, 60);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setAutoscrolls(true);
        textArea.setBackground(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane(textArea);

        centerPanel.add(scrollPane);

        add(centerPanel);
        getContentPane().add(centerPanel, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
    }

    public void addText(String text){
        textArea.append(text + "\n");
        //Updates the textbox in the event of a selection
        textArea.setCaretPosition(textArea.getDocument().getLength());
    }
}
