package by.kmiddle;

import org.w3c.dom.css.RGBColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Date;

/**
 * Created by user1 on 07.02.2017.
 */
public class KMiddle {
    private ArrayList<Dot> arrayListDots;
    private ArrayList<Dot> arrayListClassDots;
    private static Random random=new Random(new Date().getTime());

    public KMiddle(int dotsCount,int classCount,int maxX,int maxY){
        arrayListDots=getDots(dotsCount,maxX,maxY);
        arrayListClassDots=getClassDots(arrayListDots,classCount);
    }

    private ArrayList<Dot> getDots(int dotsCount,int maxX,int maxY){
        ArrayList<Dot> arrayDots=new ArrayList<>();
        for (int i=0;i<dotsCount;i++) {
            arrayDots.add(new Dot(random.nextInt(maxX),random.nextInt(maxY)));
        }
        return arrayDots;
    }

    private ArrayList<Dot> getClassDots(ArrayList<Dot> arrayDots,int classCount){
        ArrayList<Color> listColors=getColors(classCount);
        ArrayList<Dot> arrayClassDots=new ArrayList<>();
        for(int i=0;i<classCount;i++){
            int classDotIndex=0;
            if(arrayDots.size()!=0) {
                classDotIndex = random.nextInt(arrayDots.size());
                int colorIndex=random.nextInt(listColors.size());
                arrayClassDots.add(arrayDots.get(classDotIndex));
                arrayDots.remove(classDotIndex);
                arrayClassDots.get(i).setColor(listColors.get(colorIndex));
                listColors.remove(colorIndex);
            }
        }
        return arrayClassDots;
    }

    private ArrayList<Color> getColors(int classCount){
        ArrayList<Color> listColors=new ArrayList<>();
        for(int i=0;i<classCount;i++){
            listColors.add(new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256)));
        }
        return listColors;
    }

    private double getEuclideanDistance(Dot classDot,Dot dot){
        double differentX=dot.getX()-classDot.getX();
        double differentY=dot.getY()-classDot.getY();
        return Math.sqrt(Math.pow(differentX,2)+Math.pow(differentY,2));
    }

    public boolean checkClassDotsCoordinates(int dotsCount,int classDotsCount){
        boolean result=false;
        Point[] sumDotsCoordinates=new Point[classDotsCount];
        int[] dotsCountInClass=new int[classDotsCount];
        for(int i=0;i<classDotsCount;i++){
            sumDotsCoordinates[i]=new Point();
        }
        for(int i=0;i<dotsCount;i++){
            Dot currentDot=arrayListDots.get(i);
            sumDotsCoordinates[currentDot.getClassIndex()].x+=currentDot.getX();
            sumDotsCoordinates[currentDot.getClassIndex()].y+=currentDot.getY();
            dotsCountInClass[currentDot.getClassIndex()]++;
        }
        for(int i=0;i<classDotsCount;i++){
            if(dotsCountInClass[i]!=0 && arrayListClassDots.get(i).isClassDotCoordinatesChange(sumDotsCoordinates[i].x/dotsCountInClass[i],
                    sumDotsCoordinates[i].y/dotsCountInClass[i])){
                result=true;
            }
        }
        return result;
    }

    public void startSearch(){
        int dotsCount=arrayListDots.size(),classCount=arrayListClassDots.size();
        do {
            for (int i = 0; i < dotsCount; i++) {
                int classIndex = 0;
                double minDistance = getEuclideanDistance(arrayListDots.get(i), arrayListClassDots.get(0));
                for (int j = 1; j < classCount; j++) {
                    double currentDistance = getEuclideanDistance(arrayListDots.get(i), arrayListClassDots.get(j));
                    if (currentDistance < minDistance) {
                        minDistance = currentDistance;
                        classIndex = j;
                    }
                }
                arrayListDots.get(i).setColor(arrayListClassDots.get(classIndex).getColor());
                arrayListDots.get(i).setClassIndex(classIndex);
            }
        }while (checkClassDotsCoordinates(dotsCount,classCount));
    }

    public void paintResult(Graphics g){
        for (Dot dot: arrayListDots) {
            dot.drawDot(g,3,3);
        }
        for (Dot dot: arrayListClassDots) {
            dot.drawDot(g,20,20);
        }
    }
}

class Dot{
    private int x,y,classIndex;
    private Color color;

    Dot(int x,int y){
        this.x=x;
        this.y=y;
    }

    int getX(){
        return x;
    }

    int getY() { return y; }

    int getClassIndex(){return classIndex;}

    Color getColor(){
        return color;
    }

    void setClassIndex(int index){
        classIndex=index;
    }

    void setColor(Color color){
        this.color=new Color(color.getRGB());
    }

    boolean isClassDotCoordinatesChange(int newX,int newY){
        if(newX!=x || newY != y){
            x=newX;
            y=newY;
            return true;
        }
        return false;
    }

    void drawDot(Graphics g,int width,int height){
        Color oldColor=g.getColor();
        g.setColor(color);
        g.drawOval(x,y,width,height);
        g.fillOval(x,y,width+1,height+1);
        g.setColor(oldColor);
    }
}
