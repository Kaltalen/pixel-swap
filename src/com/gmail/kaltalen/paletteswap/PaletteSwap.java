package com.gmail.kaltalen.paletteswap;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashSet;

public class PaletteSwap extends JFrame implements ActionListener
{
    //Set up Menu Items
    private JMenuBar mb;
    private JMenu file;
    private JMenuItem open;
    private JMenuItem savePalette;
    private JMenuItem openPalette;
    private JMenuItem save;

    private JLabel colorSelection;
    private int selectedColorIndex = 0;


    private JLabel originSpriteLabel;
    private JLabel originPaletteLabel;
    private JLabel newPaletteLabel;
    private JLabel newSpriteLabel;

    private BufferedImage originSprite;
    private BufferedImage originPalette;
    private BufferedImage newSprite;
    private BufferedImage newPalette;

//    JTextArea ta;

    private PaletteSwap()
    {
        open=new JMenuItem("Open Sprite");
        open.addActionListener(this);
        file=new JMenu("File");
        file.add(open);

        savePalette=new JMenuItem("Save Palette");
        savePalette.addActionListener(this);
        file.add(savePalette);

        openPalette=new JMenuItem("Open Palette");
        openPalette.addActionListener(this);
        file.add(openPalette);

        save=new JMenuItem("Save Sprite");
        save.addActionListener(this);
        file.add(save);

        mb=new JMenuBar();
        mb.setBounds(0,0,800,20);
        mb.add(file);
        add(mb);


        BufferedImage temp = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
        temp.setRGB(0,0,Color.white.getRGB());

        Image spriteTemp = temp.getScaledInstance(400, 400, BufferedImage.SCALE_SMOOTH);
        Image barTemp = temp.getScaledInstance(400, 40, BufferedImage.SCALE_SMOOTH);


        originSpriteLabel = new JLabel(new ImageIcon(spriteTemp));
        originSpriteLabel.setBounds(0,20, 400, 400);
        add(originSpriteLabel);

        newSpriteLabel = new JLabel(new ImageIcon(spriteTemp));
        newSpriteLabel.setBounds(400,20, 400, 400);
        add(newSpriteLabel);

        originPaletteLabel = new JLabel(new ImageIcon(barTemp));
        originPaletteLabel.setBounds(0,420, 400, 40);
        originPaletteLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e)
            {
                paletteClick(e.getX(), e.getY());
            }
        });
        add(originPaletteLabel);

        newPaletteLabel = new JLabel(new ImageIcon(barTemp));
        newPaletteLabel.setBounds(400,420, 400, 40);
        add(newPaletteLabel);

        colorSelection = new JLabel(new ImageIcon(barTemp));
        colorSelection.setBounds(0,460, 400, 40);
        add(colorSelection);


    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==open)
        {
            JFileChooser fc=new JFileChooser();
            int res=fc.showOpenDialog(this);
            if(res==JFileChooser.APPROVE_OPTION)
            {
                File f=fc.getSelectedFile();
                String filepath=f.getPath();
                try
                {
                    originSprite = ImageIO.read(new File(filepath));
                    redraw();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        else if(e.getSource()==savePalette)
        {
            JFileChooser fc=new JFileChooser();
            int res=fc.showSaveDialog(this);
            if(res==JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                String filepath = f.getPath();
                File outputFile = new File(filepath);
                try
                {
                    ImageIO.write(originPalette, "png", outputFile);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        else if(e.getSource()==openPalette)
        {
            JFileChooser fc=new JFileChooser();
            int res=fc.showOpenDialog(this);
            if(res==JFileChooser.APPROVE_OPTION)
            {
                File f=fc.getSelectedFile();
                String filepath=f.getPath();
                try
                {
                    newPalette = ImageIO.read(new File(filepath));
                    redraw();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        else if(e.getSource()==save)
        {
            JFileChooser fc=new JFileChooser();
            int res=fc.showSaveDialog(this);
            if(res==JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                String filepath = f.getPath();
                File outputFile = new File(filepath);
                try
                {
                    ImageIO.write(newSprite, "png", outputFile);
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args)
    {
        PaletteSwap om=new PaletteSwap();
        om.setSize(800,800);
        om.setLayout(null);
        om.setVisible(true);
        om.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void redraw()
    {
        Image scaledOriginSprite = originSprite.getScaledInstance(400, 400, BufferedImage.SCALE_SMOOTH);
        originSpriteLabel.setIcon(new ImageIcon(scaledOriginSprite));

        HashSet<Color> colors = new HashSet<>();
        for(int i=0; i <originSprite.getWidth();i++)
        {
            for(int j=0; j <originSprite.getHeight();j++)
            {
                int oldPixel = originSprite.getRGB(i,j);
                Color oldColor = new Color(oldPixel);
                colors.add(oldColor);
            }
        }

        originPalette = new BufferedImage(colors.size(),1,BufferedImage.TYPE_INT_ARGB);
        int x = 0;
        for(Color color: colors)
        {
            originPalette.setRGB(x,0,color.getRGB());
            x++;
        }

        Image scaledOldPalette = originPalette.getScaledInstance(400, 40, BufferedImage.SCALE_SMOOTH);
        originPaletteLabel.setIcon(new ImageIcon(scaledOldPalette));

        if(newPalette != null)
        {
            Image scaledNewPalette = newPalette.getScaledInstance(400, 40, BufferedImage.SCALE_SMOOTH);
            newPaletteLabel.setIcon(new ImageIcon(scaledNewPalette));

            newSprite = new BufferedImage(originSprite.getWidth(),originSprite.getHeight(),BufferedImage.TYPE_INT_ARGB);
            for(int i=0; i <originSprite.getWidth();i++)
            {
                for(int j=0; j <originSprite.getHeight();j++)
                {
                    int oldPixel = originSprite.getRGB(i,j);
                    Color oldColor = new Color(oldPixel);
                    Color newColor = getSwap(oldColor);
                    newSprite.setRGB(i,j,newColor.getRGB());
                }
            }
            Image scaledNewSprite = newSprite.getScaledInstance(400, 400, BufferedImage.SCALE_SMOOTH);
            newSpriteLabel.setIcon(new ImageIcon(scaledNewSprite));
        }
        SwingUtilities.updateComponentTreeUI(this);
    }

    private Color getSwap(Color old)
    {
        for(int i=0; i <originPalette.getWidth();i++)
        {
            int oldPixel = originPalette.getRGB(i,0);
            Color oldColor = new Color(oldPixel);
            if(oldColor.equals(old))
            {
                int newPixel = newPalette.getRGB(i,0);
                return new Color(newPixel);
            }
        }
        return old;
    }

    private void paletteClick(int x, int y)
    {
        BufferedImage temp = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);

        selectedColorIndex = originPalette.getWidth() *x/400;

        temp.setRGB(0,0,originPalette.getRGB(selectedColorIndex,0));

        Image scaledTemp = temp.getScaledInstance(400, 40, BufferedImage.SCALE_SMOOTH);

        colorSelection.setIcon(new ImageIcon(scaledTemp));


        SwingUtilities.updateComponentTreeUI(this);
    }
}
