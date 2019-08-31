package com.adspiring.ad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_creative")
public class Creative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "name", nullable = false)
    private String name;

    /** Per ads type: such as (GIF, video, text, img, etc..) **/
    @Basic
    @Column(name = "type", nullable = false)
    private Integer type;

    /** Per ads material type (bmp, jpg, png, mp4 etc..) **/
    @Basic
    @Column(name = "material_type", nullable = false)
    private Integer materialType;

    @Basic
    @Column(name = "height", nullable = false)
    private Integer height;

    @Basic
    @Column(name = "width", nullable = false)
    private Integer width;

    /** Per ads size (kb, mb etc..) **/
    @Basic
    @Column(name = "size", nullable = false)
    private Long size;

    /** Only video type ads can set duration, else all set to 0**/
    @Basic
    @Column(name = "size", nullable = false)
    private Integer duration;

    /** Audit status - legal / illegal ads */
    @Basic
    @Column(name = "audit_status", nullable = false)
    private Integer auditStatus;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Basic
    @Column(name = "url", nullable = false)
    private String url;

    @Basic
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Basic
    @Column(name = "update_time", nullable = false)
    private Date updateTime;

}
