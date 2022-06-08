package com.fefu2022.summerpractice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.mariuszgromada.math.mxparser.*;      // Какой-то чоткий прекрасный мат. 

public class GraphicPanel extends JPanel {
    ////////////////////////////////////////
    // Статические методы
    
    // Метод создания линии графика со всеми характеристиками
    private Line2D.Float GraphicLine(double x1, double y1, double x2, double y2){
        int x1_int, x2_int, y1_int, y2_int;
        
        if(x1 == Double.POSITIVE_INFINITY)
            x1_int = width;
        else if(x1 == Double.NEGATIVE_INFINITY)
            x1_int = 0;
        else
            x1_int = ((int) x1) + offsetX;
        
        if(x2 == Double.POSITIVE_INFINITY)
            x2_int = width;
        else if(x2 == Double.NEGATIVE_INFINITY)
            x2_int = 0;
        else
            x2_int = ((int) x2) + offsetX;
        
        if(y1 == Double.NEGATIVE_INFINITY)
            y1_int = height;
        else if(y1 == Double.POSITIVE_INFINITY)
            y1_int = 0;
        else
            y1_int = ((int) y1) + offsetY;
        
        if(y2 == Double.NEGATIVE_INFINITY)
            y2_int = height;
        else if(y2 == Double.POSITIVE_INFINITY)
            y2_int = 0;
        else
            y2_int = ((int) y2) + offsetY;
        
        return new Line2D.Float(x1_int, y1_int, x2_int, y2_int);
    }
    
    // Метод расчитывания размеров текста на экране
    private static Rectangle2D getTextSize(String text, Font font) {
        TextLayout tl = new TextLayout(text, font, new FontRenderContext(null, true, true));
        return tl.getBounds();
    }
    
    // Метод форматизации пометки на сетке
    private static String formatGridMark(double mark){
        if(mark % 1 == 0)
            return String.valueOf(Math.round(mark));
        else
            return String.valueOf(mark);
    }
    
    ////////////////////////////////////////
    // Внутренние классы
    
    public class GraphicData{
        public Expression expression;
        public Argument xArg;
        public Color color;
        
        public GraphicData(String expressionString, Color color){
            this.xArg = new Argument("x");
            this.expression = new Expression(expressionString, this.xArg);
            this.color = color;
        }
    }
    
    ////////////////////////////////////////
    // Код класса
    
    private int width, height;
    private int offsetX = 0, offsetY = 0;
    private byte gridUnitSize;
    private double graphicScale, graphicUnitSize;
    private ArrayList<GraphicData> graphics;
    
    private static final int DEFAULT_GRAPHIC_SCALE = 1;
    private static final int DEFAULT_GRAPHIC_UNIT_SIZE = 30;
    
    public GraphicPanel(){
        super();
        
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)); // Устанавливаем другой курсор
        
        graphicScale = DEFAULT_GRAPHIC_SCALE;
        gridUnitSize = DEFAULT_GRAPHIC_UNIT_SIZE;
        graphicUnitSize = DEFAULT_GRAPHIC_UNIT_SIZE / DEFAULT_GRAPHIC_SCALE;
        graphics = new ArrayList<GraphicData>();
        
        // Добавляем слушателей мыши
        MouseAdapter mouseAdapter = new MouseAdapter() {
            private int lastX, lastY;
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent e){
                if(contains(e.getPoint())){
                    int wheelRotation = (int) -e.getPreciseWheelRotation();
                    
                    gridUnitSize += wheelRotation;
                    if(gridUnitSize < 20){
                        graphicScale *= 2;
                        gridUnitSize += 20;
                    }
                    else if(gridUnitSize >= 40){
                        graphicScale /= 2;
                        gridUnitSize -= 20;
                    }
                    graphicUnitSize = gridUnitSize / graphicScale;
                    
                    GraphicApp.statusLabel.setText(gridUnitSize + " " + graphicUnitSize + " " + wheelRotation);
                    
                    /*
                    int cursorOnGraphicX = e.getLocationOnScreen().x - getX() - width / 2;
                    int dY = e.getLocationOnScreen().y - getY();
                    if(cursorOnGraphicX < width / 2)
                        offsetX -= (int) (cursorOnGraphicX * graphicUnitSize / gridUnitSize);
                    else if(width / 2 < cursorOnGraphicX)
                        offsetX += (int) (cursorOnGraphicX * graphicUnitSize / gridUnitSize);
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
        
        drawGrid(g);        // Рисуем сетку
        drawAxis(g);        // Рисуем оси
        drawGraphic(g);     // Рисуем график
    }

    private void drawGrid(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        
        for(int x = width / 2; x < width - offsetX; x += gridUnitSize){  // Цикл от центра до правого края
            g.setColor(Color.LIGHT_GRAY);  // Задаем серый цвет
            g.drawLine(x + offsetX, 0, x + offsetX, height);    // Вертикальная линия
            
            // Отрисовка пометок на оси X справа
            int realX = x - width / 2;
            if(realX % gridUnitSize == 0){
                int num = realX / gridUnitSize;
                if(num % 5 == 0){
                    int markX = x + offsetX + 2;
                    int markY = height / 2 + offsetY - 2;
                    String markText = formatGridMark(num * graphicScale);
                    Rectangle2D markTextSize = getTextSize(markText, getFont());
                    int markWidth = (int) Math.round(markTextSize.getWidth());
                    int markHeight = (int) Math.round(markTextSize.getHeight());
                    
                    if(realX != 0){
                        if(markY + 2 > height){
                            markY += (height - markY - 2);
                        }
                        else if(markY - markHeight - 2 < 0){
                            markY -= (markY - markHeight - 2);
                        }
                    }
                    
                    g.setColor(Color.WHITE);
                    g2.fillRect(markX, markY - markHeight, markWidth, markHeight);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(markText, markX, markY);
                }
            }
        }
        
        for(int x = width / 2; x > -offsetX; x -= gridUnitSize){  // Цикл от центра до леваого края
            g.setColor(Color.LIGHT_GRAY);  // Задаем серый цвет
            g.drawLine(x + offsetX, 0, x + offsetX, height);   // Вертикальная линия
            
            // Отрисовка пометок на оси X слева
            int realX = x - width / 2;
            if(realX % gridUnitSize == 0 && realX != 0){
                int num = realX / gridUnitSize;
                if(num % 5 == 0){
                    int markX = x + offsetX + 2;
                    int markY = height / 2 + offsetY - 2;
                    String markText = formatGridMark(num * graphicScale);
                    Rectangle2D markTextSize = getTextSize(markText, getFont());
                    int markWidth = (int) Math.round(markTextSize.getWidth());
                    int markHeight = (int) Math.round(markTextSize.getHeight());
                    
                    if(markY + 2 > height){
                        markY += (height - markY - 2);
                    }
                    else if(markY - markHeight - 2 < 0){
                        markY -= (markY - markHeight - 2);
                    }
                    
                    g.setColor(Color.WHITE);
                    g2.fillRect(markX, markY - markHeight, markWidth, markHeight);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(markText, markX, markY);
                }
            }
        }
        
        for(int y = height / 2; y < height - offsetY; y += gridUnitSize){  // Цикл от центра до верхнего края
            g.setColor(Color.LIGHT_GRAY);  // Задаем серый цвет
            g.drawLine(0, y + offsetY, width, y + offsetY);    // Горизонтальная линия
            
            // Отрисовка пометок на оси Y вниз
            int realY = y - height / 2;
            if(realY % gridUnitSize == 0 && realY != 0){
                int num = realY / gridUnitSize;
                if(num % 5 == 0){
                    int markX = width / 2 + offsetX + 2;
                    int markY = y + offsetY - 2;
                    String markText = formatGridMark(-num * graphicScale);
                    Rectangle2D markTextSize = getTextSize(markText, getFont());
                    int markWidth = (int) Math.round(markTextSize.getWidth());
                    int markHeight = (int) Math.round(markTextSize.getHeight());
                    
                    if(markX + markWidth + 2 > width){
                        markX += (width - markX - markWidth - 2);
                    }
                    else if (markX - 2 < 0){
                        markX -= (markX - 2);
                    }
                    
                    g.setColor(Color.WHITE);
                    g2.fillRect(markX, markY - markHeight, markWidth, markHeight);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(markText, markX, markY);
                }
            }
        }
        
        for(int y = height / 2; y > -offsetY; y -= gridUnitSize){  // Цикл от центра до леваого края
            g.setColor(Color.LIGHT_GRAY);  // Задаем серый цвет
            g.drawLine(0, y + offsetY, width, y + offsetY);    // Горизонтальная линия
            
            // Отрисовка пометок на оси Y вверх
            int realY = y - height / 2;
            if(realY % gridUnitSize == 0 && realY != 0){
                int num = realY / gridUnitSize;
                if(num % 5 == 0){
                    int markX = width / 2 + offsetX + 2;
                    int markY = y + offsetY - 2;
                    String markText = formatGridMark(-num * graphicScale);
                    Rectangle2D markTextSize = getTextSize(markText, getFont());
                    int markWidth = (int) Math.round(markTextSize.getWidth());
                    int markHeight = (int) Math.round(markTextSize.getHeight());
                    
                    if(markX + markWidth + 2 > width){
                        markX += (width - markX - markWidth - 2);
                    }
                    else if (markX - 2 < 0){
                        markX -= (markX - 2);
                    }
                    
                    g.setColor(Color.WHITE);
                    g2.fillRect(markX, markY - markHeight, markWidth, markHeight);
                    g.setColor(Color.DARK_GRAY);
                    g.drawString(markText, markX, markY);
                }
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
        
        Stroke lastStroke = g2.getStroke(); // Сохраняем данные предыдущего Stroke
        g2.setStroke(new BasicStroke(2)); // Устанавливаем размер линии графика
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);    // Включаем антиалиасинг
        
        for(var graph : graphics){
            g.setColor(graph.color);
        
            boolean hasLastPoint = false;
            double lastPointX = 0, lastPointY = 0;

            for(int x = -offsetX; x < width - offsetX; x++){           // Делаем цикл с левой стороны экрана до правой
                //boolean canDraw = true;
                double realX = x - width / 2, realY = 0;   // Так, как слева от оси OX минус, то отнимаем от текущей точки центральную точку

                //realY = calculateFunction(graph.expression, realX / graphicUnitSize) * graphicUnitSize;
                graph.xArg.setArgumentValue(realX / graphicUnitSize);
                realY = graph.expression.calculate() * graphicUnitSize;

                if(Double.isNaN(realY)){
                    graph.xArg.setArgumentValue((realX - 0.01) / graphicUnitSize);
                    double leftLimitSign = graph.expression.calculate() * graphicUnitSize;
                    leftLimitSign = Math.signum(leftLimitSign);

                    graph.xArg.setArgumentValue((realX + 0.01) / graphicUnitSize);
                    double rightLimitSign = graph.expression.calculate() * graphicUnitSize;
                    rightLimitSign = Math.signum(rightLimitSign);

                    double y = Double.POSITIVE_INFINITY;
                    if(leftLimitSign == -1)
                        y = Double.NEGATIVE_INFINITY;

                    g2.draw(GraphicLine(lastPointX, lastPointY, x, y));

                    if(rightLimitSign == -1)
                        lastPointY = Double.NEGATIVE_INFINITY;
                    else if(rightLimitSign == 1)
                        lastPointY = Double.POSITIVE_INFINITY;

                    x++;
                    lastPointX = x;
                }
                else{
                    int y = (int) Math.round(height / 2 - realY);
                    //System.out.println("f("+ realX + ")="+realY);

                    if(hasLastPoint){
                        g2.draw(GraphicLine(lastPointX, lastPointY, x, y));

                        lastPointX = (double) x;
                        lastPointY = (double) y;
                    }
                    else{
                        hasLastPoint = true;
                        lastPointX = (double) x;
                        lastPointY = (double) y;
                    }
                }
            }
        }
        
        g2.setStroke(lastStroke); // Возвращаем предыдущий Stroke
    }
    
    private double calculateFunction(String exp, double x)/* throws Exception*/{
        /*
        MathParser parser = new MathParser();
        parser.setVariable("x", x);
        
        System.out.print("f("+x+")=");
        
        double y;
        try{
            y = parser.Parse(exp);
        }
        catch(Exception e){
            y = 0;
        }
        
        System.out.println(y);
        */
        
        Argument a = new Argument("x="+x);
        Expression e = new Expression(exp, a);
        double y = e.calculate();
        
        return y;
    }
    
    public void addGraphic(String expression, Color color){
        graphics.add(new GraphicData(expression, color));
        repaint();
    }
    
    public void clear(){
        offsetX = 0;
        offsetY = 0;
        graphicScale = DEFAULT_GRAPHIC_SCALE;
        gridUnitSize = DEFAULT_GRAPHIC_UNIT_SIZE;
        graphicUnitSize = DEFAULT_GRAPHIC_UNIT_SIZE / DEFAULT_GRAPHIC_SCALE;
        graphics.clear();
        repaint();
    }
    
    public void moveToOrigin(){
        offsetX = 0;
        offsetY = 0;
        repaint();
    }
}
