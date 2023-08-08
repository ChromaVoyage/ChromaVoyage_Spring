package com.chromavoyage.api.place.dto;

import com.chromavoyage.api.place.entity.PlaceEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlaceDTO {
    private int placeListId;
    private int coloringLocationId;
    private int groupId;
    private String locationName;
    private String placeName;
    private String address;
    private double latitude;
    private double longitude;

    public static PlaceDTO toPlaceDTO(PlaceEntity placeEntity) {
        PlaceDTO placeDTO = new PlaceDTO();
        placeDTO.setPlaceListId(placeEntity.getPlaceListId());
        placeDTO.setColoringLocationId(placeEntity.getColoringLocationId());
        placeDTO.setGroupId(placeEntity.getGroupId());
        placeDTO.setLocationName(placeEntity.getLocationName());
        placeDTO.setPlaceName(placeEntity.getPlaceName());
        placeDTO.setAddress(placeEntity.getAddress());
        placeDTO.setLatitude(placeEntity.getLatitude());
        placeDTO.setLongitude(placeEntity.getLongitude());
        return placeDTO;
    }
}
