/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fefu2022.summerpractice;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import java.awt.*;

public class GraphicPanel extends JPanel {
    private Color graphicColor = Color.GREEN;
    private int width;
    private int height;
    private int chartUnitSize = 30;
    
    public void paint(Graphics g)
    {
        super.paint(g);
        width = getWidth();
        height = getHeight();
        
        drawGrid(g);
        drawGraphic(g);
        drawAxis(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);  //задаем серый цвет
        
        int coordinateLabel = 0;    // Переменная лебла точки на оси

        for(int x=width/2; x<width; x+=chartUnitSize){  // цикл от центра до правого края
            g.drawLine(x, 0, x, height);    // вертикальная линия
            
            if(coordinateLabel <= 1 || coordinateLabel % 5 == 0)
                g.drawString(String.valueOf(coordinateLabel), x + 1, height / 2 - 1);
            coordinateLabel++;
        }

        for(int x=width/2; x>0; x-=chartUnitSize){  // цикл от центра до леваого края
            g.drawLine(x, 0, x, height);   // вертикальная линия
        }

        for(int y=height/2; y<height; y+=chartUnitSize){  // цикл от центра до верхнего края
            g.drawLine(0, y, width, y);    // горизонтальная линия
        }

        for(int y=height/2; y>0; y-=chartUnitSize){  // цикл от центра до леваого края
            g.drawLine(0, y, width, y);    // горизонтальная линия
        }
    }

    private void drawAxis(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(width/2, 0, width/2, height);
        g.drawLine(0, height/2, width, height/2);
    }
    
    private void drawGraphic(Graphics g) {
        g.setColor(graphicColor); // устанавливаем цвет графика

        for(int x=0; x<width; x++){           // делаем цикл с левой стороны экрана до правой
            int realX = x - width / 2;   // так, как слева от оси OX минус, то отнимаем от текущей точки центральную точку
            //double rad = realX/30.0;   // переводим текущую коориднату в радианы, 30 пикселей по ширине == 1 радиану
            //double sin = Math.sin(rad);       // вычисляем синус угла
            //int f = (int) (sin * 90);  // переводим значение синуса в координату нашей системы
            int f = 2 * realX + 1 * chartUnitSize;
            int y = height / 2 - f;

            g.drawOval(x-1, y-1, 2, 2);   // рисуем кружок в этой точке
        }
    }
}
