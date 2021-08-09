package com.cmc.moengagedataimport.customSqlDialect;

import org.hibernate.dialect.PostgreSQL81Dialect;

public class CustomSqlDialect extends PostgreSQL81Dialect {
    @Override
    public String getQuerySequencesString() {
        return null;
    }
}
