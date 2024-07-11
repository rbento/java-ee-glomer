package glomer.model.enums;

public class PatternType {

    public static final String EMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public static final String FQDN = "(?=^.{1,254}$)(^(?:(?!\\d|-)[a-zA-Z0-9\\-]{1,63}(?<!-)\\.?)+(?:[a-zA-Z]{2,})$)";

    public static final String ALPHANUMERIC = "^[a-zA-Z0-9 .'-]*$";

    public static final String ALPHA = "^[a-zA-Z .'-]*$";

    public static final String NUMERIC = "^[0-9]*$";
}
