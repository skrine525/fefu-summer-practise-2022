package com.fefu2022.summerpractice;

import com.fefu2022.summerpractice.GraphicPanel.Graphic;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GraphicApp {
    private JFrame frame;
    public static JLabel statusLabel;
    private JTextField colorTextField;
    private JTextField functionTextField;
    private GraphicPanel graphicPanel;
    private final Helper helper;

    public GraphicApp(Helper helper) {
        this.helper=helper;
        createFrame();
        initElements();
    }

    private void createFrame() {
        frame = new JFrame("График функции");
//        frame.setExtendedState(Frame.MAXIMIZED_HORIZ);
//        frame.setUndecorated(true);развернутый экран

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void initElements() {
        Container mainContainer = frame.getContentPane();
        mainContainer.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel(); // Нижняя панель состояния
        bottomPanel.setBackground(Color.lightGray); // Фон светло-серый
        mainContainer.add(bottomPanel, BorderLayout.SOUTH); // Распологается внизу

        statusLabel = new JLabel("Инициализация приложения.."); // Элемент, который будет показывать текст состояния программы
        bottomPanel.add(statusLabel);    // Добавляем его в нижнюю панель

        Box leftPanel = createLeftPanel(); // Создаем левую панель в другом методе
        mainContainer.add(leftPanel, BorderLayout.WEST); // Эта панель будет слева

        graphicPanel = new GraphicPanel();
        graphicPanel.setBackground(Color.WHITE);
        mainContainer.add(graphicPanel);
//        frame.add(helper.getHelperJFrame());



    }


    private Box createLeftPanel() {
        Box panel = Box.createVerticalBox();

        // Box это контейнер, в котором элементы выстраиваются в одном порядке

        panel.add(Box.createVerticalStrut(10)); // Отступы
        JLabel title = new JLabel("<html>Панель управления</html>", JLabel.CENTER);
        JButton information = new JButton("<html><span >helper</span></html>");

        information.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                helper.setVisibleHelperFrame(true);
//                frame.add(helper.getHelperJFrame());
                frame.repaint();
                //JOptionPane.showMessageDialog(null, "<html><div><p style=\"border:black 1px solid \" ><b>Степенные, показательные и логарифмические функции:</b><br><br><i><b>экспонента.....................................................</b></i> exp(x)<br><br><i><b>натуральный логарифм.................................</b></i>ln (x)<br><br><i><b>логарифм..........................................................</b></i>log <sub>a</sub>(x)>>>>>log_a (x)<br><br><i><b>степень............................................................</b></i>x<sup>n</sup>>>>>>x^n<br><br></p><p style=\"border:black 1px solid \" ><b>Тригонометрические функции:</b><br><br><i><b>косеканс...........................................................</b></i></sup>csc(x)<br><br><i><b>косеканс...........................................................</b></i></sup>csc(x)<br><br><i><b>секанс...............................................................</b></i></sup>sec(x)<br><br></p></div><p style=\"border:black 1px solid \" ><b>Обратныетригонометрическиефункции:</b><br><br><i><b>арксинус.........................................................</b></i> arcsin(x)<br><br><i><b>арккосинус......................................................</b></i>arccos(x)<br><br><i><b>арктангенс.....................................................</b></i>arctan(x)<br><br><i><b>арккотангенс.................................................</b></i>arcctan(x)<br><br><i><b>арккосеканс.....................................................</b></i></sup>arccsc(x)<br><br><i><b>арксеканс........................................................</b></i></sup>arcsec(x)<br><br></p><p style=\"border:black 1px solid \" ><b>Гиперболические функции :</b><br><br><i><b>гиперболический синус................................</b></i> sinh(x)<br><br><i><b>гиперболический косинус............................</b></i>cosh(x)<br><br><i><b>гиперболический тангенс..............................</b></i>tanh(x)<br><br><i><b>гиперболический котангенс......................</b></i>ctanh(x)<br><br><i><b>гиперболический косеканс...........................</b></i></sup>csch(x)<br><br><i><b>гиперболический секанс................................</b></i></sup>sech(x)<br><br></p></div></html>");

            }
        });
        //JOptionPane.showMessageDialog(information, "<html><br><h2>Инструкция использования графического калькулятора</h2><p>Для перемещение по графику <b>зажмите ЛКМ</b> и двигайте курсором мыши</p><br><p>Для маштабирования графика используйте <b>колесо мыши</b> </p></html>");
        //JOptionPane.showMessageDialog(information, "<html><p>в графичесом колькуляторе работают такие мат функции:</p><br><div><p style=\"border:black 1px solid \" ><b>Степенные, показательные и логарифмические функции:</b><br><br><i><b>экспонента.....................................................</b></i> exp(x)<br><br><i><b>натуральный логарифм.................................</b></i>ln (x)<br><br><i><b>логарифм..........................................................</b></i>log <sub>a</sub>(x)>>>>>log_a (x)<br><br><i><b>степень............................................................</b></i>x<sup>n</sup>>>>>>x^n<br><br></p><p style=\"border:black 1px solid \" ><b>Тригонометрическиефункции:</b><br><br><i><b>косеканс...........................................................</b></i></sup>csc(x)<br><br><i><b>косеканс...........................................................</b></i></sup>csc(x)<br><br><i><b>секанс...............................................................</b></i></sup>sec(x)<br><br></p></div><p style=\"border:black 1px solid \" ><b>Обратныетригонометрическиефункции:</b><br><br><i><b>арксинус.........................................................</b></i> arcsin(x)<br><br><i><b>арккосинус......................................................</b></i>arccos(x)<br><br><i><b>арктангенс.....................................................</b></i>arctan(x)<br><br><i><b>арккотангенс.................................................</b></i>arcctan(x)<br><br><i><b>арккосеканс.....................................................</b></i></sup>arccsc(x)<br><br><i><b>арксеканс........................................................</b></i></sup>arcsec(x)<br><br></p><p style=\"border:black 1px solid \" ><b>Гиперболические функции :</b><br><br><i><b>гиперболический синус................................</b></i> sinh(x)<br><br><i><b>гиперболический косинус............................</b></i>cosh(x)<br><br><i><b>гиперболический тангенс..............................</b></i>tanh(x)<br><br><i><b>гиперболический котангенс......................</b></i>ctanh(x)<br><br><i><b>гиперболический косеканс...........................</b></i></sup>csch(x)<br><br><i><b>гиперболический секанс................................</b></i></sup>sech(x)<br><br></p></div></html>");


        // Чтобы добавить перевод строки в тексте, нужно писать в тегах <html>
        information.setFont(new Font(null, Font.ITALIC, 12)); // изменяем шрифт

        panel.add(title);


        //panel.add(Box.createVerticalStrut(20)); // Отступы
        panel.add(Box.createVerticalGlue()); // Заполнитель пустого пространства
        panel.add(new JLabel("Функция:"));
        functionTextField = new JTextField(8);  // Поле ввода названия
        functionTextField.setMaximumSize(new Dimension(300, 30)); // Чтобы не был слишком большим
        panel.add(functionTextField);
        functionTextField.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Graphic.ReadyStatus readyStatus = graphicPanel.addGraphic(functionTextField.getText(), Color.RED);
                if(readyStatus == Graphic.ReadyStatus.Ready)
                    functionTextField.setText("");
                else if (readyStatus == Graphic.ReadyStatus.ErrorSyntax)
                    JOptionPane.showMessageDialog(new JFrame(), "Неверно введена функция", "Ошибка", JOptionPane.ERROR_MESSAGE);
                else
                    JOptionPane.showMessageDialog(new JFrame(), "Не найден аргумент X в заданной функции", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton clearButton = new JButton();
        clearButton.setText("Очистить");

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphicPanel.clear();
                functionTextField.setText("");
            }

        });
        panel.add(clearButton);

        JButton moveToOriginButton = new JButton();
        moveToOriginButton.setText("Центр графика");
        moveToOriginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                graphicPanel.moveToOrigin();


            }

        });
        panel.add(moveToOriginButton);
        panel.add(information);
        panel.add(Box.createVerticalGlue()); // Заполнитель пустого пространства


        return panel;

    }

}
