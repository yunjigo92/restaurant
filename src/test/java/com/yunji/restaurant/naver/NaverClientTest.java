package com.yunji.restaurant.naver;

import com.yunji.restaurant.naver.dto.SearchImageRequest;
import com.yunji.restaurant.naver.dto.SearchLocalRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NaverClientTest {

    @Autowired
    private NaverClient naverClient;

    @Test
    public void serachLocalTest(){

        var search = new SearchLocalRequest();
        search.setQuery("갈비집");

        var result = naverClient.localSearch(search);
        System.out.println(result);
    }

    @Test
    public void serachImageTest(){

        var search = new SearchImageRequest();
        search.setQuery("갈비집");

        var result = naverClient.imageSearch(search);
        System.out.println(result);
    }

}
