# MongoDB Advanced Assignment

## Project Overview

This assignment demonstrates advanced MongoDB concepts using a company employee database. It covers schema validation, embedded documents, array operations, bulk writes, aggregation pipelines, indexing, query optimization and database backup and restore.

**Database:** `company_advanced`

## Assignment Contents

| File | Description |
|---|---|
| `mongodb-advanced-assignment.js` | MongoDB commands and queries for Tasks 1–5 |
| `backup-restore-commands.txt` | Database backup, restore, and verification commands for Task 6 |
| `answers.md` | MCQ answers and query execution statistics |
| `company_advanced.archive` | Compressed database backup |

## Concepts Covered

- JSON Schema validation
- Embedded documents and `$elemMatch`
- Positional updates and `arrayFilters`
- Bulk write operations using `bulkWrite()`
- Aggregation using `$lookup`, `$unwind`, `$group`, and `$facet`
- Compound indexes and TTL indexes
- Query execution analysis using `explain("executionStats")`
- Database backup and restore using `mongodump` and `mongorestore`

## Execution

Select the database in MongoDB Shell:

```javascript
use("company_advanced")
```

The JavaScript file contains the commands used to complete Tasks 1–5. The backup and restore commands are documented separately in `backup-restore-commands.txt`.

