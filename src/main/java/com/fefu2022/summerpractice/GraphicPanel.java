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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Line2D;

public class GraphicPanel extends JPanel {
    private Color graphicColor = Color.RED;
    private int width, height;
    private int offsetX = 0, offsetY = 0;
    private byte drawGraphicUnitSize;
    private short scaleCount;
    private double graphicScale, graphicUnitSize;
    private String strFunction;
    
    private static final int DEFAULT_GRAPHIC_SCALE = 1;
    private static final int DEFAULT_GRAPHIC_UNIT_SIZE = 30;
    
    public GraphicPanel(){
        super();
        
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)); // Устанавливаем другой курсор
        
        graphicScale = DEFAULT_GRAPHIC_SCALE;
        drawGraphicUnitSize = DEFAULT_GRAPHIC_UNIT_SIZE;
        graphicUnitSize = drawGraphicUnitSize / graphicScale;
        scaleCount = 0;
        
        
        // Добавляем слушателей мыши
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private int lastX, lastY;
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e){
                if(contains(e.getPoint())){
                    int wheelRotation = (int) -e.getPreciseWheelRotation();
                    
                    drawGraphicUnitSize += wheelRotation;
                    if(drawGraphicUnitSize < 20){
                        graphicScale *= 2;
                        drawGraphicUnitSize += 20;
                        scaleCount--;
                    }
                    else if(drawGraphicUnitSize >= 40){
                        graphicScale /= 2;
                        drawGraphicUnitSize -= 20;
                        scaleCount++;
                    }
                    graphicUnitSize = drawGraphicUnitSize / graphicScale;
                    
                    GraphicApp.statusLabel.setText(drawGraphicUnitSize + " " + graphicUnitSize + " " + wheelRotation + " " + scaleCount);
                    
                    /*
                    int cursorOnGraphicX = e.getLocationOnScreen().x - getX() - width / 2;
                    int dY = e.getLocationOnScreen().y - getY();
                    if(cursorOnGraphicX < width / 2)
                        offsetX -= (int) (cursorOnGraphicX * graphicUnitSize / drawGraphicUnitSize);
                    else if(width / 2 < cursorOnGraphicX)
                        offsetX += (int) (cursorOnGraphicX * graphicUnitSize / drawGraphicUnitSize);
                    */
                    
                    repaint();
                }
            }
            
            @Override
            public void mousePressed(MouseEvent e){
                if(contains(e.getPoint())){
                    lastX = e.getLocationOnScreen().x - getX();
                    lastY = e.getLocationOnScreen().y - getY();
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e){
                if(contains(e.getPoint())){
                    int dX = e.getLocationOnScreen().x - getX();
                    int dY = e.getLocationOnScreen().y - getY();
                    offsetX += dX - lastX;
                    offsetY += dY - lastY;
                    lastX = dX;
                    lastY = dY;
                    repaint();
                }
            }
        };
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        width = getWidth();
        height = getHeight();
        
        drawGrid(g); // Рисуем сетку
        drawAxis(g); // Рисуем оси
        drawGraphic(g); // Рисуем график
    }

    private void drawGrid(Graphics g) { 
        for(int x = width / 2; x < width - offsetX; x += drawGraphicUnitSize){  // Цикл от центра до правого края
            g.setColor(Color.LIGHT_GRAY);  // Задаем серый цвет
            g.drawLine(x + offsetX, 0, x + offsetX, height);    // Вертикальная линия
            
            // Отрисовка пометок на оси X справа
            g.setColor(Color.DARK_GRAY);
            int realX = x - width / 2;
            if(realX % drawGraphicUnitSize == 0){
                int num = realX / drawGraphicUnitSize;
                if(num % 5 == 0)
                    g.drawString(String.valueOf(num * graphicScale), x + offsetX + 2, height / 2 + offsetY - 2);
            }
        }
        
        for(int x = width / 2; x > -offsetX; x -= drawGraphicUnitSize){  // Цикл от центра до леваого края
            g.setColor(Color.LIGHT_GRAY);  // Задаем серый цвет
            g.drawLine(x + offsetX, 0, x + offsetX, height);   // Вертикальная линия
            
            // Отрисовка пометок на оси X слева
            g.setColor(Color.DARK_GRAY);
            int realX = x - width / 2;
            if(realX % drawGraphicUnitSize == 0 && realX != 0){
                int num = realX / drawGraphicUnitSize;
                if(num % 5 == 0)
                    g.drawString(String.valueOf(num * graphicScale), x + offsetX + 2, height / 2 + offsetY - 2);
            }
        }
        
        for(int y = height / 2; y < height - offsetY; y += drawGraphicUnitSize){  // Цикл от центра до верхнего края
            g.setColor(Color.LIGHT_GRAY);  // Задаем серый цвет
            g.drawLine(0, y + offsetY, width, y + offsetY);    // Горизонтальная линия
            
            // Отрисовка пометок на оси Y вниз
            g.setColor(Color.DARK_GRAY);
            int realY = y - height / 2;
            if(realY % drawGraphicUnitSize == 0 && realY != 0){
                int num = realY / drawGraphicUnitSize;
                if(num % 5 == 0)
                    g.drawString(String.valueOf(-num * graphicScale), width / 2 + offsetX + 2, y + offsetY - 2);
            }
        }
        
        for(int y = height / 2; y > -offsetY; y -= drawGraphicUnitSize){  // Цикл от центра до леваого края
            g.setColor(Color.LIGHT_GRAY);  // Задаем серый цвет
            g.drawLine(0, y + offsetY, width, y + offsetY);    // Горизонтальная линия
            
            // Отрисовка пометок на оси Y вверх
            g.setColor(Color.DARK_GRAY);
            int realY = y - height / 2;
            if(realY % drawGraphicUnitSize == 0 && realY != 0){
                int num = realY / drawGraphicUnitSize;
                if(num % 5 == 0)
                    g.drawString(String.valueOf(-num * graphicScale), width / 2 + offsetX + 2, y + offsetY - 2);
            }
        }
    }

    private void drawAxis(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawLine(width / 2 + offsetX, 0, width / 2 + offsetX, height);
        g.drawLine(0, height / 2 + offsetY, width, height / 2 + offsetY);
    }
    
    private void drawGraphic(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(graphicColor); // Устанавливаем цвет графика
        
        Stroke lastStroke = g2.getStroke(); // Сохраняем данные предыдущего Stroke
        g2.setStroke(new BasicStroke(2)); // Устанавливаем размер линии графика
        
        
        boolean hasLastPoint = false;
        int lastPointX = 0, lastPointY = 0;

        for(int x = -offsetX; x < width - offsetX; x++){           // Делаем цикл с левой стороны экрана до правой
            boolean canDraw = true;
            double realX = x - width / 2, realY = 0;   // Так, как слева от оси OX минус, то отнимаем от текущей точки центральную точку
            
            try{
                realY = (int) Math.round((calculateFunction(realX / graphicUnitSize)) * graphicUnitSize);
            }
            catch (Exception e){
                canDraw = false;
            }
            
            if(canDraw){
                int y = (int) (height / 2 - realY);
                //System.out.println("f("+ realX + ")="+realY);
                
                if(hasLastPoint){
                    g2.draw(new Line2D.Float(lastPointX + offsetX, lastPointY + offsetY, x + offsetX, y + offsetY));
                    
                    lastPointX = x;
                    lastPointY = y;
                }
                else{
                    hasLastPoint = true;
                    lastPointX = x;
                    lastPointY = y;
                }
            }
            else
                hasLastPoint = false;
        }

        
        g2.setStroke(lastStroke); // Возвращаем предыдущий Stroke
    }
    
    private double calculateFunction(double x) throws Exception{
        double y = 0;

        String[] p = strFunction.split("x");

        String col_y=p[0]+x+p[1];


//        if(p.length > 1){
//            if(p[0].equals("sin") && p.length > 2){
//                double c = Double.valueOf(p[1]);
//                double b = Double.valueOf(p[2]);
//
//                double rad = x;   // Переводим текущую коориднату в радианы, 30 пикселей по ширине == 1 радиану
//                double sin = Math.sin(rad * b);       // Вычисляем синус угла
//                y = (sin * c);  // Переводим значение синуса в координату нашей системы
//            }
//            else if(p[0].equals("cos") && p.length > 2){
//                double c = Double.valueOf(p[1]);
//                double b = Double.valueOf(p[2]);
//
//                double rad = x;   // Переводим текущую коориднату в радианы, 30 пикселей по ширине == 1 радиану
//                double cos = Math.cos(rad * b);       // Вычисляем косинус угла
//                y = (cos * c);  // Переводим значение синуса в координату нашей системы
//            }
//            else if(p[0].equals("g")){
//                double c = Double.valueOf(p[1]);
//
//                if(x == 0)
//                    throw new Exception();
//                y = c / x;
//            }
//            else if(p[0].equals("pow") && p.length > 2){
//                double c = Double.valueOf(p[1]);
//                double b = Double.valueOf(p[2]);
//
//                y = Math.pow(x * c, b);
//            }
//            else if(p[0].equals("s") && p.length > 2){
//                double c = Double.valueOf(p[1]);
//                double b = Double.valueOf(p[2]);
//
//                y = c * x + b;
//            }
//            else if(p[0].equals("fuck") && p.length > 2){
//                double c = Double.valueOf(p[1]);
//                double b = Double.valueOf(p[2]);
//
//                double rad = x;   // Переводим текущую коориднату в радианы, 30 пикселей по ширине == 1 радиану
//                double cos = Math.cos(rad * b);       // Вычисляем синус угла
//                double sin = Math.sin(rad * c);       // Вычисляем синус угла
//                y = (sin * cos);  // Переводим значение синуса в координату нашей системы
//            }
//        }
//        else
//            throw new Exception();
        MathParser parser = new MathParser();

        String[] expressions = {col_y};

        for(String expression:expressions){
            System.out.print(expression+"  ");
            try{
                System.out.print(parser.Parse(expression)+"\n");
                y=parser.Parse(expression);
            } catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return y;
    }
    
    public void drawFunction(String function){
        strFunction = function;
        repaint();
    }
    
    public void clear(){
        offsetX = 0;
        offsetY = 0;
        graphicScale = DEFAULT_GRAPHIC_SCALE;
        drawGraphicUnitSize = DEFAULT_GRAPHIC_UNIT_SIZE;
        graphicUnitSize = drawGraphicUnitSize / graphicScale;
        scaleCount = 0;
        strFunction = "";
        repaint();
    }
    
    public void moveToOrigin(){
        offsetX = 0;
        offsetY = 0;
        repaint();
    }
}
