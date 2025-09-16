package wvsg.informatik.planner.data.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface ListRepository extends JpaRepository<ListEntity, UUID> {
    @Query("SELECT list FROM ListEntity list WHERE list.owner = :user")
    List<ListEntity> getUserLists(@Param("user") UserEntity user);

    @Modifying
    @Query("UPDATE ListEntity list SET list.title = :title WHERE list.id = :listId")
    @Transactional
    void updateListTitle(@Param("listId") UUID listId, @Param("title") String title);

    @Modifying
    @Query("UPDATE ListEntity list SET list.isPublic = :is_public WHERE list.id = :listId")
    @Transactional
    void updateListIsPublic(@Param("listId") UUID listId, @Param("is_public") Boolean is_public);

    ListEntity findByTitle(String title);

    @Query("SELECT list From ListEntity list WHERE (list.title = :dateString AND list.isDailyList = TRUE AND list.owner = :listOwner)")
    ListEntity findDailyListByDateStringTitle(@Param("dateString") String dateString, @Param("listOwner") UserEntity listOwner);
}
