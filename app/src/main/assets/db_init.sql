PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS "user_hobby" (
	"id"	INTEGER,
	"hobby_id"	INTEGER NOT NULL UNIQUE,
	"progress_id"	INTEGER NOT NULL UNIQUE,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("hobby_id") REFERENCES "hobby"("id"),
	FOREIGN KEY("progress_id") REFERENCES "progress"("id")
);
CREATE TABLE IF NOT EXISTS "progress" (
	"id"	INTEGER,
	"goal"	TEXT NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "hobby" (
	"id"	INTEGER,
	"hobby_name"	TEXT NOT NULL,
	"category_name"	TEXT NOT NULL,
	"cost"	TEXT,
	"place"	TEXT,
	"people"	TEXT,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "action" (
	"id"	INTEGER,
	"start_date"	INTEGER NOT NULL,
	"end_date"	NUMERIC NOT NULL,
	"progress_id"	INTEGER NOT NULL,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("progress_id") REFERENCES "progress"("id")
);
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (1,'Hiking','Outdoor Activities','Low','Outdoors','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (2,'Running','Outdoor Activities','Medium','Outdoors','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (3,'Cycling','Outdoor Activities','Medium','Cycling Trails','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (4,'Fishing','Outdoor Activities','Low','Fishing Spots','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (5,'Camping','Outdoor Activities','Medium','Campsites','Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (6,'Photography','Outdoor Activities','Medium','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (7,'Bird Watching','Outdoor Activities','Free','Outdoors','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (8,'Rock Climbing','Outdoor Activities','High','Climbing Gyms','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (9,'Gardening','Outdoor Activities','Medium','Home Gardens','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (10,'Geocaching','Outdoor Activities','Free','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (11,'Drawing','Art and Crafts','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (12,'Painting','Art and Crafts','Medium','Art Studios','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (13,'Sculpting','Art and Crafts','High','Art Studios','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (14,'Origami','Art and Crafts','Free','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (15,'Calligraphy','Art and Crafts','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (16,'Knitting','Art and Crafts','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (17,'Sewing','Art and Crafts','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (18,'Pottery','Art and Crafts','High','Pottery Studios','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (19,'Quilting','Art and Crafts','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (20,'Scrapbooking','Art and Crafts','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (21,'Soccer','Sports','Medium','Sports Fields','Teams');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (22,'Basketball','Sports','Medium','Basketball Courts','Teams');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (23,'Tennis','Sports','Low','Tennis Courts','Individuals or Doubles');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (24,'Golf','Sports','High','Golf Courses','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (25,'Swimming','Sports','Medium','Swimming Pools','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (26,'Volleyball','Sports','Low','Volleyball Courts','Teams');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (27,'Table Tennis','Sports','Low','Table Tennis Halls','Individuals or Doubles');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (28,'Badminton','Sports','Low','Badminton Courts','Individuals or Doubles');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (29,'Martial Arts','Sports','Medium','Dojos','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (30,'Surfing','Sports','High','Beaches','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (31,'Playing an Instrument','Music','Medium','Various Locations','Individuals or Bands');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (32,'Singing','Music','Low','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (33,'Composing Music','Music','High','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (34,'DJing','Music','High','Nightclubs','Individuals or Events');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (35,'Music Production','Music','High','Recording Studios','Individuals or Studios');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (36,'Learning a New Instrument','Music','Low','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (37,'Karaoke','Music','Low','Karaoke Bars','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (38,'Collecting Vinyl Records','Music','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (39,'Attending Concerts','Music','High','Concert Venues','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (40,'Music Appreciation','Music','Low','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (41,'Fiction','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (42,'Non-fiction','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (43,'Mystery','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (44,'Science Fiction','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (45,'Fantasy','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (46,'Historical Fiction','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (47,'Biographies','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (48,'Poetry','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (49,'Graphic Novels','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (50,'Magazines','Reading','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (51,'Cooking International Cuisines','Cooking and Baking','Medium','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (52,'Baking Bread','Cooking and Baking','Low','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (53,'Cake Decorating','Cooking and Baking','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (54,'Grilling','Cooking and Baking','Medium','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (55,'Meal Prepping','Cooking and Baking','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (56,'Home Brewing','Cooking and Baking','Medium','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (57,'Fermentation','Cooking and Baking','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (58,'Barbecuing','Cooking and Baking','Medium','Outdoors','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (59,'Pasta Making','Cooking and Baking','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (60,'Slow Cooking','Cooking and Baking','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (61,'Video Gaming','Technology and Gaming','Medium','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (62,'Board Gaming','Technology and Gaming','Low','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (63,'Coding','Technology and Gaming','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (64,'Virtual Reality','Technology and Gaming','High','VR Centers','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (65,'App Development','Technology and Gaming','High','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (66,'3D Printing','Technology and Gaming','High','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (67,'Robotics','Technology and Gaming','High','Workshops','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (68,'PC Building','Technology and Gaming','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (69,'Drone Flying','Technology and Gaming','Medium','Open Spaces','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (70,'Augmented Reality','Technology and Gaming','High','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (71,'Landscape Photography','Photography','Low','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (72,'Portrait Photography','Photography','Low','Photography Studios','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (73,'Macro Photography','Photography','Low','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (74,'Wildlife Photography','Photography','Medium','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (75,'Street Photography','Photography','Low','Urban Areas','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (76,'Astrophotography','Photography','High','Dark Sky Sites','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (77,'Drone Photography','Photography','Medium','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (78,'Fashion Photography','Photography','High','Photography Studios','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (79,'Food Photography','Photography','Low','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (80,'Time-lapse Photography','Photography','Medium','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (81,'Yoga','Fitness and Wellness','Low','Yoga Studios','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (82,'Pilates','Fitness and Wellness','Medium','Fitness Studios','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (83,'Weightlifting','Fitness and Wellness','Low','Gyms','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (84,'CrossFit','Fitness and Wellness','High','CrossFit Boxes','Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (85,'Zumba','Fitness and Wellness','Medium','Dance Studios','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (86,'Marathon Running','Fitness and Wellness','Low','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (87,'HIIT Workouts','Fitness and Wellness','Low','Fitness Studios','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (88,'Cycling','Fitness and Wellness','Medium','Cycling Trails','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (89,'Meditation','Fitness and Wellness','Free','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (90,'Tai Chi','Fitness and Wellness','Low','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (91,'Fiction Writing','Writing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (92,'Poetry Writing','Writing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (93,'Journaling','Writing','Free','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (94,'Blogging','Writing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (95,'Creative Non-Fiction','Writing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (96,'Screenwriting','Writing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (97,'Technical Writing','Writing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (98,'Copywriting','Writing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (99,'Travel Writing','Writing','Low','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (100,'Essay Writing','Writing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (101,'Backpacking','Travel','Medium','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (102,'Road Tripping','Travel','Medium','Various Routes','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (103,'Cultural Exploration','Travel','Medium','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (104,'Adventure Travel','Travel','High','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (105,'Luxury Travel','Travel','High','Luxury Destinations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (106,'Solo Travel','Travel','Medium','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (107,'Group Travel','Travel','Medium','Various Locations','Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (108,'Historical Tourism','Travel','Medium','Historical Sites','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (109,'Culinary Tourism','Travel','Medium','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (110,'Eco-Tourism','Travel','Medium','Ecotourism Sites','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (111,'DIY Projects','Home Improvement','Low','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (112,'Woodworking','Home Improvement','Medium','Home Workshops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (113,'Interior Design','Home Improvement','Medium','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (114,'Gardening','Home Improvement','Medium','Home Gardens','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (115,'Home Renovation','Home Improvement','High','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (116,'Feng Shui','Home Improvement','Medium','Homes','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (117,'Home Organization','Home Improvement','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (118,'Upcycling','Home Improvement','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (119,'Home Automation','Home Improvement','High','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (120,'DIY Electronics','Home Improvement','Medium','Home Workshops','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (121,'Stamp Collecting','Collecting','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (122,'Coin Collecting','Collecting','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (123,'Antique Collecting','Collecting','Medium','Antique Shops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (124,'Comic Book Collecting','Collecting','Low','Comic Book Stores','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (125,'Postcard Collecting','Collecting','Low','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (126,'Sports Memorabilia Collecting','Collecting','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (127,'Art Collecting','Collecting','High','Art Galleries','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (128,'Vinyl Record Collecting','Collecting','Low','Record Stores','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (129,'Toy Collecting','Collecting','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (130,'Vintage Car Collecting','Collecting','High','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (131,'Learning a New Language','Language Learning','Low','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (132,'Language Exchange','Language Learning','Free','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (133,'Linguistics','Language Learning','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (134,'Translation','Language Learning','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (135,'Sign Language','Language Learning','Free','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (136,'Conlanging','Language Learning','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (137,'Duolingo Challenges','Language Learning','Free','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (138,'Accent Acquisition','Language Learning','Low','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (139,'Study Abroad Programs','Language Learning','High','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (140,'Calligraphy in Different Scripts','Language Learning','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (141,'Flower Gardening','Gardening','Low','Home Gardens','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (142,'Vegetable Gardening','Gardening','Low','Home Gardens','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (143,'Bonsai','Gardening','Low','Home Gardens','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (144,'Herb Gardening','Gardening','Low','Home Gardens','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (145,'Landscape Design','Gardening','Medium','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (146,'Xeriscaping','Gardening','Low','Home Gardens','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (147,'Indoor Gardening','Gardening','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (148,'Orchid Cultivation','Gardening','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (149,'Permaculture','Gardening','Medium','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (150,'Hydroponics','Gardening','Medium','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (151,'Chess','Board Games and Puzzles','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (152,'Scrabble','Board Games and Puzzles','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (153,'Monopoly','Board Games and Puzzles','Low','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (154,'Card Games','Board Games and Puzzles','Low','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (155,'Puzzle-Solving','Board Games and Puzzles','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (156,'Role-Playing Games','Board Games and Puzzles','Low','Home','Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (157,'Strategy Board Games','Board Games and Puzzles','Low','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (158,'Trivia Games','Board Games and Puzzles','Low','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (159,'Sudoku','Board Games and Puzzles','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (160,'Crossword Puzzles','Board Games and Puzzles','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (161,'Circuit Building','DIY Electronics','Low','Home Workshops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (162,'Arduino Projects','DIY Electronics','Low','Home Workshops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (163,'Raspberry Pi Projects','DIY Electronics','Low','Home Workshops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (164,'3D Printing','DIY Electronics','Low','Home Workshops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (165,'Drone Building','DIY Electronics','Low','Home Workshops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (166,'Robotics','DIY Electronics','Medium','Home Workshops','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (167,'Home Automation','DIY Electronics','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (168,'Electric Vehicle Tinkering','DIY Electronics','Medium','Home Workshops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (169,'Audio Electronics','DIY Electronics','Medium','Home Workshops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (170,'Computer Hardware Modification','DIY Electronics','Medium','Home Workshops','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (171,'Environmental Conservation','Volunteering','Free','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (172,'Animal Shelters','Volunteering','Free','Animal Shelters','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (173,'Community Service','Volunteering','Free','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (174,'Homeless Shelters','Volunteering','Free','Homeless Shelters','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (175,'Elderly Care','Volunteering','Free','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (176,'Youth Mentoring','Volunteering','Free','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (177,'Disaster Relief','Volunteering','Free','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (178,'Tutoring','Volunteering','Free','Various Locations','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (179,'Hospital Volunteering','Volunteering','Free','Hospitals','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (180,'Food Banks','Volunteering','Free','Food Banks','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (181,'Sewing Clothes','Fashion and Sewing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (182,'Fashion Design','Fashion and Sewing','Medium','Fashion Studios','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (183,'Costume Design','Fashion and Sewing','Medium','Costume Studios','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (184,'Thrift Store Shopping','Fashion and Sewing','Low','Thrift Stores','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (185,'DIY Fashion Projects','Fashion and Sewing','Low','Home','Individuals or Groups');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (186,'Knitting or Crocheting Wearables','Fashion and Sewing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (187,'Jewelry Making','Fashion and Sewing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (188,'Shoe Customization','Fashion and Sewing','Low','Home','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (189,'Vintage Fashion Appreciation','Fashion and Sewing','Low','Various Locations','Individuals');
INSERT INTO "hobby" ("id","hobby_name","category_name","cost","place","people") VALUES (190,'Hat Making','Fashion and Sewing','Low','Home','Individuals');

