package wvsg.informatik.planner.data.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wvsg.informatik.planner.data.entity.BulletEntity;
import wvsg.informatik.planner.data.entity.ListEntity;
import wvsg.informatik.planner.data.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface BulletRepository extends JpaRepository<BulletEntity, UUID> {

    List<BulletEntity> findBulletsByList(ListEntity list);

    @Modifying
    @Query("UPDATE BulletEntity b SET b.content = :content WHERE b.id = :bulletId")
    @Transactional
    void updateBulletContent(@Param("bulletId") UUID bulletId, @Param("content") String content);

    @Modifying
    @Query("UPDATE BulletEntity b SET b.type = :type WHERE b.id = :bulletId")
    @Transactional
    void updateBulletType(@Param("bulletId") UUID bulletId, @Param("type") String type);

    @Modifying
    @Query("UPDATE BulletEntity b SET b.indent = :indent WHERE b.id = :bulletId")
    @Transactional
    void updateBulletIndent(@Param("bulletId") UUID bulletId, @Param("indent") int indent);

    @Modifying
    @Query("UPDATE BulletEntity b SET b.position = :position WHERE b.id = :bulletId")
    @Transactional
    void updateBulletPosition(@Param("bulletId") UUID bulletId, @Param("position") int position);

    @Modifying
    @Query("UPDATE BulletEntity b SET b.isCompleted = :isCompleted WHERE b.id = :bulletId")
    @Transactional
    void bulletIsCompleted(@Param("bulletId") UUID bulletId, @Param("isCompleted") Boolean isCompleted);

    @Modifying
    @Query("UPDATE BulletEntity b SET b.list = :list WHERE b.id = :bulletId")
    @Transactional
    void updateBulletsListId(@Param("bulletId") UUID bulletId, @Param("list") ListEntity list);

    @Modifying
    @Query("DELETE FROM BulletEntity b WHERE b.id = :bulletId")
    @Transactional
    void removeBulletById(@Param("bulletId") UUID bulletId);

    @Modifying
    @Query("DELETE FROM BulletEntity b WHERE b.list = :list")
    @Transactional
    void deleteAllBulletsFromList(@Param("list") ListEntity list);

    @Query("SELECT bullet FROM BulletEntity bullet WHERE (bullet.creator = :user AND bullet.type = 'Star')" )
    List<BulletEntity> getAllStarsOfUser(@Param("user") UserEntity user);
}
