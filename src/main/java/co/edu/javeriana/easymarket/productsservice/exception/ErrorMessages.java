package co.edu.javeriana.easymarket.productsservice.exception;

public class ErrorMessages {
    private ErrorMessages() {
        // Prevents instantiation
    }
    
    public static final class ProductErrorMessages {
        private ProductErrorMessages() {}
        
        public static String notFound(String code) {
            return String.format("Product with code %s not found", code);
        }

        public static String alreadyExists(String code) {
            return String.format("Product with code %s already exists", code);
        }

        public static String failedToDelete (String code) {
            return String.format("Failed to delete product with code %s", code);
        }
        
        public static String invalidData() {
            return "Invalid product data provided";
        }
    }

    public static class LabelErrorMessages {
        public static String notFound(Integer id) {
            return String.format("Label with id %d not found", id);
        }
        
        public static String invalidData() {
            return "Invalid label data";
        }
        
        public static String failedToDelete(Integer id) {
            return String.format("Failed to delete label with id %d", id);
        }
    }

    public static class VariantsErrorMessages{
        public static String emptyOptions() {
            return String.format("A variant must have at least one option");
        }
    }

    public static class ColorErrorMessages {
        public static String notFound(Integer id) {
            return String.format("Color with id %d not found", id);
        }
        
        public static String invalidData() {
            return "Invalid color data. Hex code is required.";
        }
        
        public static String invalidHexCode() {
            return "Invalid hex code format. Must be a 6-character hex value (e.g.,'FF5733').";
        }
        
        public static String failedToDelete(Integer id) {
            return String.format("Failed to delete color with id %d", id);
        }
    }

}
