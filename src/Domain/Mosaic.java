package Domain;

import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

public class Mosaic extends BlockImage {

    private int rotate;
    private int horizontal, vertical, flipH, flipV;

    public Mosaic(byte[] image, int x, int y, int size) {
        super(image, x, y, size);
        this.rotate = 0;
        this.horizontal = 0;
        this.vertical = 0;
        this.flipH = 1;
        this.flipV = 1;
    } // constructor

   
    
    public void draw(GraphicsContext gc) throws IOException {
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(bytes(), null));
        imageView.setRotate(imageView.getRotate() + this.rotate);
        SnapshotParameters snapshot = new SnapshotParameters();
        gc.drawImage(imageView.snapshot(snapshot, null), this.x * this.size + this.horizontal, this.y * 
                this.size + this.vertical, this.size * this.flipH, this.size * this.flipV);
    } // draw

   
    public boolean selectMouse(int xSelect, int ySelect) {
        if ((xSelect >= this.x * this.size && xSelect <= this.x * this.size + this.size)
                && (ySelect >= this.y * this.size && ySelect <= this.y * this.size + this.size)) {
            return true;
        }
        return false;
    } // chunkClicked
     

    public void setRotation() {
        this.rotate = 0;
       
    } // setRotation

    


} // fin de la clase
