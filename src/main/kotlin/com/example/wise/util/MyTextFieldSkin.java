package com.example.wise.util;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.skins.JFXTextFieldSkin;
import javafx.scene.control.TextField;

public class MyTextFieldSkin extends JFXTextFieldSkin {

    /**
     * 回显字符，可设置成自己想要的字符
     */
    private char BULLET = '\u25cf';


    public MyTextFieldSkin(TextField textField, char bullet) {
        super(textField);
        this.BULLET = bullet;
    }

    @Override
    protected String maskText(String txt) {
        if (getSkinnable() instanceof JFXPasswordField) {
            int n = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n);
            for (int i = 0; i < n; i++) {
                passwordBuilder.append(BULLET);
            }

            return passwordBuilder.toString();
        } else {
            return txt;
        }
    }

    public char getBULLET() {
        return BULLET;
    }

    public void setBULLET(char BULLET) {
        this.BULLET = BULLET;
    }

}
