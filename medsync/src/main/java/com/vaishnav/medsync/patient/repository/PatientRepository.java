package com.vaishnav.medsync.patient.repository;

import com.vaishnav.medsync.patient.entity.Patient;
import com.vaishnav.medsync.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Long> {

    Optional<Patient> findByUserId(Long userId);
}
