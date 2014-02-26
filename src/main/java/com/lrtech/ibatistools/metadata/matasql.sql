SELECT t.column_name,
       t.data_type,
       CAST(SUBSTR(t.column_type, INSTR(t.column_type, '(') + 1, INSTR(t.column_type,')') - INSTR(t.column_type, '(') - 1) AS CHAR(20)) data_length,
       CAST(t.column_type AS CHAR(20)) column_type,
       t.column_comment,
       IF (t.is_nullable='YES',1,0) is_nullable,
       IF (t.column_key = 'PRI', 1, 0) is_key
FROM information_schema.columns t
WHERE t.table_schema = SCHEMA()
      ADN t.table_name = ?
ORDER BY t.ordinal_position;

SELECT t.table_name, t.table_comment, t.create_time
  FROM information_schema.tables t
 WHERE t.table_schema = SCHEMA()

SHOW CREATE TABLE foo;