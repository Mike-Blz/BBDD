

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

public class CalcController {

	@FXML
	private Text pantalla;
	
	@FXML
	private void numeros(ActionEvent event) {
		String valor = ((Button)event.getSource()).getText();
		pantalla.setText(pantalla.getText()+valor);
	}
	
	@FXML
	private void cuentas(ActionEvent event) {
		
	}
	
	
	
	
}
