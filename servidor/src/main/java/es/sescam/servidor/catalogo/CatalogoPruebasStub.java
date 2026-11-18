package es.sescam.servidor.catalogo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import es.sescam.servidor.model.Modalidad;
import es.sescam.servidor.model.Prioridad;
import es.sescam.servidor.model.TipoPrueba;

/**
 * Implementación stub del catálogo de pruebas para la Iteración 1 (CU-06).
 * <p>
 * Devuelve un catálogo reducido de demostración con una radiografía simple, un
 * TAC con contraste y consentimiento informado y una resonancia con preparación
 * previa. Es una implementación provisional hasta que se desarrolle el CU-17.
 * </p>
 */
public class CatalogoPruebasStub implements CatalogoPruebas {

    /** Versión del catálogo de demostración. */
    public static final String VERSION = "v0.1.0";

    private final List<TipoPrueba> tiposPrueba;

    /**
     * Crea el catálogo de demostración con sus tres tipos de prueba.
     */
    public CatalogoPruebasStub() {
        List<TipoPrueba> tipos = new ArrayList<>();
        tipos.add(new TipoPrueba(1L, "Radiografía de tórax", Modalidad.RX, "Sala RX-1", 10,
                "No requiere preparación previa.", false, false, "Sin requisitos especiales",
                Prioridad.ORDINARIA, false));
        tipos.add(new TipoPrueba(2L, "TAC abdominal con contraste", Modalidad.TAC, "Sala TAC-1", 30,
                "Ayuno de 6 horas y analítica renal reciente.", true, true,
                "Comprobar función renal y alergias al contraste", Prioridad.PREFERENTE, false));
        tipos.add(new TipoPrueba(3L, "Resonancia magnética lumbar", Modalidad.RM, "Sala RM-1", 45,
                "Retirar objetos metálicos y acudir sin maquillaje.", false, false,
                "Cribado de implantes metálicos", Prioridad.ORDINARIA, false));
        this.tiposPrueba = Collections.unmodifiableList(tipos);
    }

    @Override
    public Optional<TipoPrueba> obtenerPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        for (TipoPrueba tipo : tiposPrueba) {
            if (id.equals(tipo.getId())) {
                return Optional.of(tipo);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<TipoPrueba> listar() {
        return new ArrayList<>(tiposPrueba);
    }

    @Override
    public String getVersion() {
        return VERSION;
    }
}
