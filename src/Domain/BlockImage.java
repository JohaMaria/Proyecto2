package Domain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

public  class BlockImage{

     protected int x, y;
    protected int size;
    protected byte[] imageB;
    public BlockImage(byte[] image, int x, int y, int size) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.imageB = image;
    } // constructor

    public int getX() {
        return x;
    } // getX

    public void setX(int x) {
        this.x = x;
    } // setX

    public int getY() {
        return y;
    } // getY

    public void setY(int y) {
        this.y = y;
    } // setY

    public int getSize() {
        return size;
    } // getSize

    public void setSize(int size) {
        this.size = size;
    } // setSize

   public byte[] getImageBytes() {
        return imageB;
    } // getImageBytes

    public void setBytes(byte[] imageBytes) {
        this.imageB = imageBytes;
    } // setBytes
     public BufferedImage bytes() throws IOException {
        ByteArrayInputStream byArray = new ByteArrayInputStream(this.imageB);
        BufferedImage imageConvert = ImageIO.read(byArray);
        return imageConvert;
    } // bytesToImage

  


   
} // fin de la clase
