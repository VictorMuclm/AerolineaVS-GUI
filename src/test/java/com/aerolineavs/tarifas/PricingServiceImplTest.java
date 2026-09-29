package com.aerolineavs.tarifas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PricingServiceImplTest {

    private IPricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingServiceImpl();
    }

    @Test
    void debeLanzarExcepcionSiClienteEsNulo() {
        assertThrows(NullPointerException.class, () -> pricingService.evaluar(null));
    }

    @Test
    void debeAplicarPajarilloAMenorConSeisVuelos() {
        ClientePotencial cliente = new ClientePotencial(
                17, 6, TipoViajero.MENOR, ClaseVuelo.TURISTA, RegionDestino.OTRA, 0, false, true
        );

        assertEquals(Tarifa.PAJARILLO, pricingService.evaluar(cliente).tarifa());
    }

    @Test
    void debeAplicarGorrionAEstudianteEntre18y25() {
        ClientePotencial cliente = new ClientePotencial(
                21, 9, TipoViajero.ESTUDIANTE_UNIVERSITARIO_DESPLAZADO, ClaseVuelo.TURISTA,
                RegionDestino.OTRA, 0, false, true
        );

        assertEquals(Tarifa.GORRION, pricingService.evaluar(cliente).tarifa());
    }

    @Test
    void debeAplicarViajaAhoraQuePuedesAJovenTrabajadorConPadres() {
        ClientePotencial cliente = new ClientePotencial(
                23, 3, TipoViajero.TRABAJADOR_JOVEN, ClaseVuelo.TURISTA,
                RegionDestino.OTRA, 15000, false, true
        );

        assertEquals(Tarifa.VIAJA_AHORA_QUE_PUEDES, pricingService.evaluar(cliente).tarifa());
    }

    @Test
    void debeAplicarAtreviendoseASaltarDelNidoAJovenTrabajadorIndependizado() {
        ClientePotencial cliente = new ClientePotencial(
                24, 4, TipoViajero.TRABAJADOR_JOVEN, ClaseVuelo.TURISTA,
                RegionDestino.OTRA, 18000, false, false
        );

        assertEquals(Tarifa.ATREVIENDOSE_A_SALTAR_DEL_NIDO, pricingService.evaluar(cliente).tarifa());
    }

    @Test
    void debeAplicarConoceEuropaConPeques() {
        ClientePotencial cliente = new ClientePotencial(
                35, 6, TipoViajero.GENERAL, ClaseVuelo.TURISTA,
                RegionDestino.EUROPA, 25000, true, false
        );

        assertEquals(Tarifa.CONOCE_EUROPA_CON_TUS_PEQUES, pricingService.evaluar(cliente).tarifa());
    }

    @Test
    void debeAplicarConoceEuropaSinPeques() {
        ClientePotencial cliente = new ClientePotencial(
                35, 6, TipoViajero.GENERAL, ClaseVuelo.TURISTA,
                RegionDestino.EUROPA, 25000, false, false
        );

        assertEquals(Tarifa.CONOCE_EUROPA, pricingService.evaluar(cliente).tarifa());
    }

    @Test
    void debeAplicarConoceMundoSinPeques() {
        ClientePotencial cliente = new ClientePotencial(
                40, 7, TipoViajero.GENERAL, ClaseVuelo.BUSINESS,
                RegionDestino.ASIA, 45000, false, false
        );

        assertEquals(Tarifa.CONOCE_EL_MUNDO, pricingService.evaluar(cliente).tarifa());
    }

    @Test
    void debeAplicarConoceMundoConPeques() {
        ClientePotencial cliente = new ClientePotencial(
                40, 7, TipoViajero.GENERAL, ClaseVuelo.BUSINESS,
                RegionDestino.AMERICA, 45000, true, false
        );

        assertEquals(Tarifa.CONOCE_EL_MUNDO_CON_TUS_PEQUES, pricingService.evaluar(cliente).tarifa());
    }

    @Test
    void debeDevolverSinTarifaCuandoNoCumpleReglas() {
        ClientePotencial cliente = new ClientePotencial(
                19, 1, TipoViajero.GENERAL, ClaseVuelo.BUSINESS,
                RegionDestino.OTRA, 10000, false, true
        );

        assertEquals(Tarifa.SIN_TARIFA, pricingService.evaluar(cliente).tarifa());
    }
}
