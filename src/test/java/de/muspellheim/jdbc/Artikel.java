/*
 * Copyright (c) 2017 Falko Schumann
 * Released under the terms of the MIT License.
 */

package de.muspellheim.jdbc;

import java.util.*;

public class Artikel {

    private int id;
    private String bezeichnung;
    private Money einzelpreis;
    private boolean deleted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public Money getEinzelpreis() {
        return einzelpreis;
    }

    public void setEinzelpreis(Money einzelpreis) {
        this.einzelpreis = einzelpreis;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artikel artikel = (Artikel) o;
        return id == artikel.id &&
                deleted == artikel.deleted &&
                Objects.equals(bezeichnung, artikel.bezeichnung) &&
                Objects.equals(einzelpreis, artikel.einzelpreis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bezeichnung, einzelpreis, deleted);
    }

    @Override
    public String toString() {
        return "Artikel{" +
                "id=" + id +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", einzelpreis=" + einzelpreis +
                ", deleted=" + deleted +
                '}';
    }

}
