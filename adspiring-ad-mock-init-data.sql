--
-- init data for table `ad_user`
--

INSERT INTO `ad_user`
VALUES (15, 'jay', 'B2E56F2420D73FEC125D2D51641C5713', 1, '2018-02-19 20:29:01', '2018-02-19 20:29:01');

--
-- init data for table `ad_creative`
--

INSERT INTO `ad_creative`
VALUES (10, 'Creative_one', 1, 1, 720, 1080, 1024, 0, 1, 15, 'https://www.google.com', '2018-11-19
21:31:31', '2018-11-19 21:31:31');

--
-- init data for table `ad_plan`
--

INSERT INTO `ad_plan`
VALUES (10, 15, 'Ad_plan_name', 1, '2018-11-28 00:00:00', '2019-11-20 00:00:00', '2018-11-19 20:42:27',
        '2018-11-19 20:57:12');

--
-- init data for table `ad_unit`
--

INSERT INTO `ad_unit`
VALUES (10, 10, 'First_ad_unit', 1, 1, 10000000, '2018-11-20 11:43:26', '2018-11-20 11:43:26'),
       (12, 10, 'Second_ad_unit', 1, 1, 15000000, '2018-01-01 00:00:00', '2018-01-01 00:00:00');

--
-- init data for table `ad_unit_district`
--

INSERT INTO `ad_unit_district`
VALUES (10, 10, 'California', 'San Francisco'),
       (11, 10, 'California', 'San Jose'),
       (12, 10, 'California', 'Los Angeles'),
       (14, 10, 'Nevada', 'Las Vegas');

--
-- init data for table `ad_unit_interest`
--

INSERT INTO `ad_unit_interest`
VALUES (10, 10, 'basketball'),
       (11, 10, 'baseball'),
       (12, 10, 'football');

--
-- init data for table `ad_unit_keyword`
--

INSERT INTO `ad_unit_keyword`
VALUES (10, 10, 'Tesla'),
       (11, 10, 'Audi'),
       (12, 10, 'Toyota');

--
-- init data for table `creative_unit`
--

INSERT INTO `creative_unit`
VALUES (10, 10, 10);


