package com.adspiring.ad.index.adcreative;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdCreativeObject {

    private Long id;
    private String name;
    private Integer type;
    private Integer materialType;
    private Integer height;
    private Integer width;
    private Integer auditStatus;
    private String url;

    public void update(AdCreativeObject newObject) {

        if(newObject.getId() != null) {
            this.id = newObject.getId();
        }
        if(newObject.getName() != null) {
            this.name = newObject.getName();
        }
        if(newObject.getType() != null) {
            this.type = newObject.getType();
        }
        if(newObject.getMaterialType() != null) {
            this.materialType = newObject.getMaterialType();
        }
        if(newObject.getHeight() != null) {
            this.height = newObject.getHeight();
        }
        if(newObject.getWidth() != null) {
            this.width = newObject.getWidth();
        }
        if(newObject.getAuditStatus() != null) {
            this.auditStatus = newObject.getAuditStatus();
        }
        if(newObject.getUrl() != null) {
            this.url = newObject.getUrl();
        }

    }

}
