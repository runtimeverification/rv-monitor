package com.izforge.izpack.panels;


import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.text.html.HTMLDocument;
import com.izforge.izpack.util.HyperlinkHandler;

public class HTMLPane extends JScrollPane
{
    private JEditorPane pane;

    public HTMLPane(String content)
    {
        this.pane = new JTextPane();
        this.pane.setContentType("text/html; charset=utf-8");
        // add a CSS rule to force body tags to use the default label font
        // instead of the value in javax.swing.text.html.default.csss
        Font font = UIManager.getFont("Label.font");
        String bodyRule = "body { font-family: " + font.getFamily() + "; " +
        "font-size: " + font.getSize() + "pt; }";
        ((HTMLDocument)this.pane.getDocument()).getStyleSheet().addRule(bodyRule);
        this.pane.setEditable(false);
        this.pane.setOpaque(false);
        this.pane.setBorder(null);
        this.pane.addHyperlinkListener(new HyperlinkHandler());
        this.pane.setText(content);

        this.setViewportView(this.pane);
        this.setBorder(null);
    }

    public void setContentHTML(String html)
    {
        this.pane.setText(html);
    }
}