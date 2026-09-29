package com.aerolineavs.tarifas;

/**
 * Contrato de servicio para el cálculo y evaluación de tarifas aéreas.
 * Desacopla la lógica de negocio de cualquier interfaz gráfica (GUI) o consola (CLI).
 */
public interface IPricingService {

    /**
     * Evalúa la tarifa más adecuada según el perfil y datos de viaje del cliente.
     *
     * @param cliente datos del cliente potencial
     * @return resultado de la evaluación con la tarifa y suposiciones aplicadas
     */
    ResultadoTarifa evaluar(ClientePotencial cliente);
}
