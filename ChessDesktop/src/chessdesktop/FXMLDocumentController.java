package chessdesktop;

import Chess.ChessPiece;
import Chess.ChessBoardImplementation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class FXMLDocumentController implements Initializable {
	
	ChessBoardRenderer board = new ChessBoardRenderer();

	@FXML
	private Label label;
	@FXML
	private Canvas canvas;
	
	@FXML
	private void handleButtonAction(ActionEvent event) {
		board = new ChessBoardRenderer();
		board.draw(canvas);
                label.setText("");
	}
	
	@FXML
	private void handleSaveButtonAction(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save Game");
		File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			Charset charset = Charset.forName("US-ASCII");
                        if (board.guardarpartida(file)){
                            label.setText("GUARDAR PARTIDA OK!");
                        }
                        else{
                           label.setText("ERROR AL GUARDAR PARTIDA!"); 
                        }

		}
	}

	@FXML
	private void handleLoadButtonAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Chess Files",".txt"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
                    try {
                        Scanner in = new Scanner(selectedFile);
                    } 
                    catch (IOException ex) {
                        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                    }


                    Charset charset = Charset.forName("US-ASCII");
                        if (board.cargarpartida(selectedFile)){
                            label.setText("CARGAR PARTIDA OK!");
                        }
                        else{
                            label.setText("ERROR AL CARGAR PARTIDA!"); 
                        }
                    board.draw(canvas);
                }
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		board.draw(canvas);
		
		canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent e)->{
			Chess.ChessPiece piece = board.getPieceAt(canvas, e.getX(), e.getY());
			if (board.getMovingPiece() == piece) {
				board.setMovingPiece(null);
				board.draw(canvas);
				return;
			}
			if (board.getMovingPiece() == null) {
				board.setMovingPiece(piece);
				board.draw(canvas);
				return;
			}
			if (board.movePieceTo(canvas, e.getX(), e.getY())) {
				board.setMovingPiece(null);
				board.draw(canvas);
				if (!board.containsKing(ChessPiece.Color.BLACK) || !board.containsKing(ChessPiece.Color.WHITE)) {
					if (!board.containsKing(ChessPiece.Color.BLACK))
						label.setText("Ganan las blancas");
					else
						label.setText("Ganan las negras");
				}
                                else if (board.isTie())
                                    label.setText("Empate!");
			}
		});
	}
        
        
	
}
