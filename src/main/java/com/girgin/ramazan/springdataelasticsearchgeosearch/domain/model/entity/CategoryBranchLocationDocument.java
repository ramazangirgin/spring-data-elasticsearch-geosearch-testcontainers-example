package com.girgin.ramazan.springdataelasticsearchgeosearch.domain.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "index-category-branch-location", type = "index-category-branch-location-type", shards = 1, replicas = 0, refreshInterval = "-1")
@Mapping(mappingPath = "/mappings/index-category-branch-location-mappings.json")
public class CategoryBranchLocationDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String category;

    @Field(type = FieldType.Object)
    @GeoPointField
    private GeoPoint branchLocation;

    @Field(type = FieldType.Text)
    private String branchId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public GeoPoint getBranchLocation() {
        return branchLocation;
    }

    public void setBranchLocation(GeoPoint branchLocation) {
        this.branchLocation = branchLocation;
    }
}
