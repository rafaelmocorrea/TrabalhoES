package com.esgrupo10.SATM;

import com.esgrupo10.SATM.entity.Paciente;
import com.esgrupo10.SATM.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class PacienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PacienteRepository rep;

    @Test
    public void testCreatePaciente() {
        Paciente pac = new Paciente();
        pac.setNome("Rafael Moreira Correa");
        pac.setEmail("rafaelmocorrea@gmail.com");
        pac.setSenha("123456");
        pac.setCpf("48518446899");
        pac.setEndereco("Jacarei");
        pac.setTelefone("991388102");

        Paciente savePac = rep.save(pac);
    }

}
