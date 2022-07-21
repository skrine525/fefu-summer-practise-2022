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

// Какой-то чоткий прекрасный мат. парсер
import org.mariuszgromada.math.mxparser.*;
import org.mariuszgromada.math.mxparser.parsertokens.Token;

public class GraphicPanel extends JPanel {
    ////////////////////////////////////////
    // Статические методы
    
    // Метод создания линии графика со всеми характеристиками
    private Line2D.Float GraphicLine(double x1, double y1, double x2, double y2){
        int x1_int, x2_int, y1_int, y2_int;
        
        if(x1 == Double.POSITIVE_INFINITY)
            x1_int = width + 4;
        else if(x1 == Double.NEGATIVE_INFINITY)
            x1_int = -5;
        else
            x1_int = ((int) x1) + offsetX;
        
        if(x2 == Double.POSITIVE_INFINITY)
            x2_int = width + 4;
        else if(x2 == Double.NEGATIVE_INFINITY)
            x2_int = -5;
        else
            x2_int = ((int) x2) + offsetX;
        
        if(y1 == Double.NEGATIVE_INFINITY)
            y1_int = height + 4;
        else if(y1 == Double.POSITIVE_INFINITY)
            y1_int = -5;
        else
            y1_int = ((int) y1) + offsetY;
        
        if(y2 == Double.NEGATIVE_INFINITY)
            y2_int = height + 4;
        else if(y2 == Double.POSITIVE_INFINITY)
            y2_int = -5;
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
    
    public class Graphic{
        private Expression exp;
        private Argument x;
        private Color color;
        private double unitSize;
        
        private ArrayList<Expression> breakpointExpressions;
        private Argument breakpointExpressionArgumentA, breakpointExpressionArgumentB;
        
        // Обработка проблем с тангенсом
        private boolean hasTan = false;
        
        public Graphic(String expressionString, Color color){
            x = new Argument("x");
            exp = new Expression(expressionString, x);
            unitSize = GraphicPanel.DEFAULT_GRAPHIC_UNIT_SIZE;
            this.color = color;
            
            ArrayList<String> breakSubfuncsArray = new ArrayList<String>();
            findBreakSubfunctions(breakSubfuncsArray, (ArrayList) exp.getCopyOfInitialTokens(), 0);
            System.out.println(breakSubfuncsArray);
            breakpointExpressionArgumentA = new Argument("a");
            breakpointExpressionArgumentB = new Argument("b");
            breakpointExpressions = new ArrayList<Expression>();
            for(String s : breakSubfuncsArray){
                breakpointExpressions.add(new Expression("solve(f(x), x, a, b)", new Function("f(x) = " + s), breakpointExpressionArgumentA, breakpointExpressionArgumentB));
            }
            
            for(Token token : exp.getCopyOfInitialTokens()){
                if(token.tokenStr == "tan"){
                    hasTan = true;
                    break;
                }
            }
        }
        
        public Color getColor(){
            return color;
        }
        
        public void setUnitSize(double size){
            unitSize = size;
        }
        
        public double calculate(double x){
            this.x.setArgumentValue(x / unitSize);
            return exp.calculate() * unitSize;
        }
        
        public void calculateBreakpoints(ArrayList<Double> points, double a, double b){
            a = a / unitSize;
            b = b / unitSize;
            
            //System.out.println("[" + a + "; " + b + "]");
            
            for(Expression e : breakpointExpressions){
                breakpointExpressionArgumentA.setArgumentValue(a);
                breakpointExpressionArgumentB.setArgumentValue(a + Math.PI);
                while(true){
                    if(breakpointExpressionArgumentB.getArgumentValue() > b)
                        breakpointExpressionArgumentB.setArgumentValue(b);
                    
                    double point = e.calculate();
                    //System.out.println(point);
                    if(Double.isNaN(point)){
                        if(breakpointExpressionArgumentB.getArgumentValue() == b)
                            break;
                        else{
                            breakpointExpressionArgumentA.setArgumentValue(breakpointExpressionArgumentB.getArgumentValue());
                            breakpointExpressionArgumentB.setArgumentValue(breakpointExpressionArgumentA.getArgumentValue() + Math.PI);
                        }
                    }
                    else{
                        boolean canAdd = true;
                        for(double d : points){
                            if(d == point){
                                canAdd = false;
                            }
                        }
                        if(canAdd)
                            points.add(point);
                        breakpointExpressionArgumentA.setArgumentValue(point + 0.000001);
                        breakpointExpressionArgumentB.setArgumentValue(breakpointExpressionArgumentA.getArgumentValue() + Math.PI);
                    }
                }
            }
        }
        
        public boolean checkPassageThroughBreakpoint(ArrayList<Double> points, double current){
            current = current / unitSize;
            boolean contact = false;
            
            for(int i = 0; i < points.size(); i++){
                double point = points.get(i);
                if(point <= current){
                    contact = true;
                    points.remove(i);
                }
            }
            
            return contact;
        }
        
        // Ищет подфункции, которые разрывают основную функцию
        private static void findBreakSubfunctions(ArrayList<String> subfuncs, ArrayList<Token> tokens, int startIndex){
            if("/".equals(tokens.get(startIndex).tokenStr)){
                int tokenLevel = tokens.get(startIndex).tokenLevel;
                String subfuncString = "";
                for(int i = startIndex + 1; i < tokens.size(); i++){
                    if(tokens.get(i).tokenLevel <= tokenLevel){
                        if("".equals(subfuncString))
                            subfuncString = tokens.get(i).tokenStr;
                        else
                            break;
                    }
                    else
                        subfuncString += tokens.get(i).tokenStr;
                }
                subfuncs.add(subfuncString);
            }
            else{
                for(int i = startIndex; i < tokens.size(); i++){
                    if("/".equals(tokens.get(i).tokenStr))
                        findBreakSubfunctions(subfuncs, tokens, i);
                }
            }
        }
    }
    
    ////////////////////////////////////////
    // Код класса
    
    private int width, height;
    private int offsetX = 0, offsetY = 0;
    private byte gridUnitSize;
    private double graphicScale, graphicUnitSize;
    private ArrayList<Graphic> graphics;
    
    public static final int DEFAULT_GRAPHIC_SCALE = 1;
    public static final int DEFAULT_GRAPHIC_UNIT_SIZE = 30;
    
    public GraphicPanel(){
        super();
        
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)); // Устанавливаем другой курсор
        
        graphicScale = DEFAULT_GRAPHIC_SCALE;
        gridUnitSize = DEFAULT_GRAPHIC_UNIT_SIZE;
        graphicUnitSize = DEFAULT_GRAPHIC_UNIT_SIZE / DEFAULT_GRAPHIC_SCALE;
        graphics = new ArrayList<Graphic>();
        
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
            g.setColor(graph.getColor());
        
            boolean hasLastPoint = false;
            double lastPointX = 0, lastPointY = 0;
            
            graph.setUnitSize(graphicUnitSize);                        // Устанавливаем размер графика
            
            int startX = -offsetX;
            int finishX = width - offsetX - 1;
            
            ArrayList<Double> breakpoints = new ArrayList<Double>();
            graph.calculateBreakpoints(breakpoints, (startX - width / 2), (finishX - width / 2));
//          System.out.println(breakpoints);        
            
            for(int x = startX; x <= finishX; x++){           // Делаем цикл с левой стороны экрана до правой
                //boolean canDraw = true;
                double realX = x - width / 2, realY = 0;   // Так, как слева от оси OX минус, то отнимаем от текущей точки центральную точку

                //realY = calculateFunction(graph.expression, realX / graphicUnitSize) * graphicUnitSize;
                realY = graph.calculate(realX);
                
                if(Double.isFinite(realY)){
                    int y = (int) Math.round(height / 2 - realY);
                    //System.out.println("f("+ realX + ")="+realY);
                    
                     if(hasLastPoint){
                        if(graph.checkPassageThroughBreakpoint(breakpoints, realX)){
                            double leftLimit = graph.calculate((lastPointX - width / 2) + 0.001);
                            leftLimit = Math.signum(leftLimit) > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

                            double rightLimit = graph.calculate(realX - 0.001);
                            rightLimit = Math.signum(rightLimit) > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
                            
                            g2.draw(GraphicLine(lastPointX, lastPointY, x, leftLimit));
                            g2.draw(GraphicLine(x, rightLimit, x, y));
                            
                            lastPointX = x;
                            lastPointY = y;
                        }
                        else{
                            g2.draw(GraphicLine(lastPointX, lastPointY, x, y));

                            lastPointX = x;
                            lastPointY = y;
                        }
                    }
                    else{
                        hasLastPoint = true;
                        lastPointX = x;
                        lastPointY = y;
                    }
                }
                else{
                    if(graph.checkPassageThroughBreakpoint(breakpoints, realX)){
                        double leftLimit = graph.calculate(realX - 0.001);
                        leftLimit = Math.signum(leftLimit) > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

                        double rightLimit = graph.calculate(realX + 0.001);
                        rightLimit = Math.signum(rightLimit) > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;

                        g2.draw(GraphicLine(lastPointX, lastPointY, x, leftLimit));

                        lastPointX = x;
                        lastPointY = rightLimit;
                    }
                    else
                        hasLastPoint = false;
                }
            }
        }
        
        g2.setStroke(lastStroke); // Возвращаем предыдущий Stroke
    }
    
    public void addGraphic(String expression, Color color){
        graphics.add(new Graphic(expression, color));
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
