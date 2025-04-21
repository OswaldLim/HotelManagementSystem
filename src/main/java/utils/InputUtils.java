package utils;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import static utils.AlertUtils.textPage;

public class InputUtils {
    public static void checkInputType(TextField textField, Class<?> type) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (type == Integer.class && newText.matches("\\d*")) {
                return change;
            } else if (type == Double.class && newText.matches("\\d*(\\.\\d*)?")) {
                return change;
            }
            return null;
        }));
    }

    public static boolean checkIfInputEmpty(Object defaultValue,Node... nodes){
        for (Node node : nodes) {
            if (node instanceof TextField && ((TextField) node).getText().isEmpty() ||
                node instanceof ChoiceBox<?> && ((ChoiceBox<?>) node).getValue().equals(defaultValue)
            ) {
                textPage("No Input Fields Should be Left Empty", "ERROR: Invalid Input", true);
                return false;
            }
        }
        return true;
    }

}
