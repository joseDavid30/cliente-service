package com.denkitronik.clienteservice.domain.repositories;

import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;      // Spring Boot 3.x
 import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;  // Spring Boot 4.x
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;     // Spring Boot 3.x
 import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager; // Spring Boot 4.x
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Integration tests (data slice) — IClienteDao con H2")
class IClienteDaoTest {

    @Autowired
    private TestEntityManager em;     // para preparar datos de prueba

    @Autowired
    private IClienteDao clienteDao;   // el repositorio que estamos probando

    private Region region;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        // Preparamos datos con TestEntityManager, no con el repositorio bajo prueba
        region = new Region();
        region.setNombre("Asia");
        em.persist(region);

        cliente = new Cliente();
        cliente.setNombre("Grace");
        cliente.setApellido("Hopper");
        cliente.setEmail("grace@navy.mil");
        cliente.setRegion(region);
        em.persist(cliente);

        em.flush();  // fuerza el INSERT antes de que los tests lean datos
    }
    @Test
    @DisplayName("findAllRegiones — devuelve las regiones persistidas")
    void findAllRegiones_debeRetornarListaDeRegiones() {
        // Act: llamamos a nuestra query personalizada
        List<Region> regiones = clienteDao.findAllRegiones();

        // Assert
        assertThat(regiones).hasSize(1);
        assertThat(regiones.get(0).getNombre()).isEqualTo("Asia");
    }
    @Test
    @DisplayName("deleteById — elimina el cliente verificado con em.find")
    void deleteById_debeEliminarElCliente() {
        Long id = cliente.getId();

        clienteDao.deleteById(id);
        em.flush();

        // Verificamos directamente en el contexto de persistencia
        // (más confiable que llamar findById nuevamente)
        assertThat(em.find(Cliente.class, id)).isNull();
    }
}