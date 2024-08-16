package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.ParticipantOfvisitRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.UUID;

public interface ParticipantOfvisitRoomRepository extends JpaRepository<ParticipantOfvisitRoom, UUID> {
    @Query("select p from ParticipantOfvisitRoom p where p.visitOfRoom.id= :V_id and p.notSubscriber.id= :N_id ")
    ParticipantOfvisitRoom getParticipantOfvisitRoomsByNotSubscriberAndDay(@Param("N_id") UUID notSubscriberId,@Param("V_id") UUID visitId);
}
