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

public class GraphicApp {
    private JFrame frame;
    private JLabel statusLabel;
    private JTextField colorTextField;
    private JTextField nameTextField;
    private GraphicPanel graphicPanel;

    public GraphicApp(){
        createFrame();
        initElements();
    }

    private void createFrame() {
        frame = new JFrame("Графическое приложение");
        frame.setSize(600, 300);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void show(){
        frame.setVisible(true);
    }

    private void initElements() {
        Container mainContainer = frame.getContentPane();
        mainContainer.setLayout(new BorderLayout());

        JPanel bottomPanel = new JPanel(); // нижняя панель состояния
        bottomPanel.setBackground(Color.lightGray); // фон светло-серый
        mainContainer.add(bottomPanel, BorderLayout.SOUTH); // распологается внизу

        statusLabel = new JLabel("Инициализация приложения.."); // Элемент, который будет показывать текст состояния программы
        bottomPanel.add(statusLabel);    // добавляем его в нижнюю панель

        Box leftPanel = createLeftPanel(); // создаем левую панель в другом методе
        mainContainer.add(leftPanel, BorderLayout.WEST); // эта панель будет слева
        
        graphicPanel = new GraphicPanel();
        graphicPanel.setBackground(Color.WHITE);
        mainContainer.add(graphicPanel);
    }

    private Box createLeftPanel() {
        Box panel = Box.createVerticalBox();  // вертикальный Box
        // Box это контейнер, в котором элементы выстраиваются в одном порядке

        JLabel title = new JLabel("<html>Построение графика функции</html>");
        // чтобы добавить перевод строки в тексте, нужно писать в тегах <html>
        title.setFont(new Font(null, Font.BOLD, 12)); // изменяем шрифт
        panel.add(title);

        panel.add(Box.createVerticalStrut(20)); //в Box можно добавлять отступы

        panel.add(new JLabel("Название:"));

        nameTextField = new JTextField(){
            
        };  // поле ввода названия
        nameTextField.setMaximumSize(new Dimension(300, 30)); // чтобы не был слишком большим
        panel.add(nameTextField);

        panel.add(new JLabel("Цвет:"));

        colorTextField = new JTextField("#FF0000");  // поле ввода с начальным текстом
        colorTextField.setMaximumSize(new Dimension(300, 30));
        panel.add(colorTextField);

        panel.add(Box.createVerticalGlue()); // также в Box можно добавлять заполнитель пустого места

        JButton button = new JButton("Нарисовать"); // Кнопка
        panel.add(button);
        return panel;
    }
}