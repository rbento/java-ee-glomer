package glomer.model.enums;

public enum ExpressionType {

    EQUALS("equals"),
    NOT_EQUALS("notEquals"),
    CONTAINS("contains"),
    LESS_THAN("lessThan"),
    GREATER_THAN("greaterThan");

    private String label;

    ExpressionType(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getName() {
        return this.name();
    }

    public Integer getOrdinal() {
        return this.ordinal();
    }
}
