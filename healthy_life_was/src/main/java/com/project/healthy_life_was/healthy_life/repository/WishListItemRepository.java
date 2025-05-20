package com.project.healthy_life_was.healthy_life.repository;

import com.project.healthy_life_was.healthy_life.entity.user.User;
import com.project.healthy_life_was.healthy_life.entity.whishList.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListItemRepository extends JpaRepository<WishListItem, Long> {
    List<WishListItem> findByWishList_WishListId(Long wishListId);

@Query(value = """
SELECT COUNT(DISTINCT w.WISH_LIST_ID)
FROM WISH_LIST_ITEMS w
WHERE w.P_ID = :p_id
""", nativeQuery = true)
    int countByProductId(@Param("p_id") Long pId);

@Query(value = """
SELECT DISTINCT wi.*
FROM WISH_LIST_ITEMS wi
JOIN WISH_LISTS w ON wi.WISH_LIST_ID = w.WISH_LIST_ID
WHERE wi.P_ID = :pId
AND w.USER_ID = :userId
""", nativeQuery = true)
    Optional<WishListItem> findByPIdAndUserId(@Param("pId") Long pId, @Param("userId") Long userId);

    void deleteByWishList_User(User user);
}