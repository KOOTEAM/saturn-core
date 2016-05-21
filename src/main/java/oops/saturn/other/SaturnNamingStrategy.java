package oops.saturn.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.AssertionFailure;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.util.StringHelper;

/**
 * hibernate命名策略，将驼峰式命名的实体名称转换成以下划线分隔的表名
 *
 */
public class SaturnNamingStrategy implements NamingStrategy {

	private static String tablePrefix = "";

	/**
	 * Ignore table prefix
	 */
	private static List<String> ignorePrefix = new ArrayList<String>();

	public void setIgnorePrefix(List<String> ignorePrefixArg) {

		ignorePrefix = ignorePrefixArg;
	}


	public String classToTableName(String className) {

		return addUnderscoresForTable(StringHelper.unqualify(className));
	}

	public String propertyToColumnName(String propertyName) {

		return addUnderscoresForColumn(StringHelper.unqualify(propertyName));
	}

	public String tableName(String tableName) {

		return addUnderscoresForTable(tableName);
	}

	public String columnName(String columnName) {

		return addUnderscoresForColumn(columnName);
	}

	protected static String addUnderscoresForTable(String name) {

		StringBuffer buf = new StringBuffer(name.replace('.', '_'));
		for (int i = 1; i < buf.length() - 1; i++)
			if (Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i)) && Character.isLowerCase(buf.charAt(i + 1)))
				buf.insert(i++, '_');
		for (String string : ignorePrefix) {
			if (name.toUpperCase().startsWith(string.toUpperCase())) {
				return buf.toString().toUpperCase();
			}
		}
		return tablePrefix + buf.toString().toUpperCase();

		/*
		 * if( name.toUpperCase().contains( "ALPS" ) || name.toUpperCase().contains( "DOTJ" ) ){ return buf.toString().toUpperCase(); }
		 */

	}

	protected static String addUnderscoresForColumn(String name) {

		StringBuffer buf = new StringBuffer(name.replace('.', '_'));
		for (int i = 1; i < buf.length() - 1; i++)
			if (Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i)) && Character.isLowerCase(buf.charAt(i + 1)))
				buf.insert(i++, '_');
		if (excludeColumnList.contains(buf.toString())) {
			buf.append('_');
		}
		return buf.toString().toUpperCase();

	}

	public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {

		return tableName(ownerEntityTable + '_' + propertyToColumnName(propertyName));
	}

	public String joinKeyColumnName(String joinedColumn, String joinedTable) {

		return columnName(joinedColumn);
	}

	public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {

		String header = propertyName == null ? propertyTableName : StringHelper.unqualify(propertyName);
		if (header == null)
			throw new AssertionFailure("NamingStrategy not properly filled");
		else
			return columnName(header);
	}

	public String logicalColumnName(String columnName, String propertyName) {

		return StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName);
	}

	public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {

		if (tableName != null)
			return tableName;
		else
			return ownerEntityTable + "_" + (associatedEntityTable == null ? StringHelper.unqualify(propertyName) : associatedEntityTable);
	}

	public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {

		return StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName) + "_" + referencedColumn;
	}

	public static final NamingStrategy INSTANCE = new ImprovedNamingStrategy();

	private static final List<String> excludeColumnList = Arrays.asList(new String[] { "ACCESS", "AUDIT", "COMPRESS", "DESC", "ADD", "BETWEEN", "CONNECT", "DISTINCT", "ALL", "BY", "CREATE", "DROP", "ALTER", "CHAR", "CURRENT", "ELSE", "AND", "CHECK", "DATE", "EXCLUSIVE", "ANY",
			"CLUSTER", "DECIMAL", "EXISTS", "AS", "COLUMN", "DEFAULT", "FILE", "ASC", "COMMENT", "DELETE", "FLOAT", "FOR", "LONG", "PCTFREE", "SUCCESSFUL", "FROM", "MAXEXTENTS", "PRIOR", "SYNONYM", "GRANT", "MINUS", "PRIVILEGES", "SYSDATE", "GROUP", "MODE", "PUBLIC", "TABLE",
			"HAVING", "MODIFY", "RAW", "THEN", "IDENTIFIED", "NETWORK", "RENAME", "TO", "IMMEDIATE", "NOAUDIT", "RESOURCE", "TRIGGER", "IN", "NOCOMPRESS", "REVOKE", "UID", "INCREMENT", "NOT", "ROW", "UNION", "INDEX", "NOWAIT", "ROWID", "UNIQUE", "INITIAL", "NULL", "ROWNUM",
			"UPDATE", "INSERT", "NUMBER", "ROWS", "USER", "INTEGER", "OF", "SELECT", "VALIDATE", "INTERSECT", "OFFLINE", "SESSION", "VALUES", "INTO", "ON", "SET", "VARCHAR", "IS", "ONLINESHARE", "VARCHAR2", "LEVEL", "OPTION", "SIZE", "VIEW", "LIKE", "OR", "SMALLINT", "WHENEVER",
			"LOCK", "ORDER", "START", "WHERE", "WITH" });

}
