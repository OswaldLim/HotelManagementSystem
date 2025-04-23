package utils;

import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import static utils.AlertUtils.textPage;

public class InputUtils {
    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final String PHONE_REGEX = "^\\s*(?:\\+?\\d{1,3})?[-. (]*\\d{3}[-. )]*\\d{3}[-. ]*\\d{4}\\s*$";

    private static final String IC_REGEX = "^\\d{6}-?\\d{2}-?\\d{4}$";

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
                node instanceof ChoiceBox<?> && ((ChoiceBox<?>) node).getValue().equals(defaultValue) ||
                node instanceof PasswordField && ((PasswordField) node).getText().isEmpty()
            ) {
                textPage("No Input Fields Should be Left Empty", "ERROR: Invalid Input", true);
                return false;
            }
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches(EMAIL_REGEX);
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches(PHONE_REGEX);
    }

    public static boolean isValidIC(String ic) {
        return ic != null && ic.matches(IC_REGEX);
    }



}
