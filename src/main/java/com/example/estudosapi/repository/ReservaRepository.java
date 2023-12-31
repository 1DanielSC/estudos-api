package com.example.estudosapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.estudosapi.model.Reserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>{
    
    Reserva findByCabineId(Long id);

    void deleteAllByCabineId(Long id);
}
