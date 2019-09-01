-- adspiring_ad_data database
drop DATABASE adspiring_ad_data;
CREATE DATABASE adspiring_ad_data character set utf8;

use adspiring_ad_data;

CREATE TABLE `ad_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto increment',
  `username` varchar(128) NOT NULL DEFAULT '' COMMENT 'username',
  `token` varchar(256) NOT NULL DEFAULT '' COMMENT 'generate user token',
  `user_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'user status',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'user create time',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'user update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='ad_user table';


CREATE TABLE `ad_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto increment',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'user id',
  `plan_name` varchar(48) NOT NULL COMMENT 'ad_plan name',
  `plan_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ad_plan status',
  `start_date` datetime NOT NULL COMMENT 'ad_plan start time；',
  `end_date` datetime NOT NULL COMMENT 'ad_plan end time；',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'ad_plan create time',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'ad_plan update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='ad_plan table';


CREATE TABLE `ad_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto increment',
  `plan_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ad_plan id',
  `unit_name` varchar(48) NOT NULL COMMENT 'ad_unit name',
  `unit_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ad_unit status',
  `position_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ad_unit position type (e.g: Open Screen, Pre-middle-post movie/video ads)',
  `budget` bigint(20) NOT NULL COMMENT 'ad_unit budget',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'ad_unit create time',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'ad_unit update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='ad_unit table';


CREATE TABLE `ad_creative` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'auto increment',
  `name` varchar(48) NOT NULL COMMENT 'ad_creative name',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ad_creative type (e.g: GIF, video, text, img..)',
  `material_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ad_creative material type (e.g: jpg, png, mp4..)',
  `height` int(10) NOT NULL DEFAULT '0' COMMENT 'ad_creative height',
  `width` int(10) NOT NULL DEFAULT '0' COMMENT 'ad_creative width',
  `size` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ad_creative size, (e.g: unit in kb, mb, gb)',
  `duration` int(10) NOT NULL DEFAULT '0' COMMENT 'ad_creative duration, (Note: video type ads can set duration, else all set to 0)',
  `audit_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ad_creative audit status - legal/illegal ads',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'user id',
  `url` varchar(256) NOT NULL COMMENT 'ad_creative url',
  `create_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'ad_creative create time ',
  `update_time` datetime NOT NULL DEFAULT '1970-01-01 00:00:00' COMMENT 'ad_creative update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='ad_creative table';


-- creative <-> unit relation table
CREATE TABLE `creative_unit` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creative_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ad_creative id',
  `unit_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ad_unit id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='create_unit table';


-- ad_unit_keyword feature
CREATE TABLE `ad_unit_keyword` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unit_id` int(11) NOT NULL COMMENT 'ad_unit id',
  `keyword` varchar(30) NOT NULL COMMENT 'ad_unit keyword',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='ad_unit_keyword table';


-- ad_unit_interest feature
CREATE TABLE `ad_unit_interest` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unit_id` int(11) NOT NULL COMMENT 'ad_unit id',
  `tag` varchar(30) NOT NULL COMMENT 'ad_unit interest tag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='ad_unit_interest table';


-- ad_unit_district feature
CREATE TABLE `ad_unit_district` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unit_id` int(11) NOT NULL COMMENT 'ad_unit id',
  `state` varchar(30) NOT NULL COMMENT 'ad_unit_district state',
  `city` varchar(30) NOT NULL COMMENT 'ad_unit_district city',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='ad_unit_district table';
