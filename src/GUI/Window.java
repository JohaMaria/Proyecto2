/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import Domain.ImageDraw;

import Domain.BlockImage;
import Domain.Mosaic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
//import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.imageio.ImageIO;
import proyecto2progra2.*;

/**
 *
 * @author Arturo
 */
public class Window extends Application {

    private final int WIDTH = 1350;
    private final int HEIGHT = 700;

    private Label labelPx, labelRows, labelColumns;
    private Button btnSelectImage, btnDelete, saveImage, btnLoanProject, btDrawLines,
            btnSaveProject, btnRotate1, btnRotate2, btnFlip1, btnFlip2, btnDraw;
    private TextField txtSize, txtRows, txtColumns;
    private GraphicsContext gc, graphicsContext;
    private Scene scene;
    private Pane pane;
    private Canvas canvasImage;
    private Canvas canvasM;

    private ImageView myImageView; //modifica una imagen
    private SnapshotParameters snapshot; //le da atributos a la hora de modificar una imagen

    private WritableImage writable; //convierte pixeles en una imagen
    private BufferedImage image;
    private int size, mosaicRow, mosaicColums = 0;
    private FileChooser fileChooserData, fileChooserImage;
    private ImageDraw[][] blockImage;
    private Mosaic[][] blockMosaic;
    private Image imageB;
    private BackgroundSize backS;
    private Background background;
    private BackgroundImage backI;

    private int rowsImage, colsImage, k, l, i, j;
    private int contImageChanges, contMosaicChanges = 0;
    private int pixelSize = 0;

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MosaicMaker");
        init(primaryStage);
        primaryStage.resizableProperty().set(false);
        primaryStage.show();
    } // start

    public void init(Stage primaryStage) throws Exception {
        ScrollPane scrollImage = new ScrollPane();
        ScrollPane scrollMosaic = new ScrollPane();
        this.pane = new Pane();
        this.scene = new Scene(this.pane, WIDTH, HEIGHT);
        this.canvasImage = new Canvas(100, 100);
        this.canvasM = new Canvas(100, 100);
        scrollImage.setContent(this.canvasM);
        scrollMosaic.setContent(this.canvasImage);
        scrollImage.setPrefSize(550, 500);
        scrollMosaic.setPrefSize(550, 500);
        scrollImage.relocate(700, 10);
        scrollMosaic.relocate(50, 10);
        scrollImage.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollImage.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollMosaic.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollMosaic.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        imageB = new Image("Assets/fondo1.jpg");
        backS = new BackgroundSize(2000, 1100, true, true, true, true);
        backI = new BackgroundImage(imageB, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backS);
        background = new Background(backI);
        this.pane.setBackground(background);
        primaryStage.setScene(this.scene);
//        
        gc = this.canvasImage.getGraphicsContext2D();

        this.labelPx = new Label("Pixel size:");
        this.labelRows = new Label("Row:");
        this.labelColumns = new Label("Columns:");
        this.txtSize = new TextField();
        this.txtRows = new TextField();
        this.txtColumns = new TextField();
        this.btnSelectImage = new Button("Select an Image");
        this.btnDelete = new Button("Delete");
        this.saveImage = new Button("Save Image");
        this.btnDraw = new Button("Draw");
        this.btnDraw.relocate(1300, 50);
        this.btDrawLines = new Button("Draw Mosaic");
        this.btnSaveProject = new Button("Save Project");
        this.btnSaveProject.relocate(450, 620);
        this.btnLoanProject = new Button("Loan Project");
        this.btnLoanProject.relocate(650, 620);
        this.btnFlip1 = new Button("Flip1");
        btnFlip1.relocate(1300, 100);
        this.btnFlip2 = new Button("Flip2");
        btnFlip2.relocate(1300, 200);
//        
        this.btnRotate1 = new Button("Rotate Left");
        btnRotate1.relocate(1275, 300);
        this.btnRotate2 = new Button("Rotate Right");
        btnRotate2.relocate(1275, 400);
//        
        saveImage.relocate(1174, 620);
        btnDelete.relocate(1275, 500);
        labelPx.relocate(50, 625);
        btnSelectImage.relocate(250, 620);
        txtSize.relocate(102, 620);
        txtSize.setPrefSize(100, 26);

        labelRows.relocate(830, 625);
        txtRows.relocate(860, 620);
        txtRows.setPrefSize(50, 26);
        labelColumns.relocate(948, 625);
        txtColumns.relocate(1000, 620);
        txtColumns.setPrefSize(50, 26);
        btDrawLines.relocate(1070, 620);
        graphicsContext = this.canvasM.getGraphicsContext2D();
        FileChooser fileChooserImage = new FileChooser();
        //en este metodo se selecciona mediante un filechooser la imagen, se corta en la matriz y se carga los objetos
        btnSelectImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedDirectory = fileChooserImage.showOpenDialog(primaryStage);
                System.out.print("Ruta:" + selectedDirectory.getPath());
                if (selectedDirectory != null) {
                    try {
                        BufferedImage aux = ImageIO.read(selectedDirectory);
                        if (size != 0) {
                            if (aux.getHeight() >= size && aux.getWidth() >= size) {
                                gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
                                image = aux;
                                canvasImage.setHeight(image.getHeight());
                                canvasImage.setWidth(image.getWidth());
                                gc.drawImage(SwingFXUtils.toFXImage(image, null), 0, 0);
                                blocks(gc, canvasImage);

                            }
                        } else {
                            image = aux;
                            canvasImage.setHeight(image.getHeight());
                            canvasImage.setWidth(image.getWidth());
                            gc.drawImage(SwingFXUtils.toFXImage(image, null), 0, 0);
                            blocks(gc, canvasImage);

                        } // if
                    } catch (IOException ex) {
                        Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } 

            } 
        }
        );//dibuja lineas 
        btDrawLines.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mosaicRow = Integer.parseInt(txtRows.getText());
                mosaicColums = Integer.parseInt(txtColumns.getText());
                int num = 0;
                mosaicRow = division(mosaicRow, size, num);
                mosaicColums = division(mosaicColums, size, num);
                lineMosaic(graphicsContext, canvasM);
                initMosiac();
            }
        }
        );
        this.canvasImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectImage((int) event.getX(), (int) event.getY());
            } // handle
        }
        );
        GraphicsContext gcM = this.canvasM.getGraphicsContext2D();
        this.canvasM.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                paintInMosaic((int) event.getX(), (int) event.getY(), gcM);
            }
        });
        saveImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createImage(primaryStage, gcM, canvasM);
            }
        });

        this.btnRotate1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvasM.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            selectBlockMosaic((int) event.getX(), (int) event.getY());
                            int rotate = 0;
                            rotate = rotate(0);
                           
                            ((Mosaic
                            ) getMosaicC()
                            ).draw(gcM);
                        } catch (IOException ex) {
                            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                });

            }
        });
        this.btnRotate2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvasM.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            selectBlockMosaic((int) event.getX(), (int) event.getY());

                            int rotate = 0;
                            rotate = rotate(0);
                            ((Mosaic) getMosaicC()).draw(gcM);
                        } catch (IOException ex) {
                            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                });

            }
        });
        this.btnFlip1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvasM.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            selectBlockMosaic((int) event.getX(), (int) event.getY());

                            int flip = 0;
                            flip = flipHorizontal(1);
                            ((Mosaic) getMosaicC()).draw(gcM);
                        } catch (IOException ex) {
                            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                });

            }
        });
        this.btnFlip2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvasM.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            selectBlockMosaic((int) event.getX(), (int) event.getY());
                            int flip = 0;
                            flip = flipVertical(1);
                            ((Mosaic) getMosaicC()).draw(gcM);
                        } catch (IOException ex) {
                            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }

                });

            }
        });
        this.btnDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvasM.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        selectBlockMosaic((int) event.getX(), (int) event.getY());
                        delete(gcM, canvasM);

                    }

                });

            }
        });
        this.btnDraw.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                canvasM.setOnMouseClicked(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        paintInMosaic((int) event.getX(), (int) event.getY(), gcM);

                    }

                });

            }
        });

        this.pane.getChildren().add(scrollImage);
        this.pane.getChildren().add(scrollMosaic);

        this.pane.getChildren().add(this.btnSelectImage);
        this.pane.getChildren().add(this.btnDelete);
        this.pane.getChildren().add(this.txtSize);
        this.pane.getChildren().add(this.txtRows);
        this.pane.getChildren().add(this.txtColumns);
        this.pane.getChildren().add(this.labelPx);
        this.pane.getChildren().add(this.labelColumns);
        this.pane.getChildren().add(this.labelRows);

        this.pane.getChildren().add(this.btDrawLines);
        this.pane.getChildren().add(this.saveImage);
        this.pane.getChildren().add(this.btnFlip1);
        this.pane.getChildren().add(this.btnFlip2);
        this.pane.getChildren().add(this.btnRotate1);
        this.pane.getChildren().add(this.btnRotate2);
        this.pane.getChildren().add(this.btnDraw);
        this.pane.getChildren().add(this.btnSaveProject);
        this.pane.getChildren().add(this.btnLoanProject);

        primaryStage.setScene(this.scene);

    }
//toma el x y de la imagen y lo pasa a los objetos para ingresarlos en la matriz

    public void blocks(GraphicsContext gc, Canvas canvasImage) {

        gc.clearRect(0, 0, canvasImage.getWidth(), canvasImage.getHeight());
        size = Integer.parseInt(txtSize.getText());
        this.rowsImage = (int) (this.image.getHeight() / this.size);
        this.colsImage = (int) (this.image.getWidth() / this.size); 
        canvasImage.setHeight((this.rowsImage) * this.size + ((this.rowsImage + 1) * 5));
        canvasImage.setWidth((this.colsImage) * this.size + ((this.colsImage + 1) * 5));
        blockImage = new ImageDraw[this.rowsImage][this.colsImage];
        for (int x = 0; x < this.rowsImage; x++) {
            for (int y = 0; y < this.colsImage; y++) {
                try {

                    BufferedImage aux = image.getSubimage((y * this.size), (x * this.size), this.size, this.size);
                    this.blockImage[x][y] = new ImageDraw(bytesImage(aux), y, x, size);
                    this.blockImage[x][y].draw(gc);

                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            } // for y
        } // for x
    }
    //convierte a bytes los pedazos de las imagenes
    public byte[] bytesImage(BufferedImage image) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArray);
        return byteArray.toByteArray();
    } // imageToBytes

    //dibuja las lineas del mosaico
    public void lineMosaic(GraphicsContext gcM, Canvas canvasMosaic) {
        if (this.size > 0 && this.mosaicRow > 0 && this.mosaicColums > 0) {
            canvasM.setHeight(this.mosaicRow * this.size);
            canvasM.setWidth(this.mosaicColums * this.size);
            for (int x = 0; x <= this.mosaicRow; x++) {
                gcM.strokeLine(0, x * this.size, this.mosaicColums * this.size, x * this.size); // rows
            } // for x
            for (int y = 0; y <= this.mosaicColums; y++) {
                gcM.strokeLine(y * this.size, 0, y * size, size * this.mosaicRow); // cols
            } // for y
        } // if 
    } // lineMosaic

    //selecciona la imagen 
    public void selectImage(int posX, int posY) {
        for (int x = 0; x < rowsImage; x++) {
            for (int y = 0; y < colsImage; y++) {
                if (blockImage[x][y].SelectMouse(posX, posY)) {
                    i = x;
                    j = y;
                    break;
                }
            } 
        } 
    } 

    //selecciona en el mosaico la image
    public void selectBlockMosaic(int xP, int yP) {
        for (int x = 0; x < mosaicRow; x++) {
            for (int y = 0; y < mosaicColums; y++) {
                if (blockMosaic[x][y].selectMouse(xP, yP)) {
                    k = x;
                    l = y;
                    break;
                }
            } 
        } 
    }
    //pinta las imagenes en el mosiaco
    public void paintInMosaic(int xP, int yP, GraphicsContext gcM) {
        selectBlockMosaic(xP, yP);
        ((Mosaic) this.blockMosaic[this.k][this.l]).setRotation();
        this.blockMosaic[this.k][this.l].setBytes(this.blockImage[this.i][this.j].getImageBytes());
        try {
            this.blockMosaic[this.k][this.l].draw(gcM);
        } catch (IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    //crea los objetos en la matriz para el canvasMosaico
    public void initMosiac() {
        this.blockMosaic = new Mosaic[this.mosaicRow][this.mosaicColums];
        for (int x = 0; x < this.mosaicRow; x++) {
            for (int y = 0; y < this.mosaicColums; y++) {
                this.blockMosaic[x][y] = new Mosaic(new byte[0], x, y, size);
                
            } 
        } 
    } 
    //crea la imagen del canvasM en una imagen png en el lugar que el usuario quiera
    public void createImage(Stage primaryStage, GraphicsContext gcM, Canvas canvasMosaic) {
        FileChooser fileChooser = new FileChooser();
        if (this.blockMosaic != null) {
            gcM.clearRect(0, 0, this.mosaicColums * this.size, this.mosaicRow * this.size);
            repaint(gcM);
            WritableImage wim = new WritableImage((int) Math.round(canvasM.getWidth()), (int) Math.round(canvasM.getHeight()));
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setFill(Color.TRANSPARENT);
            canvasM.snapshot(snapshotParameters, wim);
            lineMosaic(gcM, canvasM);
            repaint(gcM);
            File destino = new File("\\\\ARTURO-PC\\Users\\Arturo\\Desktop\\Proyecto2\\Proyecto2Progra2\\src\\Assets");

            String paths = fileChooser.showSaveDialog(primaryStage).getPath() + ".png";
            File file = new File(paths);

            if (paths != null) {
                try {
                    System.err.println("entra");
                    ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        } 
    } 
    //repinta en el mosaico
    public void repaint(GraphicsContext gcM) {
        for (int x = 0; x < mosaicRow; x++) {
            for (int y = 0; y < mosaicColums; y++) {
                try {
                    if (blockMosaic[x][y].getImageBytes().length != 0) {
                        blockMosaic[x][y].draw(gcM);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        } 
    } 
    // el mejor recursivo de la historia
    public static int division(int n, int size, int num) {

        if (n == 0 || n < size) {

            return num;

        } else {

            //System.out.print(num);
            return division(n - size, size, num + 1);
        }
    }
    //indica la ubicaion seleccionada en la matiz
    public BlockImage getMosaicC() {
        return blockMosaic[k][l];
    } 
    //elimina la imagen seleccionada 
    public void delete(GraphicsContext gcM, Canvas canvasM) {
        gcM.clearRect(0, 0, canvasM.getWidth(), canvasM.getHeight());
        ((Mosaic) getMosaicC()).setBytes(new byte[0]);
        lineMosaic(gcM, canvasM);
        repaint(gcM);
    } 
    //metodos de rotar y voltear
    public int rotate(int click) {
        int rotation = 0;
        if (click == 0) {
            if (rotation < 360) {
                rotation += 90;
            } else {
                rotation = rotation * 0 + 90;
            }
        } else {
            if (rotation > 0) {
                rotation -= 90;
            } else {
                rotation = 360 - 90;
            }
        }
        return rotation;
    } 

    public int flipHorizontal(int click) {
        int horizontal = 0;
        int flipH = 1;

        if (click == 1) { 
            if (horizontal == this.size) {
                horizontal = 0;
                flipH = 1;
            } else {
                horizontal = this.size;
                flipH = -1;
            }
        } else { 
            horizontal = 0;
            flipH = 1;
        }return flipH;
    } 

    public int flipVertical(int click) {
        int vertical = 0;
        int flipV = 1;
        if (click == 1) { 
            if (vertical == this.size) {
                vertical = 0;
                flipV = 1;
            } else {
                vertical = this.size;
                flipV = -1;
            }
        } else { 
            vertical = 0;
            flipV = 1;
        }return flipV;

    }

}
