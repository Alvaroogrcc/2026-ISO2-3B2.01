/**
 * Nucleo transversal del Sistema de Gestion de Radiodiagnostico e Imagen Medica.
 *
 * <p>Este paquete aloja el nucleo compartido del que dependen el resto de
 * componentes (modulos Maven): identificadores, tipos de valor, contratos
 * estables entre componentes y utilidades tecnicas.</p>
 *
 * <p>Regla de dependencia: este modulo NO conoce ningun dominio concreto. Solo
 * el resto de componentes dependen de el. La justificacion de la decision
 * esta registrada en {@code DECISIONS.md} (ADR-001).</p>
 */
package es.escam.radiodiagnostico.nucleo;
