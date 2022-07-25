package com.fefu2022.summerpractice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Helper {
    private JFrame helperJFrame;
    private JPanel panelMain;
    private JButton button1;
    private JLabel label;
    private JButton button2;
    private static int index = 0;
    String[] ArrayMessage;


    public Helper() {
        //String[] ArrayMessage;
        ArrayMessage = new String[4];
        ArrayMessage[0] = "<html><p style=\"border:black 1px solid \" >\n" +
                "                <b>Степенные, показательные и\n" +
                "                    логарифмические функции:</b><br>\n" +
                "                <br>\n" +
                "                <i><b>экспонента.....................................................</b></i> exp(x)<br>\n" +
                "                <br>\n" +
                "                <i><b>натуральный логарифм...............................</b></i>ln (x)<br>\n" +
                "                <br>\n" +
                "                <i><b>логарифм..........................................................</b></i>log_a (x)<br>\n" +
                "                <br>\n" +
                "                <i><b>степень............................................................</b></i>x^n<br>\n" +
                "                <br>\n" +
                "            </p></html>";
        ArrayMessage[1] = "<html><p style=\"border:black 1px solid \" >\n" +
                "                <b>Тригонометрические\n" +
                "                    функции:</b><br>\n" +
                "\n" +
                "                <br>\n" +
                "                <i><b>синус.................................................................</b></i>sin(x)<br>\n" +
                "                <br><i><b>косинус.............................................................</b></i>cos(x)<br>\n" +
                "                <br><i><b>тангенс............................................................</b></i>tan(x)<br>\n" +
                "                <br><i><b>котангенс........................................................</b></i>ctan(x)<br>\n" +
                "                <br>\n" +
                "\n" +
                "                <i><b>косеканс...........................................................</b></i>csc(x)<br>\n" +
                "                <br>\n" +
                "                <i><b>секанс...............................................................</b></i>sec(x)<br>\n" +
                "                <br>\n" +
                "\n" +
                "\n" +
                "            </p></html>";
        ArrayMessage[2] = "<html><p style=\"border:black 1px solid \" >\n" +
                "            <b>Обратные тригонометрические\n" +
                "                функции:</b><br>\n" +
                "            <br>\n" +
                "            <i><b>арксинус.........................................................</b></i> arcsin(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>арккосинус......................................................</b></i>arccos(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>арктангенс.....................................................</b></i>arctan(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>арккотангенс.................................................</b></i>arcctan(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>арккосеканс.....................................................</b></i></sup>arccsc(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>арксеканс........................................................</b></i></sup>arcsec(x)<br>\n" +
                "            <br>\n" +
                "\n" +
                "        </p></html>";
        ArrayMessage[3] = "<html><p style=\"border:black 1px solid \" >\n" +
                "            <b>Гиперболические функции :</b><br>\n" +
                "            <br>\n" +
                "            <i><b>гиперболический синус................................</b></i> sinh(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>гиперболический косинус............................</b></i>cosh(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>гиперболический тангенс..............................</b></i>tanh(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>гиперболический котангенс......................</b></i>ctanh(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>гиперболический косеканс...........................</b></i></sup>csch(x)<br>\n" +
                "            <br>\n" +
                "            <i><b>гиперболический секанс................................</b></i></sup>sech(x)<br>\n" +
                "            <br>\n" +
                "        </p></html>";

        label.setText(ArrayMessage[index]);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (index >= 3) {
                    index = 0;
                } else {
                    index += 1;
                }
                label.setText(ArrayMessage[index]);
            }
        });
        button2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (index <= 0) {
                    index = 3;
                } else {
                    index -= 1;
                }
                label.setText(ArrayMessage[index]);


            }
        });
        initFrame();
    }

    public JFrame getHelperJFrame() {
        return helperJFrame;
    }

    public void initFrame() {

        helperJFrame = new JFrame("helper");
        helperJFrame.setContentPane(panelMain);
        helperJFrame.pack();
        helperJFrame.setLocationRelativeTo(null);
//        helperJFrame.setVisible(true);
        helperJFrame.setMinimumSize(new Dimension(300, 300));

    }
    public void setVisibleHelperFrame(boolean flag){
        helperJFrame.setVisible(flag);

    }
}
