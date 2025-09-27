package com.ejemplo.tests;

import com.ejemplo.core.BaseTest1;
import com.ejemplo.pages.HomePageMercado;
import com.ejemplo.pages.SearchResultsMercado;
import org.junit.jupiter.api.Test;

public class MercadoLibreTest1 extends BaseTest1 {

    @Test
    void searchPlaystation() {
        SearchResultsMercado results = new HomePageMercado(driver)
                .open()
                .selectMexico()
                .search("playstation 5");

        results
            .filterNew()
            .filterCDMX()
            .sortByPriceDesc()
            .printTopNToConsole(5); // ← imprime en consola los 5 primeros
    }
}
