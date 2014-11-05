package com.ielfgame.stupidGame.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.font.*;
import java.text.*;
import java.awt.geom.*;
import java.awt.image.*;

public class TAttributes extends JFrame {
    // 1. Constructor
    public TAttributes(String title) {
        super(title);
        setBackground(Color.white);
    }

    // 2. The main method...
    public static void main(String arg[]) {
        TAttributes frame = new TAttributes("TAttributes");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        frame.getContentPane().add("Center", new DisplayPanel());
        frame.pack();
        frame.setSize(new Dimension(500,200));
        frame.show();
    }
}

// 3. Definition of the display panel class
class DisplayPanel extends JPanel { 
    String text = "  This is  Java 2D Text!!!";
    AttributedString attribString;
    AttributedCharacterIterator attribCharIterator;
    Image javaImage;

    public DisplayPanel() {
        // 4. Prepare the display panel with suitable size and background
        setBackground(Color.lightGray);
        setSize(500, 200);

        // 5. Create the attributed string object using the given text.
        attribString = new AttributedString(text);

        // 6. Add a star shaped graphic attribute in the begining of the
        // given text.
        GeneralPath star = new GeneralPath();
        
        star.moveTo(0, 0); // x and y coordinates
        star.lineTo(10, 30); star.lineTo(-10, 10);
        star.lineTo(10, 10); star.lineTo(-10, 30);
        star.closePath();
        GraphicAttribute starShapeAttr = new ShapeGraphicAttribute(star,
                                             GraphicAttribute.TOP_ALIGNMENT,
                                             false);
        
//      TextAttribute.
        
        attribString.addAttribute(TextAttribute.CHAR_REPLACEMENT, /* Attribute Key */
                                  starShapeAttr, /* Attribute Value */
                                  0, /* Begin Index */
                                  1); /* End Index */
        attribString.addAttribute(TextAttribute.FOREGROUND,
                                  new Color(255, 255, 0),
                                  0, 1); // Start and end indexes.

        // 7. Modify the foreground and font attributes of "This is"
        // in the given string text.
        int index = text.indexOf("This is");
        attribString.addAttribute(TextAttribute.FOREGROUND,
                                  Color.blue,
                                  index, index+7); // Start and end indexes.
        Font font = new Font("sanserif", Font.ITALIC, 40);
        attribString.addAttribute(TextAttribute.FONT, font, index, index+7);
        
        attribString.addAttribute(TextAttribute.FONT, font.deriveFont(Font.BOLD|Font.ITALIC), index+3, index+7);
        
        attribString.addAttribute(TextAttribute.FONT, font.deriveFont(Font.BOLD|Font.ITALIC, 30), index+5, index+7);
        // 8. Load the specified image and prepare the image attribute
        loadImage();
        BufferedImage bimage = new BufferedImage(javaImage.getWidth(null),
                                                 javaImage.getHeight(null),
                                                 BufferedImage.TYPE_INT_ARGB);
        Graphics2D big = bimage.createGraphics();
        big.drawImage(javaImage, null, this);
        GraphicAttribute javaImageAttr = new ImageGraphicAttribute(bimage,
                                         GraphicAttribute.HANGING_BASELINE,
                                         0, 0);

        // 9. Add the image attribute before the "Java" substring
        index = text.indexOf("Java");
        attribString.addAttribute(TextAttribute.CHAR_REPLACEMENT,
                                  javaImageAttr, index-1, index);
        
        // 10. Modify the attributes of the substring "Java"
        font = new Font("serif", Font.BOLD, 60); 
        attribString.addAttribute(TextAttribute.FONT, font, index, index+4);
        attribString.addAttribute(TextAttribute.FOREGROUND,
                                  new Color(243, 63, 163),
                                  index, index+4); // Start and end indexes.

        // 11. Underline the substring "2D"
        index = text.indexOf("2D");
        attribString.addAttribute(TextAttribute.UNDERLINE,
                                 TextAttribute.UNDERLINE_ON,
                                 index, index+2);

        // 12. Modify the font sizes after the substring Java.
        index = text.indexOf("2D Text!!!"); 
        font = new Font("sanserif", Font.ITALIC, 40);
        attribString.addAttribute(TextAttribute.FONT, font, index, index+10);

        // 13. Change the color of "2D" to white and the color of remaining
        // text to blue.
        attribString.addAttribute(TextAttribute.FOREGROUND,
                                  Color.white,
                                  index, index+2); // Start and end indexes.
        attribString.addAttribute(TextAttribute.FOREGROUND,
                                  Color.blue,
                                  index+3, index+10); // Start and end indexes.
    }
    
    // 14. This method loads the specified image.
    public void loadImage() {
        // Create an image object using the specified file
        javaImage = Toolkit.getDefaultToolkit().getImage("D:/work/DesignerWorkspace/image/golden.png");
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(javaImage, 1);
        try {
            mt.waitForAll();
        } catch (Exception e) {
            System.out.println("Exception while loading.");
        }

        // If the image has an unknown width, the image is not created
        // by using the suggested file. Therefore, exit the program.  
        if (javaImage.getWidth(this) == -1) {
            System.out.println("*** Make sure you have the suggested image "
                            + "(java.gif) file in the images directory.***");
            System.exit(0);
        }
    }

    // 15. The paintComponent method...
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Retrieve the character iterator from the attributed string
        attribCharIterator = attribString.getIterator();

        // Create a font render contex object and text layout
        FontRenderContext frc = new FontRenderContext(null, false, false);
        TextLayout layout = new TextLayout(attribCharIterator, frc);

        // Draw the string
        layout.draw(g2, 20, 100);
    }
}
