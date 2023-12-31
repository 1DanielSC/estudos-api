package com.example.estudosapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.estudosapi.exceptions.NotFoundException;
import com.example.estudosapi.model.Reserva;
import com.example.estudosapi.model.Usuario;
import com.example.estudosapi.repository.UsuarioRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @PostConstruct
    @Transactional(readOnly = false)
    public void mockUsers(){
        if(repository.count() != 0)
            return;

        Usuario daniel = new Usuario();
        daniel.setNome("Daniel Sehn Colao");
        daniel.setMatricula("20190043936");
        daniel.setEmail("danielscolao@gmail.com");
        daniel.setSenha("daniel@123");

        Usuario gabriel = new Usuario();
        gabriel.setNome("Gabriel Bassani");
        gabriel.setEmail("gabrielbassabarreto@gmail.com");
        gabriel.setMatricula("20190043794");
        gabriel.setSenha("gabriel@123");

        Usuario isaac = new Usuario();
        isaac.setNome("Isaac Reinaldo");
        isaac.setEmail("isaacrpl7@hotmail.com");
        isaac.setMatricula("20190043533");
        isaac.setSenha("isaac@123");

        Usuario joao = new Usuario();
        joao.setNome("João Paulo Azevedo");
        joao.setEmail("jpazcdo@gmail.com");
        joao.setMatricula("20191283927");
        joao.setSenha("joao@123");

        Usuario leticia = new Usuario();
        leticia.setNome("Letícia Azevedo");
        leticia.setEmail("leleazvdo@gmail.com");
        leticia.setMatricula("20181283927");
        leticia.setSenha("leticia@123");

        Usuario sabrina = new Usuario();
        sabrina.setNome("Sabrina Gurgel");
        sabrina.setEmail("brinagurgel@gmail.com");
        sabrina.setMatricula("20171283927");
        sabrina.setSenha("sabrina@123");

        List<Usuario> users = new ArrayList<>();
        users.add(daniel);
        users.add(gabriel);
        users.add(isaac);
        users.add(joao);
        users.add(sabrina);
        users.add(leticia);

        repository.saveAllAndFlush(users);
    }

    public Usuario save(Usuario entity){
        return repository.save(entity);
    }

    public List<Usuario> findAll(){
        return repository.findAll();
    }

    public Usuario findById(Long id){
        return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Usuario não encontrado com este id."));
    }

    public Usuario findByMatricula(String matricula){
        Optional<Usuario> usuario = repository.findByMatricula(matricula);
        return usuario.isPresent() ? usuario.get() : null;
    }

    public Usuario findByEmail(String email){
        return repository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado com este e-mail."));
    }

    public List<Reserva> listarReservas(Long id){
        Usuario usuario = findById(id);
        return usuario.getReservas();
    }

    public boolean validarCredenciais(String email, String senha){
        Usuario user = findByEmail(email);
        return user.getSenha().equals(senha) ? true : false;
    }

}
