/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.io.*;
import java.nio.charset.*;

/**
 * Loads SQL queries as resources from the classpath.
 * <p>
 * Search for queries in a package referenced by a class passed in constructor. This class is typically a repository
 * class holding all queries for a bounded context. The query to load must have the file extension <code>.sql</code>.
 * </p>
 * <p>
 * Optionally placeholders in the loaded query can be replaced. A placeholder is limited by curly braces like
 * <code>{example_placeholder}</code>. Placeholders help managing dynamic SQL, in example variant column names in
 * select.
 * </p>
 */
public class QueryLoader {

    private final Class<?> clazz;
    private String sql;

    /**
     * Initialize the query loader.
     *
     * @param clazz a class in the same package as the SQL queries to load.
     */
    public QueryLoader(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Load a query resource.
     *
     * @param queryname name of the query resource without file extension.
     * @return <code>this</code>.
     * @throws IOException if resource can not be loaded or read.
     */
    public QueryLoader loadSQL(String queryname) throws IOException {
        String filename = queryname + ".sql";
        InputStream in = clazz.getResourceAsStream(filename);
        if (in == null)
            throw new FileNotFoundException(filename + " not found in Package " + clazz.getPackage());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")))) {
            StringBuilder sql = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sql.append(line).append('\n');
            this.sql = sql.toString();
        }
        return this;
    }

    /**
     * Replace a placeholder in the query.
     *
     * @param placeholder the name of a placeholder without curly braces.
     * @param replacement the replacement text.
     * @return <code>this</code>.
     */
    public QueryLoader replace(String placeholder, String replacement) {
        sql = sql.replace("{" + placeholder + "}", replacement);
        return this;
    }

    /**
     * Return the final SQL query.
     *
     * @return the query string.
     */
    public String getSQL() {
        return sql;
    }

}
