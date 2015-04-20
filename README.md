# OpenEMap Permalink

JAX-RS Servlet to persist and read permalink data from MySQL.

DB connection properties can be configured in `WEB-INF/db.properties`.

## Schema

```sql
CREATE TABLE `permalinks`.`permalinks` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `data` TEXT NULL,
  PRIMARY KEY (`id`));
```
