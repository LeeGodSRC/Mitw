package me.skymc.taboolib.mysql.builder;

import com.ilummc.tlib.util.Strings;

import java.util.Arrays;


public class SQLColumn {

    public static final SQLColumn PRIMARY_KEY_ID = new SQLColumn(SQLColumnType.INT, "id", SQLColumnOption.NOTNULL, SQLColumnOption.PRIMARY_KEY, SQLColumnOption.AUTO_INCREMENT);

    private SQLColumnType columnType;
    private int m;
    private int d;

    private String columnName;
    private Object defaultValue;

    private SQLColumnOption[] columnOptions;

    
    public SQLColumn(SQLColumnType columnType, String columnName) {
        this(columnType, 0, 0, columnName, null);
    }

    
    public SQLColumn(SQLColumnType columnType, int m, String columnName) {
        this(columnType, m, 0, columnName, null);
    }

    
    public SQLColumn(SQLColumnType columnType, String columnName, SQLColumnOption... columnOptions) {
        this(columnType, 0, 0, columnName, null, columnOptions);
    }

    
    public SQLColumn(SQLColumnType columnType, String columnName, Object defaultValue) {
        this(columnType, 0, 0, columnName, defaultValue);
    }

    
    public SQLColumn(SQLColumnType columnType, int m, int d, String columnName, Object defaultValue, SQLColumnOption... columnOptions) {
        this.columnType = columnType;
        this.m = m;
        this.d = d;
        this.columnName = columnName;
        this.defaultValue = defaultValue;
        this.columnOptions = columnOptions;
    }

    public SQLColumn m(int m) {
        this.m = m;
        return this;
    }

    public SQLColumn d(int d) {
        this.d = d;
        return this;
    }

    public SQLColumn defaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public SQLColumn columnOptions(SQLColumnOption... columnOptions) {
        this.columnOptions = columnOptions;
        return this;
    }

    public String convertToCommand() {
        if (this.m == 0 && this.d == 0) {
            return Strings.replaceWithOrder("`{0}` {1}{2}", columnName, columnType.name().toLowerCase(), convertToOptions());
        } else if (this.d == 0) {
            return Strings.replaceWithOrder("`{0}` {1}({2}){3}", columnName, columnType.name().toLowerCase(), m, convertToOptions());
        } else {
            return Strings.replaceWithOrder("`{0}` {1}({2},{3}){4}", columnName, columnType.name().toLowerCase(), m, d, convertToOptions());
        }
    }

    private String convertToOptions() {
        StringBuilder builder = new StringBuilder();
        for (SQLColumnOption options : columnOptions) {
            switch (options) {
                case NOTNULL:
                    builder.append(" NOT NULL");
                    break;
                case PRIMARY_KEY:
                    builder.append(" PRIMARY KEY");
                    break;
                case AUTO_INCREMENT:
                    builder.append(" AUTO_INCREMENT");
                    break;
                case UNIQUE_KEY:
                    builder.append(" UNIQUE KEY");
                    break;
                default:
            }
        }
        if (defaultValue != null) {
            if (defaultValue instanceof String) {
                builder.append(" DEFAULT '").append(defaultValue).append("'");
            } else {
                builder.append(" DEFAULT ").append(defaultValue);
            }
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return "columnType=" + "SQLColumn{" + columnType + ", m=" + m + ", d=" + d + ", columnName='" + columnName + '\'' + ", defaultValue=" + defaultValue + ", columnOptions=" + Arrays.toString(columnOptions) + '}';
    }
}