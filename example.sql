update ad_plan
set start_date = '2018-11-30 00:00:00'
where id = 10;
INSERT INTO `ad_plan` (`user_id`, `plan_name`, `plan_status`, `start_date`, `end_date`, `create_time`, `update_time`)
VALUES (10, 'Ad Plan 2', 1, '2018-01-01 00:00:00', '2019-12-01 00:00:00', '2018-01-01 00:00:00', '2018-01-01
00:00:00');
delete
from ad_plan
where id = 12;


update ad_creative
set url = 'https://www.google.com';
INSERT INTO `ad_creative` (`name`, `type`, `material_type`, `height`, `width`, `size`, `duration`, `audit_status`,
                           `user_id`, `url`, `create_time`, `update_time`)
VALUES ('Creative 2', 1, 1, 720, 1080, 1024,
        0, 1, 10, 'www.google.com',
        '2018-01-01
00:00:00', '2018-01-01 00:00:00');
delete
from ad_creative
where id = 13;


update ad_unit
set unit_status = 1
where id = 10;
INSERT INTO `ad_unit` (`plan_id`, `unit_name`, `unit_status`, `position_type`, `budget`, `create_time`,
                       `update_time`)
VALUES (10, 'Ad Unit 2', 1, 1, 15000000, '2018-01-01 00:00:00', '2018-01-01
00:00:00');
delete
from ad_unit
where id = 11;


INSERT INTO `creative_unit` (`creative_id`, `unit_id`)
VALUES (10, 12);
delete
from creative_unit
where creative_id = 10
  and unit_id = 12;


INSERT INTO `ad_unit_district` (`unit_id`, `state`, `city`)
VALUES (10, 'California', 'San Francisco');
delete
from ad_unit_district
where unit_id = 10
  and state = 'California'
  and city = 'San Francisco';

INSERT INTO `ad_unit_interest` (`unit_id`, `tag`)
VALUES (10, 'Jogging');
delete
from ad_unit_interest
where unit_id = 10
  and tag = 'Jogging';

INSERT INTO `ad_unit_keyword` (`unit_id`, `keyword`)
VALUES (10, 'keyword');
delete
from ad_unit_keyword
where unit_id = 10
  and keyword = 'keyword';
