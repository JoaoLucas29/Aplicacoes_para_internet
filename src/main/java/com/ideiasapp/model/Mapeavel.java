package com.ideiasapp.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Mapeavel<T> {
    T mapear(ResultSet rs) throws SQLException;
}