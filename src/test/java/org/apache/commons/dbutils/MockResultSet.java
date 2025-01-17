/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.dbutils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * MockResultSet dynamically implements the ResultSet interface.
 */
public class MockResultSet implements InvocationHandler {

    /**
     * Create a {@code MockResultSet} proxy object.  This is equivalent to:
     * <pre>
     * ProxyFactory.instance().createResultSet(new MockResultSet(metaData, rows));
     * </pre>
     *
     * @param metaData
     * @param rows A null value indicates an empty {@code ResultSet}.
     */
    public static ResultSet create(final ResultSetMetaData metaData,
            final Object[][] rows) {
        return ProxyFactory.instance().createResultSet(
            new MockResultSet(metaData, rows));
    }

    private Object[] currentRow = null;

    private Iterator<Object[]> iter;

    private ResultSetMetaData metaData;

    private Boolean wasNull = Boolean.FALSE;

    /**
     * MockResultSet constructor.
     * @param metaData
     * @param rows A null value indicates an empty {@code ResultSet}.
     */
    public MockResultSet(final ResultSetMetaData metaData, final Object[][] rows) {
        this.metaData = metaData;
        if (rows == null) {
            final List<Object[]> empty = Collections.emptyList();
            this.iter = empty.iterator();
        } else {
            this.iter = Arrays.asList(rows).iterator();
        }
    }

    /**
     * The get* methods can have an int column index or a String column name as
     * the parameter.  This method handles both cases and returns the column
     * index that the client is trying to get at.
     * @param args
     * @return A column index.
     * @throws SQLException if a database access error occurs
     */
    private int columnIndex(final Object[] args) throws SQLException {

        if (args[0] instanceof Integer) {
            return ((Integer) args[0]).intValue();

        }
        if (args[0] instanceof String) {
            return this.columnNameToIndex((String) args[0]);

        }
        throw new SQLException(args[0] + " must be Integer or String");
    }

    /**
     * Returns the column index for the given column name.
     * @return A 1 based index
     * @throws SQLException if the column name is invalid
     */
    private int columnNameToIndex(final String columnName) throws SQLException {
        for (int i = 0; i < this.currentRow.length; i++) {
            final int c = i + 1;
            if (this.metaData.getColumnName(c).equalsIgnoreCase(columnName)) {
                return c;
            }
        }

        throw new SQLException(columnName + " is not a valid column name.");
    }

    /**
     * Gets the boolean value at the given column index.
     * @param columnIndex A 1 based index.
     * @throws SQLException if a database access error occurs
     */
    protected Object getBoolean(final int columnIndex) throws SQLException {
        final Object obj = this.currentRow[columnIndex - 1];
        this.setWasNull(obj);

        try {
            return obj == null
                ? Boolean.FALSE
                : Boolean.valueOf(obj.toString());

        } catch (final NumberFormatException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Gets the byte value at the given column index.
     * @param columnIndex A 1 based index.
     * @throws SQLException if a database access error occurs
     */
    protected Object getByte(final int columnIndex) throws SQLException {
        final Object obj = this.currentRow[columnIndex - 1];
        this.setWasNull(obj);

        try {
            return obj == null
                ? Byte.valueOf((byte) 0)
                : Byte.valueOf(obj.toString());

        } catch (final NumberFormatException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Gets the double value at the given column index.
     * @param columnIndex A 1 based index.
     * @throws SQLException if a database access error occurs
     */
    protected Object getDouble(final int columnIndex) throws SQLException {
        final Object obj = this.currentRow[columnIndex - 1];
        this.setWasNull(obj);

        try {
            return obj == null
                ? Double.valueOf(0)
                : Double.valueOf(obj.toString());

        } catch (final NumberFormatException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Gets the float value at the given column index.
     * @param columnIndex A 1 based index.
     * @throws SQLException if a database access error occurs
     */
    protected Object getFloat(final int columnIndex) throws SQLException {
        final Object obj = this.currentRow[columnIndex - 1];
        this.setWasNull(obj);

        try {
            return obj == null ? Float.valueOf(0) : Float.valueOf(obj.toString());

        } catch (final NumberFormatException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Gets the int value at the given column index.
     * @param columnIndex A 1 based index.
     * @throws SQLException if a database access error occurs
     */
    protected Object getInt(final int columnIndex) throws SQLException {
        final Object obj = this.currentRow[columnIndex - 1];
        this.setWasNull(obj);

        try {
            return obj == null
                ? Integer.valueOf(0)
                : Integer.valueOf(obj.toString());

        } catch (final NumberFormatException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Gets the long value at the given column index.
     * @param columnIndex A 1 based index.
     * @throws SQLException if a database access error occurs
     */
    protected Object getLong(final int columnIndex) throws SQLException {
        final Object obj = this.currentRow[columnIndex - 1];
        this.setWasNull(obj);

        try {
            return obj == null ? Long.valueOf(0) : Long.valueOf(obj.toString());

        } catch (final NumberFormatException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * @throws SQLException
     */
    protected ResultSetMetaData getMetaData() throws SQLException {
        return this.metaData;
    }

    /**
     * Gets the object at the given column index.
     * @param columnIndex A 1 based index.
     * @throws SQLException if a database access error occurs
     */
    protected Object getObject(final int columnIndex) throws SQLException {
        final Object obj = this.currentRow[columnIndex - 1];
        this.setWasNull(obj);
        return obj;
    }

    /**
     * Gets the short value at the given column index.
     * @param columnIndex A 1 based index.
     * @throws SQLException if a database access error occurs
     */
    protected Object getShort(final int columnIndex) throws SQLException {
        final Object obj = this.currentRow[columnIndex - 1];
        this.setWasNull(obj);

        try {
            return obj == null
                ? Short.valueOf((short) 0)
                : Short.valueOf(obj.toString());

        } catch (final NumberFormatException e) {
            throw new SQLException(e.getMessage());
        }
    }

    /**
     * Gets the String at the given column index.
     * @param columnIndex A 1 based index.
     * @throws SQLException if a database access error occurs
     */
    protected String getString(final int columnIndex) throws SQLException {
        final Object obj = this.getObject(columnIndex);
        this.setWasNull(obj);
        return obj == null ? null : obj.toString();
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args)
        throws Throwable {

        final String methodName = method.getName();

        if (methodName.equals("getMetaData")) {
            return this.getMetaData();

        }
        if (methodName.equals("next")) {
            return this.next();

        }
        if (methodName.equals("previous")) {

        } else if (methodName.equals("close")) {

        } else if (methodName.equals("getBoolean")) {
            return this.getBoolean(columnIndex(args));

        } else if (methodName.equals("getByte")) {
            return this.getByte(columnIndex(args));

        } else if (methodName.equals("getDouble")) {
            return this.getDouble(columnIndex(args));

        } else if (methodName.equals("getFloat")) {
            return this.getFloat(columnIndex(args));

        } else if (methodName.equals("getInt")) {
            return this.getInt(columnIndex(args));

        } else if (methodName.equals("getLong")) {
            return this.getLong(columnIndex(args));

        } else if (methodName.equals("getObject")) {
            return this.getObject(columnIndex(args));

        } else if (methodName.equals("getShort")) {
            return this.getShort(columnIndex(args));

        } else if (methodName.equals("getString")) {
            return this.getString(columnIndex(args));

        } else if (methodName.equals("wasNull")) {
            return this.wasNull();

        } else if (methodName.equals("isLast")) {
            return this.isLast();

        } else if (methodName.equals("hashCode")) {
            return Integer.valueOf(System.identityHashCode(proxy));

        } else if (methodName.equals("toString")) {
            return "MockResultSet " + System.identityHashCode(proxy);

        } else if (methodName.equals("equals")) {
            return Boolean.valueOf(proxy == args[0]);
        }

        throw new UnsupportedOperationException("Unsupported method: " + methodName);
    }

    /**
     * @throws SQLException
     */
    protected Boolean isLast() throws SQLException {
        return this.iter.hasNext() ? Boolean.FALSE : Boolean.TRUE;
    }

    /**
     * @throws SQLException
     */
    protected Boolean next() throws SQLException {
        if (!this.iter.hasNext()) {
            return Boolean.FALSE;
        }
        this.currentRow = iter.next();
        return Boolean.TRUE;
    }

    /**
     * Assigns this.wasNull a Boolean value based on the object passed in.
     * @param isNull
     */
    private void setWasNull(final Object isNull) {
        this.wasNull = isNull == null ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * @throws SQLException
     */
    protected Boolean wasNull() throws SQLException {
        return this.wasNull;
    }
}
