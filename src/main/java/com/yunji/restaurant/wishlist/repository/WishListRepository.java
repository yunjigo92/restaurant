package com.yunji.restaurant.wishlist.repository;

import com.yunji.restaurant.db.MemoryDbRepositoryAbstract;
import com.yunji.restaurant.wishlist.entity.WishListEntity;
import org.springframework.stereotype.Repository;

@Repository
public class WishListRepository extends MemoryDbRepositoryAbstract<WishListEntity> {
}
