CREATE DATABASE lion;

USE lion;

CREATE TABLE `lion_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `mapKey` varchar(64) NOT NULL COMMENT 'map的key',
  `mapValue` text NOT NULL COMMENT 'map的value',
  `lazy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否懒加载0:否,1:是',
  `projectName` varchar(64) NOT NULL COMMENT 'map的所属项目',
  `env` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1:开发,2:Beta,3:Alpha,4:PPE,5:Online,6:Performance',
  PRIMARY KEY (`id`),
  UNIQUE KEY `projectName_key_env_unique` (`mapKey`,`projectName`,`env`)
);

CREATE TABLE `lion_listener` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一主键',
  `projectName` varchar(64) NOT NULL COMMENT '项目名',
  `listenerURL` varchar(128) NOT NULL COMMENT '通知的URL',
  `env` tinyint(4) NOT NULL COMMENT '1:开发,2:Beta,3:Alpha,4:PPE,5:Online,6:Performance',
  `active` tinyint(4) NOT NULL COMMENT '是否需要通知,1:需要,0:不需要',
  PRIMARY KEY (`id`),
  UNIQUE KEY `projectName_listenerURL_env_unique` (`projectName`,`env`,`listenerURL`)
)