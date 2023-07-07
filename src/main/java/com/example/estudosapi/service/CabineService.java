package com.example.estudosapi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.estudosapi.exceptions.BadRequestException;
import com.example.estudosapi.exceptions.ConflictException;
import com.example.estudosapi.exceptions.NotFoundException;
import com.example.estudosapi.model.Cabine;
import com.example.estudosapi.model.enums.EnumStatusCabine;
import com.example.estudosapi.repository.CabineRepository;

@Service
public class CabineService {

    @Autowired
    private CabineRepository repository;
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<Cabine> findAll(){
        return repository.findAll();
    }
    
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Cabine findById(Long id){
        return repository.findById(id)
        .orElseThrow(() -> new NotFoundException("Cabine não encontrada com este id."));
    }
    
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cabine cadastrar(Cabine entity){
        entity.setStatus(EnumStatusCabine.DISPONIVEL);
        return repository.save(entity);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cabine update(Cabine entity){
        return repository.save(entity);
    }

    public Cabine modificarStatus(Long id, EnumStatusCabine novoStatus){
        Cabine cabine = findById(id);

        if(novoStatus == EnumStatusCabine.DISPONIVEL){
            return liberar(cabine);
        }
        else if(novoStatus == EnumStatusCabine.RESERVADA){
            return reservar(cabine);
        }
        else if(novoStatus == EnumStatusCabine.OCUPADA){
            return ocupar(cabine);
        }
        else
            throw new BadRequestException("Status inválido.");
    }


    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private Cabine reservar(Cabine cabine){

        if(cabine.getStatus() != EnumStatusCabine.DISPONIVEL)
            throw new ConflictException("A cabine não está disponível para reserva.");
        
        cabine.setStatus(EnumStatusCabine.RESERVADA);
        return update(cabine);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private Cabine liberar(Cabine cabine){

        if(cabine.getStatus() == EnumStatusCabine.DISPONIVEL)
            throw new ConflictException("A cabine já está disponível.");
        
        cabine.setStatus(EnumStatusCabine.DISPONIVEL);
        return update(cabine);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    private Cabine ocupar(Cabine cabine){

        if(cabine.getStatus() == EnumStatusCabine.RESERVADA)
            throw new ConflictException("Esta cabine não pode ser ocupada, pois está reservada.");
        if(cabine.getStatus() == EnumStatusCabine.OCUPADA)
            throw new ConflictException("Esta já está ocupada.");
        
        cabine.setStatus(EnumStatusCabine.OCUPADA);
        return update(cabine);
    }
}