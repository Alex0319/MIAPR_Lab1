import by.kmiddle.KMiddle;
import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

/**
 * Created by user1 on 06.02.2017.
 */
public class MainForm extends JFrame{
    private JPanel pnlMain;
    private JPanel pnlDraw;
    private JButton btnSearch;
    private JTextField txtfldDotsCount;
    private JTextField txtfldClassCount;

    private final static int windowWidth=700;
    private final static int windowHeight=500;

    public MainForm(){
        super("Алгоритм K-средних");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(pnlMain);
        Point location=getCenterLocation();
        setBounds(location.x,location.y,windowWidth,windowHeight);
        setResizable(false);
        setLookAndFeel();
        setVisible(true);

        btnSearch.addActionListener(new SearchAction(this));
    }

    public Dimension getDrawPanelDimension(){
        return pnlDraw.getSize();
    }

    public Graphics getDrawPanelGraphics(){
        return pnlDraw.getGraphics();
    }

    public int getDotsCount(){
        if(checkTextFieldValue(txtfldDotsCount.getText(),10,Integer.MAX_VALUE)){
            return Integer.parseInt(txtfldDotsCount.getText());
        }
        return -1;
    }

    public int getClassCount(){
        if(checkTextFieldValue(txtfldClassCount.getText(),1,100)){
            return Integer.parseInt(txtfldClassCount.getText());
        }
        return -1;
    }

    private boolean checkTextFieldValue(String text, int lowerBound,int upperBound){
        String errorStr=String.format("Input number from %d to %d",lowerBound,upperBound);
        if(txtfldDotsCount.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, errorStr, "Error", JOptionPane.ERROR_MESSAGE);
        }else{
            try{
                int value =Integer.parseInt(text);
                if(value<lowerBound || value>upperBound){
                    JOptionPane.showMessageDialog(this, errorStr, "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    return true;
                }
            }catch(Exception e){
                JOptionPane.showMessageDialog(this, errorStr, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return false;
    }

    private Point getCenterLocation(){
        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        return new Point((screenSize.width-windowWidth)/2,(screenSize.height-windowHeight)/2);
    }

    private void setLookAndFeel(){
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        }
        catch (UnsupportedLookAndFeelException e) {
            JFrame.setDefaultLookAndFeelDecorated(true);
        }
    }

    public static void main(String[] args){
        new MainForm();
    }

    public void paint(Graphics g){
        super.paint(g);
        SearchAction searchAction= (SearchAction)btnSearch.getActionListeners()[0];
        searchAction.paintKMiddleResult(getDrawPanelDimension());
        g.dispose();
    }
}

class SearchAction implements ActionListener{

    private KMiddle kMiddle;
    private MainForm mainForm;


    public SearchAction(MainForm mainForm){
        this.mainForm=mainForm;
    }

    public void paintKMiddleResult(Dimension dim){
        if(kMiddle!=null){
            Graphics g=mainForm.getDrawPanelGraphics();
            g.clearRect(0,0,dim.width,dim.height);
            kMiddle.paintResult(g);
            g.dispose();
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int dotsCount=mainForm.getDotsCount();
        if(dotsCount>0){
            int classCount=mainForm.getClassCount();
            if(classCount>0){
                Dimension dim=mainForm.getDrawPanelDimension();
                kMiddle=new KMiddle(dotsCount,classCount,dim.width,dim.height);
                kMiddle.startSearch();
                paintKMiddleResult(dim);
                mainForm.setFocusable(true);
            }
        }
    }
}