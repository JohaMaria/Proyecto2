package Domain;

import java.io.IOException;
import static java.nio.file.Files.size;
import static javafx.beans.binding.Bindings.size;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;

import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;



public class ImageDraw extends BlockImage{

    public ImageDraw(byte[] imageB, int x, int y, int size) {
        super(imageB, x, y, size);
    } // constructor

    public boolean SelectMouse(int posX, int posY) {
         if ((posX >= (x * size) + (1 + x) * 5 && posX <= (x * size) + (1 + x) * 5 + this.size)
                && (posY >= (y * size) + (1 + y) * 5 && posY <= (y * size) + (1 + y) * 5 + this.size)) {
            return true;
        }
        return false;
    } // 
     public void draw(GraphicsContext gc)throws IOException{
        ImageView imageView = new ImageView(SwingFXUtils.toFXImage(super.bytes(), null));
        SnapshotParameters snapshot = new SnapshotParameters();
        gc.drawImage(imageView.snapshot(snapshot, null), (x * size) + (1 + x) * 5, (y * size) + (1 + y) * 5, size, size);
    } // draw
  
} // fin de la clase
