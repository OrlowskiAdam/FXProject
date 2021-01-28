--
-- Dumping data for table `account_type`
--

LOCK TABLES account_type WRITE;
ALTER TABLE account_type DISABLE KEYS;
INSERT IGNORE INTO account_type VALUES (1,'USER'),(2,'BOT');
ALTER TABLE account_type ENABLE KEYS;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES user WRITE;
ALTER TABLE user DISABLE KEYS;
INSERT IGNORE INTO user VALUES (1,'https://www.gottaaimfast.com/assets/mailbot.png','mail.bot@poczta.pl','admin','Mail','admin','Bot',1);
ALTER TABLE user ENABLE KEYS;
UNLOCK TABLES;
