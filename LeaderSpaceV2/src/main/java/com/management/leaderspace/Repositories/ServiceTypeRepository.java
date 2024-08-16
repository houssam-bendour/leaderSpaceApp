package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, UUID> {
    @Query("select s from ServiceType s where s.name like :ser ")
    ServiceType getByName(String ser);
}
