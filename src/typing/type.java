package typing;

public  enum type {
    INTEGER,
    REAL,
    STRING,
    BOOLEAN,
    CHAR
}

public String toString() {
    switch (this) {
        case INTEGER:
            return "INTEGER";
        case REAL:
            return "REAL";
        case STRING:
            return "STRING";
        case BOOLEAN:
            return "BOOLEAN";
        case CHAR:
            return "CHAR";
        default:
            System.err.println("ERROR: type enumaration has an invalid value");
            System.exit(1);
            return "";
    }
}