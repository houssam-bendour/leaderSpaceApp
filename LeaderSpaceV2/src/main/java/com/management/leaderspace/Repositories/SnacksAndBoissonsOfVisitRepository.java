package com.management.leaderspace.Repositories;

import com.management.leaderspace.Entities.SnacksAndBoissonsOfVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SnacksAndBoissonsOfVisitRepository extends JpaRepository<SnacksAndBoissonsOfVisit, UUID> {
    @Query("select s from SnacksAndBoissonsOfVisit s where s.visit.id =:visitId and s.snacksAndBoissons.id = :snackId")
    SnacksAndBoissonsOfVisit getByVisitIdAndSnackId(@Param("visitId") UUID visitId, @Param("snackId") UUID snackId);

    @Query("select coalesce(sum(sbv.quantity*sbv.selling_price),0.0) from SnacksAndBoissonsOfVisit sbv where sbv.visit.id=:uuid")
    Double getTotalPriceOfSnacksAndBoissonsByVisit(@Param("uuid") UUID uuid);

    @Query("select s from SnacksAndBoissonsOfVisit s where s.visitOfRoom.id =:visitId and s.snacksAndBoissons.id = :snackId")
    SnacksAndBoissonsOfVisit getByVisitOfRoomIdAndSnackId(@Param("visitId") UUID visitId, @Param("snackId") UUID snackId);

    @Query("select s from SnacksAndBoissonsOfVisit s where s.participant.id =:participantId and s.snacksAndBoissons.id = :snackId")
    SnacksAndBoissonsOfVisit getByParticipantOfRoomIdAndSnackId(@Param("participantId") UUID participantId,@Param("snackId") UUID snackId);
    @Query("select s from SnacksAndBoissonsOfVisit s where s.visitOfDesk.id =:visitId and s.snacksAndBoissons.id = :snackId")
    SnacksAndBoissonsOfVisit getByVisitOfDeskIdAndSnackId(@Param("visitId") UUID visitId, @Param("snackId") UUID snackId);


    @Query("select coalesce(sum(sbv.quantity*sbv.selling_price), 0.0) from SnacksAndBoissonsOfVisit sbv where sbv.visitOfRoom.id=:uuid")
    Double getTotalPriceOfSnacksAndBoissonsByVisitOfRoom(@Param("uuid") UUID uuid);

    @Query("select coalesce(sum(sbv.quantity*sbv.selling_price), 0.0) from SnacksAndBoissonsOfVisit sbv where sbv.participant.id=:uuid")
    Double getTotalPriceOfSnacksAndBoissonsByParticipant(@Param("uuid") UUID uuid);

    @Query("select coalesce(sum(sbv.quantity*sbv.selling_price), 0.0) from SnacksAndBoissonsOfVisit sbv where sbv.visitOfDesk.id=:uuid")
    Double getTotalPriceOfSnacksAndBoissonsByVisitOfDesk(@Param("uuid") UUID uuid);

    @Query("select coalesce(sum(sbv.quantity*sbv.selling_price), 0.0) from SnacksAndBoissonsOfVisit sbv where sbv.visitOfTeam.id=:uuid")
    Double sommePriceOfSnacksAndBoissonsByVisitOfTeam(@Param("uuid") UUID uuid);

    @Query("select s from SnacksAndBoissonsOfVisit s where s.visitOfTeam.id =:visitId and s.snacksAndBoissons.id = :snackId")
    SnacksAndBoissonsOfVisit getByVisitOfTeamIdAndSnackId(@Param("visitId") UUID visitId, @Param("snackId") UUID snackId);

}
