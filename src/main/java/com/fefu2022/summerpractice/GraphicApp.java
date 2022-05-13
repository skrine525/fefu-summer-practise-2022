package com.fefu2022.summerpractice;

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

    public GraphicApp(){
        createFrame();
        initElements();
    }

    private void createFrame() {
        frame = new JFrame("График функции");
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void show(){
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
    }

    private Box createLeftPanel() {
        Box panel = Box.createVerticalBox();  // Вертикальный Box
        // Box это контейнер, в котором элементы выстраиваются в одном порядке
        
        panel.add(Box.createVerticalStrut(10)); // Отступы
        JLabel title = new JLabel("<html>Панель управления</html>", JLabel.CENTER);
        // Чтобы добавить перевод строки в тексте, нужно писать в тегах <html>
        title.setFont(new Font(null, Font.BOLD, 12)); // изменяем шрифт
        
        panel.add(title);

        //panel.add(Box.createVerticalStrut(20)); // Отступы
        panel.add(Box.createVerticalGlue()); // Заполнитель пустого пространства
        panel.add(new JLabel("Функция:"));
        functionTextField = new JTextField(8);  // Поле ввода названия
        functionTextField.setMaximumSize(new Dimension(300, 30)); // Чтобы не был слишком большим
        panel.add(functionTextField);
        functionTextField.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e)
            {
                graphicPanel.addGraphic(functionTextField.getText(), Color.RED);
                functionTextField.setText("");
            }
        });
        JButton clearButton = new JButton();
        clearButton.setText("Очистить");
        clearButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                graphicPanel.clear();
                functionTextField.setText("");
            }
            
        });
        panel.add(clearButton);
        
        JButton moveToOriginButton = new JButton();
        moveToOriginButton.setText("Центр графика");
        moveToOriginButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                graphicPanel.moveToOrigin();
            }
            
        });
        panel.add(moveToOriginButton);

        panel.add(Box.createVerticalGlue()); // Заполнитель пустого пространства

        return panel;
    }
}