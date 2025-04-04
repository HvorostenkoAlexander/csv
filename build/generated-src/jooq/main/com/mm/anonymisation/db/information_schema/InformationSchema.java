/*
 * This file is generated by jOOQ.
 */
package com.mm.anonymisation.db.information_schema;


import com.mm.anonymisation.db.DefaultCatalog;
import com.mm.anonymisation.db.information_schema.tables.Columns;
import com.mm.anonymisation.db.information_schema.tables.Tables;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InformationSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>information_schema</code>
     */
    public static final InformationSchema INFORMATION_SCHEMA = new InformationSchema();

    /**
     * The table <code>information_schema.COLUMNS</code>.
     */
    public final Columns COLUMNS = Columns.COLUMNS;

    /**
     * The table <code>information_schema.TABLES</code>.
     */
    public final Tables TABLES = Tables.TABLES;

    /**
     * No further instances allowed
     */
    private InformationSchema() {
        super("information_schema", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Columns.COLUMNS,
            Tables.TABLES
        );
    }
}
