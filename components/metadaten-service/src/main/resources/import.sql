INSERT INTO MasterData (id, anbieter, appName, appVersion) VALUES ('c3ee1b4a-2278-479a-ba09-14e9cc0a0886', 'gematik', 'TI-M', 1.0);
INSERT INTO DiscoveryObject (id, name) VALUES ('c3ee1b4a-2278-479a-ba09-14e9cc0a0886', 'TIM Discovery Object');
----Inserting a relationship between MasterData and DiscoveryObject
INSERT INTO MasterData_DiscoveryObject (MasterData_id,  discoveryObjects_id) VALUES ('c3ee1b4a-2278-479a-ba09-14e9cc0a0886', 'c3ee1b4a-2278-479a-ba09-14e9cc0a0886');
INSERT INTO MasterData (id, anbieter, appName, appVersion) VALUES ('c3ee1b4a-2278-479a-ba09-14e9cc0a0882', 'famedly', 'TI-M', 1.0);
INSERT INTO MasterData (id, anbieter, appName, appVersion) VALUES ('c3ee1b4a-2278-479a-ba09-14e9cc0a0883', 'gematik', 'KIM', 1.0);
INSERT INTO MasterData (id, anbieter, appName, appVersion) VALUES ('c3ee1b4a-2278-479a-ba09-14e9cc0a0884', 'gematik', 'KIM', 1.5);
