package com.yunji.restaurant.wishlist.service;

import com.yunji.restaurant.naver.NaverClient;
import com.yunji.restaurant.naver.dto.SearchImageRequest;
import com.yunji.restaurant.naver.dto.SearchLocalRequest;
import com.yunji.restaurant.naver.dto.SearchLocalResponse;
import com.yunji.restaurant.wishlist.dto.WishListDto;
import com.yunji.restaurant.wishlist.entity.WishListEntity;
import com.yunji.restaurant.wishlist.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final NaverClient naverClient;

    private final WishListRepository wishListRepository;

    public WishListDto search(String query){
        //지역검색
        var searchLocalRequest = new SearchLocalRequest();
        searchLocalRequest.setQuery(query);

        var searchLocalResponse = naverClient.localSearch(searchLocalRequest);

        if(searchLocalResponse.getTotal() > 0){
            var localItem = searchLocalResponse.getItems().stream().findFirst().get();
            var imageQuery = localItem.getTitle().replaceAll("<[^>]*>","");
            var searchImageRequest = new SearchImageRequest();
            searchImageRequest.setQuery(imageQuery);


            //이미지 검색
            var searchImageResponse = naverClient.imageSearch(searchImageRequest);

            if(searchImageResponse.getTotal() > 0){
                var imageItem = searchImageResponse.getItems().stream().findFirst().get();

                //결과전달
                var result = new WishListDto();
                result.setTitle(localItem.getTitle());
                result.setCategory(localItem.getCategory());
                result.setAddress(localItem.getAddress());
                result.setRoadAddress(localItem.getLoadAddress());
                result.setHomepageLink(localItem.getLink());
                result.setImageLink(imageItem.getLink());

                return result;
            }
        }
        return new WishListDto();
    }

    public WishListDto add(WishListDto wishListDto) {
        var entity = dtoToEntity(wishListDto);
        var saveEntity = wishListRepository.save(entity);
        return entityToDto(saveEntity);
    }

    private WishListEntity dtoToEntity(WishListDto wishListDto){
        var entity = new WishListEntity();
        entity.setTitle(wishListDto.getTitle());
        entity.setCategory(wishListDto.getCategory());
        entity.setIndex(wishListDto.getIndex());
        entity.setAddress(wishListDto.getAddress());
        entity.setRoadAddress(wishListDto.getRoadAddress());
        entity.setVisit(wishListDto.isVisit());
        entity.setVisitCount(wishListDto.getVisitCount());
        entity.setLastVisitDate(wishListDto.getLastVisitDate());
        entity.setImageLink(wishListDto.getImageLink());
        entity.setHomepageLink(wishListDto.getHomepageLink());
        return entity;
    }

    private WishListDto entityToDto(WishListEntity wishListEntity){
        var dto = new WishListDto();
        dto.setTitle(wishListEntity.getTitle());
        dto.setCategory(wishListEntity.getCategory());
        dto.setIndex(wishListEntity.getIndex());
        dto.setAddress(wishListEntity.getAddress());
        dto.setRoadAddress(wishListEntity.getRoadAddress());
        dto.setVisit(wishListEntity.isVisit());
        dto.setVisitCount(wishListEntity.getVisitCount());
        dto.setLastVisitDate(wishListEntity.getLastVisitDate());
        dto.setImageLink(wishListEntity.getImageLink());
        dto.setHomepageLink(wishListEntity.getHomepageLink());
        return dto;
    }

    public List<WishListDto> findAll() {
        return wishListRepository.findAll().stream().map(it -> entityToDto(it)).collect(Collectors.toList());
    }

    public void delete(int index) {
        wishListRepository.deleteById(index);
    }

    public void addVisit(int index){
        var wishItem = wishListRepository.findById(index);
        if(wishItem.isPresent()){
            var item = wishItem.get();

            item.setVisit(true);
            item.setVisitCount(item.getVisitCount() + 1);
        }
    }
}
