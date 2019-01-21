package com.girgin.ramazan.springdataelasticsearchgeosearch.domain.repository;

import com.girgin.ramazan.springdataelasticsearchgeosearch.base.BaseElasticSearchIT;
import com.girgin.ramazan.springdataelasticsearchgeosearch.domain.model.entity.CategoryBranchLocationDocument;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class CategoryBranchLocationDocumentRepositoryIT extends BaseElasticSearchIT {

    @Autowired
    CategoryBranchLocationDocumentRepository categoryBranchLocationDocumentRepository;

    @Test
    public void should_save_and_search() {
        //given
        saveCategoryBranchLocation("FOOD", "first-branch-id", 51.106985, 17.037325);
        saveCategoryBranchLocation("COFFEE", "first-branch-id", 51.106985, 17.037325);
        saveCategoryBranchLocation("SEA-PRODUCT", "first-branch-id", 51.106985, 17.037325);
        saveCategoryBranchLocation("FOOD", "second-branch-id", 51.107591, 17.038869);
        saveCategoryBranchLocation("COFFEE", "third-branch-id", 51.107591, 17.038869);
        saveCategoryBranchLocation("FOOD", "fourth-branch-id", 51.064175, 17.000850);
        saveCategoryBranchLocation("FOOD", "fifth-branch-id", 51.106985, 17.037325);
        saveCategoryBranchLocation("FOOD", "sixth-branch-id", 51.107591, 17.038869);
        saveCategoryBranchLocation("FOOD", "seventh-branch-id", 51.106985, 17.037325);
        saveCategoryBranchLocation("COFFEE", "eighth-branch-id", 51.046911, 16.934961);
        saveCategoryBranchLocation("FOOD", "ninth-branch-id", 51.106985, 17.037325);

        String searchCategory = "FOOD";
        Point searchPoint = new Point(51.106985, 17.037325);
        Distance searchDistance = new Distance(3, Metrics.KILOMETERS);

        //when - then
        PageRequest firstPagePageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "branchId"));
        Page<CategoryBranchLocationDocument> firstPageSearchResultPage = categoryBranchLocationDocumentRepository.findByCategoryAndBranchLocationWithin(searchCategory, searchPoint, searchDistance, firstPagePageable);
        assertThat(firstPageSearchResultPage).isNotNull();
        assertThat(firstPageSearchResultPage.getTotalElements()).isEqualTo(6);
        List<CategoryBranchLocationDocument> firstPageElementList = firstPageSearchResultPage.get().collect(Collectors.toList());
        assertThat(firstPageElementList)
                .hasSize(2)
                .extracting("category", "branchId", "branchLocation.lat", "branchLocation.lon")
                .contains(
                        tuple("FOOD", "sixth-branch-id", 51.107591, 17.038869),
                        tuple("FOOD", "seventh-branch-id", 51.106985, 17.037325)
                );

        Pageable secondPagePageable = firstPagePageable.next();
        Page<CategoryBranchLocationDocument> secondPageSearchResultPage = categoryBranchLocationDocumentRepository.findByCategoryAndBranchLocationWithin(searchCategory, searchPoint, searchDistance, secondPagePageable);
        assertThat(secondPageSearchResultPage).isNotNull();
        assertThat(secondPageSearchResultPage.getTotalElements()).isEqualTo(6);
        List<CategoryBranchLocationDocument> secondPageElementList = secondPageSearchResultPage.get().collect(Collectors.toList());
        assertThat(secondPageElementList)
                .hasSize(2)
                .extracting("category", "branchId", "branchLocation.lat", "branchLocation.lon")
                .contains(
                        tuple("FOOD", "second-branch-id", 51.107591, 17.038869),
                        tuple("FOOD", "ninth-branch-id", 51.106985, 17.037325)
                );

        Pageable thirdPagePageable = secondPagePageable.next();
        Page<CategoryBranchLocationDocument> thirdPageSearchResultPage = categoryBranchLocationDocumentRepository.findByCategoryAndBranchLocationWithin(searchCategory, searchPoint, searchDistance, thirdPagePageable);
        assertThat(thirdPageSearchResultPage).isNotNull();
        assertThat(thirdPageSearchResultPage.getTotalElements()).isEqualTo(6);
        List<CategoryBranchLocationDocument> thirdPageElementList = thirdPageSearchResultPage.get().collect(Collectors.toList());
        assertThat(thirdPageElementList)
                .hasSize(2)
                .extracting("category", "branchId", "branchLocation.lat", "branchLocation.lon")
                .contains(
                        tuple("FOOD", "first-branch-id", 51.106985, 17.037325),
                        tuple("FOOD", "fifth-branch-id", 51.106985, 17.037325)
                );
    }

    private void saveCategoryBranchLocation(String category, String branchId, double branchLatitude, double branchLongitude) {
        CategoryBranchLocationDocument categoryBranchLocationDocument = new CategoryBranchLocationDocument();
        categoryBranchLocationDocument.setId(UUID.randomUUID().toString());
        categoryBranchLocationDocument.setCategory(category);
        categoryBranchLocationDocument.setBranchId(branchId);
        GeoPoint location = new GeoPoint(branchLatitude, branchLongitude);
        categoryBranchLocationDocument.setBranchLocation(location);
        categoryBranchLocationDocumentRepository.save(categoryBranchLocationDocument);
    }
}
